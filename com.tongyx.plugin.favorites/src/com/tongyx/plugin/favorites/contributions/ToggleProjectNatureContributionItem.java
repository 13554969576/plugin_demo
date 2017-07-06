package com.tongyx.plugin.favorites.contributions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.tongyx.plugin.favorites.Constant;
import com.tongyx.plugin.favorites.builder.PropertiesAuditor;
import com.tongyx.plugin.favorites.builder.PropertiesAuditorNature;

public class ToggleProjectNatureContributionItem extends ContributionItem {
	protected boolean hasNature = true;

	public ToggleProjectNatureContributionItem() {
		
	}
	
	public ToggleProjectNatureContributionItem(String id) {
		super(id);
	}
	
	@Override
	public void fill(Menu menu, int index) {
		MenuItem item = new MenuItem(menu, SWT.CHECK,index);
		item.setText("Add/Remove propertiesAuditor project nature");
		item.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {				
				run();
			}
		});
		menu.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(MenuEvent e) {
				updateState(item);
			}
		});
	}
	
	private void run(){
		List<IProject> projects = getSelectedProjects();
		for(IProject proj: projects){
			if(!hasNature)
				toggleBuilder(proj);
			else
				toggleNature(proj);
				
		}
	}
	
	private void updateState(MenuItem menuItem){
		List<IProject> projects = getSelectedProjects();
		boolean enabled = projects.size() > 0;
		menuItem.setEnabled(enabled);
		menuItem.setSelection(enabled && PropertiesAuditorNature.hasNature(projects.get(0))); 
	}
	
	private List<IProject> getSelectedProjects(){
		List<IProject> projects = new ArrayList<>();
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		ISelection selection = window.getActivePage().getSelection();
		if(!(selection instanceof IStructuredSelection))return Collections.emptyList();
		for(Object ele: ((IStructuredSelection)selection).toList()){
			if(!(ele instanceof IResource)){
				if(!(ele instanceof IAdaptable))continue;
				ele = ((IAdaptable)ele).getAdapter(IResource.class);
				if(!(ele instanceof IResource))continue;
			}
			if(!(ele instanceof IProject)){
				ele = ((IResource)ele).getProject();
				if(!(ele instanceof IProject)) continue;
			}
			projects.add((IProject)ele);
		}
		return projects;
	}
	
	private void toggleBuilder(IProject proj){
		if(PropertiesAuditor.hasBuilder(proj)){
			PropertiesAuditor.deleteAuditMarker(proj);
			PropertiesAuditor.removeBuilderFromProject(proj);
		} else {
			PropertiesAuditor.addBuilderToProject(proj);
			new Job("Properties File Audit") {
				
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					try {
						proj.build(IncrementalProjectBuilder.FULL_BUILD, Constant.ID_AUDITOR_BUILDER,null,monitor);
					} catch (CoreException e) {						
						e.printStackTrace();
					}
					return Status.OK_STATUS;
				}
			}.schedule();
		}
	}
	
	private void toggleNature(IProject proj){
		if(PropertiesAuditorNature.hasNature(proj)){
			PropertiesAuditorNature.removeNature(proj);
		} else {
			PropertiesAuditorNature.addNature(proj);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
