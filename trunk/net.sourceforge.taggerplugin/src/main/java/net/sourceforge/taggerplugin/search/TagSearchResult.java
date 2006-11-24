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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.taggerplugin.event.TagSearchResultEvent;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;

class TagSearchResult implements ISearchResult {
	
	private final Set<ISearchResultListener> listeners;
//	private final ISearchQuery query;
	private ISearchQuery query;
	private Set<IResource> matches;
	
	TagSearchResult(final ISearchQuery query){
		super();
		this.listeners = new HashSet<ISearchResultListener>();
		this.query = query;
		this.matches = Collections.synchronizedSet(new HashSet<IResource>());
	}
	
	TagSearchResult(){
		super();
		this.listeners = new HashSet<ISearchResultListener>();
		this.matches = Collections.synchronizedSet(new HashSet<IResource>());
	}
	
	void setQuery(ISearchQuery query){this.query = query;}
	
	public void addMatch(IResource resource){
		if(matches.add(resource)){
			fireSearchResultEvent(new TagSearchResultEvent(this,resource));
		}
	}
	
	public int getMatchCount(){
		return(matches.size());
	}
	
	public IResource[] getMatches(){
		return(matches.toArray(new IResource[matches.size()]));
	}

	public void addListener(ISearchResultListener l) {
		listeners.add(l);
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getLabel() {
		return "Tag Search Results";
	}

	public ISearchQuery getQuery() {
		return query;
	}

	public String getTooltip() {
		return "Tag search results.";
	}

	public void removeListener(ISearchResultListener l) {
		listeners.remove(l);
	}
	
	private void fireSearchResultEvent(SearchResultEvent sre){
		for(ISearchResultListener listener : listeners){
			listener.searchResultChanged(sre);
		}
	}
}