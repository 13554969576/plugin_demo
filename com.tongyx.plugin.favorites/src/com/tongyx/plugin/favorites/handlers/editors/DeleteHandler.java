package com.tongyx.plugin.favorites.handlers.editors;

import java.util.List;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.handlers.HandlerUtil;

import com.tongyx.plugin.favorites.editors.PropertiesEditor;
import com.tongyx.plugin.favorites.model.PropertyElement;

public class DeleteHandler extends AbstractHandler{

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		System.out.println("Excuting DeleteHandler...");
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if(!(selection instanceof IStructuredSelection))return null;
		IEditorPart editor = HandlerUtil.getActiveEditor(event);
		if(!(editor instanceof PropertiesEditor))return null;
		List<PropertyElement> elements = (List<PropertyElement>)((IStructuredSelection)selection).toList();
		return execute((PropertiesEditor)editor,elements);
		//return runInLocal((PropertiesEditor)editor, elements);
	}
	
	protected Object execute(PropertiesEditor editor, List<PropertyElement> elements){
		if(elements==null) return null;
		DeleteOperation op = new DeleteOperation(elements.toArray(new PropertyElement[elements.size()]));
		op.addContext(editor.getUndoContext());
		IProgressMonitor monitor = editor.getEditorSite().getActionBars().getStatusLineManager().getProgressMonitor();
		IAdaptable info = new IAdaptable() {			
			@SuppressWarnings("unchecked")
			@Override
			public <T> T getAdapter(Class<T> clazz) {
				if(Shell.class==clazz)
					return (T)editor.getSite().getShell();
				else					
					return null;
			}
		};
		try{
			editor.getOperationHistory().execute(op,monitor,info);
		} catch (ExecutionException e){
			MessageDialog.openError(editor.getSite().getShell(), "Remove Properties Error", "Exception while removing properties: " + e.getMessage());
		}
		return null;	
	}
	
	protected Object runInLocal(PropertiesEditor editor, List<PropertyElement> elements){
		if(elements==null) return null;
		IProgressMonitor monitor = editor.getEditorSite().getActionBars().getStatusLineManager().getProgressMonitor();
		if(monitor!=null)monitor.beginTask("Remove properties", elements.size());
		Shell shell = editor.getSite().getShell();
		shell.setRedraw(false);
		try{
			for(PropertyElement ele: elements){
				ele.remove();
				if(monitor!=null)monitor.worked(1);
			}
			
		} finally {
			shell.setRedraw(true);
		}
		if(monitor!=null)monitor.done();
		return null;
	}
}
