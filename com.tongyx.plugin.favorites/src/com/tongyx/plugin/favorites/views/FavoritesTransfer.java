package com.tongyx.plugin.favorites.views;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

import com.tongyx.plugin.favorites.model.FavoritesManager;
import com.tongyx.plugin.favorites.model.IFavoriteItem;

public class FavoritesTransfer extends ByteArrayTransfer{
	private static final FavoritesTransfer INSTANCE = new FavoritesTransfer();
	private static final String TYPE_NAME="favorites-transfer-format:" + System.currentTimeMillis()+":"+INSTANCE.hashCode();
	private static final int TYPEID = registerType(TYPE_NAME);
	
	private FavoritesTransfer() {
		super();
	}
	
	public static FavoritesTransfer getInstance(){
		return INSTANCE;
	}

	@Override
	protected int[] getTypeIds() {		
		return new int[]{TYPEID};
	}

	@Override
	protected String[] getTypeNames() {
		return new String[]{TYPE_NAME};
	}
	
	@Override
	protected void javaToNative(Object data, TransferData transferData) {
		if(!(data instanceof IFavoriteItem[]))return;
		IFavoriteItem[] items = (IFavoriteItem[])data; 
		try{
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(stream);
			out.writeInt(items.length);
			for(IFavoriteItem item: items){
				out.writeUTF(item.getType().getId());
				out.writeUTF(item.getInfo());
			}
			out.close();
			super.javaToNative(stream.toByteArray(), transferData);
		} catch(IOException e){
			e.printStackTrace();
		}		
	}
	
	@Override
	protected Object nativeToJava(TransferData transferData) {
		byte[] bytes = (byte[])super.nativeToJava(transferData);
		if(bytes==null)return null;
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(bytes));
		try{
			FavoritesManager manager = FavoritesManager.getManager();
			int count = in.readInt();
			List<IFavoriteItem> items = new ArrayList<>(count);
			for(int i=0;i<count;i++){
				String typeId = in.readUTF();
				String info = in.readUTF();
				items.add(manager.loadFavorite(typeId,info));
			}
			return items.toArray(new IFavoriteItem[items.size()]);
		}catch(IOException e){
			e.printStackTrace();
			return null;
		}
	}

}
