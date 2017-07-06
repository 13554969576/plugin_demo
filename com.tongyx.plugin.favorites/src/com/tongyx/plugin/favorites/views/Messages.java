package com.tongyx.plugin.favorites.views;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.tongyx.plugin.favorites.views.messages"; //$NON-NLS-1$
	public static String FavoritesView_action1;
	public static String FavoritesView_action2;
	public static String FavoritesView_edit;
	public static String FavoritesView_filter;
	public static String FavoritesView_location;
	public static String FavoritesView_msg_action1;
	public static String FavoritesView_msg_action2;
	public static String FavoritesView_msg_dbclick;
	public static String FavoritesView_name;
	public static String FavoritesView_tooltip_action1;
	public static String FavoritesView_tooltip_action2;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
