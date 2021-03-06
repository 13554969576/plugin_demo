package com.tongyx.plugin.favorites.gef.parts;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

public class ResourceFigure extends Figure{
	private static final int FOLDED_CORNER_LENGTH = 12;
	private Label label;
	
	public ResourceFigure() {
		super();
		label = new Label();
		label.setTextAlignment(PositionConstants.LEFT);
		add(label);
		setLayoutManager(new FreeformLayout());
	}
	
	public void setText(String text){
		label.setText(text);
	}
	
	public void setImage(Image icon){
		label.setIcon(icon);
	}
	
	@Override
	protected void paintFigure(Graphics g) {		
		super.paintFigure(g);
		Rectangle r= getClientArea();
		g.drawLine(r.x, r.y, r.x+r.width-FOLDED_CORNER_LENGTH-1, r.y);
		g.drawLine(r.x, r.y, r.x, r.y+r.height-1);
		g.drawLine(r.x, r.y+r.height-1, r.x+r.width-1, r.y+r.height-1);
		g.drawLine(r.x+r.width-1, r.y+FOLDED_CORNER_LENGTH-1, r.x+r.width-1, r.y+r.height-1);
		
		setConstraint(label, new Rectangle(r.x+10,r.y+10,r.width-21,r.height-21));
		Point topLeftCorner, bottomLeftCorner,bottomRightCorner;
		PointList trianglePolygon;
		topLeftCorner = new Point(r.x+r.width-FOLDED_CORNER_LENGTH-1,r.y);
		bottomLeftCorner = new Point(r.x+r.width-FOLDED_CORNER_LENGTH-1,r.y+FOLDED_CORNER_LENGTH);
		bottomRightCorner = new Point(r.x+r.width-1,r.y+FOLDED_CORNER_LENGTH);
		
		trianglePolygon = new PointList(3);
		trianglePolygon.addPoint(topLeftCorner);
		trianglePolygon.addPoint(bottomLeftCorner);
		trianglePolygon.addPoint(bottomRightCorner);
		g.setBackgroundColor(ColorConstants.lightGray);
		g.fillPolygon(trianglePolygon);
		
		g.drawLine(topLeftCorner, bottomLeftCorner);
		g.drawLine(bottomLeftCorner, bottomRightCorner);
		g.setLineDash(new int[]{1});
		g.drawLine(bottomRightCorner, topLeftCorner);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
