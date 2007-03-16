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
package net.sourceforge.taggerplugin.model;

import net.sourceforge.taggerplugin.TaggerActivator;

import org.eclipse.core.resources.IFile;

class TaggableResource implements ITaggableResource {

	private final IFile ifile;
	private final ITagSetManager manager;

	TaggableResource(final IFile ifile){
		this.manager = TaggerActivator.getDefault().getTagSetManager();
		this.ifile = ifile;
	}

	/**
	 * Used to associat the resoruce with the given tag. The association will be added to the appropriate TagSetContainer.
	 */
	public void addAssociation(Tag tag){
		manager.findTagSetContainer(tag).addAssociation(ifile,tag);
	}

	/**
	 * Used to retrieve all of the tags associated with this taggable resource. The returned array will
	 * also include those workspace tagset tags the resource is associated with.
	 *
	 * @return an array of all workspace and project level tags associated with this file
	 */
	public Tag[] getAssociations() {
		return(manager.getAssociations(ifile));
	}

	public boolean hasAssociation(Tag tag) {
		return(manager.findTagSetContainer(tag).getAssociation(ifile).hasTag(tag));
	}

	public boolean hasAssociations() {
		return(manager.hasAssocations(ifile));
	}

	public void clearAssociations() {
		manager.clearAssociations(ifile);
	}

	public void removeAssociation(Tag tag) {
		manager.findTagSetContainer(tag).removeAssociation(ifile, tag);
	}
}
