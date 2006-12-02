package net.sourceforge.taggerplugin.resource;

import java.util.UUID;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class TaggedResourceMarker implements ITaggedMarker {

	private final IMarker marker;
	
	TaggedResourceMarker(final IMarker marker){
		super();
		this.marker = marker;
	}

	public UUID getResourceId() throws CoreException {
		final String rcid = (String)marker.getAttribute(KEY_RESOURCEID);
		if(rcid == null){
			final UUID resourceId = UUID.randomUUID();
			marker.setAttribute(KEY_RESOURCEID, resourceId.toString());
			return(resourceId);
		} else {
			return(UUID.fromString(rcid));
		}
	}
	
	public IResource getResource(){
		return(marker.getResource());
	}

	public ITaggable getTaggableResource() {
		return((ITaggable)marker.getResource().getAdapter(ITaggable.class));
	}
}
