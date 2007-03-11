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
import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.nature.TaggableProjectNature;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;

public class TagSetContainerAdapterFactory implements IAdapterFactory {

	private static final Class[] ADAPTERS = {ITagSetContainer.class};
	
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		ITagSetContainer tsc = null;
		if(adaptableObject instanceof IWorkspace){
			tsc = adapt((IWorkspace)adaptableObject);
		} else if(adaptableObject instanceof IProject){
			final IProject project = (IProject)adaptableObject;
			try {
				if(project.isOpen() && project.hasNature(TaggableProjectNature.ID)){
					tsc = adapt((IProject)adaptableObject);	
				} else if(project.isOpen()){
					// default to workspace if project is not taggable itself
					tsc = adapt(ResourcesPlugin.getWorkspace());
				}
			} catch(CoreException ce){
				TaggerLog.error(ce);
			}
		} 
		return(tsc);
	}
	
	public Class[] getAdapterList() {
		return(ADAPTERS);
	}
	
	protected ITagSetContainer adapt(final IWorkspace workspace){
		return(TaggerActivator.getDefault().getTagSetContainerManager().getTagSetContainer(WorkspaceTagSetContainer.NAME));
	}
	
	protected ITagSetContainer adapt(final IProject project){
		return(TaggerActivator.getDefault().getTagSetContainerManager().getTagSetContainer(project.getName()));
	}
}
