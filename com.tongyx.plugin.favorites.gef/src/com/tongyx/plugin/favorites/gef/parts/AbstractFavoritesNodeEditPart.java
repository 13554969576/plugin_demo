package com.tongyx.plugin.favorites.gef.parts;

public abstract class AbstractFavoritesNodeEditPart extends AbstractFavoritesGraphicalEditPart{

	public boolean addFavoritesTargetConnection(FavoritesConnectionEditPart collectionPart){
		return false;
	}
	
	public boolean removeFavoritesTargetConnection(FavoritesConnectionEditPart collectionPart){
		return false;
	}
	
	public abstract String getSortKey();
}
