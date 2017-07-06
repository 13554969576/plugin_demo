package com.tongyx.plugin.favorites.builder;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IMarkerResolution2;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.ITextEditor;

import com.tongyx.plugin.favorites.utils.EditorUtil;
import com.tongyx.plugin.favorites.utils.MyUtil;

public class DeletePropertyKeyResolution implements IMarkerResolution2{

	@Override
	public String getLabel() {		
		return "Delete the unused property key";
	}

	@Override
	public void run(IMarker marker) {
		IFile file = marker.getResource().getParent().getFile(new Path("plugin.properties"));	
		if(!file.exists()) return;
		
		ITextEditor editor = EditorUtil.openTextEditor(file);
		if(editor==null) return;
		
		IDocument doc = editor.getDocumentProvider().getDocument(new FileEditorInput(file));
		
		Integer start = MyUtil.getMarkPropValue(marker, IMarker.CHAR_START);
		Integer end = MyUtil.getMarkPropValue(marker, IMarker.CHAR_END);
		if(start==null || end==null)return;
			
		boolean isLastLine = true;
		char c = 0;
		for(int i =end+1;i<doc.getLength();i++){		
			try {
				c = doc.getChar(i);
				if(c=='\r' || c=='\n') {
					end = i;
					isLastLine = false;
					break;
				}
			} catch (BadLocationException e) {				
				e.printStackTrace();
				return;
			}
		}
		if(isLastLine)
			end=doc.getLength();
		else 
			end++;

		
		try {
			doc.replace(start, end-start, "");
			
			if(isLastLine){
				if(start-1>0){
					c = doc.getChar(start-1);
					if(c=='\n'||c=='\r') doc.replace(start-1, 1, "");	
				}
				
			} else {
				if(start<doc.getLength()-1 && c=='\r'){				
					c = doc.getChar(start);
					if(c=='\n') doc.replace(start, 1, "");				
				}
			}
			

		} catch (BadLocationException e) {				
			e.printStackTrace();
			return;
		}
		
		editor.selectAndReveal(start, 0);
	}

	@Override
	public String getDescription() {		
		return "Delete the unused property key/value from the plugin.properties file";
	}

	@Override
	public Image getImage() {
		return null;
	}

}
