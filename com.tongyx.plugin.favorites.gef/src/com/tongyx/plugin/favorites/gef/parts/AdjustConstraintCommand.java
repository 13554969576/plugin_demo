package com.tongyx.plugin.favorites.gef.parts;

import org.eclipse.core.resources.IResource;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;

import com.tongyx.plugin.favorites.model.IFavoriteItem;

public class AdjustConstraintCommand extends Command{
	
	private FavoritesManagerEditpart manager;
	private Object model;
	private Rectangle newBounds, oldBounds;
	
	public AdjustConstraintCommand(GraphicalEditPart editPart, Rectangle constraint) {
		this.manager = (FavoritesManagerEditpart)editPart.getParent();
		this.model = editPart.getModel();
		this.newBounds = constraint;
		this.oldBounds = new Rectangle(editPart.getFigure().getBounds());
		setLabel(getOp(oldBounds, newBounds) + getName(editPart));
	}
	
	private String getOp(Rectangle oldBounds, Rectangle newBounds){
		if(oldBounds.getSize().equals(newBounds.getSize()))
			return "Move";
		else
			return "Resize";
	}
	
	private static String getName(GraphicalEditPart editPart){
		Object model = editPart.getModel();
		if(model instanceof IFavoriteItem)
			return ((IFavoriteItem)model).getName();
		else if(model instanceof IResource)
			return ((IResource)model).getName();
		else 
			return "Favorites Element";
	}
	
	@Override
	public void execute() {
		redo();
	}
	
	@Override
	public void redo() {
		GraphicalEditPart editPart = getEditPart();
		if(editPart==null)return;
		((GraphicalEditPart)editPart.getParent()).setLayoutConstraint(editPart, editPart.getFigure(), newBounds);
	}
	
	@Override
	public void undo() {
		GraphicalEditPart editPart = getEditPart();
		if(editPart==null)return;
		((GraphicalEditPart)editPart.getParent()).setLayoutConstraint(editPart, editPart.getFigure(), oldBounds);
	}
	
	private GraphicalEditPart getEditPart(){
		for(Object child: manager.getChildren()){
			if(model.equals(((GraphicalEditPart)child).getModel())) return (GraphicalEditPart)child;
		}
		return null;
	}

}
