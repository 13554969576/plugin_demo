package com.tongyx.plugin.favorites.gef.editors;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import com.tongyx.plugin.favorites.model.FavoritesManager;

public class FavoritesGEFEditorInput implements IEditorInput{
	private final FavoritesManager model;
	
	public FavoritesGEFEditorInput(FavoritesManager model) {
		this.model = model;
	}
	
	public FavoritesManager getModel() {
		return model;
	}
	
	@Override
	public boolean exists() {		
		return false;
	}
	
	@Override
	public ImageDescriptor getImageDescriptor() {		
		return null;
	}
	
	@Override
	public String getName() {		
		return "Favorites GEF Editor";
	}
	
	@Override
	public IPersistableElement getPersistable() {		
		return null;
	}
	
	@Override
	public String getToolTipText() {		
		return "Favorites GEF Editor";
	}
	
	@Override
	public <T> T getAdapter(Class<T> adapter) {		
		return null;
	}

}
