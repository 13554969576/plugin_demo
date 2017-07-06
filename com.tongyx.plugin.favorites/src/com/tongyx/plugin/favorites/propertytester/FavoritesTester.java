package com.tongyx.plugin.favorites.propertytester;

import org.eclipse.core.expressions.PropertyTester;

import com.tongyx.plugin.favorites.model.FavoritesManager;
import com.tongyx.plugin.favorites.model.IFavoriteItem;

public class FavoritesTester extends PropertyTester{

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		boolean found = false;
		IFavoriteItem[] favorites = FavoritesManager.getManager().getFavorites();
		for(IFavoriteItem item: favorites){
			found=item.isFavoriteFor(receiver);
			if(found)break;
		}
		if("isFavorite".equals(property)){
			return found;
		}
		if("notFavorite".equals(property)){
			return !found;
		}
		return false;
	}

}
