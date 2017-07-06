package com.tongyx.plugin.favorites.builder;

import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.IMarkerResolution2;
import org.eclipse.ui.IMarkerResolutionGenerator2;

import static com.tongyx.plugin.favorites.Constant.*;

import java.util.ArrayList;
import java.util.List;

public class VoilationResolutionGenerator implements IMarkerResolutionGenerator2{

	@Override
	public IMarkerResolution[] getResolutions(IMarker marker) {
		List<IMarkerResolution2> resolutions = new ArrayList<>();
		
		switch (getViolation(marker)) {
		case MARKER_VIOLATION_MISSING_KEY:
			resolutions.add(new CreatePropertyKeyResolution());
			break;		
		case MARKER_VIOLATION_UNUSED_KEY:
			resolutions.add(new DeletePropertyKeyResolution());
			resolutions.add(new CommentPropertyKeyResolution());
			break;
		default:
			break;
		}
		return resolutions.toArray(new IMarkerResolution2[resolutions.size()]);
	}

	@Override
	public boolean hasResolutions(IMarker marker) {
		switch (getViolation(marker)) {
		case MARKER_VIOLATION_MISSING_KEY:
			return true;			
		case MARKER_VIOLATION_UNUSED_KEY:
			return true;
		default:
			return false;
		}
	}
	
	private int getViolation(IMarker marker){
		return marker.getAttribute(MARKER_PROP_VIOLATION, 0);
	}

}
