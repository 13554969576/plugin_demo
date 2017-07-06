package com.tongyx.plugin.favorites;

import org.eclipse.core.runtime.Path;

public enum Icons {
	JAVACLASSFILE("jcu_obj.png"),JAVACLASS("class_obj.png"),BYTECODEFILE(""),JAVAINTERFACE(""),JAVAENUM("");
	
	private final static String ICONSFOLDER="icons/";
	private Path file;
	private Icons(String filename) {
		file=new Path(ICONSFOLDER +filename);
	}
	public Path getFile(){
		return file;
	}
}
