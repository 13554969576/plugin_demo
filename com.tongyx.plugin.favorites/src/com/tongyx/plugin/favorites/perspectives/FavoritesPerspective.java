package com.tongyx.plugin.favorites.perspectives;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;

import com.tongyx.plugin.favorites.Constant;

public class FavoritesPerspective implements IPerspectiveFactory{

	@Override
	public void createInitialLayout(IPageLayout layout) {
		String editorArea = layout.getEditorArea();
		layout.addView(IPageLayout.ID_OUTLINE, IPageLayout.LEFT, 0.25f, editorArea);
		IFolderLayout bottom = layout.createFolder("bottom", IPageLayout.BOTTOM, 0.66f, editorArea);
		bottom.addView(Constant.ID_VIEW_FAVORITES);
		bottom.addView(IPageLayout.ID_TASK_LIST);
//		bottom.addView(IPageLayout.ID_PROBLEM_VIEW);
		bottom.addPlaceholder(IPageLayout.ID_PROBLEM_VIEW);
		//layout.addActionSet(arg0);
	}

}
