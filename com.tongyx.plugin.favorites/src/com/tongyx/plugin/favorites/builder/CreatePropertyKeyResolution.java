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

import com.tongyx.plugin.favorites.Constant;
import com.tongyx.plugin.favorites.utils.EditorUtil;
import com.tongyx.plugin.favorites.utils.FileUtil;
import com.tongyx.plugin.favorites.utils.MyUtil;

public class CreatePropertyKeyResolution implements IMarkerResolution2{

	@Override
	public String getLabel() {		
		return "Create a new property key";
	}

	@Override
	public void run(IMarker marker) {
		IFile file = marker.getResource().getParent().getFile(new Path("plugin.properties"));	
		if(!file.exists() && !FileUtil.createEmpty(file)) return;
		ITextEditor editor = EditorUtil.openTextEditor(file);
		if(editor==null) return;
		
		IDocument doc = editor.getDocumentProvider().getDocument(new FileEditorInput(file));
		
		String key = MyUtil.getMarkPropValue(marker, Constant.MARKER_PROP_KEY);
		if(key==null)return;
		String text = String.format("%s=Value for %s", key, key);
		
		int index = doc.getLength();
		if(index > 0){
			char c;
			try {
				c = doc.getChar(index - 1);
			} catch (BadLocationException e) {				
				e.printStackTrace();
				return;
			}
			if(c!='\r' || c!='\n') text = System.getProperty("line.separator") + text;			
		}
		
		try {
			doc.replace(index, 0, text);
		} catch (BadLocationException e) {				
			e.printStackTrace();
			return;
		}
		
		index += text.indexOf('=') + 1;
		editor.selectAndReveal(index, doc.getLength() - index);
	}

	@Override
	public String getDescription() {		
		return "Append a new property key/value pair to the plugin.properties file";
	}

	@Override
	public Image getImage() {
		return null;
	}

}
