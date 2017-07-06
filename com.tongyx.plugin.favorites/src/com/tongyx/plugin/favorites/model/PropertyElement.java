package com.tongyx.plugin.favorites.model;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

public abstract class PropertyElement implements IAdaptable, Cloneable{
	
	public static final PropertyElement[] NO_CHILDREN = {};
	private  PropertyElement parent;	
	
	public PropertyElement getParent(){
		return parent;
	}

	@SuppressWarnings("unchecked")
	public <T extends PropertyElement> T getParentAs(Class<T> clazz) {
		return (T)parent;
	}

	public PropertyElement(PropertyElement parent) {
		this.parent=parent;
	}
	
	
	public abstract PropertyElement[] getChildren();
	public abstract void remove();
	
	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> clazz) {
		if(clazz==this.getClass()){
			return (T)this;
		} else if(clazz.isAssignableFrom(IContentOutlinePage.class)){
			return (T)new IContentOutlinePage() {
				
				@Override
				public void setSelection(ISelection arg0) {
					
				}
				
				@Override
				public void removeSelectionChangedListener(ISelectionChangedListener arg0) {
					
				}
				
				@Override
				public ISelection getSelection() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public void addSelectionChangedListener(ISelectionChangedListener arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void setFocus() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void setActionBars(IActionBars arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public Control getControl() {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public void dispose() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void createControl(Composite arg0) {
					// TODO Auto-generated method stub
					
				}
			};
		}
		return null;
	}
}
