package com.tongyx.plugin.favorites;

public abstract class Constant {
	public static final String PLUGIN_ID = "com.tongyx.plugin.favorites"; 
	public static final String ID_CONTEXT_PROPERTIES_EDITOR = "com.tongyx.plugin.favorites.editors.context";
	public static final String ID_VIEW_FAVORITES = "com.tongyx.plugin.favorites.views.FavoritesView";
	public static final String ID_AUDITOR_BUILDER = PLUGIN_ID + ".propertiesAuditor";
	public static final String ID_AUDITOR_MARKER = PLUGIN_ID + ".auditmarker";
	public static final String ID_AUDITOR_PROJECT = PLUGIN_ID +".propertiesauditor";
	public static final String MARKER_PROP_KEY = "key";
	public static final String MARKER_PROP_VIOLATION = "violation";
	public static final int MARKER_VIOLATION_MISSING_KEY = 1;
	public static final int MARKER_VIOLATION_UNUSED_KEY = 2;

}
