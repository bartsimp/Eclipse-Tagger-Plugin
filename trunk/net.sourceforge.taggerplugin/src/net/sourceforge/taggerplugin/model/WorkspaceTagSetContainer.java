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

import java.io.File;

import net.sourceforge.taggerplugin.TaggerActivator;

import org.apache.commons.lang.builder.HashCodeBuilder;

class WorkspaceTagSetContainer extends AbstractTagSetContainer {

	public static final String NAME = "Workspace";
	
	private static final String WORKSPACE_TAGSETFILENAME = ".workspace-tagset";
	
	WorkspaceTagSetContainer(){
		super(NAME);
		setTagSetFile(new File(TaggerActivator.getDefault().getStateLocation().toFile(),WORKSPACE_TAGSETFILENAME)); 
	}
	
	public int hashCode(){
		return(new HashCodeBuilder(7,13).toHashCode());
	}
	
	public boolean equals(Object obj){
		return(obj instanceof WorkspaceTagSetContainer);
	}
}
