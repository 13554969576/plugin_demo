package com.tongyx.plugin.favorites.gef.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PartInitException;

import com.tongyx.plugin.favorites.gef.Activator;
import com.tongyx.plugin.favorites.gef.editors.FavoritesGEFEditor;
import com.tongyx.plugin.favorites.gef.editors.FavoritesGEFEditorInput;
import com.tongyx.plugin.favorites.model.FavoritesManager;

public class OpenFavoritesEditorHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {		
		
		IEditorInput input = new FavoritesGEFEditorInput(FavoritesManager.getManager());
		try {
			Activator.getDefault().getWorkbench().getActiveWorkbenchWindow().getActivePage().openEditor(input, FavoritesGEFEditor.ID);
		} catch (PartInitException e) {			
			e.printStackTrace();
		}
		return null;
	}
}
