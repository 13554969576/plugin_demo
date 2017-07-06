package com.tongyx.plugin.favorites.model;

@FunctionalInterface
public interface IFavoritesMamagerListener {
	void favoritesChanged(FavoritesManagerEvent event);
}
