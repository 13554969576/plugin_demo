package com.tongyx.plugin.favorites.editors;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.tongyx.plugin.favorites.model.PropertyCategory;
import com.tongyx.plugin.favorites.model.PropertyEntry;

public class PropertiesEditorlabelProvider extends LabelProvider implements ITableLabelProvider{

	@Override
	public Image getColumnImage(Object element, int columnIdx) {		
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIdx) {
		if(element==null)return "<null>";
		if(element instanceof PropertyCategory){
			PropertyCategory c = (PropertyCategory)element;
			return columnIdx==0?c.getName():"";			
		} else if(element instanceof PropertyEntry){
			PropertyEntry p = (PropertyEntry)element;
			return columnIdx==0?p.getKey():p.getValue();			
		}
		
		return element.toString();
	}
}
