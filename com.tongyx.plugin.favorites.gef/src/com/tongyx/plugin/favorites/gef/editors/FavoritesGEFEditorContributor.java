package com.tongyx.plugin.favorites.gef.editors;

import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.ui.actions.ActionFactory;

public class FavoritesGEFEditorContributor extends ActionBarContributor{
	@Override
	protected void buildActions() {
		
	}
	
	@Override
	protected void declareGlobalActionKeys() {
		addGlobalActionKey(ActionFactory.UNDO.getId());
		addGlobalActionKey(ActionFactory.REDO.getId());
	}
}
