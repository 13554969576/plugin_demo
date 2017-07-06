package com.tongyx.plugin.favorites.model;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;

public class FavoriteResource extends AbstractFavoriteItem {

	private FavoriteItemType type;
	private IResource resource;
	private String name;	
	
	
	public FavoriteResource(FavoriteItemType type, IResource resource) {	
		this.type = type;
		this.resource = resource;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getAdapter(Class<T> adapter) {		
		return adapter.isInstance(resource)?(T)resource:Platform.getAdapterManager().getAdapter(this, adapter);
	}

	@Override
	public String getName() {		
		return name==null?name=resource.getName():name;
	}

	@Override
	public void setName(String name) {
		this.name=name;		
	}

	@Override
	public String getLocation() {
		IPath path = resource.getLocation().removeLastSegments(1);		
		return path.segmentCount()==0?"":path.toString();
	}

	
	@Override
	public boolean isFavoriteFor(Object obj) {		
		return resource.equals(obj);
	}

	@Override
	public FavoriteItemType getType() {		
		return type;
	}

	@Override
	public String getInfo() {		
		return resource.getFullPath().toOSString();
	}
	
	@Override
	public boolean equals(Object obj) {
		return this == obj||((obj instanceof FavoriteResource) && resource.equals(((FavoriteResource)obj).resource));
	}

	@Override
	public int hashCode() {		
		return resource.hashCode();
	}
	
	public static FavoriteResource loadFavorite(FavoriteItemType type, String info){
		IResource res = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(info));
		return res==null?null:new FavoriteResource(type, res);
	}
}
