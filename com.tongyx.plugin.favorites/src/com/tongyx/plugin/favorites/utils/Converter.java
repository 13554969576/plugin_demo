package com.tongyx.plugin.favorites.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

public class Converter {
	private static final Map<RGB, Color> colorCache = new HashMap<>();
	private static final Map<ImageDescriptor, Image> imageCache = new HashMap<>();
	
	
	private static Pattern pColor = Pattern.compile("^RGB\\((\\d+),(\\d+),(\\d+)\\)$");
	
	public static String color2Str(Color color){
		return rgb2Str(color.getRGB());
	}
	
	public static String rgb2Str(RGB rgb){
		return String.format("RGB(%d,%d,%d)", rgb.red,rgb.green,rgb.blue);
	}
	
	public static RGB str2Rgb(String s){
		if(s==null)return null;
		Matcher m = pColor.matcher(s);
		if(m.matches()){
			return new RGB(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));			
		}
		return null;
	}
	
	public static Color str2Color(String s){
		RGB rgb = str2Rgb(s);
		if(rgb==null)return null;
		return getColor(rgb);
	}
	
	public static Color rgb2Color(RGB rgb){
		return getColor(rgb);
	}
	
	private static Color getColor(RGB rgb){
		Color color = colorCache.get(rgb);
		if(color==null){
			synchronized (colorCache) {
				color = colorCache.get(rgb);
				if(color!=null) return color;
				Display display = Display.getCurrent();
				color = new Color(display, rgb);
				colorCache.put(rgb, color);
			}
		}
		return color;
	}
	
	public static void dispose(){
		for(Color c: colorCache.values()){
			c.dispose();
		}
		colorCache.clear();
		
		for(Image img: imageCache.values()){
			img.dispose();
		}
		imageCache.clear();
	}
	
	public static Image imageDiscriptor2Image(ImageDescriptor imgDesc){
		if(imgDesc==null) return null;
		Image img = imageCache.get(imgDesc);
		if(img!=null)return img;
		img=imgDesc.createImage();
		imageCache.put(imgDesc, img);
		return img;
	}
}
