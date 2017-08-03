package com.tongyx.plugin.favorites.gef.parts;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.tongyx.plugin.favorites.model.FavoritesManager;
import com.tongyx.plugin.favorites.model.IFavoriteItem;

public class FavoritesItemEditPolicy extends ComponentEditPolicy{

	@Override
	protected Command createDeleteCommand(GroupRequest request) {	
		FavoritesManager manager = ((FavoritesManagerEditpart)getHost().getParent()).getFavoritesManager();
		CompoundCommand delete = new CompoundCommand();
		for(Object part : request.getEditParts()){
			Object item = ((EditPart)part).getModel();
			if(!(item instanceof IFavoriteItem)){
				continue;
			}
			delete.add(new FavoritesItemDeleteCommand(manager,(IFavoriteItem)item));
		}
		return delete;
	}
}
