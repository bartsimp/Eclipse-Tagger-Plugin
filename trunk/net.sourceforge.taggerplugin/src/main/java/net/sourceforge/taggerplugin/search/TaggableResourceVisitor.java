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
package net.sourceforge.taggerplugin.search;

import java.util.UUID;

import net.sourceforge.taggerplugin.resource.ITaggable;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;

/**
 * Visitor used to walk the workspace resource tree.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TaggableResourceVisitor implements IResourceProxyVisitor {
	
	private final UUID[] tagIds;
	private final boolean requireAll;
	private final ITagSearchResult result;
	
	public TaggableResourceVisitor(final UUID[] tagIds, final boolean requireAll, final ITagSearchResult result){
		super();
		this.tagIds = tagIds;
		this.requireAll = requireAll;
		this.result = result;
	}

	public boolean visit(IResourceProxy proxy) throws CoreException {
		final IResource resource = proxy.requestResource();
		final ITaggable taggable = (ITaggable)resource.getAdapter(ITaggable.class);
		
		if(requireAll ? matchesAll(taggable,tagIds) : matchesAny(taggable,tagIds)){
			result.addMatch(resource);
		}
		
		return true;
	}
	
	private boolean matchesAny(ITaggable taggable, UUID[] ids){
		if(taggable.hasTags()){
			for(UUID id : ids){
				if(taggable.hasTag(id)){
					return(true);
				}
			}	
		}
		return(false);
	}
	
	private boolean matchesAll(ITaggable taggable, UUID[] ids){
		boolean hasAll = false;
		if(taggable.hasTags()){
			for(UUID id : ids){
				hasAll = taggable.hasTag(id);
				if(!hasAll){
					return(false);
				}
			}
		}
		return(hasAll);
	}
}