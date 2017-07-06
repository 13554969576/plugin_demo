package com.tongyx.plugin.favorites.contributions;

import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;

import com.tongyx.plugin.favorites.handlers.RemoveFavoritesHandler;
import com.tongyx.plugin.favorites.views.FavoritesView;

public class RemoveFavoritesContributionItem extends ContributionItem{
	private static Logger log = Logger.getLogger(RemoveFavoritesContributionItem.class.getName());
	
	private final FavoritesView view;
	private final IHandler handler;
	boolean enabled =false;
	private MenuItem menuItem;
	private ToolItem toolItem;
	
	public RemoveFavoritesContributionItem(FavoritesView view,IHandler handler){
		this.view=view;
		this.handler=handler;
		view.addSelectionChangedListener((e)->{enabled=!e.getSelection().isEmpty();updateEnablement();});
	}
	
	@Override
	public void fill(Menu menu,int idx){
		menuItem=new MenuItem(menu, SWT.NONE,idx);
		menuItem.setText("Remove");		
		menuItem.addSelectionListener(new SelectionAdapter() {		
			@Override
			public void widgetSelected(SelectionEvent event) {
				run();				
			}		
		});
		updateEnablement();
	}
	
	@Override
	public void fill(ToolBar parent, int index) {
		toolItem=new ToolItem(parent, SWT.None,index);
		toolItem.setToolTipText("Remove the selected favorite items");
		toolItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				run();
			}
		});
		updateEnablement();
	
	}
	
	private void updateEnablement(){
		Image img = PlatformUI.getWorkbench().getSharedImages().getImage(enabled?ISharedImages.IMG_TOOL_DELETE:ISharedImages.IMG_TOOL_DELETE_DISABLED);
		if(menuItem!=null){
			menuItem.setImage(img);
			menuItem.setEnabled(enabled);
		}
		if(toolItem!=null){
			toolItem.setImage(img);
			toolItem.setEnabled(enabled);
		}
	}
	
	public void run(){
		final IHandlerService handlerService=view.getViewSite().getService(IHandlerService.class);
		IEvaluationContext context = handlerService.createContextSnapshot(true);
		ExecutionEvent event = new ExecutionEvent(null, Collections.EMPTY_MAP, null,context);
		try{
			handler.execute(event);
		}catch(ExecutionException e){
			log.log(Level.WARNING, e.getMessage(), e);
		}
	}
	
	public RemoveFavoritesHandler getHandler(){
		return (RemoveFavoritesHandler)handler;
	}

}
