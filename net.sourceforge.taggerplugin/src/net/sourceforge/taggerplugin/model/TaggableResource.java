package net.sourceforge.taggerplugin.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.taggerplugin.TaggerActivator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;

class TaggableResource implements ITaggableResource {

	private final IFile ifile;
	private final TagSetContainerManager manager;

	TaggableResource(final IFile ifile){
		this.manager = TaggerActivator.getDefault().getTagSetContainerManager();
		this.ifile = ifile;
	}

	/**
	 * Used to associat the resoruce with the given tag. The association will be added to the appropriate TagSetContainer.
	 */
	public void addAssociation(Tag tag){
		final ITagSetContainer container = manager.getTagSetContainer(tag);
		container.addAssociation(ifile,tag);
	}

	/**
	 * Used to retrieve all of the tags associated with this taggable resource. The returned array will
	 * also include those workspace tagset tags the resource is associated with.
	 *
	 * @return an array of all workspace and project level tags associated with this file
	 */
	public Tag[] getAssociations() {// FIXME: verify
		final Set<Tag> tags = new HashSet<Tag>();

		final ITagSetContainer wstsc = (ITagSetContainer)ResourcesPlugin.getWorkspace().getAdapter(ITagSetContainer.class);
		final TagAssociation wsta = wstsc.getAssociation(ifile);
		if(wsta != null){
			tags.addAll(Arrays.asList(wsta.getTags()));
		}

		final TagAssociation ta = getTagSetContainer().getAssociation(ifile);
		if(ta != null){
			tags.addAll(Arrays.asList(ta.getTags()));
		}

		return(tags.toArray(new Tag[tags.size()]));
	}

	public boolean hasAssociation(Tag tag) {
		return(manager.getTagSetContainer(tag).getAssociation(ifile).hasTag(tag));
	}

	public boolean hasAssociations() {
		boolean inContainer = getTagSetContainer().hasAssociations(ifile);
		boolean inWorkspace = manager.getTagSetContainer(WorkspaceTagSetContainer.NAME).hasAssociations(ifile);
		return(inContainer || inWorkspace);
	}

	/**
	 * Used to retrieve the tagsetcontainer containing this resource.
	 *
	 * @return the containing tagsetcontainer or null
	 */
	public ITagSetContainer getTagSetContainer(){
		return((ITagSetContainer)(ifile.getProject()).getAdapter(ITagSetContainer.class));
	}

	public void clearAssociations() {
		manager.getTagSetContainer(WorkspaceTagSetContainer.NAME).clearAssociations(ifile);
		getTagSetContainer().clearAssociations(ifile);
	}

	public void removeAssociation(Tag tag) {
		manager.getTagSetContainer(tag).removeAssociation(ifile, tag);
	}
}
