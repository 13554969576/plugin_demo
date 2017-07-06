package com.tongyx.plugin.favorites.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import com.tongyx.plugin.favorites.Activator;
import com.tongyx.plugin.favorites.Constant;
import com.tongyx.plugin.favorites.Icons;
import com.tongyx.plugin.favorites.utils.Converter;

public class FavoriteItemType implements Comparable<FavoriteItemType>{
	private static final String TAG_ITEMTYPE = "itemType";
	private static final String ATT_NAME="name";
	private static final String ATT_ID = "id";
	private static final String ATT_CLASS = "class";
	private static final String ATT_TARGETCLASS = "targetClass";
	private static final String ATT_ICON = "icon";
	private static final ISharedImages PLATFORM_IMAGES = PlatformUI.getWorkbench().getSharedImages();
	private static FavoriteItemType[] cachedTypes ;
	private final IConfigurationElement config;
	private final String targetClassName;
	private FavoriteItemFactory factory;
	private ImageDescriptor imageDescriptor;
	private static final BuildInFavoriteItemType[] buildInTypes; 
	

	private final String id;
	private final String printName;
	private final int ordinal;

//	public static final BuildInFavoriteItemType WORKBENCH_FILE = new BuildInFavoriteItemType("WBFile",1,"Workbench File",PLATFORM_IMAGES.getImage(ISharedImages.IMG_OBJ_FILE),
//			(type,obj)->(obj instanceof IFile)?new FavoriteResource(type, (IFile)obj):null,(type,info)->FavoriteResource.loadFavorite(type, info));
//	
	public static final BuildInFavoriteItemType WORKBENCH_FOLDER = new BuildInFavoriteItemType("WBFolder",2,"Workbench Folder",PLATFORM_IMAGES.getImage(ISharedImages.IMG_OBJ_FOLDER),
			(type,obj)->(obj instanceof IFolder)?new FavoriteResource(type, (IFolder)obj):null,(type,info)->FavoriteResource.loadFavorite(type, info)) ;
	
	public static final BuildInFavoriteItemType WORKBENCH_PROJECT = new BuildInFavoriteItemType("WBProject",3,"Workbench Project",PLATFORM_IMAGES.getImage(ISharedImages.IMG_OBJ_FOLDER),
			(type,obj)->(obj instanceof IProject)?new FavoriteResource(type, (IProject)obj):null,(type,info)->FavoriteResource.loadFavorite(type, info)) ;

	public static final BuildInFavoriteItemType JAVA_PROJECT = new BuildInFavoriteItemType("JProject",4,"Java Project",PLATFORM_IMAGES.getImage(ISharedImages.IMG_OBJ_FOLDER),
			(type,obj)->(obj instanceof IJavaElement) && ((IJavaElement)obj).getElementType()==IJavaElement.JAVA_PROJECT?new FavoriteJavaElement(type, (IJavaElement)obj):null,
					(type,info)->FavoriteJavaElement.loadFavorite(type, info)) ;	
	public static final BuildInFavoriteItemType JAVA_PACKAGE_ROOT = new BuildInFavoriteItemType("JPackageRoot",5,"Java Package Root",PLATFORM_IMAGES.getImage(ISharedImages.IMG_OBJ_FOLDER),
			(type,obj)->(obj instanceof IJavaElement) && ((IJavaElement)obj).getElementType()==IJavaElement.PACKAGE_FRAGMENT_ROOT?new FavoriteJavaElement(type, (IJavaElement)obj):null,
					(type,info)->FavoriteJavaElement.loadFavorite(type, info)) ;
	
	public static final BuildInFavoriteItemType JAVA_PACKAGE = new BuildInFavoriteItemType("JPackage",6,"Java Package",PLATFORM_IMAGES.getImage(ISharedImages.IMG_OBJ_FOLDER),
			(type,obj)->(obj instanceof IJavaElement)&& ((IJavaElement)obj).getElementType()==IJavaElement.PACKAGE_FRAGMENT? new FavoriteJavaElement(type, (IJavaElement)obj):null,
					(type,info)->FavoriteJavaElement.loadFavorite(type, info)) ;
	
	public static final BuildInFavoriteItemType JAVA_BYTECODE_FILE = new BuildInFavoriteItemType("JByteCode",7,"Java Class File",PLATFORM_IMAGES.getImage(ISharedImages.IMG_OBJ_FILE),
			(type,obj)->(obj instanceof IJavaElement)&& ((IJavaElement)obj).getElementType()==IJavaElement.CLASS_FILE?new FavoriteJavaElement(type,  (IJavaElement)obj):null,
					(type,info)->FavoriteJavaElement.loadFavorite(type, info)) ;
	
	public static final BuildInFavoriteItemType JAVA_COMP_UNIT = new BuildInFavoriteItemType("JClassFile",8,"Java Comp Unit",Activator.getDefault().getImageRegistry().get(Icons.JAVACLASSFILE.toString()),
			(type,obj)->(obj instanceof IJavaElement) && ((IJavaElement)obj).getElementType()==IJavaElement.COMPILATION_UNIT?new FavoriteJavaElement(type, (IJavaElement)obj):null,
					(type,info)->FavoriteJavaElement.loadFavorite(type, info)) ;
	
	public static final BuildInFavoriteItemType JAVA_INTERFACE = new BuildInFavoriteItemType("JInterface",9,"Java Interface",PLATFORM_IMAGES.getImage(ISharedImages.IMG_OBJ_FILE),
			(type,obj)->(obj instanceof IJavaElement && ((IJavaElement)obj).getElementType()==100 )?new FavoriteJavaElement(type, (IJavaElement)obj):null,
					(type,info)->FavoriteJavaElement.loadFavorite(type, info)) ;
	
	public static final BuildInFavoriteItemType JAVA_CLASS = new BuildInFavoriteItemType("JClass",10,"Java Class",Activator.getDefault().getImageRegistry().get(Icons.JAVACLASS.toString()),
			(type,obj)->(obj instanceof IJavaElement) && ((IJavaElement)obj).getElementType()==IJavaElement.TYPE ?new FavoriteJavaElement(type, (IJavaElement)obj):null,
					(type,info)->FavoriteJavaElement.loadFavorite(type, info)) ;
	

	static{
		buildInTypes=new BuildInFavoriteItemType[]{//WORKBENCH_FILE,
				WORKBENCH_FOLDER,WORKBENCH_PROJECT,JAVA_PROJECT,JAVA_PACKAGE_ROOT,JAVA_PACKAGE,JAVA_BYTECODE_FILE,JAVA_COMP_UNIT,JAVA_INTERFACE,JAVA_CLASS};
	}
	
	public FavoriteItemType(IConfigurationElement cfg, int idx) {
		this.config = cfg;
		this.ordinal = idx;
		id = getAttribute(config,ATT_ID,null);
		printName = getAttribute(config, ATT_NAME, id);
		targetClassName = getAttribute(config, ATT_TARGETCLASS, null);
		getAttribute(config, ATT_CLASS, null);
	}
	
	private FavoriteItemType(){
		this("Unknow",0,"Unknow");
	}
	
	private FavoriteItemType(String id, int ordinal, String name){
		this.id=id;
		this.ordinal=ordinal;
		printName=name;
		config=null;
		targetClassName="";
	}
	
	public String getId(){
		return id;
	}
	
	public String getName(){
		return printName;
	}
	
	public Image getImage(){
		return Converter.imageDiscriptor2Image(getImageDescriptor());
	};
	
	public IFavoriteItem newFavorite(Object obj){
		if(!isTarget(obj)) return null;
		FavoriteItemFactory factory = getFactory();
		if(factory==null)return null;
		return factory.newFavorite(this, obj);
	};
	
	public IFavoriteItem loadFavorite(String info){
		FavoriteItemFactory factory = getFactory();
		if(factory==null)return null;
		return factory.loadFavorite(this, info);
	};
	
	@Override
	public int compareTo(FavoriteItemType another) {		
		return this.ordinal-another.ordinal;
	}
	
	public static final FavoriteItemType UNKNOW = new FavoriteItemType(){
		@Override
		public IFavoriteItem loadFavorite(String info) {			
			return null;
		}
		
		@Override
		public IFavoriteItem newFavorite(Object obj) {			
			return null;
		}
	};
	
	public static FavoriteItemType[] getTypes(){
		if(cachedTypes!=null)return cachedTypes;
		IExtension[] extensions = Platform.getExtensionRegistry().getExtensionPoint(Constant.PLUGIN_ID,"favorites").getExtensions();
		List<FavoriteItemType> found = new ArrayList<>(20);
		found.add(UNKNOW);
		for(IExtension ext: extensions){
			IConfigurationElement[] configs = ext.getConfigurationElements();
			for(IConfigurationElement cfg: configs){
				FavoriteItemType proxy = parseType(cfg,found.size()+buildInTypes.length);
				if(proxy != null)found.add(proxy);
			}
		}
		for(FavoriteItemType buildinType: buildInTypes){
			found.add(buildinType);
		}
		cachedTypes = (FavoriteItemType[])found.toArray(new FavoriteItemType[found.size()]);
		return cachedTypes;
	}
	
	private static FavoriteItemType parseType(IConfigurationElement cfg, int idx){
		if(!cfg.getName().equals(TAG_ITEMTYPE))return null;
		try{
			return new FavoriteItemType(cfg,idx);
		} catch (Exception e) {
			String name = cfg.getAttribute(ATT_NAME);
			String msg = String.format("Failed to load itemType named %s in %s", name,cfg.getDeclaringExtension().getNamespaceIdentifier());
			new RuntimeException(msg,e).printStackTrace();
			return null;
		}
	}
	
	public static FavoriteItemType getById(String id){
		for(FavoriteItemType type: getTypes()){
			if(type.getId().equals(id)) return type;
		}
		return null;
	}
	
	private static String getAttribute(IConfigurationElement cfg, String name, String defaultValue){
		String value = cfg.getAttribute(name);
		if(value!=null)return value;
		if(defaultValue != null)return defaultValue;
		throw new IllegalArgumentException(String.format("Missing %s attribute", name));
	}
	
	public ImageDescriptor getImageDescriptor(){
		if(imageDescriptor != null) return imageDescriptor;
		String iconName = config.getAttribute(ATT_ICON);
		if(iconName==null) return null;
		IExtension extension = config.getDeclaringExtension();
		String pluginId = extension.getNamespaceIdentifier();
		imageDescriptor = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, iconName);
		return imageDescriptor;
	}
	
	
	private boolean isTarget(Object obj){
		if(obj==null)return false;
		Class<?> clazz = obj.getClass();
		if(clazz.getName().equals(targetClassName)){
			return true;
		}
				
		Class<?>[] interfaces = clazz.getInterfaces();
		for(Class<?> aInterface: interfaces){
			if(aInterface.getName().equals(targetClassName)) return true;
		}
		return false;
	}
	
	private FavoriteItemFactory getFactory(){
		if(factory!=null)return factory;
		try {
			factory = (FavoriteItemFactory)config.createExecutableExtension(ATT_CLASS);			
		} catch (CoreException e) {	
			String msg = String.format("Failed to instantiate factory: %s in type: %s in plugin: %s", config.getAttribute(ATT_CLASS),id,config.getDeclaringExtension().getNamespaceIdentifier());
			new RuntimeException(msg,e).printStackTrace();			
		}
		return factory;
	}
	
	public static void disposeTypes(){
		if(cachedTypes==null)return;
		for(FavoriteItemType type: cachedTypes){			
			type.dispose();
		}
		cachedTypes=null;		
	}
	
	public void dispose(){
		if(factory==null) return;
		factory.dispose();
		factory=null;
	}
	
	private static class BuildInFavoriteItemType extends FavoriteItemType{
		Image img;
		IFavoriteItemCreater creater;
		IFavoriteItemLoader itemLoader;
		private BuildInFavoriteItemType(String id, int ordinal, String name, Image img,IFavoriteItemCreater creater,IFavoriteItemLoader itemLoader){
			super(id,ordinal,name);
			this.img=img;
			this.creater=creater;
			this.itemLoader=itemLoader;
		}		
		
		@Override
		public IFavoriteItem loadFavorite(String info) {			
			return itemLoader.load(this, info);
		}
		
		@Override
		public IFavoriteItem newFavorite(Object obj) {			
			return creater.create(this, obj);
		}
		
		@Override
		public Image getImage() {			
			return img;			
		}
	}
	
	@FunctionalInterface
	public static interface IFavoriteItemCreater {
		IFavoriteItem create(FavoriteItemType type,Object obj);
	}
	
	@FunctionalInterface
	public static interface IFavoriteItemLoader {
		IFavoriteItem load(FavoriteItemType type,String info);
	}
}
 