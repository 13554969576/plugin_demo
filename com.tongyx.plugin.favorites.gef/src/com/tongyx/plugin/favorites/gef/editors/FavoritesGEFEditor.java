package com.tongyx.plugin.favorites.gef.editors;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ConnectionLayer;
import org.eclipse.draw2d.ShortestPathConnectionRouter;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.actions.ActionFactory;

import com.tongyx.plugin.favorites.gef.parts.FavoritesManagerEditpart;
import com.tongyx.plugin.favorites.gef.parts.FavoritiesEditPartFactory;
import com.tongyx.plugin.favorites.model.FavoritesManager;

public class FavoritesGEFEditor extends GraphicalEditorWithFlyoutPalette {

	public static final String ID = "com.tongyx.plugin.favorites.gef.editors.FavoritesGEFEditor";
	
	private FavoritesManager model;
	
	public FavoritesGEFEditor(){
		setEditDomain(new DefaultEditDomain(this));
	}
	
	@Override
	protected void setInput(IEditorInput input) {		
		super.setInput(input);
		model = ((FavoritesGEFEditorInput)input).getModel();
	}
	
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(new FavoritiesEditPartFactory(true));
		viewer.setRootEditPart(new ScalableFreeformRootEditPart());
	}
	
	@Override
	protected void initializeGraphicalViewer() {		
		super.initializeGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setContents(model);
		
		ScalableFreeformRootEditPart rootEditPart = (ScalableFreeformRootEditPart)viewer.getRootEditPart();
		FavoritesManagerEditpart managePart = (FavoritesManagerEditpart)rootEditPart.getChildren().get(0);
		ConnectionLayer connectionLayer = (ConnectionLayer)rootEditPart.getLayer(LayerConstants.CONNECTION_LAYER);
		connectionLayer.setConnectionRouter(new ShortestPathConnectionRouter(managePart.getFigure()));
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class type) {
		if(type==FavoritesManager.class) return model;
		return super.getAdapter(type);
	}
	
	@Override
	public boolean isDirty() {		
		return false;
	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		
	}
	
	@Override
	protected PaletteRoot getPaletteRoot() {		
		return FavoritesEdtiorPaletteFactory.createPalette();
	}
	
	public void deleteSelection(){
		getActionRegistry().getAction(ActionFactory.DELETE.getId()).run();
	}

}
