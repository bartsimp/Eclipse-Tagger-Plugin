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
package net.sourceforge.taggerplugin.event;

import org.eclipse.core.resources.IResource;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.SearchResultEvent;

public class TagSearchResultEvent extends SearchResultEvent {

	private static final long serialVersionUID = -5424223750735617727L;
	private final IResource resource;
	
	public TagSearchResultEvent(ISearchResult searchResult, IResource resource){
		super(searchResult);
		this.resource = resource;
	}
	
	public IResource getResource() {
		return this.resource;
	}
}