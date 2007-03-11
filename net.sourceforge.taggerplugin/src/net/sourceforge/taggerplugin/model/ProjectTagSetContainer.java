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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

class ProjectTagSetContainer extends AbstractTagSetContainer {
	
	private static final String PROJECT_TAGSETFILENAME = ".tagset";
	private final IProject project;
	
	ProjectTagSetContainer(final IProject project){
		super(project.getName());
		this.project = project;
		setTagSetFile(((IResource)project.getAdapter(IResource.class)).getLocation().append(PROJECT_TAGSETFILENAME).toFile());
	}
	
	public int hashCode(){
		return(new HashCodeBuilder(7,13).append(project).toHashCode());
	}
	
	public boolean equals(Object obj){
		boolean eq = false;
		if(obj instanceof ProjectTagSetContainer){
			eq = new EqualsBuilder().append(project,((ProjectTagSetContainer)obj).project).isEquals();
		}
		return(eq);
	}
}
