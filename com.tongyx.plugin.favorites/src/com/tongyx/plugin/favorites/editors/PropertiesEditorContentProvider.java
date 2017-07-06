package com.tongyx.plugin.favorites.editors;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.tongyx.plugin.favorites.model.PropertyElement;

public class PropertiesEditorContentProvider implements ITreeContentProvider{

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}
	
	@Override
	public Object[] getChildren(Object element) {		
		if(element instanceof PropertyElement){
			return ((PropertyElement)element).getChildren();
		}
		return null;
	}

	@Override
	public Object[] getElements(Object element) {		
		return getChildren(element);
	}

	@Override
	public Object getParent(Object element) {		
		if(element instanceof PropertyElement){
			return ((PropertyElement)element).getParent();
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof PropertyElement){
			return ((PropertyElement)element).getChildren().length>0;
		}
		return false;
	}

}
