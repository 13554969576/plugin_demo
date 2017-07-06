package com.tongyx.plugin.favorites.builder;

import static com.tongyx.plugin.favorites.Constant.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;

import com.tongyx.plugin.favorites.utils.StringUtil;

public class PropertiesAuditor extends IncrementalProjectBuilder {

	@Override
	protected IProject[] build(int kind, Map<String, String> args, IProgressMonitor monitor)
			throws CoreException {
		if(shouldAudit(kind)){
			ResourcesPlugin.getWorkspace().run((m)->{auditPluginmanifest(m);}, monitor);
		}
		return null;
	}
	
	private boolean shouldAudit(int kind){
		if(kind==FULL_BUILD){
			return true;
		}
		IResourceDelta delta = getDelta(getProject());
		if(delta==null)return false;
		for(IResourceDelta detail: delta.getAffectedChildren()){
			String fileName = detail.getProjectRelativePath().lastSegment();
			if(fileName.equals("plugin.xml")||fileName.equals("plugin.properties")){
				return true;
			}
		}
		return false;
	}

	private void auditPluginmanifest(IProgressMonitor monitor){
		monitor.beginTask("Audit plugin manifest", 4);		
		
		IProject proj = getProject();
		
		if(!deleteAuditMarker(proj)){
			return;
		}
		
		if(checkCancel(monitor))return;
		
		Map<String, Location> pluginKeys = scanPlugin(proj.getFile("plugin.xml"));
		monitor.worked(1);
		if(checkCancel(monitor))return;
		
		Map<String, Location> propertiesKeys = scanProperties(proj.getFile("plugin.properties"));
		monitor.worked(1);
		if(checkCancel(monitor))return;
		
		for(Entry<String, Location> entry: pluginKeys.entrySet()){
			if(!propertiesKeys.containsKey(entry.getKey())){
				reportProblem("Missing property key",entry.getValue(),MARKER_VIOLATION_MISSING_KEY,true);				
			}
		}
		monitor.worked(1);
		if(checkCancel(monitor))return;
		
		for(Entry<String, Location> entry: propertiesKeys.entrySet()){
			if(!pluginKeys.containsKey(entry.getKey())){
				reportProblem("Unused property key",entry.getValue(),MARKER_VIOLATION_UNUSED_KEY,false);				
			}
		}
		monitor.done();
	}
	
	private boolean checkCancel(IProgressMonitor monitor){
		if(monitor.isCanceled()){
			throw new OperationCanceledException();
		}
		
		if(isInterrupted()){
			return true;
		}
		return false;
	}
	
	private Map<String, Location> scanPlugin(IFile file){
		Map<String, Location> keys = new HashMap<>();
		String content =StringUtil.nvl(readFile(file));
		int start = 0;
		while(true){
			start = content.indexOf("\"%",start);
			if(start<0)break;
			int end = content.indexOf('"', start+2);
			if(end<0)break;
			Location loc = new Location();
			loc.file = file;
			loc.key=content.substring(start+2,end);
			loc.charStart = start + 1;
			loc.charEnd = end;
			keys.put(loc.key, loc);
			start = end + 1;
		}
		
		return keys;
	}
	
	private Map<String, Location> scanProperties(IFile file){
		Map<String, Location> keys = new HashMap<>();
		String content = readFile(file);
		if(content==null)return Collections.emptyMap();
		int end = 0;
		while(true){
			end = content.indexOf('=',end);
			if(end<0)break;
			int start = end - 1;
			while(start >= 0){
				char c = content.charAt(start);
				if(c=='\r' || c=='\n')break;
				start--;
			}
			start++;
			String key = content.substring(start, end).trim();
			if(key.length()==0 || key.charAt(0)=='#' || key.indexOf('=')!=-1){
				end++;
				continue;
			}
			Location loc = new Location();
			loc.file = file;
			loc.key = key;
			loc.charStart=start;
			loc.charEnd=end;
			keys.put(key, loc);
			end++;
		}
		return keys;
	}
	
	
	private String readFile(IFile file){
		if(!file.exists())return null;
		InputStream stream=null;
		try {
			stream = file.getContents();
			Reader reader = new BufferedReader(new InputStreamReader(stream));
			StringBuilder sb = new StringBuilder();
			char[] buf = new char[2048];
			while(true){
				int count = reader.read(buf);
				if(count<0)break;
				sb.append(buf,0,count);
			}
			return sb.toString();
		} catch (CoreException | IOException e) {			
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	private void reportProblem(String msg, Location loc, int errCode, boolean isError){
		try {
			IMarker marker = loc.file.createMarker(ID_AUDITOR_MARKER);
			marker.setAttribute(IMarker.MESSAGE, msg + ": " + loc.key);
			marker.setAttribute(IMarker.CHAR_START, loc.charStart);
			marker.setAttribute(IMarker.CHAR_END, loc.charEnd);
			marker.setAttribute(IMarker.SEVERITY, isError?IMarker.SEVERITY_ERROR:IMarker.SEVERITY_WARNING);
			marker.setAttribute(MARKER_PROP_KEY, loc.key);
			marker.setAttribute(MARKER_PROP_VIOLATION, errCode);
		} catch (CoreException e) {			
			e.printStackTrace();
		}		
	}
	
	
	private class Location{
		IFile file;
		String key;		
		int charStart;
		int charEnd;
	}
	
	public static void addBuilderToProject(IProject proj)  {
		if(!(proj.isOpen()))return;
		IProjectDescription pd = null;
		try {
			pd = proj.getDescription();
		} catch (CoreException e) {			
			e.printStackTrace();
			return;
		}
		ICommand[] cmds = pd.getBuildSpec();
		for(ICommand cmd: cmds){
			if(cmd.getBuilderName().equals(ID_AUDITOR_BUILDER))return;
		}
		ICommand newCmd = pd.newCommand();
		newCmd.setBuilderName(ID_AUDITOR_BUILDER);
		ICommand[] newCmds = new ICommand[cmds.length+1]; 
		if(cmds.length>0)System.arraycopy(cmds, 0, newCmds, 0, cmds.length);
		newCmds[cmds.length]=newCmd;
		pd.setBuildSpec(newCmds);
		try {
			proj.setDescription(pd,null);
		} catch (CoreException e) {			
			e.printStackTrace();
			return;
		}		
	}
	
	public static void removeBuilderFromProject(IProject proj){
		if(!(proj.isOpen()))return;
		IProjectDescription pd = null;
		try {
			pd = proj.getDescription();
		} catch (CoreException e) {			
			e.printStackTrace();
			return;
		}
		ICommand[] cmds = pd.getBuildSpec();
		List<ICommand> cmdLst = new ArrayList<ICommand>(Arrays.asList(cmds));
		boolean found = false;
		for(ICommand cmd: cmds){
			if(cmd.getBuilderName().equals(ID_AUDITOR_BUILDER)){
				cmdLst.remove(cmd);
				found = true;
				break;
			}
		}
		if(!found)return;		
		pd.setBuildSpec(cmdLst.toArray(new ICommand[cmds.length-1]));
		try {
			proj.setDescription(pd,null);
		} catch (CoreException e) {			
			e.printStackTrace();
			return;
		}
	}
	
	public static boolean hasBuilder(IProject proj){
		if(!(proj.isOpen()))return false;
		IProjectDescription pd = null;
		try {
			pd = proj.getDescription();
		} catch (CoreException e) {			
			e.printStackTrace();
			return false;
		}
		ICommand[] cmds = pd.getBuildSpec();		
		for(ICommand cmd: cmds){
			if(cmd.getBuilderName().equals(ID_AUDITOR_BUILDER)){
				return true;
			}
		}
		return false;
	}
	
	public static boolean deleteAuditMarker(IProject proj){
		try {
			proj.deleteMarkers(ID_AUDITOR_MARKER, false, IResource.DEPTH_INFINITE);
			return true;
		} catch (CoreException e) {			
			e.printStackTrace();
			return false;
		}
	}
}
