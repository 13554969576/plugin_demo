package com.tongyx.plugin.favorites.handlers;

import java.util.Collection;
import java.util.HashSet;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.part.ResourceTransfer;

import com.tongyx.plugin.favorites.model.IFavoriteItem;

public class CopyFavoritesHandler extends ClipboardHandler{
	
	@Override
	protected Object execute(ExecutionEvent e, Clipboard clipboard) {	
		ISelection selection = HandlerUtil.getCurrentSelection(e);
		if(selection instanceof IStructuredSelection){
			Object[] objects = ((IStructuredSelection)selection).toArray();
			if(objects.length>0){
				try{
					clipboard.setContents(new Object[]{asResources(objects),asText(objects)}, new Transfer[]{ResourceTransfer.getInstance(),TextTransfer.getInstance()});					
				} catch (SWTError err) {
					// 
				}
			}
		}
		return null;
	}
	
	public static IResource[] asResources(Object[] objects){
		Collection<IResource> resources = new HashSet<>(objects.length);
		for(Object o: objects){
			if(o instanceof IAdaptable){
				IResource res = ((IAdaptable)o).getAdapter(IResource.class);
				if(res!=null){
					resources.add(res);
				}
			}
		}
		return resources.toArray(new IResource[resources.size()]);
	}
	
	public static String asText(Object[] objects){
		StringBuilder sb = new StringBuilder();
		for(Object o: objects){
			if(o instanceof IFavoriteItem){
				sb.append(((IFavoriteItem)o).getName());				
			} else {
				if(o!=null)sb.append(o.toString());
			}
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
	
}
