package net.sourceforge.taggerplugin.model;


public interface ITaggableResource {

	public void addAssociation(Tag tag);
	
	public Tag[] getAssociations();
	
	public boolean hasAssociation(Tag tag);
	
	public boolean hasAssociations();
	
	public ITagSetContainer getTagSetContainer();
	
	public void clearAssociations();
	
	public void removeAssociation(Tag tag);
}
