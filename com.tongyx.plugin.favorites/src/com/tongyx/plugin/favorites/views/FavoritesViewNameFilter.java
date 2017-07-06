package com.tongyx.plugin.favorites.views;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IMemento;

import com.tongyx.plugin.favorites.model.FavoriteItemType;
import com.tongyx.plugin.favorites.model.IFavoriteItem;
import com.tongyx.plugin.favorites.utils.StringMatcher;
import com.tongyx.plugin.favorites.utils.StringUtil;

public class FavoritesViewNameFilter extends ViewerFilter{
	private final static String TAG_TYPE="ViewFilterInfo";
	private final static String TAG_NAME_PATTERN="Name_Pattern";
	private final static String TAG_LOCATION_PATTERN="Location_Pattern";
	private final static String TAG_TYPE_PATTERN="Types";
	private final StructuredViewer viewer;
	private String namePatternStr;
	private String locationPatternStr;
	private List<FavoriteItemType> types;
	private Matcher matcher;	
	
	public FavoritesViewNameFilter(StructuredViewer viewer){
		this.viewer=viewer;
	}

	public void filter() {
		boolean filtering=matcher!=null;
		if(!StringUtil.isBlank(namePatternStr) ||!StringUtil.isBlank(locationPatternStr) || (types!=null && !types.isEmpty())){
			matcher=new Matcher();
			if(filtering){
				viewer.refresh();
			} else{
				viewer.addFilter(this);
			}
			
		} else {
			matcher=null;
			if(filtering){
				viewer.refresh();
			}
		}
	}

	@Override
	public boolean select(Viewer viewer, Object parent, Object ele) {		
		return matcher.match((IFavoriteItem)ele);
	}
	
	public void saveState(IMemento memento){
		if(StringUtil.isBlank(namePatternStr) && StringUtil.isBlank(locationPatternStr) && (types==null || types.isEmpty()))return;
		IMemento mem = memento.createChild(TAG_TYPE);
		if(!StringUtil.isBlank(namePatternStr)){
			mem.putString(TAG_NAME_PATTERN, namePatternStr);
		}
		if(!StringUtil.isBlank(locationPatternStr)){
			mem.putString(TAG_LOCATION_PATTERN, locationPatternStr);
		}
		if(types!=null && !types.isEmpty()){
			StringBuilder sb = new StringBuilder();
			for(FavoriteItemType type: types){
				if(sb.length()>0)sb.append("|");
				sb.append(type.getId());
			}
			mem.putString(TAG_TYPE_PATTERN, sb.toString());
		}
	}
	
	public void init(IMemento memento){
		IMemento mem= memento.getChild(TAG_TYPE);
		if(mem==null)return;
		String s = mem.getString(TAG_NAME_PATTERN);
		if(s!=null)namePatternStr=s;
		s = mem.getString(TAG_LOCATION_PATTERN);
		if(s!=null)locationPatternStr=s;
		s = mem.getString(TAG_TYPE_PATTERN);
		if(s!=null){
			types = new ArrayList<>();
			String[] typeStrs = s.split("\\|");
			for(String typeStr: typeStrs){
				FavoriteItemType type = FavoriteItemType.getById(typeStr);
				if(type!=null)types.add(type);
			}
		}
		filter();
	}

	public void setNamePattern(String namePattern) {
		this.namePatternStr = namePattern;
	}

	public void setLocationPattern(String locationPattern) {
		this.locationPatternStr = locationPattern;
	}

	public void setTypes(List<FavoriteItemType> types) {
		this.types = types;
	}

	public String getNamePattern() {
		return namePatternStr;
	}

	public String getLocationPattern() {
		return locationPatternStr;
	}

	public List<FavoriteItemType> getTypes() {
		return types;
	}
	
	private class Matcher{
		StringMatcher namePattern;
		StringMatcher locationPattern;		
		public Matcher() {
			if(!StringUtil.isBlank(namePatternStr))namePattern=new StringMatcher(namePatternStr, true, false);
			if(!StringUtil.isBlank(locationPatternStr))locationPattern=new StringMatcher(locationPatternStr, true, false);
		}
		
		boolean match(IFavoriteItem item){
			if(namePattern!=null && !namePattern.match(item.getName()))return false;
			if(locationPattern!=null && !locationPattern.match(item.getName()))return false;
			if(types!=null && !types.isEmpty()){
				return types.contains(item.getType());
			}
			return true;
		}
	}
	
}
