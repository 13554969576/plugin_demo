package com.tongyx.plugin.favorites.handlers.editors;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import com.tongyx.plugin.favorites.model.PropertyCategory;
import com.tongyx.plugin.favorites.model.PropertyElement;
import com.tongyx.plugin.favorites.model.PropertyEntry;
import com.tongyx.plugin.favorites.model.PropertyParentElement;

public class DeleteOperation extends AbstractOperation{
	
	private final PropertyElement[] elements;
	private PropertyElement[] parents;
	private int[] indexes;
	
	public DeleteOperation(PropertyElement[] elements) {
		super(getLabelFor(elements));
		this.elements=elements;
	}
	
	private static String getLabelFor(PropertyElement[] elements){
		if(elements!=null && elements.length == 1){
			PropertyElement ele = elements[0];
			if(ele instanceof PropertyEntry){
				return "Remove property " + ((PropertyEntry)ele).getKey();
			} else if (ele instanceof PropertyCategory){
				return "Remove category " + ((PropertyCategory)ele).getName();
			}
		}		
		return "Remove properties";
	}

	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		if(info !=null){
			Shell shell = (Shell)info.getAdapter(Shell.class);
			if(shell!=null){
				if(!MessageDialog.openQuestion(shell, "Remove properties", "Do you want to remove the currently selected properties?"))
					return Status.CANCEL_STATUS;
			}
		}
		return redo(monitor,info);
	}

	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		parents = new PropertyElement[elements.length];
		indexes = new int[elements.length];
		if(monitor!=null)monitor.beginTask("Remove properties", elements.length);
		Shell shell = info.getAdapter(Shell.class);
		shell.setRedraw(false);
		try{
			for(int i=elements.length-1;i>=0;i--){
				parents[i]=elements[i].getParent();
				PropertyElement[] nodes = parents[i].getChildren();
				for(int index=0;index<nodes.length;index++){
					if(nodes[index]==elements[i]){
						indexes[i]=index;
						break;
					}
				}
				elements[i].remove();
				if(monitor!=null)monitor.worked(1);
			}
		} finally {
			shell.setRedraw(true);
		}
		if(monitor!=null)monitor.done();
		return Status.OK_STATUS;
	}

	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info) throws ExecutionException {
		Shell shell = info.getAdapter(Shell.class);
		shell.setRedraw(false);
		try{
			for(int i=0;i<elements.length;i++){
				if(parents[i] instanceof PropertyParentElement){
					((PropertyParentElement)parents[i]).addChild(indexes[i],elements[i]);
				}
			}
		} finally {
			shell.setRedraw(true);
		}
		return Status.OK_STATUS;
	}

}
