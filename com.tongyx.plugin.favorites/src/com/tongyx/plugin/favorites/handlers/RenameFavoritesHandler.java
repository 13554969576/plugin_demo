package com.tongyx.plugin.favorites.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.tongyx.plugin.favorites.views.FavoritesView;

public class RenameFavoritesHandler extends AbstractHandler{
	private static final int COLUMN_TO_EDIT=1;	

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if(!(part instanceof FavoritesView)) return null;
		editElement((FavoritesView)part);
		return null;
	}
	
	public void editElement(FavoritesView view){
		TableViewer tv = view.getFavoritesView();
		IStructuredSelection selection = (IStructuredSelection)tv.getSelection();
		if(selection.isEmpty())return;
		tv.editElement(selection.getFirstElement(),COLUMN_TO_EDIT);
	}

}
