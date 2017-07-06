package com.qualityeclipse.favorites.model;

import org.eclipse.core.resources.IFile;

import com.tongyx.plugin.favorites.model.FavoriteItemFactory;
import com.tongyx.plugin.favorites.model.FavoriteItemType;
import com.tongyx.plugin.favorites.model.FavoriteResource;
import com.tongyx.plugin.favorites.model.IFavoriteItem;

public class FileFactory extends FavoriteItemFactory {
	@Override
	public IFavoriteItem newFavorite(FavoriteItemType type, Object obj) {
		if(!(obj instanceof IFile))return null;
		return new FavoriteResource(type, (IFile)obj);
	}

	@Override
	public IFavoriteItem loadFavorite(FavoriteItemType type, String info) {		
		return FavoriteResource.loadFavorite(type, info);
	}

}
