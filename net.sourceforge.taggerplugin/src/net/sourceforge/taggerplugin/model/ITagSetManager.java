package net.sourceforge.taggerplugin.model;

import org.eclipse.core.resources.IResource;

import net.sourceforge.taggerplugin.event.ITagSetContainerListener;
import net.sourceforge.taggerplugin.event.ITagSetRegistrationListener;

public interface ITagSetManager {

	public void addTagSetContainerListener(ITagSetContainerListener trml);

	public void removeTagSetContainerListener(ITagSetContainerListener trml);

	public void addTagSetRegistrationListener(ITagSetRegistrationListener trml);

	public void removeTagSetRegistrationListener(ITagSetRegistrationListener trml);
	
	public ITagSetContainer findTagSetContainer(Tag tag);
	
	public Tag[] getAssociations(IResource resource);
	
	public boolean hasAssocations(IResource resource);
	
	public void clearAssociations(IResource resource);
}
