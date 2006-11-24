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


import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerLog;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.search.ui.ISearchQuery;
import org.eclipse.search.ui.ISearchResult;

class TagSearchQuery implements ISearchQuery {
	
	private final TagSearchInput input;
	private final ISearchResult result;
	
	TagSearchQuery(final TagSearchInput input, final TagSearchResult result){
		super();
		this.input = input;
		
		this.result = result;
		result.setQuery(this);
	}
	
	public boolean canRerun() {return true;}

	public boolean canRunInBackground() {return true;}

	public String getLabel() {return("Tag Search Query");}	// TODO: externalize

	public ISearchResult getSearchResult() {return(result);}

	public IStatus run(IProgressMonitor monitor) throws OperationCanceledException {
		try {
			final TaggableResourceVisitor visitor = new TaggableResourceVisitor(input,(TagSearchResult)result);
			ResourcesPlugin.getWorkspace().getRoot().accept(visitor, IResource.NONE);
			return(new Status(IStatus.OK,TaggerActivator.PLUGIN_ID,IStatus.OK,"Tag search completed.",null));
		} catch(CoreException ce){
			// FIXME: send to user
			TaggerLog.error("Unable to perform search: " + ce.getMessage(), ce);
			return(new Status(IStatus.ERROR,TaggerActivator.PLUGIN_ID,ce.getStatus().getCode(),"Tag search error.",ce));
		}
	}
}