package net.sourceforge.taggerplugin.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdapterFactory;

public class TaggableResourceAdapterFactory implements IAdapterFactory {

	private static final Class[] ADAPTERS = {ITaggableResource.class};
	
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		ITaggableResource taggable = null;
		if(adaptableObject instanceof IFile){
			taggable = adapt((IFile)adaptableObject);
		}
		return(taggable);
	}
	
	protected ITaggableResource adapt(final IFile file){
		return(new TaggableResource(file));
	}

	public Class[] getAdapterList() {return(ADAPTERS);}
}
