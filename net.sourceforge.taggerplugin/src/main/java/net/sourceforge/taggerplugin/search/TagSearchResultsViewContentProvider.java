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

import net.sourceforge.taggerplugin.event.TagSearchResultEvent;
import net.sourceforge.taggerplugin.event.TagSearchResultEvent.Type;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.swt.widgets.Display;

/**
 * Content provider for the tableviewer in the Tag Search Result page.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagSearchResultsViewContentProvider implements IStructuredContentProvider,ISearchResultListener {

	private TagSearchResult result;
	private TableViewer viewer;
	
	TagSearchResultsViewContentProvider(){
		super();
	}
	
	public Object[] getElements(Object inputElement) {
		return(result.getMatches());
	}

	public void dispose() {}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TableViewer)viewer;	
		
		if(result != null){
			// remove the listener from the old input
			result.removeListener(this);
		}
		
		if(newInput != null){
			result = (TagSearchResult)newInput;
			result.addListener(this);
		}
	}
	
	public void searchResultChanged(SearchResultEvent e) {
		final TagSearchResultEvent evt = (TagSearchResultEvent)e;

		// NOTE: this is a bit hacky but there are NO good search examples on line and SWT is single-threaded and 
		// throws an error if you access the ui from another thread
		Display.getDefault().asyncExec(new Runnable(){
			public void run() {
				viewer.getTable().setRedraw(false);
				try {
					if(evt.getType().equals(Type.ADDED)){
						viewer.add(evt.getResources());	
					} else if(evt.getType().equals(Type.REMOVED)){
						viewer.remove(evt.getResources());
					}
				} finally {
					viewer.getTable().setRedraw(true);
				}		
			}
		});
	}
}