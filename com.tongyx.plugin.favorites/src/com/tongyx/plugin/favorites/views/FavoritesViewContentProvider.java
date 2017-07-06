package com.tongyx.plugin.favorites.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;

import com.tongyx.plugin.favorites.model.FavoritesManager;
import com.tongyx.plugin.favorites.model.FavoritesManagerEvent;
import com.tongyx.plugin.favorites.model.IFavoriteItem;
import com.tongyx.plugin.favorites.model.IFavoritesListener;
import com.tongyx.plugin.favorites.model.IFavoritesMamagerListener;

public class FavoritesViewContentProvider implements IStructuredContentProvider, IFavoritesMamagerListener,IFavoritesListener {

	private TableViewer viewer;
	private FavoritesManager manager;
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput){
		this.viewer=(TableViewer)viewer;
		if(manager!=null)manager.removeFavoritesManagerListener(this);
		manager=(FavoritesManager)newInput;
		if(manager!=null){
			manager.addFavoritesManagerListener(this);
			manager.addFavoritesListener(this);
		}
	}
	
	@Override
	public void favoritesChanged(FavoritesManagerEvent event) {
		if(Display.getCurrent()!=null){
			updateUIByEnvent(event);
		} else {
			Display.getDefault().asyncExec(()->{updateUIByEnvent(event);});
		}
	}
	
	private void updateUIByEnvent(FavoritesManagerEvent event){
		viewer.getTable().setRedraw(false);
		viewer.remove(event.getRemoved());
		viewer.add(event.getAdded());
		viewer.getTable().setRedraw(true);
	}

	@Override
	public Object[] getElements(Object arg0) {	
		return manager.getFavorites();
	}
	
	@Override
	public void dispose(){
		if(manager!=null){
			manager.removeFavoritesManagerListener(this);
			manager.removeFavoritesListener(this);
		}
	}

	@Override
	public void favoritesItemChanged(IFavoriteItem item) {
		Display.getDefault().asyncExec(()->{viewer.update(item,null);});
	}
	
	
}
