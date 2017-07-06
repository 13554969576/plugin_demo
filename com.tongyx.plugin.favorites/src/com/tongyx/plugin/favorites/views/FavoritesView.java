package com.tongyx.plugin.favorites.views;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.GroupMarker;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerActivation;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.help.IWorkbenchHelpSystem;
import org.eclipse.ui.part.ViewPart;

import com.tongyx.plugin.favorites.Constant;
import com.tongyx.plugin.favorites.contributions.RemoveFavoritesContributionItem;
import com.tongyx.plugin.favorites.handlers.RemoveFavoritesHandler;
import com.tongyx.plugin.favorites.model.AltClickCellEditorListener;
import com.tongyx.plugin.favorites.model.FavoritesManager;
import com.tongyx.plugin.favorites.model.IFavoriteItem;
import com.tongyx.plugin.favorites.utils.EditorUtil;


public class FavoritesView extends ViewPart {

	private TableViewer viewer;
	private Action action1;
	private Action action2;
	@SuppressWarnings("unused")
	private Action doubleClickAction;
	private TableColumn typeCol;
	private TableColumn nameCol;
	private TableColumn urlCol;
	private ContributionItem remove;
	private FavoritesViewFilterAction filter;
	private ISelectionListener pageSelectionListener;
	private IMemento memento;
	private FavoritesViewComparator viewComparator;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			return getText(obj);
		}
		public Image getColumnImage(Object obj, int index) {
			return getImage(obj);
		}
		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}
	
	class ViewContentProvider implements IContentProvider{
		
	}


	public FavoritesView() {
	}


	public void createPartControl(Composite parent) {
		createTableView(parent);
		makeActions();
		createContextMenu();
		createToolbarButtons();
		createViewpulldownMenu();
		hookKeyBoard();
		hookGlobalHandlers();
		hookGragAndDrop();
		createInlineEditor();
		hookPageSelection();
		hookDoubleClickAction();
		//contributeToActionBars();
		setHelpContextId();
	}
	
	@SuppressWarnings({ "unchecked" })
	private void createTableView(Composite parent){
		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION);
		final Table table = viewer.getTable();
		typeCol = new TableColumn(table, SWT.LEFT);
		typeCol.setText(" "); //$NON-NLS-1$
		typeCol.setWidth(18);
		
		nameCol = new TableColumn(table, SWT.LEFT);
		nameCol.setText(Messages.FavoritesView_name);
		nameCol.setWidth(200);
		
		urlCol = new TableColumn(table, SWT.LEFT);
		urlCol.setText(Messages.FavoritesView_location);
		urlCol.setWidth(450);
		
		Comparator<IFavoriteItem> nameComparator = (o1,o2)->o1.getName().compareTo(o2.getName());
		Comparator<IFavoriteItem> urlComparator = (o1,o2)->o1.getLocation().compareTo(o2.getLocation());
		Comparator<IFavoriteItem> typeComparator = (o1,o2)->o1.getType().compareTo(o2.getType());
		
		table.setHeaderVisible(true);
		table.setLinesVisible(false);
		
		viewer.setContentProvider(new FavoritesViewContentProvider());		
		viewer.setLabelProvider(new FavoritesViewLabelProvider());
		viewer.setInput(FavoritesManager.getManager());
		viewComparator=new FavoritesViewComparator(viewer,new TableColumn[]{nameCol,urlCol,typeCol}, new Comparator[]{nameComparator,urlComparator,typeComparator});
		viewComparator.init(memento);
		viewer.setComparator(viewComparator);
		getSite().setSelectionProvider(viewer);
	}

	private void createContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		menuMgr.setRemoveAllWhenShown(true);		
		menuMgr.addMenuListener((manager)->{FavoritesView.this.fillContextMenu(manager);});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());		
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}
	
	private void createToolbarButtons(){
		IToolBarManager manager= getViewSite().getActionBars().getToolBarManager();
		manager.add(new GroupMarker(Messages.FavoritesView_edit));
		manager.add(remove);
	}
	
	private void createViewpulldownMenu(){
		IMenuManager menu = getViewSite().getActionBars().getMenuManager();
		filter = new FavoritesViewFilterAction(viewer,Messages.FavoritesView_filter);
		filter.init(memento);
		menu.add(filter);
	}
	
	private void hookKeyBoard(){
		viewer.getControl().addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				handleKeyReleased(e);
			}
		});
	}
	
	private void handleKeyReleased(KeyEvent e){
		if(e.character==SWT.DEL && e.stateMask==0){
			((RemoveFavoritesContributionItem)remove).run();
		}
	}
	
	private void hookGlobalHandlers(){
		IHandlerService handlerService = getViewSite().getService(IHandlerService.class);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			private IHandlerActivation removeActivation;
			@Override
			public void selectionChanged(SelectionChangedEvent e) {
				if(e.getSelection().isEmpty()){
					
					if(removeActivation!=null){
						handlerService.deactivateHandler(removeActivation);
						removeActivation=null;
					}
				} else {
					if(removeActivation==null){
						//removeActivation=handlerService.activateHandler(ActionFactory.DELETE.getId(),((RemoveFavoritesContributionItem)remove).getHandler());
						removeActivation=handlerService.activateHandler("org.eclipse.ui.edit.delete",((RemoveFavoritesContributionItem)remove).getHandler()); //$NON-NLS-1$
					}
				}
				
			}
		});
	}
	
	private void hookGragAndDrop(){
		new FavoritesDragSource(viewer);
		new FavoritesDropTarget(viewer);
	}

	protected void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(action1);
		manager.add(new Separator());
		manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		//manager.add(action1);
		//manager.add(action2);
		manager.add(remove);	
		manager.add(new Separator(Messages.FavoritesView_edit));
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				showMessage(Messages.FavoritesView_msg_action1);
			}
		};
		action1.setText(Messages.FavoritesView_action1);
		action1.setToolTipText(Messages.FavoritesView_tooltip_action1);
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		action2 = new Action() {
			public void run() {
				showMessage(Messages.FavoritesView_msg_action2);
			}
		};
		action2.setText(Messages.FavoritesView_action2);
		action2.setToolTipText(Messages.FavoritesView_tooltip_action2);
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				Object obj = ((IStructuredSelection)selection).getFirstElement();
				showMessage(Messages.FavoritesView_msg_dbclick+obj.toString());
			}
		};
		remove = new RemoveFavoritesContributionItem(this, new RemoveFavoritesHandler());
	}
	
	private void createInlineEditor(){
		TableViewerColumn column=new TableViewerColumn(viewer, nameCol);
		column.setLabelProvider(new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {				
				return ((IFavoriteItem)element).getName();
			}
			
			@Override
			public Color getForeground(Object element) {				
				return ((IFavoriteItem)element).getColor();
			}
		});
		
		column.setEditingSupport(new EditingSupport(viewer) {
			TextCellEditor editor = null;
			
			@Override
			protected void setValue(Object element, Object value) {
				((IFavoriteItem)element).setName((String)value);
				viewer.refresh(element);				
			}
			
			@Override
			protected Object getValue(Object o) {				
				return ((IFavoriteItem)o).getName();
			}
			
			@Override
			protected CellEditor getCellEditor(Object o) {	
				if(editor==null){
					Composite table = (Composite)viewer.getControl();
					editor=new TextCellEditor(table);
				}
				return editor;
			}
			
			@Override
			protected boolean canEdit(Object o) {				
				return true;
			}
		});
		viewer.getColumnViewerEditor().addEditorActivationListener(new AltClickCellEditorListener());		
	}
	
	private void hookPageSelection(){	
		pageSelectionListener=(part,selection)->{
			if(part==this)return;
			if(!(selection instanceof IStructuredSelection))return;
			FavoritesManager manager = FavoritesManager.getManager();
			List<IFavoriteItem>  items = new ArrayList<>();
			for(Object o: ((IStructuredSelection)selection).toList()){
				IFavoriteItem item = manager.existingFavoriteFor(o);
				if(item!=null){
					items.add(item);
				}
			}
			viewer.setSelection(new StructuredSelection(items),true);
			
		};
		getViewSite().getPage().addPostSelectionListener(pageSelectionListener);
	}

	private void hookDoubleClickAction() {
//		viewer.addDoubleClickListener(new IDoubleClickListener() {
//			public void doubleClick(DoubleClickEvent event) {
//				doubleClickAction.run();
//			}
//		});
		viewer.getTable().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {				
				EditorUtil.openEditor(getViewSite().getPage(), (IStructuredSelection)viewer.getSelection());				
			}			
		});
	}
	private void showMessage(String message) {
		MessageDialog.openInformation(
			viewer.getControl().getShell(),
			"FavoritesView", //$NON-NLS-1$
			message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public TableViewer getFavoritesView(){
		return viewer;
	}
	
	
	public IStructuredSelection getSelection() {
		return (IStructuredSelection)viewer.getSelection();
	}
	
	public void addSelectionChangedListener(ISelectionChangedListener listener){
		viewer.addSelectionChangedListener(listener);
	}
	
	@Override
	public void dispose() {
		getViewSite().getPage().removePostSelectionListener(pageSelectionListener);
		super.dispose();
	}
	
	@Override
	public void saveState(IMemento memento) {
		super.saveState(memento);
		if(viewComparator!=null)viewComparator.saveState(memento);
		if(filter!=null)filter.saveState(memento);
	}
	
	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {		
		super.init(site, memento);
		this.memento=memento;
	}
	
	private void setHelpContextId(){
		IWorkbenchHelpSystem help = getSite().getWorkbenchWindow().getWorkbench().getHelpSystem();
		help.setHelp(viewer.getControl(), Constant.PLUGIN_ID + ".favorites_view_help"); //$NON-NLS-1$
	}

}
