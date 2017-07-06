package com.tongyx.plugin.favorites.utils;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;

public class MyUtil {
	@SuppressWarnings("unchecked")
	public static <T> T getMarkPropValue(IMarker marker, String propName){
		try {
			return (T)marker.getAttribute(propName);
		} catch (CoreException e) {			
			e.printStackTrace();
			return null;
		}
		
	}

}
