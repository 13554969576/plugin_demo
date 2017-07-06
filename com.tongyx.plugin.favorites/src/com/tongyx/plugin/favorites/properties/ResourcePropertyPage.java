package com.tongyx.plugin.favorites.properties;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

import com.tongyx.plugin.favorites.model.IFavoriteItem;

public class ResourcePropertyPage extends PropertyPage {

	private Text txtField;

	public ResourcePropertyPage() {
		super();
	}

	@Override
	protected Control createContents(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;		
		panel.setLayout(layout);
		
		Label label = new Label(panel, SWT.NONE);
		label.setLayoutData(new GridData());
		label.setText("Comment that appears as hover help in the Favorites view:");
		
		txtField = new Text(panel, SWT.BORDER | SWT.MULTI | SWT.WRAP);
		txtField.setLayoutData(new GridData(GridData.FILL_BOTH));
		txtField.setText(getCommentPropertyValue());
		return panel;
	}
	
	protected String getCommentPropertyValue(){
		IResource res = getElement().getAdapter(IResource.class);
		try {
			String value = res.getPersistentProperty(IFavoriteItem.COMMENT_PROPKEY);
			if(value==null){
				return IFavoriteItem.getDefaultCommnt();
			
			} else
				return value;
		} catch (CoreException e) {			
			e.printStackTrace();
			return e.getMessage();
		}
	}
	
	protected void setCommentPropertyValue(String comment){
		IResource res = getElement().getAdapter(IResource.class);
		String value = IFavoriteItem.getDefaultCommnt();
		if(value.equals(comment))value=null;
		try {
			res.setPersistentProperty(IFavoriteItem.COMMENT_PROPKEY, comment);
		} catch (CoreException e) {			
			e.printStackTrace();
		}
		
	}
	
	@Override
	public boolean performOk() {
		setCommentPropertyValue(txtField.getText());
		return super.performOk();
	}

}