package com.tongyx.plugin.favorites.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;

import com.tongyx.plugin.favorites.model.FavoritesManager;

public class AddToFavoritesHandler implements IHandler {	
	//private static Logger log = Logger.getLogger(AddToFavoritesHandler.class.getName());

	@Override
	public void addHandlerListener(IHandlerListener listener) {}

	@Override
	public void dispose() {}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		String viewId = event.getParameter("com.tongyx.plugin.favorites.command.sourceView");
		if(viewId != null){
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
			try{
				window.getActivePage().showView(viewId);
			} catch (PartInitException e) {
				e.printStackTrace();
				return null;
			}
			
		}
		
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if(selection instanceof IStructuredSelection){
			FavoritesManager.getManager().addFavorites(((IStructuredSelection)selection).toArray());
		}
		return null;
	}

	@Override
	public boolean isEnabled() {		
		return false;
	}

	@Override
	public boolean isHandled() {		
		return false;
	}

	@Override
	public void removeHandlerListener(IHandlerListener listener) {		

	}

	public static void main(String[] args) {		

	}

}
