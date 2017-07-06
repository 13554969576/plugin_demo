package com.tongyx.plugin.favorites.preferences;

import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tongyx.plugin.favorites.model.AbstractFavoriteItem;
import com.tongyx.plugin.favorites.model.IFavoriteItem;
import com.tongyx.plugin.favorites.properties.FavoritesItemPropertyPage;
import com.tongyx.plugin.favorites.utils.Converter;

public class FavoriteDefaultPrefPage extends FavoritesItemPropertyPage implements IWorkbenchPreferencePage{

	@Override
	public void init(IWorkbench workbench) {
		
	}
	
	@Override
	protected RGB getColorPropertyValue() {		
		return AbstractFavoriteItem.getDefaultColor().getRGB();
	}
	
	@Override
	protected String getCommentPropertyValue() {		
		return IFavoriteItem.getDefaultCommnt();
	}
	
	@Override
	protected void setColorPropertyValue(RGB rgb) {		
		AbstractFavoriteItem.setDefaultColor(Converter.rgb2Color(rgb));
	}
	
	@Override
	protected void setCommentPropertyValue(String comment) {		
		IFavoriteItem.setDefaultCommnt(comment);
	}

}
