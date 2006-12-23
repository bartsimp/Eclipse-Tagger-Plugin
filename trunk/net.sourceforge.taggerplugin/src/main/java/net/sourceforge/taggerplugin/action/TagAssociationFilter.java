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
package net.sourceforge.taggerplugin.action;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.resource.ITaggable;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * ViewerFilter used to filter the ResourceNavigator and CommonNavigator so that only resources with 
 * the specified tags are shown.
 * 
 * Currently, this filter only matches resources that have any of the selected tag associations.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
final class TagAssociationFilter extends ViewerFilter {
	
	private final List<Tag> tags = new LinkedList<Tag>();
	
	TagAssociationFilter(){
		super();
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		final IResource resource = (IResource)element;
		
		// closed projects fail fast
		if(isClosedProject(resource)){
			return(false);
		}
		
		boolean tagged = isTagged(resource,tags);
		if(!tagged){
			final Holder holder = new Holder();
			
			// check the children of the resource to see if any of them are tagged
			try {
				resource.accept(new IResourceVisitor(){
					public boolean visit(IResource resource) throws CoreException {
						if(!holder.accept){
							holder.accept = isTagged(resource, tags);
						}
						return(true);
					}
				});
			} catch(CoreException ce){
				TaggerLog.error("Unable to filter resources: " + ce.getMessage(), ce);
			}
			
			tagged = holder.accept;
		}
		return(tagged);
	}

	void addTag(Tag tag){
		tags.add(tag);
	}
	
	Tag[] getTags(){
		return(tags.toArray(new Tag[tags.size()]));
	}
	
	private static boolean isTagged(final IResource resource, final List<Tag> tags){
		final ITaggable taggable = (ITaggable)resource.getAdapter(ITaggable.class);
		if(taggable != null){
			for(Tag tag : tags){
				if(taggable.hasTag(tag.getId())){
					return(true);
				}
			}
		}
		return(false);
	}
	
	private boolean isClosedProject(IResource resource){
		final IProject project = (IProject)resource.getAdapter(IProject.class);
		return(project != null && !project.isOpen());
	}
	
	private static final class Holder {
		public boolean accept = false;
	}
}