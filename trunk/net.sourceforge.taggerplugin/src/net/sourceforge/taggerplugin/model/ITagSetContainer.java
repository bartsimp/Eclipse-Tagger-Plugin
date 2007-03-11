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
