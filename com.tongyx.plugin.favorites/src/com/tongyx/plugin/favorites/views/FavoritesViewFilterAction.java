package com.tongyx.plugin.favorites.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IMemento;

public class FavoritesViewFilterAction extends Action {
	private final Shell shell;
	private final FavoritesViewNameFilter nameFilter;
	
	public FavoritesViewFilterAction(StructuredViewer viewer, String text){
		super(text);
		shell=viewer.getControl().getShell();
		nameFilter=new FavoritesViewNameFilter(viewer);
	}
	
	@Override
	public void run() {
//		InputDialog dialog =new InputDialog(shell, "Favorites View Filter", "Enter a name filter pattern (* for any string, ? for any character) \r\n or an empty string for no filtering", nameFilter.getPattern(), null);
		FavoritesFilterDialog dialog = new FavoritesFilterDialog(shell, nameFilter.getNamePattern(), nameFilter.getLocationPattern(), nameFilter.getTypes());
		if(dialog.open()==InputDialog.OK){
			//nameFilter.setPattern(dialog.getValue().trim());
			nameFilter.setNamePattern(dialog.getNamePattern());
			nameFilter.setLocationPattern(dialog.getLocationPattern());
			nameFilter.setTypes(dialog.getTypes());
			nameFilter.filter();
		}
	}
	
	public void saveState(IMemento memento){
		nameFilter.saveState(memento);		
	}
	
	public void init(IMemento memento){
		nameFilter.init(memento);		
	}

}
