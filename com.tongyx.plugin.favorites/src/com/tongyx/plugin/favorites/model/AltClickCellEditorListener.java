package com.tongyx.plugin.favorites.model;

import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationListener;
import org.eclipse.jface.viewers.ColumnViewerEditorDeactivationEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;

public class AltClickCellEditorListener extends ColumnViewerEditorActivationListener{

	@Override
	public void beforeEditorActivated(ColumnViewerEditorActivationEvent event) {
		if(event.eventType==ColumnViewerEditorActivationEvent.MOUSE_CLICK_SELECTION){
			if(!(event.sourceEvent instanceof MouseEvent)){
				event.cancel=true;
			} else {
				MouseEvent e = (MouseEvent)event.sourceEvent;
				if((e.stateMask & SWT.ALT)==0)
					event.cancel=true;
			}
		} else if(event.eventType != ColumnViewerEditorActivationEvent.PROGRAMMATIC){
			event.cancel=true;
		}
		
	}

	@Override
	public void afterEditorActivated(ColumnViewerEditorActivationEvent event) {}

	@Override
	public void beforeEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {}

	@Override
	public void afterEditorDeactivated(ColumnViewerEditorDeactivationEvent event) {}

}
