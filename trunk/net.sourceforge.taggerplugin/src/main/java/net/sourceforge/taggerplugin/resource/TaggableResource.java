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
package net.sourceforge.taggerplugin.resource;

import java.util.UUID;

import net.sourceforge.taggerplugin.manager.TagAssociationManager;

import org.eclipse.core.resources.IResource;

/**
 * Implentation of the ITaggable interface that delegates to the internally held
 * IResource implementation and the TagAssociationManager.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TaggableResource implements ITaggable {
	
	private final IResource resource;

	TaggableResource(final IResource resource) {
		this.resource = resource;
	}
	
	public void setTag(UUID id) {
		TagAssociationManager.getInstance().addAssociation(resource, id);
	}

	public void clearTag(UUID id) {
		TagAssociationManager.getInstance().clearAssociation(resource, id);
	}

	public void clearTags() {
		TagAssociationManager.getInstance().clearAssociations(resource);
	}

	public boolean hasTag(UUID id) {
		return(TagAssociationManager.getInstance().hasAssociation(resource, id));
	}

	public UUID[] listTags() {
		return(TagAssociationManager.getInstance().getAssociations(resource));
	}
	
	public boolean hasTags(){
		return(TagAssociationManager.getInstance().hasAssociations(resource));
	}
}