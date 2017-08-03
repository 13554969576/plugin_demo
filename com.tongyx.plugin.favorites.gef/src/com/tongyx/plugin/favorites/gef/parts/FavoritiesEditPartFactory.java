package com.tongyx.plugin.favorites.gef.parts;

import org.eclipse.core.resources.IResource;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;

import com.tongyx.plugin.favorites.model.AbstractFavoriteItem;
import com.tongyx.plugin.favorites.model.FavoritesManager;

public class FavoritiesEditPartFactory implements EditPartFactory {
	private final boolean editable;
	
	public FavoritiesEditPartFactory(boolean editable) {
		this.editable = editable;
	}
	

	@Override
	public EditPart createEditPart(EditPart part, Object model) {
		if(model instanceof FavoritesManager)
			return new FavoritesManagerEditpart((FavoritesManager)model,editable);
		else if(model instanceof AbstractFavoriteItem)
			return new BasicFavoriteItemEditPart((AbstractFavoriteItem)model);
		else if(model instanceof IResource)
			return new ResourceEditPart((IResource)model);
		else if(model instanceof FavoritesConnection)
			return new FavoritesConnectionEditPart((FavoritesConnection)model);
		else
			throw new IllegalStateException("Could't create an edit part for the model object: " + model.getClass().getName());
		
	}

}
