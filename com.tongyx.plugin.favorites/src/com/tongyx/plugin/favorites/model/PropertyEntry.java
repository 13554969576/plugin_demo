package com.tongyx.plugin.favorites.model;

public class PropertyEntry extends PropertyElement{
	String key;
	String value;
	
	public PropertyEntry(PropertyCategory parent, String key, String value) {
		super(parent);
		this.key=key;
		this.value=value;
	}
	
	private PropertyEntry() {
		super(null);
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		if(this.key!=null && this.key.equals(key)) return;
		this.key = key;
		getParentAs(PropertyCategory.class).keyChanged(this);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		if(this.value!=null && this.value.equals(value)) return;
		this.value = value;
		getParentAs(PropertyCategory.class).valueChanged(this);
	}

	@Override
	public PropertyElement[] getChildren() {		
		return NO_CHILDREN;
	}

	@Override
	public void remove() {
		getParentAs(PropertyCategory.class).removeChild(this);
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		PropertyEntry e = new PropertyEntry();
		e.key=this.key;
		e.value=this.value;
		return e;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null)return false;
		if(!(obj instanceof PropertyEntry)) return false;
		PropertyEntry p = (PropertyEntry)obj;
		return key.equals(p.key) && value.equals(p.value);
	}
	
	@Override
	public int hashCode() {
		return 31*key.hashCode() + value.hashCode();
	}
}
