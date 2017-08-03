package com.tongyx.plugin.favorites.gef.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.draw2d.IFigure;

public class ResourceEditPart extends AbstractFavoritesNodeEditPart{
	private final ResourceFigure resourceFigure = new ResourceFigure();
	
	
	private final List<FavoritesConnection> modelTargetConnections = new ArrayList<>();

	public ResourceEditPart(IResource resource) {
		setModel(resource);
		resourceFigure.setText(resource.getName());
	}
	
	public IResource getResource(){
		return (IResource)getModel();
	}
	
	@Override
	protected IFigure createFigure() {	
		resourceFigure.setSize(150,40);
		resourceFigure.setToolTip(createToolTipLabel());
		return resourceFigure;
	}

	@Override
	protected void createEditPolicies() {
	}

	public List<FavoritesConnection> getModelTargetConnections() {
		return modelTargetConnections;
	}
	
	@Override
	public boolean addFavoritesTargetConnection(FavoritesConnectionEditPart collectionPart) {
		FavoritesConnection conn = collectionPart.getFavoritesConnection();
		if(!conn.getResource().equals(getResource()))return false;
		modelTargetConnections.add(conn);
		addTargetConnection(collectionPart, 0);
		return true;
	}
	
	
	@Override
	public boolean removeFavoritesTargetConnection(FavoritesConnectionEditPart collectionPart) {
		if(!modelTargetConnections.remove(collectionPart.getModel()))return false;
		removeTargetConnection(collectionPart);
		return true;
	}

	@Override
	public String getSortKey() {		
		return getResource().getName();
	}
}
