package com.tongyx.plugin.favorites.properties;

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.tongyx.plugin.favorites.model.AbstractFavoriteItem;
import com.tongyx.plugin.favorites.model.IFavoriteItem;
import com.tongyx.plugin.favorites.utils.Converter;

public class FavoritesItemPropertyPage extends ResourcePropertyPage{

	private ColorSelector colorInput;

	public FavoritesItemPropertyPage() {
		super();
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;		
		panel.setLayout(layout);
		
		Label label = new Label(panel, SWT.NONE);
		label.setLayoutData(new GridData());
		label.setText("Color of item in Favorites View:");
		
		colorInput = new ColorSelector(panel);
		colorInput.setColorValue(getColorPropertyValue());
		colorInput.getButton().setLayoutData(new GridData(100,SWT.DEFAULT));
		
		Composite subPanel = (Composite)super.createContents(panel);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.horizontalSpan = 2;
		subPanel.setLayoutData(gridData);
		return panel;
	}
	
	protected RGB getColorPropertyValue(){
		IFavoriteItem item = (IFavoriteItem)getElement();
		return item.getColor().getRGB();
	}
	
	protected void setColorPropertyValue(RGB rgb){
		IFavoriteItem item = (IFavoriteItem)getElement();
		Color color = Converter.rgb2Color(rgb);
		if(color.equals(AbstractFavoriteItem.getDefaultColor()))color=null;
		item.setColor(color);
	}
	
	@Override
	public boolean performOk() {
		setColorPropertyValue(colorInput.getColorValue());
		return super.performOk();
	}

}