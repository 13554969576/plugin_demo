package com.tongyx.plugin.favorites.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.tongyx.plugin.favorites.model.FavoriteItemType;

public class FavoritesFilterDialog extends Dialog{
	private String namePattern;
	private String locationPattern;
	private List<FavoriteItemType> types;
	private Text namePatternField;
	private Text locationPatternField;
	private Map<FavoriteItemType, Button> typeFields;

	protected FavoritesFilterDialog(Shell parent, String namePattern, String locationPattern, List<FavoriteItemType> types) {
		super(parent);
		this.namePattern=namePattern==null?"":namePattern;
		this.locationPattern=locationPattern==null?"":locationPattern;
		this.types=types==null?new ArrayList<>():types;
		
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {		
		Composite container = (Composite)super.createDialogArea(parent);
		GridLayout layout = new GridLayout();
		layout.numColumns=2;
		container.setLayout(layout);
		
		Label filterLabel = new Label(container, SWT.NONE);
		filterLabel.setLayoutData(new GridData(GridData.BEGINNING,GridData.CENTER,false,false,2,1));
		filterLabel.setText("Enter a filter (* = any number of characters, ? = any single character)\nor an empty string for no filtering");
		
		Label nameLabel = new Label(container, SWT.NONE);
		nameLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER,false, false));
		nameLabel.setText("Name:");		
		namePatternField = new Text(container, SWT.BORDER);
		namePatternField.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		Label locationLabel = new Label(container, SWT.NONE);
		GridData gridData = new GridData(GridData.END,GridData.CENTER,false,false);
		gridData.horizontalIndent = 20;
		locationLabel.setLayoutData(gridData);
		locationLabel.setText("Location:");
		locationPatternField = new Text(container, SWT.BORDER);
		locationPatternField.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false));
		
		Label typesLabel = new Label(container, SWT.NONE);
		typesLabel.setLayoutData(new GridData(GridData.BEGINNING,GridData.CENTER,false,false,2,1));
		typesLabel.setText("Select the types of favorites to be shown:");
		Composite typeCheckboxComposite = new Composite(container, SWT.NONE);
		gridData = new GridData(GridData.FILL,GridData.FILL,false,false,2,1);
		gridData.horizontalIndent = 20;
		typeCheckboxComposite.setLayoutData(gridData);
		GridLayout typesLayout = new GridLayout();
		typesLayout.numColumns = 2;
		typeCheckboxComposite.setLayout(typesLayout);
		createTypesCheckboxes(typeCheckboxComposite);
		initContent();
		return container;
	}
	
	private void createTypesCheckboxes(Composite parent){
		typeFields = new HashMap<>();
		for(FavoriteItemType type: FavoriteItemType.getTypes()){
			if(type==FavoriteItemType.UNKNOW)continue;
			Button button = new Button(parent, SWT.CHECK);
			button.setText(type.getName());
			typeFields.put(type, button);
			button.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if(button.getSelection()){
						types.add(type);
					} else {
						types.remove(type);
					}
				}
			});
		}
	}
	
	private void initContent(){
		namePatternField.setText(namePattern);
		namePatternField.addModifyListener((e)->{namePattern=namePatternField.getText();});
		
		locationPatternField.setText(locationPattern);
		locationPatternField.addModifyListener((e)->{locationPattern=locationPatternField.getText();});
		
		for(FavoriteItemType type: FavoriteItemType.getTypes()){
			if(type==FavoriteItemType.UNKNOW)continue;
			Button button = typeFields.get(type);
			button.setSelection(types.contains(type));
		}		
	}
	
	@Override
	protected void configureShell(Shell newShell) {		
		super.configureShell(newShell);
		newShell.setText("Favorites View Filter Options");
	}

	public String getNamePattern() {
		return namePattern;
	}

	public String getLocationPattern() {
		return locationPattern;
	}

	public List<FavoriteItemType> getTypes() {
		return types;
	}

}
