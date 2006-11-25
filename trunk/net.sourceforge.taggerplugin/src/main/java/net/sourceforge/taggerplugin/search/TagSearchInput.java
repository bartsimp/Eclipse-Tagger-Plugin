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

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.ui.IWorkingSet;

/**
 * Tag Search page input object. Used to store the input values from the UI form.
 * This object also stores the search scope-based input added by the search container.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagSearchInput {
	
	private final UUID[] tagIds;
	private final boolean required;
	private final int scope;
	private String[] projectNames;
	private IWorkingSet[] workingSets;
	private IResource[] selectedResources;

	TagSearchInput(UUID[] tagIds, boolean required, final ISearchPageContainer container){
		super();
		this.tagIds = tagIds;
		this.required = required;
		
		this.scope = container.getSelectedScope();
		if(scope == ISearchPageContainer.SELECTED_PROJECTS_SCOPE){
			this.projectNames = container.getSelectedProjectNames();	
		} else if(scope == ISearchPageContainer.WORKING_SET_SCOPE){
			this.workingSets = container.getSelectedWorkingSets();	
		} else if(scope == ISearchPageContainer.SELECTION_SCOPE){
			final ISelection selection = container.getSelection();
			if(selection instanceof IStructuredSelection){
				final IStructuredSelection iss = (IStructuredSelection)selection;
				this.selectedResources = new IResource[iss.size()];
				int i = 0;
				for(Object obj : iss.toArray()){
					selectedResources[i++] = (IResource)obj;
				}
			}
		} else {
			// workspace scope -- nothing special
		}
	}
	
	public boolean isProjectsScope(){return(scope == ISearchPageContainer.SELECTED_PROJECTS_SCOPE);}
	
	public boolean isWorkingSetScope(){return(scope == ISearchPageContainer.WORKING_SET_SCOPE);}
	
	public boolean isSelectionScope(){return(scope == ISearchPageContainer.SELECTION_SCOPE);}
	
	public boolean isWorkspaceScope(){return(scope == ISearchPageContainer.WORKSPACE_SCOPE);}
	
	public String[] getProjectNames() {
		return this.projectNames;
	}
	
	public IWorkingSet[] getWorkingSets() {
		return this.workingSets;
	}
	
	public IResource[] getSelectedResources() {
		return this.selectedResources;
	}
	
	public UUID[] getTagIds() {
		return this.tagIds;
	}
	
	public boolean isRequired() {
		return this.required;
	}
}