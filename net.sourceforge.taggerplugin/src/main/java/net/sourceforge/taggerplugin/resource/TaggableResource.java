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

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.manager.TagAssociationManager;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

class TaggableResource implements ITaggable {
	
	private static final QualifiedName RESOURCE_ID = new QualifiedName(TaggerActivator.PLUGIN_ID,"resource.id");
	private final IResource resource;

	TaggableResource(final IResource resource) {
		this.resource = resource;
	}

	public void clearTag(UUID id) {
		TagAssociationManager.getInstance().addAssociation(extractResourceId(), id);
	}

	public void clearTags() {
		TagAssociationManager.getInstance().clearAssociations(extractResourceId());
	}

	public boolean hasTag(UUID id) {
		return(TagAssociationManager.getInstance().hasAssociation(extractResourceId(), id));
	}

	public UUID[] listTags() {
		return(TagAssociationManager.getInstance().getAssociations(extractResourceId()));
	}

	public void setTag(UUID id) {
		TagAssociationManager.getInstance().addAssociation(extractResourceId(), id);
	}
	
	public String getResourceId(){
		return(extractResourceId());
	}
	
	public boolean hasTags(){
		return(TagAssociationManager.getInstance().hasAssociations(extractResourceId()));
	}
	
	/**
	 * Used to retrieve the unique resource id from the resource. If the resource
	 * does not have one it will be created and stored.
	 *
	 * @return the unique resource id
	 */
	private String extractResourceId(){
		String rid = null;
		try {
			rid = resource.getPersistentProperty(RESOURCE_ID);
			if(rid == null || rid.length() == 0){
				rid = UUID.randomUUID().toString();
				resource.setPersistentProperty(RESOURCE_ID, rid);
			}
		} catch(CoreException ce){
			// FIXME: do something more useful
			throw new RuntimeException(ce);
		}
		return(rid);
	}
}