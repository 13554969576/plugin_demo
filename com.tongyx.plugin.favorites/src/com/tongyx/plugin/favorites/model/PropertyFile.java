package com.tongyx.plugin.favorites.model;

import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class PropertyFile extends PropertyElement implements PropertyFileListener,PropertyParentElement{
	//private PropertyCategory unnamedCategory;
	private List<PropertyCategory> categories;
	private List<PropertyFileListener> listeners;
	
	private PropertyFile(){
		super(null);
	}

	public PropertyFile(String content) {		
		super(null);
		categories=new ArrayList<>();
		listeners=new ArrayList<>();
		LineNumberReader reader = new LineNumberReader(new StringReader(content));
		try{
//			unnamedCategory=new PropertyCategory(this, reader);			
			while(true){
				reader.mark(1);
				int c = reader.read();
				if(c==-1)break;
				reader.reset();
				PropertyCategory category = new PropertyCategory(this, reader);
				categories.add(category);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public PropertyElement[] getChildren() {
		return categories.toArray(new PropertyElement[categories.size()]);
	}

	@Override
	public void remove() {		
	}
	
	public void addPropertyFileListener(PropertyFileListener listener){
		if(!listeners.contains(listener))listeners.add(listener);
	}
	
	public void removePropertyFileListener(PropertyFileListener listener){
		listeners.remove(listener);
	}

	@Override
	public void keyChanged(PropertyCategory category, PropertyEntry entry) {
		for(PropertyFileListener listener: listeners){
			listener.keyChanged(category, entry);
		}
		
	}

	@Override
	public void valueChanged(PropertyCategory category, PropertyEntry entry) {
		for(PropertyFileListener listener: listeners){
			listener.valueChanged(category, entry);
		}
		
	}

	@Override
	public void nameChanged(PropertyCategory category) {
		for(PropertyFileListener listener: listeners){
			listener.nameChanged(category);
		}		
	}

	@Override
	public void entryAdded(PropertyCategory category, PropertyEntry entry) {
		for(PropertyFileListener listener: listeners){
			listener.entryAdded(category, entry);
		}
	}

	@Override
	public void entryRemoved(PropertyCategory category, PropertyEntry entry) {
		for(PropertyFileListener listener: listeners){
			listener.entryRemoved(category, entry);
		}		
	}

	@Override
	public void categoryAdded(PropertyCategory category) {
		for(PropertyFileListener listener: listeners){
			listener.categoryAdded(category);
		}			
	}

	@Override
	public void categoryRemoved(PropertyCategory category) {
		for(PropertyFileListener listener: listeners){
			listener.categoryRemoved(category);
		}	
	}
	
	public String asString(){
		StringBuilder sb = new StringBuilder();
//		if(unnamedCategory!=null){
//			if(unnamedCategory.getName().length()>0)sb.append(String.format("[%s]", unnamedCategory.getName()) + "");
//			String s = asString(unnamedCategory);
//			if(s.length()>0){
//				sb.append("\r\n");
//				sb.append(s);
//			}
//		}
		for(PropertyCategory c: categories){
			if(sb.length()>0)sb.append("\r\n\r\n");
			sb.append(String.format("[%s]", c.getName()) + "");
			String s = asString(c);
			if(s.length()>0){
				sb.append("\r\n");
				sb.append(s);
			}
		}		
		return sb.toString();
	}
	
	private String asString(PropertyCategory c){
		StringBuilder sb = new StringBuilder();
		for(PropertyElement e: c.getChildren()){
			if(!(e instanceof PropertyEntry))continue;
			PropertyEntry p = (PropertyEntry)e;
			sb.append(String.format("\r\n%s=%s", p.getKey(),p.getValue()));
		};
		if(sb.length()>2)sb.delete(0, 2);
		return sb.toString();
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		PropertyFile p = new PropertyFile();
//		p.unnamedCategory=(PropertyCategory)this.unnamedCategory.clone();
		List<PropertyCategory> categories = new ArrayList<>();
		p.categories=categories;
		for(PropertyCategory c: this.categories){
			categories.add((PropertyCategory)c.clone());
		}
		List<PropertyFileListener> listeners = new ArrayList<>();
		p.listeners=listeners;
		for(PropertyFileListener l: this.listeners){
			listeners.add(l);
		}
		return p;
	}
	
	public PropertyFile cloneAsPropertyFile(){
		try {
			return (PropertyFile)clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public int hashCode() {
		return categories.hashCode();		
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null)return false;
		if(!(obj instanceof PropertyFile)) return false;
		PropertyFile f = (PropertyFile)obj;
		return categories.equals(f.categories);
	}

	@Override
	public void removeChild(PropertyElement child) {
		if(!(child instanceof PropertyCategory)) throw new IllegalArgumentException("can't remove a non-PropertyCategory object from PropertyFile");
		if(categories.remove((PropertyCategory)child))categoryRemoved((PropertyCategory)child);
	}

	@Override
	public void addChild(PropertyElement child) {
		if(!(child instanceof PropertyCategory)) throw new IllegalArgumentException("can't add a non-PropertyCategory object to PropertyFile");
		if(categories.add((PropertyCategory)child))categoryAdded((PropertyCategory)child);
	}

	@Override
	public void addChild(int index, PropertyElement child) {
		if(!(child instanceof PropertyCategory)) throw new IllegalArgumentException("can't add a non-PropertyCategory object to PropertyFile");
		if(index<0 || index>categories.size()) throw new IllegalArgumentException("the index is out of bounds");
		categories.add(index, (PropertyCategory)child);
		categoryAdded((PropertyCategory)child);
	}

}
