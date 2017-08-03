package com.tongyx.plugin.favorites.gef.parts;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartListener;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;

public class FavoritesConnectionEditPart extends AbstractConnectionEditPart{
	
	public FavoritesConnectionEditPart(FavoritesConnection connection) {
		setModel(connection);
	}
	
	public FavoritesConnection getFavoritesConnection(){
		return (FavoritesConnection)getModel();
	}

	@Override
	protected void createEditPolicies() {
		
		
	}
	
	@Override
	public void activate() {		
		super.activate();
		final EditPart manager = (EditPart)getParent().getChildren().get(0);
		for(Object o: manager.getChildren()){
			AbstractFavoritesNodeEditPart child = (AbstractFavoritesNodeEditPart)o;
			if(child.addFavoritesTargetConnection(this))return;
		}
		manager.addEditPartListener(new EditPartListener.Stub(){
			@Override
			public void childAdded(EditPart editPart, int index) {
				AbstractFavoritesNodeEditPart child = (AbstractFavoritesNodeEditPart)editPart;
				if(child.addFavoritesTargetConnection(FavoritesConnectionEditPart.this))manager.removeEditPartListener(this);				
			}
		});
	}
	
	@Override
	public void deactivate() {
		final EditPart manager = (EditPart)getParent().getChildren().get(0);
		for(Object o: manager.getChildren()){
			AbstractFavoritesNodeEditPart child = (AbstractFavoritesNodeEditPart)o;
			if(child.removeFavoritesTargetConnection(this))break;
		}
		super.deactivate();
	}
	
	@Override
	protected IFigure createFigure() {
		PolylineConnection conn = (PolylineConnection)super.createFigure();
		conn.setTargetDecoration(new PolygonDecoration());
		return conn;
	}

}
