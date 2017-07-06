package com.tongyx.plugin.favorites.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;

public class CutFavoritesHandler extends AbstractHandler{
	private IHandler copy = new CopyFavoritesHandler();
	private IHandler remove = new RemoveFavoritesHandler();

	@Override
	public Object execute(ExecutionEvent e) throws ExecutionException {
		copy.execute(e);
		remove.execute(e);
		return null;
	}
	
	@Override
	public void dispose() {
		copy.dispose();
		remove.dispose();
		super.dispose();
	}

}
