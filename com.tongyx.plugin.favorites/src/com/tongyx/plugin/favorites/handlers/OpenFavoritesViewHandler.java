package com.tongyx.plugin.favorites.handlers;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

public class OpenFavoritesViewHandler implements IHandler {
	private static final String VIEW_ID_FAVORITES = "com.tongyx.plugin.favorites.views.FavoritesView";
	private static Logger log = Logger.getLogger(OpenFavoritesViewHandler.class.getName());

	@Override
	public void addHandlerListener(IHandlerListener arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if(window==null) return null;
		IWorkbenchPage page = window.getActivePage();
		if(page==null)return null;
		try{
			page.showView(VIEW_ID_FAVORITES);
		} catch (PartInitException e){
			log.log(Level.SEVERE, e.getMessage(), e);
		}
		return null;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void removeHandlerListener(IHandlerListener arg0) {
		// TODO Auto-generated method stub

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
