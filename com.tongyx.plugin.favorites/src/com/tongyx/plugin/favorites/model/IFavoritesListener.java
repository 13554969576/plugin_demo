package com.tongyx.plugin.favorites.model;

@FunctionalInterface
public interface IFavoritesListener {
	void favoritesItemChanged(IFavoriteItem item);
}
