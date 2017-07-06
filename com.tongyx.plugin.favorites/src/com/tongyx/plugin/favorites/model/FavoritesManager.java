package com.tongyx.plugin.favorites.model;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;

import com.tongyx.plugin.favorites.utils.Converter;
import com.tongyx.plugin.favorites.utils.EnvUtil;

public class FavoritesManager implements IResourceChangeListener{
	private static final String TAG_ROOT="Favorites";
	private static final String TAG_TYPE="Favorite";
	private static final String TAG_ITEMTYPEID="TypeId";
	private static final String TAG_INFO="Info";
	private static final String TAG_COLOR="Color";
	private static final String CONFIG_FILE_NAME="favorites.xml";
	private static FavoritesManager manager;
	private Collection<IFavoriteItem> favorites;
	private List<IFavoritesMamagerListener> listeners = new ArrayList<>();
	private List<IFavoritesListener> itemListeners = new ArrayList<>();	
	
	private FavoritesManager(){
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);		
	}
	
	public static FavoritesManager getManager(){
		if(manager==null)
		{
			synchronized (FavoritesManager.class) {
				if(manager==null)manager= new FavoritesManager();
			}
		}
		return manager;
	}
	
	public void addFavoritesManagerListener(IFavoritesMamagerListener listener) {
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}
	
	public void removeFavoritesManagerListener(IFavoritesMamagerListener listener) {
		listeners.remove(listener);
	}
	
	public void addFavoritesListener(IFavoritesListener listener) {
		if(!itemListeners.contains(listener)){
			itemListeners.add(listener);
		}
	}
	
	public void removeFavoritesListener(IFavoritesListener listener) {
		itemListeners.remove(listener);
	}
	
	private void fireFavoritesChanged(IFavoriteItem[] added, IFavoriteItem[] removed){
		FavoritesManagerEvent event = new FavoritesManagerEvent(this, added, removed);
		for(IFavoritesMamagerListener listener: listeners){
			listener.favoritesChanged(event);
		}
	}
	
	public void fireFavoritesItemChanged(IFavoriteItem item){
		for(IFavoritesListener listener: itemListeners){
			listener.favoritesItemChanged(item);
		}
	}
	
	public IFavoriteItem[] getFavorites(){
		if(favorites==null){
			synchronized (FavoritesManager.class) {
				if(favorites==null)loadFavorites();
			}
		}
		return favorites.toArray(new IFavoriteItem[favorites.size()]);
	}
	
	private void loadFavorites(){
		//IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		//for(IProject project: projects){			
		//		favorites.add(new FavoriteResource(FavoriteItemType.WORKBENCH_PROJECT, project));
		//}
		favorites = new HashSet<>();
		FileReader reader = null;
		try {
			File config = EnvUtil.getStateLocation().append(CONFIG_FILE_NAME).toFile();
			if(!config.exists())return;
			reader=new FileReader(config);
			XMLMemento memento = XMLMemento.createReadRoot(reader);
			loadFavorites(memento);
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (WorkbenchException e) {			
			e.printStackTrace();
		} finally{
			if(reader!=null)
				try {
					reader.close();
				} catch (IOException e) {					
					e.printStackTrace();
				}
		}

	}
	
	private void loadFavorites(XMLMemento memento){
		IMemento[] mementos = memento.getChildren(TAG_TYPE);
		for(IMemento mem: mementos){
			IFavoriteItem item=loadFavorite(mem.getString(TAG_ITEMTYPEID), mem.getString(TAG_INFO)); 
			if(item!=null){
				String sColor = mem.getString(TAG_COLOR);
				item.setColor(Converter.str2Color(sColor));
				favorites.add(item);
			}
		}
	}
	
	public void addFavorites(Object[] objects){
		if(objects==null)return;
		getFavorites();
		Collection<IFavoriteItem> items = new HashSet<>(objects.length);	
		for(Object obj: objects){
			IFavoriteItem item = existingFavoriteFor(obj);
			if(item==null){
				item = newFavoriteFor(obj);
				if(item!=null && favorites.add(item)){
					items.add(item);
				}
			}
		}
		if(items.size()>0){
			IFavoriteItem[] added = items.toArray(new IFavoriteItem[items.size()]);
			fireFavoritesChanged(added,IFavoriteItem.NONE);
		}
	}
	
	public void removeFavorites(List<Object> objects){
		if(objects==null)return;
		if(favorites==null)loadFavorites();
		Collection<IFavoriteItem> items = new HashSet<>(objects.size());
		for(Object obj: objects){
			IFavoriteItem item = existingFavoriteFor(obj);
			if(item!=null && favorites.remove(item)){
				items.add(item);				
			}
		}
		if(items.size()>0){
			IFavoriteItem[] removed = items.toArray(new IFavoriteItem[items.size()]);
			fireFavoritesChanged(IFavoriteItem.NONE,removed);
		}
	}
	
	public IFavoriteItem existingFavoriteFor(Object obj){
		if(obj==null) return null;
		if(obj instanceof IFavoriteItem) return (IFavoriteItem)obj;
		for(IFavoriteItem item: favorites){
			if(item.isFavoriteFor(obj))return item;
		}
		return null;
	}
	
	public IFavoriteItem newFavoriteFor(Object obj){
		FavoriteItemType[] types=FavoriteItemType.getTypes();
		for(FavoriteItemType type: types){
			IFavoriteItem item=type.newFavorite(obj);
			if(item!=null)return item;
		}
		return null;
	}
	
	public IFavoriteItem loadFavorite(String typeId, String info){
		FavoriteItemType[] types=FavoriteItemType.getTypes();
		for(FavoriteItemType type: types){
			if(type.getId().equals(typeId)){
				return type.loadFavorite(info);
			}
		}
		return null;
	}
	
	public void saveState(){
		if(favorites==null)return;
		XMLMemento memento = XMLMemento.createWriteRoot(TAG_ROOT);
		saveFavorites(memento);
		PrintWriter writer=null;
		try {
			writer = new PrintWriter(EnvUtil.getStateLocation().append(CONFIG_FILE_NAME).toFile());
			memento.save(writer);
		} catch (FileNotFoundException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		} finally {
			if(writer!=null){
				writer.flush();
				writer.close();
			}
		}
	}
	
	private void saveFavorites(XMLMemento memento){
		for(IFavoriteItem item: favorites){
			IMemento mem = memento.createChild(TAG_TYPE);
			mem.putString(TAG_ITEMTYPEID, item.getType().getId());
			mem.putString(TAG_INFO, item.getInfo());
			if(item.getColor()!=AbstractFavoriteItem.getDefaultColor()){				
				mem.putString(TAG_COLOR, Converter.color2Str(item.getColor()));
			}			
		}
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		IResourceDeltaVisitor visitor = null;
		List<Object> toBeRemovedItems = new ArrayList<>();
		visitor = (delta)->{
			toBeRemovedItems.clear();	
			IFavoriteItem o = null;
			if(delta.getKind()==IResourceDelta.REMOVED && (o=existingFavoriteFor(delta.getResource())) != null){
				toBeRemovedItems.add(o);
			}
			return true;
		};
		try {
			event.getDelta().accept(visitor);
			if(toBeRemovedItems.size()>0)removeFavorites(toBeRemovedItems);
		} catch (CoreException e) {			
			e.printStackTrace();
		}
		
	}
	
	public static void shutdown(){
		if(manager!=null){
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(manager);;
			manager.saveState();
			manager=null;
		}
	}
	

}
