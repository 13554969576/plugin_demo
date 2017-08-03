package com.tongyx.plugin.favorites.gef.editors;

import org.eclipse.gef.requests.CreationFactory;

public class FavoritesCreationFactory implements CreationFactory{
	
	private final Class<?> clazz;
	
	public FavoritesCreationFactory(Class<?> clazz){
		this.clazz = clazz;		
	}

	@Override
	public Object getNewObject() {		
		return clazz;
	}

	@Override
	public Object getObjectType() {
		return clazz;
	}

}
