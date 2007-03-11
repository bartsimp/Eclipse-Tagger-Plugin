package net.sourceforge.taggerplugin.event;

import net.sourceforge.taggerplugin.model.ITagSetContainer;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.model.TagAssociation;

import org.eclipse.core.resources.IResource;

public class TagAssociationEvent extends TagSetContainerEvent<TagAssociation> {

	private static final long serialVersionUID = 7668643447729695337L;
	private final IResource resource;
	private final Tag tag;
	
	public TagAssociationEvent(ITagSetContainer container, IResource resource, Type type,TagAssociation assoc, Tag tag){
		super(container,type,assoc);
		this.resource = resource;
		this.tag = tag;
	}
	
	public IResource getResource(){return(resource);}
	
	public Tag getTag() {
		return tag;
	}
}
