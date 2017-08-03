package com.tongyx.plugin.favorites.gef.editors;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PanningSelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;

import com.tongyx.plugin.favorites.model.FavoriteResource;

public class FavoritesEdtiorPaletteFactory {
	public static PaletteRoot createPalette(){
		PaletteRoot palette = new PaletteRoot();
		palette.add(createToolsGroup(palette));
		palette.add(createFavoritesDrawer());
		return palette;
	}
	
	private static PaletteContainer createToolsGroup(PaletteRoot palette){
		PaletteGroup tools = new PaletteGroup("Tools");		
		ToolEntry tool = new PanningSelectionToolEntry();
		tools.add(tool);
		palette.setDefaultEntry(tool);		
		tools.add(new MarqueeToolEntry());		
		return tools;
	}
	
	private static PaletteContainer createFavoritesDrawer(){
		PaletteDrawer drawer = new PaletteDrawer("Favorites Parts");
		FavoritesCreationFactory factory = new FavoritesCreationFactory(FavoriteResource.class);
		CombinedTemplateCreationEntry entry = new CombinedTemplateCreationEntry("Favorite Resource", 
				"Create a favorite resource",FavoriteResource.class, factory, null, null);
		drawer.add(entry);
		return drawer;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
