package com.tongyx.plugin.favorites.gef.parts;

import org.eclipse.core.resources.IResource;

import com.tongyx.plugin.favorites.model.AbstractFavoriteItem;

public class FavoritesConnection {
	private final AbstractFavoriteItem source;
	private final IResource target;
	
	public FavoritesConnection(AbstractFavoriteItem item, IResource resource){
		this.source = item;
		this.target = resource;
	}
	
	public AbstractFavoriteItem getAbstractFavoriteItem(){
		return source;
	}
	
	public IResource getResource(){
		return target;
	}

}
