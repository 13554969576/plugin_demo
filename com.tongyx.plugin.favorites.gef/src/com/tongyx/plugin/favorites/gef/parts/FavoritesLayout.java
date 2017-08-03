package com.tongyx.plugin.favorites.gef.parts;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.eclipse.draw2d.AbstractLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

public class FavoritesLayout extends AbstractLayout{
	
	private final Map<IFigure, String> constraints = new HashMap<>();

	@Override
	public Object getConstraint(IFigure child) {
		return constraints.get(child);
	}
	
	
	@Override
	public void setConstraint(IFigure child, Object constraint) {
		super.setConstraint(child, constraint);
		if(constraint instanceof String){
			constraints.put(child, (String)constraint);
		}
	}
	
	@Override
	public void remove(IFigure child) {
		super.remove(child);
		constraints.remove(child);
	}
	
	@Override
	public void layout(IFigure parent) {
		TreeMap<String, IFigure> childen = new TreeMap<>();
		for(Entry<IFigure, String> child : constraints.entrySet()){
			childen.put(child.getValue(), child.getKey());
		}
		int y = 5,w=0;
		for(Entry<String, IFigure> child: childen.entrySet()){
			IFigure figure = child.getValue();
			Point offset;
			Dimension size = figure.getSize();
			if(figure instanceof ResourceFigure){
				offset = new Point(5,y);
			} else {
				offset = new Point(200,y);
				y += size.height + 5;
			}
			w = Math.max(w, offset.x + size.width);
			figure.setBounds(new Rectangle(offset,size));
		}
		parent.setBounds(new Rectangle(0,0,w+5,y));
	}


	@Override
	protected Dimension calculatePreferredSize(IFigure figure, int x, int y) {		
		return null;
	}
	
	
	
	
	
	
	
	

}
