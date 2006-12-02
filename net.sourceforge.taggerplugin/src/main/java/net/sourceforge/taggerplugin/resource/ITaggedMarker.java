package net.sourceforge.taggerplugin.resource;

import java.util.UUID;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public interface ITaggedMarker {

	public static final String MARKER_TYPE = "com.sourceforge.taggerplugin.tagged";
	public static final String KEY_RESOURCEID = "resourceId";
	
	public UUID getResourceId() throws CoreException;
	
	public IResource getResource();
	
	public ITaggable getTaggableResource();
}
