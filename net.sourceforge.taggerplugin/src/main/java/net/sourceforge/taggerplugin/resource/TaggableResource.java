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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.sourceforge.taggerplugin.TaggerActivator;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

class TaggableResource implements ITaggable {
	
	private static final QualifiedName TAG_ASSOCIATIONS = new QualifiedName(TaggerActivator.PLUGIN_ID,"tag.associations");
	private final IResource resource;

	TaggableResource(final IResource resource) {
		this.resource = resource;
	}

	public void clearTag(UUID id) {
		final Set<String> tags = loadTags();
		tags.remove(id.toString());
		saveTags(tags);
	}

	public void clearTags() {
		try {
			resource.setPersistentProperty(TAG_ASSOCIATIONS, null);
		} catch(CoreException ce){
			throw new RuntimeException(ce);
		}
	}

	public boolean hasTag(UUID id) {
		return(loadTags().contains(id.toString()));
	}

	public UUID[] listTags() {
		final Set<String> tags = loadTags();
		final UUID[] uuids = new UUID[tags.size()];
		int idx = 0;
		for (String tag : tags) {
			uuids[idx++] = UUID.fromString(tag);
		}
		return(uuids);
	}

	public void setTag(UUID id) {
		final Set<String> tags = loadTags();
		tags.add(id.toString());
		saveTags(tags);
	}

	private Set<String> loadTags(){
		try {
			final Set<String> tags = new HashSet<String>();
			final String tagsStr = this.resource.getPersistentProperty(TAG_ASSOCIATIONS);
			if(tagsStr != null){
				tags.addAll(Arrays.asList(tagsStr.split(";")));
			}
			return(tags);
		} catch(CoreException ce){
			throw new RuntimeException(ce);	// TODO: is this the best way?
		}
	}

	private void saveTags(Set<String> tags){
		try {
			final StringBuilder str = new StringBuilder();
			for (String tag : tags) {
				str.append(tag).append(";");
			}
			if(!tags.isEmpty()){
				str.deleteCharAt(str.length()-1);
			}
			
			this.resource.setPersistentProperty(TAG_ASSOCIATIONS, str.toString());
		} catch(CoreException ce){
			throw new RuntimeException(ce);	// TODO: is this the best way?
		}
	}
}