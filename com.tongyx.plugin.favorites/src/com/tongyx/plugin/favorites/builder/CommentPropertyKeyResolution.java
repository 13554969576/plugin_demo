package com.tongyx.plugin.favorites.builder;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IMarkerResolution2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import com.tongyx.plugin.favorites.utils.MyUtil;

public class CommentPropertyKeyResolution implements IMarkerResolution2{

	@Override
	public String getLabel() {		
		return "Comment the unused property key";
	}

	@Override
	public void run(IMarker marker) {
		IFile file = marker.getResource().getParent().getFile(new Path("plugin.properties"));	
		if(!file.exists()){
			ByteArrayInputStream sm = new ByteArrayInputStream(new byte[]{});
			try {
				file.create(sm, false, null);
			} catch (CoreException e) {					
				e.printStackTrace();
				return;
			}
		}
		
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		IEditorPart part;
		try {
			part = IDE.openEditor(page, file,true);
			
		} catch (PartInitException e) {			
			e.printStackTrace();
			return;
		}
		
		if(!(part instanceof ITextEditor))return;
		
		ITextEditor editor = (ITextEditor)part;
		IDocument doc = editor.getDocumentProvider().getDocument(new FileEditorInput(file));
		
		Integer start = MyUtil.getMarkPropValue(marker, IMarker.CHAR_START);
		Integer end = MyUtil.getMarkPropValue(marker, IMarker.CHAR_END);
		if(start==null || end==null)return;
			
		boolean isLastLine = true;
		for(int i =end+1;i<doc.getLength();i++){		
			try {
				char c = doc.getChar(i);
				if(c!='\r' || c!='\n') {
					end = i + 1;
					isLastLine = false;
					break;
				}
			} catch (BadLocationException e) {				
				e.printStackTrace();
				return;
			}
		}
		if(isLastLine)end=doc.getLength();

		
		try {
			doc.replace(start, 0, "#");
		} catch (BadLocationException e) {				
			e.printStackTrace();
			return;
		}
		
		editor.selectAndReveal(start, end-start+1);
	}

	@Override
	public String getDescription() {		
		return "Comment the unused property key/value pair from the plugin.properties file";
	}

	@Override
	public Image getImage() {
		return null;
	}

}
