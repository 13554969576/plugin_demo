package com.tongyx.plugin.favorites.model;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetEntry;
import org.eclipse.ui.views.properties.IPropertySource2;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.tongyx.plugin.favorites.Activator;
import com.tongyx.plugin.favorites.utils.Converter;

public abstract class AbstractFavoriteItem implements IFavoriteItem,IPropertySource2{
	private Color color;
	private static Color defaultColor;	
	private static final String PROPERTY_ID_COLOR="favority.color";
	private static final String PROPERTY_ID_HASH="favority.hash";
	private static final ColorPropertyDescriptor COLOR_PROPERTY_DESCRIPTOR = new ColorPropertyDescriptor(PROPERTY_ID_COLOR, "Color");
	private static final TextPropertyDescriptor HASH_PROPERTY_DESCRIPTOR = new TextPropertyDescriptor(PROPERTY_ID_HASH, "Hash Code");
	private static final IPropertyDescriptor[] DESCRIPTORS = new IPropertyDescriptor[]{COLOR_PROPERTY_DESCRIPTOR,HASH_PROPERTY_DESCRIPTOR};
	
	static {
		HASH_PROPERTY_DESCRIPTOR.setCategory("Other");
		HASH_PROPERTY_DESCRIPTOR.setFilterFlags(new String[]{IPropertySheetEntry.FILTER_ID_EXPERT});
		HASH_PROPERTY_DESCRIPTOR.setAlwaysIncompatible(true);		
		Activator.getDefault().getPreferenceStore().getString("defaultColor");
	}
	
	
	@Override
	public Color getColor() {
		if(color==null) return getDefaultColor();
		return color;
	}
	
	@Override
	public void setColor(Color color) {
		if(color==this.color)return;
		if(color==getDefaultColor()){
			color=null;
		} else {
			this.color = color;
		}		
		FavoritesManager.getManager().fireFavoritesItemChanged(this);		
	}
	
	public static Color getDefaultColor(){
		if(defaultColor==null){
			String sRgb = Activator.getDefault().getPreferenceStore().getString("defaultColor");
			defaultColor = Converter.str2Color(sRgb);
			if(defaultColor==null) defaultColor = Converter.rgb2Color(new RGB(0,0,0));			
		}
		return defaultColor;
	}
	
	public static void setDefaultColor(Color color){
		if(color==null)color=Converter.rgb2Color(new RGB(0,0,0));
		defaultColor=color;
		Activator.getDefault().getPreferenceStore().setValue("defaultColor", Converter.color2Str(color));
	}

	
	@Override
	public Object getEditableValue() {		
		return this;
	}
	
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {		
		return DESCRIPTORS;
	}
	
	@Override
	public Object getPropertyValue(Object id) {
		if(PROPERTY_ID_COLOR.equals(id)){
			return getColor().getRGB();
		} else if(PROPERTY_ID_HASH.equals(id)) {
			return hashCode();
		}
		return null;
	}
	
	@Override
	public boolean isPropertySet(Object id) {
		if(PROPERTY_ID_COLOR.equals(id)){
			return color != null;
		} else if(PROPERTY_ID_HASH.equals(id)) {
			return true;
		}
		return false;
	}
	
	@Override
	public void resetPropertyValue(Object id) {
		if(PROPERTY_ID_COLOR.equals(id)){
			setColor(null);
		}		
	}
	
	@Override
	public void setPropertyValue(Object id, Object vaue) {
		if(PROPERTY_ID_COLOR.equals(id)){
			setColor(Converter.rgb2Color((RGB)vaue));
		}
	}
	
	@Override
	public boolean isPropertyResettable(Object id) {
		if(PROPERTY_ID_COLOR.equals(id)){
			return true;
		} else {
			return false;
		}
	}
	
	public static void main(String[] args){
		System.out.println(null==null);
	}

}
