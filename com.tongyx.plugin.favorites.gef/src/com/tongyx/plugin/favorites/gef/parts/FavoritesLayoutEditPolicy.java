package com.tongyx.plugin.favorites.gef.parts;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;

import com.tongyx.plugin.favorites.model.FavoriteResource;
import com.tongyx.plugin.favorites.model.FavoritesManager;

public class FavoritesLayoutEditPolicy extends XYLayoutEditPolicy{
	
	@SuppressWarnings("deprecation")
	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		if(child instanceof AbstractFavoritesNodeEditPart){
			if(constraint instanceof Rectangle){
				return new AdjustConstraintCommand((AbstractFavoritesNodeEditPart)child,(Rectangle)constraint);
			}
		}
		return super.createChangeConstraintCommand(child, constraint);
	}
	

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if(request.getNewObject() == FavoriteResource.class){
			FavoritesManager manager =(FavoritesManager)getHost().getModel();
			return new FavoritesItemCreateCommand(manager);
		}
		
		return UnexecutableCommand.INSTANCE;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
