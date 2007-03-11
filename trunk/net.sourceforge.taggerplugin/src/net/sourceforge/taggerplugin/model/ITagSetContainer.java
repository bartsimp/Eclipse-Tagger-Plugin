package net.sourceforge.taggerplugin.model;

import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.ui.IMemento;


public interface ITagSetContainer {

	public boolean hasTags();

	public Tag[] getTags();

	public String getName();

	public Tag addTag(String name, String description);

	public void importTag(String id, String name, String description);

	public void updateTag(String tagid, String name, String description);

	public void removeTag(Tag tag);

	public TagAssociation[] getAssociations(Tag tag);

	public TagAssociation getAssociation(IResource resource);

	public boolean hasAssociations(Tag tag);

	public boolean hasAssociations(IResource resource);

	public void addAssociation(IResource resource, Tag tag);

	public void importAssociations(String resourceId, String[] tagIds);

	public void clearAssociations(IResource resource);

	public void removeAssociation(IResource resource, Tag tag);

	public void save();

	public void load() throws IOException;

	public void write(IMemento memento);

	public void read(IMemento memento);
}
