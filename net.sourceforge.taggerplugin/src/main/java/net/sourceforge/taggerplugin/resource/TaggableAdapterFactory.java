package net.sourceforge.taggerplugin.resource;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

public class TaggableAdapterFactory implements IAdapterFactory {

	private static final Class[] ADAPTERS = {ITaggable.class};
	
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		return(new TaggableResource((IResource)adaptableObject));
	}

	public Class[] getAdapterList() {
		return(ADAPTERS);
	}
}
