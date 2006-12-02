/*
 *	Copyright 2006 Christopher J. Stehno (chris@stehno.com)
 *
 * 	Licensed under the Apache License, Version 2.0 (the "License");
 *	you may not use this file except in compliance with the License.
 *	You may obtain a copy of the License at
 *
 *		http://www.apache.org/licenses/LICENSE-2.0
 *
 *	Unless required by applicable law or agreed to in writing, software
 *	distributed under the License is distributed on an "AS IS" BASIS,
 * 	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	See the License for the specific language governing permissions and
 *	limitations under the License.
 */
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