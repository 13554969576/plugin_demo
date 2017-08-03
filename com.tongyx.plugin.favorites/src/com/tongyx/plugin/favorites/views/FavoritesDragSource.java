package com.tongyx.plugin.favorites.views;

import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.part.ResourceTransfer;

import com.tongyx.plugin.favorites.handlers.CopyFavoritesHandler;

public class FavoritesDragSource implements DragSourceListener{
private final TableViewer viewer;
	
	
	public FavoritesDragSource(TableViewer viewer) {		
		this.viewer = viewer;
		DragSource source = new DragSource(viewer.getControl(),DND.DROP_COPY);
		source.setTransfer(new Transfer[]{TextTransfer.getInstance(),ResourceTransfer.getInstance(),JavaUI.getJavaElementClipboardTransfer()});
		source.addDragListener(this);
	}


	@Override
	public void dragFinished(DragSourceEvent event) {		
		
	}


	@Override
	public void dragSetData(DragSourceEvent event) {
		Object[] objects = ((IStructuredSelection)viewer.getSelection()).toArray();
		if(ResourceTransfer.getInstance().isSupportedType(event.dataType)){
			event.data=CopyFavoritesHandler.asResources(objects);
		} else if(TextTransfer.getInstance().isSupportedType(event.dataType)){
			event.data=CopyFavoritesHandler.asText(objects);
		}
	}


	@Override
	public void dragStart(DragSourceEvent event) {
		event.doit=!viewer.getSelection().isEmpty();
		
	}
}
