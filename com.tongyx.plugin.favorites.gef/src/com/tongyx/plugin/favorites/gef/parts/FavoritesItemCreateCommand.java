package com.tongyx.plugin.favorites.gef.parts;

import java.util.Collections;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ResourceSelectionDialog;

import com.tongyx.plugin.favorites.model.FavoritesManager;

public class FavoritesItemCreateCommand extends Command{
	private final FavoritesManager manager;
	private Object object = null;
	
	public FavoritesItemCreateCommand(FavoritesManager manager) {
		this.manager = manager;
	}
	
	
	@Override
	public void execute() {
		Shell shell = Display.getCurrent().getActiveShell();
		IWorkspaceRoot workspace = ResourcesPlugin.getWorkspace().getRoot();
		ResourceSelectionDialog dialog = new ResourceSelectionDialog(shell, workspace, "Select a resource to add as a favorite item:");
		if(dialog.open() == Window.CANCEL) return;
		Object[] result = dialog.getResult();
		if(result.length == 0) return;
		if(!(result[0] instanceof IFile)) return;
		object = result[0];		
		redo();
	}
	
	@Override
	public void redo() {
		manager.addFavorites(new Object[]{object});
	}
	
	@Override
	public void undo() {
		manager.removeFavorites(Collections.singletonList(object));
	}
	
	@Override
	public boolean canUndo() {
		return object != null;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
