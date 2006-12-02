/*   ********************************************************************** **
**   Copyright (c) 2006-2007 Christopher J. Stehno (chris@stehno.com)       **
**   http://www.stehno.com                                                  **
**                                                                          **
**   All rights reserved                                                    **
**                                                                          **
**   This program and the accompanying materials are made available under   **
**   the terms of the Eclipse Public License v1.0 which accompanies this    **
**   distribution, and is available at:                                     **
**   http://www.stehno.com/legal/epl-1_0.html                               **
**                                                                          **
**   A copy is found in the file license.txt.                               **
**                                                                          **
**   This copyright notice MUST APPEAR in all copies of the file!           **
**  **********************************************************************  */
package net.sourceforge.taggerplugin.manager;

import java.util.UUID;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * Helper for dealing with the "tagged" marker.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TaggedMarker {

	public static final String MARKER_TYPE = "com.sourceforge.taggerplugin.tagged";

	private static final String ATTR_RESOURCEID = "resourceId";

	private TaggedMarker(){super();}

	/**
	 * Used to retrieve the "tagged" marker associated with the given resource. If no marker
	 * exists, one is created (with a random resourceId) and returned.
	 *
	 * @param resource
	 * @return
	 * @throws CoreException
	 */
	public static final IMarker getOrCreateMarker(IResource resource) throws CoreException {
		final IMarker[] markers = resource.findMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		IMarker marker = null;
		if(markers.length != 0){
			marker = markers[0];
		} else {
			marker = resource.createMarker(MARKER_TYPE);
			marker.setAttribute(ATTR_RESOURCEID,UUID.randomUUID().toString());
		}
		return(marker);
	}

	public static final void deleteMarker(IResource resource) throws CoreException {
		resource.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
	}

	/**
	 * Used to retrieve the "tagged" marker on the given resource. If no "tagged" marker is defined
	 * for the resource a value of null is returned.
	 *
	 * @param resource the resource
	 * @return the marker or null
	 * @throws CoreException if there is a problem retrieving the marker
	 */
	public static final IMarker getMarker(IResource resource) throws CoreException {
		final IMarker[] markers = resource.findMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		return(markers.length != 0 ? markers[0] : null);
	}

	/**
	 * Used to extract the resourceId from the marker.
	 *
	 * @param marker
	 * @return
	 * @throws CoreException
	 */
	public static final String getResourceId(IMarker marker) throws CoreException {
		return((String)marker.getAttribute(ATTR_RESOURCEID));
	}

	public static final String getResourceId(IResource resource) throws CoreException {
		final IMarker marker = getMarker(resource);
		if(marker != null){
			return(getResourceId(marker));
		} else {
			return(null);
		}
	}
}
