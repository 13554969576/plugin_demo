package com.tongyx.plugin.favorites.model;

public abstract class FavoriteItemFactory {
	public abstract IFavoriteItem newFavorite(FavoriteItemType type, Object obj);
	public abstract IFavoriteItem loadFavorite(FavoriteItemType type, String info);
	public void dispose(){
		
	}
}
