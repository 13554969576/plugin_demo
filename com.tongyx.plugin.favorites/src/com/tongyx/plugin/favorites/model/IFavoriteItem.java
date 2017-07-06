package com.tongyx.plugin.favorites.model;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.graphics.Color;

import com.tongyx.plugin.favorites.Activator;
import static com.tongyx.plugin.favorites.Constant.PLUGIN_ID;

public interface IFavoriteItem extends IAdaptable{
	QualifiedName COMMENT_PROPKEY =	new QualifiedName(PLUGIN_ID, "comment");	
	String getName();
	void setName(String name);
	String getLocation();	
	boolean isFavoriteFor(Object obj);
	FavoriteItemType getType();
	String getInfo();
	Color getColor();
	void setColor(Color color);
	static IFavoriteItem[] NONE = new IFavoriteItem[]{};
	static String getDefaultCommnt(){return Activator.getDefault().getPreferenceStore().getString("defaultComment");};
	static void setDefaultCommnt(String comment){ Activator.getDefault().getPreferenceStore().setValue("defaultComment",comment);};

}
