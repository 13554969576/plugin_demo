package com.tongyx.plugin.favorites.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.ui.handlers.HandlerUtil;

public abstract class ClipboardHandler  extends AbstractHandler{
	@Override
	public Object execute(ExecutionEvent e) throws ExecutionException {
		Clipboard clipboard = new Clipboard(HandlerUtil.getActiveShell(e).getDisplay());
		try{
			return execute(e,clipboard);
		} finally {
			clipboard.dispose();
		}
	}
	
	protected abstract Object execute(ExecutionEvent e,Clipboard clipboard);
}
