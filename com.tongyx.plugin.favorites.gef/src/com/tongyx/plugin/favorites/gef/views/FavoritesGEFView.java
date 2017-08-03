package com.tongyx.plugin.favorites.gef.views;


import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import com.tongyx.plugin.favorites.gef.parts.FavoritesManagerEditpart;
import com.tongyx.plugin.favorites.gef.parts.FavoritiesEditPartFactory;
import com.tongyx.plugin.favorites.model.FavoritesManager;

public class FavoritesGEFView extends ViewPart {

	public static final String ID = "com.tongyx.plugin.favorites.gef.views.FavoritesGEFView";
	
	private ScrollingGraphicalViewer gv;
	
	@Override
	public void createPartControl(Composite parent) {
		ScalableFreeformRootEditPart rootEditPart = new ScalableFreeformRootEditPart();
		gv = new ScrollingGraphicalViewer();
		gv.createControl(parent);
		gv.getControl().setBackground(ColorConstants.listBackground);
		gv.setEditPartFactory(new FavoritiesEditPartFactory(false));
		gv.setContents(FavoritesManager.getManager());	
		
		FavoritesManagerEditpart managerPart = (FavoritesManagerEditpart)rootEditPart.getChildren().get(0);
		ConnectionLayer connectionLayer  = (ConnectionLayer)rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER);
		connectionLayer.setConnectionRouter(new ShortestPathConnectionRouter(managerPart.getFigure()));
	}
	
	
	@Override
	public void setFocus() {
		gv.setContents(FavoritesManager.getManager());
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
