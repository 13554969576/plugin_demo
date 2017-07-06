package com.tongyx.plugin.favorites.views;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IMemento;

public class FavoritesViewComparator extends ViewerComparator{
	private static final String TAG_DESCENDING="descending";
	private static final String TAG_COLUMN_INDEX="columnIndex";
	private static final String TAG_TYPE="SortInfo";
	private static final String TAG_TRUE="true";

	private class SortInfo{
		Comparator<Object> comparator;
		boolean descending;
		int idx;
		public SortInfo(int idx,Comparator<Object> comparator, boolean descending) {			
			this.comparator = comparator;
			this.descending = descending;
		}
	}
	
	private TableViewer viewer;
	private SortInfo[] infos;
	
	public FavoritesViewComparator(TableViewer viewer, TableColumn[] columns,Comparator<Object>[] comparators) {
		this.viewer=viewer;
		infos=new SortInfo[columns.length];
		int i=0;
		for(TableColumn column: columns){
			int idx=i++;			
			infos[idx]=new SortInfo(idx,comparators[idx], false); 	
			createSelectionListener(column, infos[idx]);
		}
	}
	
	private void createSelectionListener(TableColumn column,SortInfo info){
		column.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				sortUsing(info);
			}
		});
	}
	
	protected void sortUsing(SortInfo info){
		if(info==infos[0]){
			info.descending=!info.descending;
		} else {
			for(int i=0;i<infos.length;i++){
				SortInfo info1=infos[i];
				if(info==info1){
					System.arraycopy(infos, 0, infos, 1, i);
					infos[0]=info;
					info.descending=false;
					break;
				}
				
			}
		}
		viewer.refresh();
	}

	
	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		for(SortInfo info: infos){
			int ret = info.comparator.compare(e1, e2);
			if(ret!=0){
				return info.descending?-ret:ret;
			}
		}		
		return 0;
	}
	
	public void saveState(IMemento memento){
		for(SortInfo info: infos){
			IMemento mem = memento.createChild(TAG_TYPE);
			mem.putInteger(TAG_COLUMN_INDEX, info.idx);
			if(info.descending){
				mem.putString(TAG_DESCENDING, TAG_TRUE);
			}
		}
	}
	
	public void init(IMemento memento){
		List<SortInfo> newinfos = new ArrayList<>();
		IMemento[] mems = memento.getChildren(TAG_TYPE);
		int i=-1;
		for(IMemento mem:mems){
			i++;
			Integer v = mem.getInteger(TAG_COLUMN_INDEX);
			if(v==null)continue;
			if(v<0 || v>=infos.length)continue;
			SortInfo info=infos[i];
			if(newinfos.contains(info))continue;
			info.descending=TAG_TRUE.equals(mem.getString(TAG_DESCENDING));
			newinfos.add(info);			
		}
		for(SortInfo info: infos){
			if(!newinfos.contains(info)){
				newinfos.add(info);
			}
		}
		infos=newinfos.toArray(new SortInfo[newinfos.size()]);
	}
	
}