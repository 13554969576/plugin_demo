package com.tongyx.plugin.favorites.gef.parts;

import java.util.Collections;

import org.eclipse.gef.commands.Command;

import com.tongyx.plugin.favorites.model.FavoritesManager;
import com.tongyx.plugin.favorites.model.IFavoriteItem;

public class FavoritesItemDeleteCommand extends Command{
	
	private final FavoritesManager manager;
	private final Object item;

	public FavoritesItemDeleteCommand(FavoritesManager manager, IFavoriteItem item) {
		this.manager = manager;
		this.item=item.getAdapter(Object.class);
		setLabel("Delete " + item.getName());
	}
	
	@Override
	public void execute() {
		redo();
	}
	
	@Override
	public void redo() {
		manager.removeFavorites(Collections.singletonList(item));
	}
	
	@Override
	public void undo() {
		manager.addFavorites(new Object[]{item});
	}
}
