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
package net.sourceforge.taggerplugin.resource;


import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * Helper for dealing with the ITaggedMarkers.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TaggedMarkerHelper {

	private TaggedMarkerHelper(){super();}

	/**
	 * Used to retrieve the "tagged" marker associated with the given resource. If no marker
	 * exists, one is created (with a random resourceId) and returned.
	 *
	 * @param resource
	 * @return
	 * @throws CoreException
	 */
	public static final ITaggedMarker getOrCreateMarker(IResource resource) throws CoreException {
		ITaggedMarker marker = getMarker(resource);
		if(marker == null){
			marker = (ITaggedMarker)resource.createMarker(ITaggedMarker.MARKER_TYPE).getAdapter(ITaggedMarker.class);
		}
		return(marker);
	}

	public static final void deleteMarker(IResource resource) throws CoreException {
		resource.deleteMarkers(ITaggedMarker.MARKER_TYPE, false, IResource.DEPTH_ZERO);
	}

	/**
	 * Used to retrieve the "tagged" marker on the given resource. If no "tagged" marker is defined
	 * for the resource a value of null is returned.
	 *
	 * @param resource the resource
	 * @return the marker or null
	 * @throws CoreException if there is a problem retrieving the marker
	 */
	public static final ITaggedMarker getMarker(IResource resource) throws CoreException {
		final IMarker[] markers = resource.findMarkers(ITaggedMarker.MARKER_TYPE, false, IResource.DEPTH_ZERO);
		return(markers != null && markers.length != 0 ? (ITaggedMarker)markers[0].getAdapter(ITaggedMarker.class) : null);
	}
}