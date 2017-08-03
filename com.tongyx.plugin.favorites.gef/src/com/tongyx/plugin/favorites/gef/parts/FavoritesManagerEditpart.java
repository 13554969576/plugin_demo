package com.tongyx.plugin.favorites.gef.parts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.eclipse.core.resources.IResource;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;

import com.tongyx.plugin.favorites.model.FavoritesManager;
import com.tongyx.plugin.favorites.model.FavoritesManagerEvent;
import com.tongyx.plugin.favorites.model.IFavoriteItem;
import com.tongyx.plugin.favorites.model.IFavoritesMamagerListener;

@SuppressWarnings("rawtypes")
public class FavoritesManagerEditpart extends AbstractFavoritesGraphicalEditPart{
	
	private boolean editable;
	
	private final IFavoritesMamagerListener modelListener = (e)->{
		if(editable){
				updateChildren(e);
			} else {
				refresh();
			}
		
		};
	
	public FavoritesManagerEditpart(FavoritesManager manager, boolean editable ) {
		setModel(manager);
		this.editable = editable;
	}
	
	
	public FavoritesManager getFavoritesManager(){
		return (FavoritesManager)getModel();
	}
	
	@Override
	protected List getModelChildren() {
		IFavoriteItem[] items = getFavoritesManager().getFavorites();
		Collection<Object> result = new HashSet<>(items.length*2);
		for(IFavoriteItem item: items){
			result.add(item);
			result.add(item.getAdapter(IResource.class));
		}
		return new ArrayList<>(result);
	}

	@Override
	protected IFigure createFigure() {	
		IFigure figure;
		if(editable){
			figure = new FreeformLayer();
			figure.setLayoutManager(new FreeformLayout());
		} else {
			figure = new Panel();
			figure.setLayoutManager(new FavoritesLayout());
		}
		figure.setToolTip(createToolTipLabel());
		return figure;
	}
	
	@Override
	protected void addChild(EditPart child, int index) {		
		super.addChild(child, index);
		if(editable)return;
		AbstractFavoritesNodeEditPart childEditPart = (AbstractFavoritesNodeEditPart)child;
		IFigure childFigure = childEditPart.getFigure();
		LayoutManager layout = getFigure().getLayoutManager();
		String constraint = childEditPart.getSortKey();
		layout.setConstraint(childFigure, constraint);
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new FavoritesLayoutEditPolicy());
	}
	
	@Override
	public void activate() {		
		super.activate();
		if(editable)initEditorLayout();
		getFavoritesManager().addFavoritesManagerListener(modelListener);
	}
	
	@Override
	public void deactivate() {
		getFavoritesManager().removeFavoritesManagerListener(modelListener);
		super.deactivate();
	}
	
	private void initEditorLayout(){
		LayoutManager layout = new FavoritesLayout();
		for(Object part : getChildren()){
			AbstractFavoritesNodeEditPart child = (AbstractFavoritesNodeEditPart)part;
			IFigure childFigure = child.getFigure();
			String constraint = child.getSortKey();
			layout.setConstraint(childFigure, constraint);
		}
		layout.layout(getFigure());
		layout = getFigure().getLayoutManager();
		for(Object part : getChildren()){
			AbstractFavoritesNodeEditPart child = (AbstractFavoritesNodeEditPart)part;
			IFigure childFigure = child.getFigure();
			Rectangle constraint = childFigure.getBounds();
			layout.setConstraint(childFigure, constraint);
		}
		
	}
	
	private void updateChildren(FavoritesManagerEvent event){
		Map<Object, GraphicalEditPart> modelPartMap = new HashMap<>();
		for(Object child: getChildren()){
			modelPartMap.put(((GraphicalEditPart)child).getModel(), (GraphicalEditPart)child);
		}
		
		for(IFavoriteItem item: event.getRemoved()){
			EditPart child = modelPartMap.get(item);
			if(child==null)continue;
			removeChild(child);
			
			IResource res = item.getAdapter(IResource.class);
			child = modelPartMap.get(res);
			if(child==null)continue;
			GraphicalEditPart rep = (GraphicalEditPart)child;
			if(rep.getTargetConnections().size()==0){
				removeChild(child);
			}
		}
		
		for(IFavoriteItem item: event.getAdded()){
			GraphicalEditPart child = modelPartMap.get(item);
			if(child !=null)continue;
			child = (GraphicalEditPart)createChild(item);
			setRandomChildLocation(child);
			addChild(child, getChildren().size());
			
			IResource res = item.getAdapter(IResource.class);
			child = modelPartMap.get(res);
			if(child != null)continue;
			child = (GraphicalEditPart)createChild(res);
			setRandomChildLocation(child);
			addChild(child, getChildren().size());
		}
	}

	private void setRandomChildLocation(GraphicalEditPart child){
		IFigure childFigure = child.getFigure();
		Random random = new Random();
		int x = random.nextInt() % 150 + 150;
		int y = random.nextInt() % 150 + 150;
		childFigure.setLocation(new Point(x,y));
		Rectangle constraint = childFigure.getBounds();
		LayoutManager layout = getFigure().getLayoutManager();
		layout.setConstraint(childFigure, constraint);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
}
