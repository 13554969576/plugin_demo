package com.tongyx.plugin.favorites;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.tongyx.plugin.favorites.model.FavoritesManager;
import com.tongyx.plugin.favorites.utils.Converter;
import static com.tongyx.plugin.favorites.Constant.PLUGIN_ID;

public class Activator extends AbstractUIPlugin {
	private static Activator plugin;
	

	public Activator() {}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		getLog();
	}


	public void stop(BundleContext context) throws Exception {
		FavoritesManager.shutdown();
		plugin = null;	
		Converter.dispose();
		super.stop(context);
	}


	public static Activator getDefault() {
		return plugin;
	}


	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
	
	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		Bundle bundle = Platform.getBundle(PLUGIN_ID);  
        reg.put(Icons.JAVACLASSFILE.toString(), ImageDescriptor.createFromURL(FileLocator.find(bundle, Icons.JAVACLASSFILE.getFile(), null)));
        reg.put(Icons.JAVACLASS.toString(), ImageDescriptor.createFromURL(FileLocator.find(bundle, Icons.JAVACLASS.getFile(), null)));
        super.initializeImageRegistry(reg);
	}
	
	
}
