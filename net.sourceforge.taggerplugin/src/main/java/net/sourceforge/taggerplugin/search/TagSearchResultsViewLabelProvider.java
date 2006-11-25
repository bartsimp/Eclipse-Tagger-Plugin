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

import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.resource.ITaggable;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider for the tableviewer in the Tag Search results page.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagSearchResultsViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	private static final String DELIM = ", ";
	private static final int COLUMN_NAME = 0;
	private static final int COLUMN_PATH = 1;
	private static final int COLUMN_TAGS = 2;
	
	TagSearchResultsViewLabelProvider(){
		super();
	}
	
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		final IResource resource = (IResource)element;
		switch(columnIndex){
			case COLUMN_NAME:
				return(resource.getName());
			case COLUMN_PATH:
				return(String.valueOf(resource.getLocation()));
			case COLUMN_TAGS:
				final ITaggable taggable = (ITaggable)resource.getAdapter(ITaggable.class);
				final Tag[] tags = TagManager.getInstance().findTags(taggable.listTags());
				
				final StringBuilder str = new StringBuilder();
				for (Tag tag : tags) {
					str.append(tag.getName()).append(DELIM);
				}
				
				if(tags.length > 0){
					str.delete(str.length() - 2, str.length());
				}
				
				return(str.toString());
			default:
				return("???");
		}
	}
}