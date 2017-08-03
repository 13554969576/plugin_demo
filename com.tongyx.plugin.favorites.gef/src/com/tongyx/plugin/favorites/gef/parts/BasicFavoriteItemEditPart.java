package com.tongyx.plugin.favorites.gef.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResource;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;

import com.tongyx.plugin.favorites.model.AbstractFavoriteItem;

public class BasicFavoriteItemEditPart extends AbstractFavoritesNodeEditPart{
	
	private final List<FavoritesConnection> modelSourceConnections;
	private final Label label = new Label();
	private static final Insets CLIENT_AREA_INSETS = new Insets(10, 10, 21, 21);
	
	
	public BasicFavoriteItemEditPart(AbstractFavoriteItem item) {
		setModel(item);
		label.setText(item.getName());
		label.setIcon(item.getType().getImage());
		IResource res = item.getAdapter(IResource.class);
		modelSourceConnections = new ArrayList<>();
		modelSourceConnections.add(new FavoritesConnection(item, res));
	}
	
	public AbstractFavoriteItem getAbstractFavoriteItem(){
		return (AbstractFavoriteItem)getModel();
	}

	@Override
	protected IFigure createFigure() {	
		RoundedRectangle figure = new RoundedRectangle(){
			@SuppressWarnings("deprecation")
			@Override
			public Rectangle getClientArea(Rectangle rect) {
				Rectangle clientArea = super.getClientArea(rect);
				clientArea.crop(CLIENT_AREA_INSETS);
				return clientArea;
			}
		};
		figure.setSize(150,40);
		FlowLayout layout = new FlowLayout();
		layout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
		layout.setMinorAlignment(FlowLayout.ALIGN_CENTER);
		figure.setLayoutManager(layout);
		figure.add(label);
		figure.setToolTip(createToolTipLabel());
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new FavoritesItemEditPolicy());
	}

	public List<FavoritesConnection> getModelSourceConnections() {
		return modelSourceConnections;
	}

	@Override
	public String getSortKey() {
		AbstractFavoriteItem item = getAbstractFavoriteItem();
		IResource res = item.getAdapter(IResource.class);
		return res.getName() + "," + item.getName();
	}
}
