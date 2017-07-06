package com.tongyx.plugin.favorites.utils;

import org.eclipse.core.runtime.IPath;

import com.tongyx.plugin.favorites.Activator;

public class EnvUtil {
	public static IPath getStateLocation(){
		return Activator.getDefault().getStateLocation();
	}

}
