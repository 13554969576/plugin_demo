package com.tongyx.plugin.favorites.views;

import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;

import com.tongyx.plugin.favorites.model.AbstractFavoriteItem;
import com.tongyx.plugin.favorites.model.IFavoriteItem;
import com.tongyx.plugin.favorites.utils.Converter;

public class FavoritesViewLabelProvider extends LabelProvider implements ITableLabelProvider,IColorProvider{

	@Override
	public Image getColumnImage(Object obj, int index) {
		return (index==0)&&(obj instanceof IFavoriteItem)?((IFavoriteItem)obj).getType().getImage():null;
	}

	@Override
	public String getColumnText(Object obj, int index) {
		if(index==0){
			return "";
		} else if(index==1){
			return obj==null?"":( (obj instanceof IFavoriteItem)?((IFavoriteItem)obj).getName():obj.toString() );
		} else if(index==2){
			return obj!=null && (obj instanceof IFavoriteItem)?((IFavoriteItem)obj).getLocation():"";
		} else {
			return "";
		}
	}
	
	@Override
	public Color getForeground(Object obj) {
		if(!(obj instanceof IFavoriteItem)) return AbstractFavoriteItem.getDefaultColor();
		return ((IFavoriteItem)obj).getColor();
	}

	@Override
	public Color getBackground(Object obj) {
		return Converter.rgb2Color(new RGB(255,255,255));
	}

}
