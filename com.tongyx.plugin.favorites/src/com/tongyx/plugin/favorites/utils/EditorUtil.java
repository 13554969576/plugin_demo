package com.tongyx.plugin.favorites.utils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

public class EditorUtil {
	public static void openEditor(IWorkbenchPage page, IStructuredSelection selection){
		for(Object element: selection.toList()){
			if(!(element instanceof IAdaptable))continue;
			IFile file = ((IAdaptable)element).getAdapter(IFile.class);
			if(file==null)continue;
			try{
				IDE.openEditor(page, file);
			}catch(PartInitException e){
				e.printStackTrace();
			}
					
		}
	}
	
	public static ITextEditor openTextEditor(IFile file){
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart part;
		try {
			part = IDE.openEditor(page, file,true);			
		} catch (PartInitException e) {			
			e.printStackTrace();
			return null;
		}
		return (part instanceof ITextEditor)?(ITextEditor)part:null;	
	}

}
