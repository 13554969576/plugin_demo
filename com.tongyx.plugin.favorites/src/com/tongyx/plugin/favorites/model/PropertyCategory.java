package com.tongyx.plugin.favorites.model;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PropertyCategory extends PropertyElement implements PropertyParentElement{
	private String name;
	private List<PropertyEntry> entries;
	private final Pattern categoryPattern = Pattern.compile("^\\s*\\[([^#]*)\\]\\s*(#(.*))?$");
	private final Pattern commentPattern = Pattern.compile("^\\s*(#(.*))?$");
	private final Pattern entryPattern = Pattern.compile("^\\s*([^=#]*)=?([^#]*)(#(.*))?$");
	
	private PropertyCategory(){
		super(null);
	}
	
	public PropertyCategory(PropertyFile parent,LineNumberReader reader) {
		super(parent);	
		try {
			while (true) {
				reader.mark(1);
				int c = reader.read();
				if(c==-1)break;
				reader.reset();
				//if(c!='#')break;
				String line=reader.readLine();
				Matcher m = categoryPattern.matcher(line);
				if(m.matches()){
					name=m.group(1).trim();
					@SuppressWarnings("unused")
					String comment = null;
					if(m.groupCount()>3)comment=m.group(3);
					break;
				} else if(line.trim().length()==0 || commentPattern.matcher(line).matches()){
					//empty line or comment line
				} else {
					reader.reset();
					break;
				}
			}
			if(name==null)name="";
			
			entries=new ArrayList<>();
			while(true){
				reader.mark(1);
				int c = reader.read();
				if(c==-1)break;
				reader.reset();
//				if(c=='#')break;
				
				String line=reader.readLine();
				if(categoryPattern.matcher(line).matches()){
					reader.reset();
					break; //next category
				}
				Matcher m = entryPattern.matcher(line);
				if(m.matches()){
					String key = m.group(1).trim();
					String value=m.group(2).trim();					
					@SuppressWarnings("unused")
					String comment=m.groupCount()>3? m.group(3):null;
					if(key.length()==0 && value.length()==0) continue;
					entries.add(new PropertyEntry(this, key, value));
				} else if(commentPattern.matcher(line).matches()){
					//comment line
				}
					
//				int index=line.indexOf('=');
//				if(index!=-1){
//					String key = line.substring(0,index).trim();
//					String value=line.substring(index+1).trim();
//					entries.add(new PropertyEntry(this, key, value));
//				}
			}
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		
	}

	public void valueChanged(PropertyEntry entry){
		getParentAs(PropertyFile.class).keyChanged(this, entry);
	}
	
	public void keyChanged(PropertyEntry entry){
		getParentAs(PropertyFile.class).valueChanged(this, entry);
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		if(this.name!=null && this.name.equals(name))return;
		this.name=name;
		getParentAs(PropertyFile.class).nameChanged(this);
	}
	
	public List<PropertyEntry> getEntries(){
		return entries;
	}
	
	public void addEntry(PropertyEntry entry){
		if(entries.contains(entry))return;
		entries.add(entry);
		getParentAs(PropertyFile.class).entryAdded(this, entry);
	}
	
	@Override
	public PropertyElement[] getChildren() {
		return entries.toArray(new PropertyElement[entries.size()]);		
	}

	@Override
	public void remove() {
		getParentAs(PropertyFile.class).removeChild(this);		
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		PropertyCategory c = new PropertyCategory();
		c.name=this.name;
		List<PropertyEntry> entries = new ArrayList<>();
		c.entries=entries;
		for(PropertyEntry e: this.entries){
			entries.add((PropertyEntry)e.clone());
		}
		return c;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null)return false;
		if(!(obj instanceof PropertyCategory)) return false;
		PropertyCategory c = (PropertyCategory)obj;
		return name.equals(c.name) && entries.equals(c.entries);
	}
	
	@Override
	public int hashCode() {		
		return 31*name.hashCode() + entries.hashCode();
	}

	@Override
	public void removeChild(PropertyElement child) {
		if(!(child instanceof PropertyEntry)) throw new IllegalArgumentException("can't remove a non-PropertyEntry object from PropertyCategory");
		if(entries.remove((PropertyEntry)child))getParentAs(PropertyFile.class).entryRemoved(this, (PropertyEntry)child);
	}

	@Override
	public void addChild(PropertyElement child) {
		if(!(child instanceof PropertyEntry)) throw new IllegalArgumentException("can't add a non-PropertyEntry object to PropertyCategory");
		if(entries.add((PropertyEntry)child))getParentAs(PropertyFile.class).entryAdded(this, (PropertyEntry)child);
	}

	@Override
	public void addChild(int index, PropertyElement child) {
		if(!(child instanceof PropertyEntry)) throw new IllegalArgumentException("can't add a non-PropertyEntry object to PropertyCategory");
		if(index<0 || index>entries.size()) throw new IllegalArgumentException("the index is out of bounds");
		entries.add(index, (PropertyEntry)child);
		getParentAs(PropertyFile.class).entryAdded(this, (PropertyEntry)child);
	}
}
