package com.tongyx.plugin.favorites.gef.parts;

import org.eclipse.draw2d.Label;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

public abstract class AbstractFavoritesGraphicalEditPart extends AbstractGraphicalEditPart {
	protected Label createToolTipLabel() {
		Label label = new Label();
		String longName = getClass().getName();
		String shortName = longName.substring(longName.lastIndexOf('.') + 1);
		label.setText(shortName);
		return label;
	}

}
