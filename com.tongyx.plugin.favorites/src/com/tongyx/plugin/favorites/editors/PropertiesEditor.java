package com.tongyx.plugin.favorites.editors;

import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.Collections;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.IOperationHistory;
import org.eclipse.core.commands.operations.IUndoContext;
import org.eclipse.core.commands.operations.ObjectUndoContext;
import org.eclipse.core.commands.operations.OperationHistoryFactory;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.operations.RedoActionHandler;
import org.eclipse.ui.operations.UndoActionHandler;
import org.eclipse.ui.part.MultiPageEditorPart;

import com.tongyx.plugin.favorites.Constant;
import com.tongyx.plugin.favorites.handlers.editors.DeleteHandler;
import com.tongyx.plugin.favorites.model.AltClickCellEditorListener;
import com.tongyx.plugin.favorites.model.PropertyCategory;
import com.tongyx.plugin.favorites.model.PropertyEntry;
import com.tongyx.plugin.favorites.model.PropertyFile;
import com.tongyx.plugin.favorites.model.PropertyFileListener;

public class PropertiesEditor extends MultiPageEditorPart {
	
	private TreeViewer treeViewer;
	private TextEditor txtEditor;
	
	private TreeColumn keyCol;
	private TreeColumn valueCol;
	
	private PropertiesEditorContentProvider contentProvider;
	private PropertiesEditorlabelProvider labelProvider;
	private PropertyFileListener listener;
//	private String sourceContent;
//	private PropertyFile treeContent;
	private boolean txtChanged;
	private IAction remove;
	private IUndoContext undoContext;
	private UndoActionHandler undo;
	private RedoActionHandler redo;
	

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		System.out.println("init");
		if (!(input instanceof IFileEditorInput))
			throw new PartInitException("This editor can only support file input");
		super.init(site, input);		
	}
	
	@Override
	protected void createPages() {	
		System.out.println("createpages");
		createPropertiesPage();
		createSourcePage();
		updateTitle();
	}
	
	private void createPropertiesPage(){
		System.out.println("createPropertiesPage");
		Composite container = new Composite(getContainer(), SWT.NONE) ;
		treeViewer = new TreeViewer(container,SWT.MULTI | SWT.FULL_SELECTION);
		TreeColumnLayout layout = new TreeColumnLayout();
		container.setLayout(layout);
		Tree tree = treeViewer.getTree();
		tree.setHeaderVisible(true);
		keyCol=new TreeColumn(tree, SWT.NONE);
		keyCol.setText("Key");
		layout.setColumnData(keyCol, new ColumnWeightData(2));
		
		valueCol=new TreeColumn(tree, SWT.NONE);
		valueCol.setText("Value");
		layout.setColumnData(valueCol, new ColumnWeightData(3));
		
		TreeViewerColumn c1 = new TreeViewerColumn(treeViewer, keyCol);
		TreeViewerColumn c2 = new TreeViewerColumn(treeViewer, valueCol);
		c1.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {				
				return labelProvider.getColumnText(element, 0);
			}
		});
		c2.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {				
				return labelProvider.getColumnText(element, 1);
			}
		});
		c1.setEditingSupport(new EditingSupport(treeViewer) {
			TextCellEditor editor = null;
			@Override
			protected void setValue(Object element, Object value) {
				if(value==null)return;
				String s = value.toString().trim();
				if(element instanceof PropertyCategory){
					((PropertyCategory)element).setName(s);
				} else if(element instanceof PropertyEntry){
					((PropertyEntry)element).setKey(s);
				}
				
			}
			
			@Override
			protected Object getValue(Object element) {				
				return labelProvider.getColumnText(element, 0);
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				if(editor==null){
					Composite tree = (Composite)treeViewer.getControl();
					editor=new TextCellEditor(tree);
					ICellEditorValidator v = (o)->((String)o).trim().length()==0?"Key mush not be empty string":null;
					editor.setValidator(v);
					editor.addListener(new ICellEditorListener() {						
						@Override
						public void editorValueChanged(boolean arg0, boolean arg1) {
							setErrorMsg(editor.getErrorMessage());
						}
						
						@Override
						public void cancelEditor() {
							setErrorMsg(null);
						}
						
						@Override
						public void applyEditorValue() {
							setErrorMsg(null);
						}
						
						private void setErrorMsg(String msg){
							getEditorSite().getActionBars().getStatusLineManager().setErrorMessage(msg);
						}
					});
				}
				return editor;
			}
			
			@Override
			protected boolean canEdit(Object element) {				
				return true;
			}
		});
		
		c2.setEditingSupport(new EditingSupport(treeViewer) {
			TextCellEditor editor = null;
			@Override
			protected void setValue(Object element, Object value) {
				String s = value.toString().trim();
				if(element instanceof PropertyEntry){
					((PropertyEntry)element).setValue(s);
				}
				
			}
			
			@Override
			protected Object getValue(Object element) {				
				return labelProvider.getColumnText(element, 1);
			}
			
			@Override
			protected CellEditor getCellEditor(Object element) {
				if(editor==null){
					Composite tree = (Composite)treeViewer.getControl();
					editor=new TextCellEditor(tree);
				}
				return editor;
			}
			
			@Override
			protected boolean canEdit(Object element) {				
				return true;
			}
		});
		
		listener = new PropertyFileListener() {
			
			@Override
			public void valueChanged(PropertyCategory category, PropertyEntry entry) {
				treeViewer.refresh(entry);
				treeModified();				
			}
			
			@Override
			public void nameChanged(PropertyCategory category) {
				treeViewer.refresh(category);
				treeModified();
			}
			
			@Override
			public void keyChanged(PropertyCategory category, PropertyEntry entry) {
				treeViewer.refresh(entry);
				treeModified();				
			}
			
			@Override
			public void entryRemoved(PropertyCategory category, PropertyEntry entry) {
				treeViewer.refresh();
				treeModified();					
			}
			
			@Override
			public void entryAdded(PropertyCategory category, PropertyEntry entry) {
				treeViewer.refresh();
				treeModified();					
			}
			
			@Override
			public void categoryRemoved(PropertyCategory category) {
				treeViewer.refresh();
				treeModified();				}
			
			@Override
			public void categoryAdded(PropertyCategory category) {
				treeViewer.refresh();
				treeModified();					
			}
		};
		
		int pageIndex = addPage(container);
		initTreeContent();
		treeViewer.getColumnViewerEditor().addEditorActivationListener(new AltClickCellEditorListener());
		getSite().setSelectionProvider(treeViewer);
		createContextMenu();
		createRemoveAction();
//		tree.addKeyListener(new KeyListener() {
//			
//			@Override
//			public void keyReleased(KeyEvent event) {
//				
//				
//			}
//			
//			@Override
//			public void keyPressed(KeyEvent event) {
//				if(event.character == SWT.DEL){
//					remove.run();
//				}
//			}
//		});
		initKeyBindingContext();
		initRedoUndo();		
		setPageText(pageIndex, "Properties");
	}
	
	private void createContextMenu(){
		MenuManager manager = new MenuManager("#PopupMenu");
		manager.setRemoveAllWhenShown(true);
		IMenuListener l = (m)->{
			m.add(undo);
			m.add(redo);
			m.add(new Separator("edit"));
			m.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		};
		manager.addMenuListener(l);
		Tree t = treeViewer.getTree();
		t.setMenu(manager.createContextMenu(t));
		getSite().registerContextMenu(manager, treeViewer);
	}
	
	public void treeModified(){
//		PropertyFile pf = (PropertyFile)treeViewer.getInput();	
		updateTextFromTree();
//		if(!pf.equals(treeContent))
//			firePropertyChange(IEditorPart.PROP_DIRTY);		
	}
	
	private void createRemoveAction(){
		remove = new Action() {
			@Override
			public String getText() {
				return "Remove";
			}
			
			@Override
			public String getId() {			
				return ActionFactory.DELETE.getId();
			}
			
			@Override
			public ImageDescriptor getImageDescriptor() {
				return PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_TOOL_DELETE);
			}
			
			@Override
			public boolean isEnabled() {
				return true;
				//return treeViewer.getTree().getSelection().length>0;
			}
			
			@Override
			public void run() {
				final IHandlerService handlerService=getSite().getService(IHandlerService.class);
				IEvaluationContext context = handlerService.createContextSnapshot(true);
				ExecutionEvent event = new ExecutionEvent(null, Collections.EMPTY_MAP, null,context);
				try{
					new DeleteHandler().execute(event);
				}catch(ExecutionException e){
					throw new RuntimeException(e);
				}
			}
			
		};
	}
	
	@Override
	protected void pageChange(int newPageIndex) {	
		System.out.println(String.format("page changed to %d", newPageIndex));
		if(newPageIndex==0 && txtChanged){
			updateTreeFromTextEditor();
			txtChanged=false;
		}
		super.pageChange(newPageIndex);
		if(newPageIndex==0){
			setGlobalMenuForTree();
		} else {
			setGlobalMenuForText();
		}
		
//		IEditorActionBarContributor contributor= getEditorSite().getActionBarContributor();
//		if(contributor instanceof PropertiesEditorContributor){
//			((PropertiesEditorContributor)contributor).setActivePage(this,newPageIndex);
//		}
	}
	
	private void createSourcePage(){
		System.out.println("createSourcePage");
		txtEditor = new TextEditor();		
		int pageIndex;
		try {
			pageIndex = addPage(txtEditor, getEditorInput());	
			txtEditor.getDocumentProvider().getDocument(txtEditor.getEditorInput()).addDocumentListener(new IDocumentListener() {
				@Override
				public void documentChanged(DocumentEvent doc) {
					if(getActivePage()==pageIndex)
					{
						txtChanged=true;
						getOperationHistory().dispose(undoContext, true, true, false);
					}
				}
				
				@Override
				public void documentAboutToBeChanged(DocumentEvent doc) {
				}
			});
//			sourceContent=txtEditor.getDocumentProvider().getDocument(txtEditor.getEditorInput()).get();
			setPageText(pageIndex, "Source");
		} catch (PartInitException e) {			
			e.printStackTrace();
		}		
	}
	
	private void initTreeContent(){
		contentProvider = new PropertiesEditorContentProvider();
		treeViewer.setContentProvider(contentProvider);
		labelProvider=new PropertiesEditorlabelProvider();
		treeViewer.setLabelProvider(labelProvider);
		
		treeViewer.setInput(new PropertyFile(""));
		treeViewer.getTree().getDisplay().asyncExec(new Runnable() {			
			@Override
			public void run() {				
				updateTreeFromTextEditor();
			}
		});
		treeViewer.setAutoExpandLevel(TreeViewer.ALL_LEVELS);
	}
	
	private void initKeyBindingContext(){
		IContextService service = getSite().getService(IContextService.class);
		treeViewer.getControl().addFocusListener(new FocusListener() {
			IContextActivation curContext = null;
			@Override
			public void focusLost(FocusEvent arg0) {
				if(curContext!=null)service.deactivateContext(curContext);				
			}
			
			@Override
			public void focusGained(FocusEvent arg0) {
				if(curContext==null)curContext=service.activateContext(Constant.ID_CONTEXT_PROPERTIES_EDITOR);
			}
		});
	}
	
	private void updateTreeFromTextEditor(){
		PropertyFile propertyFile = (PropertyFile)treeViewer.getInput();
		propertyFile.removePropertyFileListener(listener);
		String content=txtEditor.getDocumentProvider().getDocument(txtEditor.getEditorInput()).get();
		propertyFile = new PropertyFile(content);
		treeViewer.setInput(propertyFile);
		propertyFile.addPropertyFileListener(listener);
//		if(isInit)treeContent=propertyFile.cloneAsPropertyFile();
	}
	
	@SuppressWarnings("unused")
	private void updateTextFromTree(){
		String s = txtEditor.getDocumentProvider().getDocument(txtEditor.getEditorInput()).get();
		LineNumberReader li = new LineNumberReader(new StringReader(s));
		String line = null;
		PropertyFile f=(PropertyFile)treeViewer.getInput();
		PropertyCategory c = null;
		int cCount=f.getChildren().length;
		int cIdx = -1;
		
		PropertyEntry e = null;
		int eCount= 0;
		int eIdx = -1;
		
		if(cCount>0){
			cIdx = 0;
			c=(PropertyCategory)f.getChildren()[cIdx];
			eCount=c.getChildren().length;
			if(eCount>0){
				eIdx=0;
				e=(PropertyEntry)c.getChildren()[eIdx];
			}
		} else {
			txtEditor.getDocumentProvider().getDocument(txtEditor.getEditorInput()).set("");
			return;
		}
		
		txtEditor.getDocumentProvider().getDocument(txtEditor.getEditorInput()).set(f.asString());
		return;
		
//		try {
//			while((line=li.readLine())!=null){
//				//if()
//			}
//		} catch (IOException err) {
//			throw new RuntimeException(err);
//		}
		
	}
	
	private void updateTitle(){
		IEditorInput in = getEditorInput();
		setPartName(in.getName());
		setTitleToolTip(in.getToolTipText()); 
	}
	
	@Override
	public void setFocus() {
		int pageIndex=getActivePage();
		if(pageIndex==0){
			treeViewer.getTree().setFocus();
		} else if(pageIndex==1){
			txtEditor.setFocus();
		}
	}
	
	

	@Override
	public void doSave(IProgressMonitor monitor) {
		txtEditor.doSave(monitor);
		
	}

	@Override
	public void doSaveAs() {
		txtEditor.doSaveAs();	
		setInput(txtEditor.getEditorInput());
		updateTitle();
	}

	@Override
	public boolean isSaveAsAllowed() {		
		return true;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if(adapter.isAssignableFrom(IGotoMarker.class)){
			IGotoMarker marker = (m)->{
				setActivePage(1);
				txtEditor.getAdapter(IGotoMarker.class).gotoMarker(m);				
			};
			return (T)marker;
		}
		return super.getAdapter(adapter);
	}
	
	@Override
	public boolean isDirty() {		
//		return !treeContent.equals(treeViewer.getInput()) || !sourceContent.equals(txtEditor.getDocumentProvider().getDocument(txtEditor.getEditorInput()).get());
		return super.isDirty();
	}
	
	public IAction getTreeAction(String id){
//		if(ActionFactory.DELETE.getId().equals(id))
//			return remove;
//		else 
//			return null;
		return null;
	}
	
	public TextEditor getSourceEditor(){
		return txtEditor;
	}
	
	public IOperationHistory getOperationHistory(){
		return OperationHistoryFactory.getOperationHistory();
	}
	
	public IUndoContext getUndoContext(){
		return undoContext;
	}
	
	private void initRedoUndo(){
		System.out.println("initRedoUndo");
		undoContext = new ObjectUndoContext(this);
		undo = new UndoActionHandler(getSite(), undoContext);
		redo = new RedoActionHandler(getSite(), undoContext);		
	}
	
	private void setGlobalMenuForTree(){
		System.out.println("setGlobalMenuForTree");
		IActionBars actionBars = getEditorSite().getActionBars();
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), remove);
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId() , undo);
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redo);
		actionBars.updateActionBars();
	}
	
	private void setGlobalMenuForText(){
		System.out.println("setGlobalMenuForText");
		IActionBars actionBars = getEditorSite().getActionBars();
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), txtEditor.getAction(ActionFactory.DELETE.getId()));
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId() , txtEditor.getAction(ActionFactory.UNDO.getId()));
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), txtEditor.getAction(ActionFactory.REDO.getId()));
		actionBars.updateActionBars();
	}
	
	
}
