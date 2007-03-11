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
