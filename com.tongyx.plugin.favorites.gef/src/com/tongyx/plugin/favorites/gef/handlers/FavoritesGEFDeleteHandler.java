package com.tongyx.plugin.favorites.gef.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.tongyx.plugin.favorites.gef.editors.FavoritesGEFEditor;

public class FavoritesGEFDeleteHandler extends AbstractHandler{

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if(editor instanceof FavoritesGEFEditor){
			((FavoritesGEFEditor)editor).deleteSelection();			
		}
		return null;
	}

}
