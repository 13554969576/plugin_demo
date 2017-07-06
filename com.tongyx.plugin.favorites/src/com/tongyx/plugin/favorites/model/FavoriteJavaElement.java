package com.tongyx.plugin.favorites.model;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class FavoriteJavaElement extends AbstractFavoriteItem {

	private FavoriteItemType type;
	private IJavaElement element;
	private String name;
	private static Logger log = Logger.getLogger(FavoriteJavaElement.class.getName());
	
	public FavoriteJavaElement(FavoriteItemType type, IJavaElement element) {		
		this.type = type;
		this.element = element;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {
		if(adapter.isInstance(element))return (T)element;
		if(adapter.isInstance(element.getResource()))return (T)element.getResource();
		return Platform.getAdapterManager().getAdapter(this, adapter);
	}

	@Override
	public String getName() {		
		if(name==null)name=element.getElementName();				
		return name;
	}

	@Override
	public void setName(String name) {
		this.name=name;
	}

	@Override
	public String getLocation() {
		try{
			IResource res=element.getUnderlyingResource();
			if(res!=null)
			{
				IPath path = res.getLocation().removeLastSegments(1);
				if(path.segmentCount()==0)return "";
				return path.toString();
			}
		} catch (JavaModelException e) {
			log.log(Level.WARNING, e.getMessage(), e);
		}
		return "";
	}

	@Override
	public boolean isFavoriteFor(Object obj) {		
		return element.equals(obj);
	}

	@Override
	public FavoriteItemType getType() {
		return type;
	}

	@Override
	public String getInfo() {
		try {
			return element.getUnderlyingResource().getFullPath().toString();
		} catch (JavaModelException e) {
			log.log(Level.WARNING, e.getMessage(), e);
			return null;
		}		
	}
	
	
	
	@Override
	public boolean equals(Object obj) {
		return this==obj || ( (obj instanceof FavoriteJavaElement) && element.equals( ((FavoriteJavaElement)obj).element) );		
	}

	@Override
	public int hashCode() {		
		return element.hashCode();
	}

	public static FavoriteJavaElement loadFavorite(FavoriteItemType type,String info){
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(info));
		if(res==null)return null;
		IJavaElement elem = JavaCore.create(res);
		if(elem==null)return null;
		return new FavoriteJavaElement(type, elem);
	}

}
