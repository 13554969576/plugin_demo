package com.tongyx.plugin.favorites.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.part.ResourceTransfer;

import com.tongyx.plugin.favorites.model.FavoritesManager;

public class PasteFavoritesHandler extends ClipboardHandler{

	@Override
	protected Object execute(ExecutionEvent e, Clipboard clipboard) {
		if(!paste(clipboard, JavaUI.getJavaElementClipboardTransfer()))
			paste(clipboard, ResourceTransfer.getInstance());			
		return null;
	}
	
	private boolean paste(Clipboard clipboard, Transfer transfer){
		Object[] elements = (Object[])clipboard.getContents(transfer);
		if(elements!=null && elements.length != 0){
			FavoritesManager.getManager().addFavorites(elements);
			return true;
		}
		return false;
	}

}
