/*   ********************************************************************** **
**   Copyright (c) 2006-2007 Christopher J. Stehno (chris@stehno.com)       **
**   http://www.stehno.com                                                  **
**                                                                          **
**   All rights reserved                                                    **
**                                                                          **
**   This program and the accompanying materials are made available under   **
**   the terms of the Eclipse Public License v1.0 which accompanies this    **
**   distribution, and is available at:                                     **
**   http://www.stehno.com/legal/epl-1_0.html                               **
**                                                                          **
**   A copy is found in the file license.txt.                               **
**                                                                          **
**   This copyright notice MUST APPEAR in all copies of the file!           **
**  **********************************************************************  */
package net.sourceforge.taggerplugin.action;

import java.util.UUID;

import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.search.ITagSearchResult;
import net.sourceforge.taggerplugin.search.TagSearchResult;
import net.sourceforge.taggerplugin.search.TaggableResourceVisitor;
import net.sourceforge.taggerplugin.view.TagView;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;

/**
 * Action to create a new working set from the resources marked with the selected tag (from tag view).
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class CreateWorkingSetFromTagsAction implements IViewActionDelegate {

	private IViewPart viewPart;

	public void init(IViewPart view) {
		this.viewPart = view;
	}

	public void run(IAction action) {
		final TagView tagView = (TagView)viewPart;
		final Tag[] selectedTags = tagView.getSelectedTags();
		if(selectedTags != null && selectedTags.length != 0){
			try {
				final IWorkingSetManager workingSetMgr = PlatformUI.getWorkbench().getWorkingSetManager();

				final IWorkingSet workingSet = workingSetMgr.createWorkingSet(createWsName(selectedTags),findResourcesWithTags(selectedTags));
				workingSetMgr.addWorkingSet(workingSet);

				// FIXME: needs externalization
				MessageDialog.openInformation(viewPart.getSite().getShell(), "Working Set Created","Working Set '" + workingSet.getName() + "' has been created.");

			} catch(CoreException ce){
				// FIXME: send to user
				TaggerLog.error("Unable to create working set: " + ce.getMessage(),ce);
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {}

	private IResource[] findResourcesWithTags(final Tag[] selectedTags) throws CoreException {
		final UUID[] tagids = new UUID[selectedTags.length];
		for(int t=0; t<selectedTags.length; t++){
			tagids[t] = selectedTags[t].getId();
		}
		
		final ITagSearchResult result = new TagSearchResult();
		ResourcesPlugin.getWorkspace().getRoot().accept(new TaggableResourceVisitor(tagids,false,result), IResource.NONE);

		// TODO: since this uses the search it would prb be best to pull common code into a mutual helper
		// TODO: should swtich out the search to use the marker rather than check all resources
		
		return(result.getMatches());
	}
	
	private String createWsName(final Tag[] tags){
		StringBuilder str = new StringBuilder("Working Set (");
		for(Tag t : tags){
			str.append(t.getName()).append(",");
		}
		if(tags.length != 0){
			str.deleteCharAt(str.length()-1);
		}
		str.append(")");
		return(str.toString());
	}
}
