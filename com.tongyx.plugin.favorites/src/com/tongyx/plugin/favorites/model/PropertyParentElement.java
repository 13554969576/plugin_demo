package com.tongyx.plugin.favorites.model;

public interface PropertyParentElement {
	void removeChild(PropertyElement child);
	void addChild(PropertyElement child);
	void addChild(int index, PropertyElement child);
}
