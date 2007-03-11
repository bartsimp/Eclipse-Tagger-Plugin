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
package net.sourceforge.taggerplugin.nature;

import net.sourceforge.taggerplugin.TaggerActivator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class TaggableProjectNature implements IProjectNature {

	public static final String ID = "net.sourceforge.taggerplugin.Nature";

	private IProject project;
	
	public void configure() throws CoreException {
		TaggerActivator.getDefault().getTagSetContainerManager().registerTagSet(getProject());
	}

	public void deconfigure() throws CoreException {
		TaggerActivator.getDefault().getTagSetContainerManager().deregisterTagSet(getProject());
	}

	public IProject getProject() {
		return(project);
	}

	public void setProject(IProject project) {
		this.project = project;
	}
}
