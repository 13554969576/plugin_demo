package com.tongyx.plugin.favorites.utils;

import java.io.ByteArrayInputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

public class FileUtil {
	public static boolean createEmpty(IFile file){
		ByteArrayInputStream sm = new ByteArrayInputStream(new byte[]{});
		try {
			file.create(sm, false, null);
			return true;
		} catch (CoreException e) {					
			e.printStackTrace();
			return false;
		}
	}

}
