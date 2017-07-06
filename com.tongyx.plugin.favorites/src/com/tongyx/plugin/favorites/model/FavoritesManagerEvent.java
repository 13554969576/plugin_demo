package com.tongyx.plugin.favorites.model;

import java.util.EventObject;

public class FavoritesManagerEvent extends EventObject{

	private static final long serialVersionUID = 1L;
	
	private final IFavoriteItem[] added;
	private final IFavoriteItem[] removed;
	
	public FavoritesManagerEvent(FavoritesManager source,IFavoriteItem[] added,IFavoriteItem[] removed){
		super(source);
		this.added=added;
		this.removed=removed;
	}

	public IFavoriteItem[] getAdded() {
		return added;
	}

	public IFavoriteItem[] getRemoved() {
		return removed;
	}
}
