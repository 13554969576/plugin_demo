package com.tongyx.plugin.favorites.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.tongyx.plugin.favorites.Constant;

public class PropertiesAuditorNature implements IProjectNature {


	private IProject proj;

	@Override
	public void configure() throws CoreException {
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

	@Override
	public void deconfigure() throws CoreException {
		PropertiesAuditor.removeBuilderFromProject(proj);
		PropertiesAuditor.deleteAuditMarker(proj);
	}

	@Override
	public IProject getProject() {
		return proj;
	}

	@Override
	public void setProject(IProject project) {
		this.proj = project;
	}
	
	public static boolean hasNature(IProject project){
		try {
			return project.isOpen() && project.hasNature(Constant.ID_AUDITOR_PROJECT);
		} catch (CoreException e) {			
			e.printStackTrace();
			return false;
		}
	}
	
	public static void addNature(IProject project){
		if(!project.isOpen())return;
		
		IProjectDescription desc;
		try {
			desc = project.getDescription();
		} catch (CoreException e) {			
			e.printStackTrace();
			return;
		}
		
		String[] natures = desc.getNatureIds();
		List<String> newIds = new ArrayList<>(Arrays.asList(natures)) ;
		if(newIds.contains(Constant.ID_AUDITOR_PROJECT))return;
		newIds.add(Constant.ID_AUDITOR_PROJECT);
		desc.setNatureIds(newIds.toArray(new String[newIds.size()]));
		
		try {
			project.setDescription(desc, null);
		} catch (CoreException e) {			
			e.printStackTrace();
			return;
		}
	}
	
	public static void removeNature(IProject project){
		if(!project.isOpen())return;
		
		IProjectDescription desc;
		try {
			desc = project.getDescription();
		} catch (CoreException e) {			
			e.printStackTrace();
			return;
		}
		
		String[] natures = desc.getNatureIds();
		List<String> newIds = new ArrayList<>(Arrays.asList(natures));
		if(!newIds.contains(Constant.ID_AUDITOR_PROJECT))return;
		newIds.remove(Constant.ID_AUDITOR_PROJECT);
		desc.setNatureIds(newIds.toArray(new String[newIds.size()]));
		
		try {
			project.setDescription(desc, null);
		} catch (CoreException e) {			
			e.printStackTrace();
			return;
		}
	}

}
