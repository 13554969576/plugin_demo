package com.tongyx.plugin.favorites.editors;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IDEActionFactory;
import org.eclipse.ui.part.EditorActionBarContributor;
import org.eclipse.ui.texteditor.ITextEditor;

public class PropertiesEditorContributor extends EditorActionBarContributor{
	private static final String[] WORKBENCH_ACTION_IDS = {
			ActionFactory.DELETE.getId(),
			ActionFactory.UNDO.getId(),
			ActionFactory.REDO.getId(),
			ActionFactory.CUT.getId(),
			ActionFactory.COPY.getId(),
			ActionFactory.PASTE.getId(),
			ActionFactory.SELECT_ALL.getId(),
			ActionFactory.FIND.getId(),
			IDEActionFactory.BOOKMARK.getId()
	};
	
	private static final String[] TEXTEDITOR_ACTION_IDS = {
			ActionFactory.DELETE.getId(),
			ActionFactory.UNDO.getId(),
			ActionFactory.REDO.getId(),
			ActionFactory.CUT.getId(),
			ActionFactory.COPY.getId(),
			ActionFactory.PASTE.getId(),
			ActionFactory.SELECT_ALL.getId(),
			ActionFactory.FIND.getId(),
			IDEActionFactory.BOOKMARK.getId()
	};
	
//	private LabelRetargetAction removeAction = new LabelRetargetAction(ActionFactory.DELETE.getId(), "Remove");
//	private LabelRetargetAction undoAction = new LabelRetargetAction(ActionFactory.UNDO.getId(), "Undo");
//	private LabelRetargetAction redoAction = new LabelRetargetAction(ActionFactory.REDO.getId(), "Redo");
//	
	@Override
	public void init(IActionBars bars) {		
		super.init(bars);
//		getPage().addPartListener(removeAction);
//		getPage().addPartListener(undoAction);
//		getPage().addPartListener(redoAction);
	}
	
	@Override
	public void contributeToMenu(IMenuManager menuManager) {
//		IMenuManager menu = new MenuManager("Properties Editor");
//		menuManager.prependToGroup(IWorkbenchActionConstants.MB_ADDITIONS, menu);
//		menu.add(undoAction);
//		menu.add(redoAction);
//		menu.add(removeAction);
	}
	
	@Override
	public void contributeToToolBar(IToolBarManager toolBarManager) {
//		toolBarManager.add(new Separator());
//		toolBarManager.add(removeAction);
	}
	
	@Override
	public void dispose() {
//		getPage().removePartListener(removeAction);
//		getPage().removePartListener(undoAction);
//		getPage().removePartListener(redoAction);
		super.dispose();
	}
	
	public void setActiveEditor(IEditorPart part){
		PropertiesEditor editor = (PropertiesEditor)part;
		setActivePage(editor, editor.getActivePage());
	}
	
	public void setActivePage(PropertiesEditor editor, int pageIndex){
		IActionBars actionBars = getActionBars();
		if(actionBars!=null){
			switch (pageIndex) {
			case 0:
				hookGlobalTreeActions(editor,actionBars);
				break;
			case 1:
				hookGlobalTextActions(editor,actionBars);
				break;
			}
			actionBars.updateActionBars();
		}
	}
	
	private void hookGlobalTreeActions(PropertiesEditor editor,IActionBars actionBars){
		for(String id: WORKBENCH_ACTION_IDS){
			actionBars.setGlobalActionHandler(id, editor.getTreeAction(id));
		}
	}
	
	private void hookGlobalTextActions(PropertiesEditor editor,IActionBars actionBars){
		ITextEditor textEditor = editor.getSourceEditor();
		for(String id: TEXTEDITOR_ACTION_IDS){
			actionBars.setGlobalActionHandler(id, textEditor.getAction(id));
		}
	}
}
