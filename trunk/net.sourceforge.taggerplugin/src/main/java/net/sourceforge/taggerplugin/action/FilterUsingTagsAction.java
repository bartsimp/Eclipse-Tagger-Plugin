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


import net.sourceforge.taggerplugin.dialog.TagSelectionDialog;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.navigator.CommonNavigator;
import org.eclipse.ui.views.navigator.ResourceNavigator;

/**
 * When triggered, this action will open a tag selection dialog to filter the associated view by the 
 * selected tag associtaions.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class FilterUsingTagsAction implements IViewActionDelegate {

	private IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		// TODO: look into this... there is probably a better way
		TreeViewer viewer = null;
		if(view instanceof ResourceNavigator){
			viewer = ((ResourceNavigator)view).getViewer();	
		} else if(view instanceof CommonNavigator){
			viewer = ((CommonNavigator)view).getCommonViewer();	
		} else {
			return;
		}
		
		TagAssociationFilter tagFilter = findTagFilter(viewer);
			
		final TagSelectionDialog dialog = new TagSelectionDialog(view.getSite().getShell(),TagManager.getInstance().getTags());
		if(tagFilter != null){
			dialog.setInitialSelections(tagFilter.getTags());
		}
		if(dialog.open() == TagSelectionDialog.OK){
			final Object[] results = dialog.getResult();
			if(results != null && results.length != 0){
				if(tagFilter == null){
					// create a new filter populate it and add it
					tagFilter = new TagAssociationFilter();
					for(Object obj : results){
						tagFilter.addTag((Tag)obj);
					}
					viewer.addFilter(tagFilter);
				} else {
					// populate the filter that currently exists
					for(Object obj : results){
						tagFilter.addTag((Tag)obj);
					}
				}
			} else {
				if(tagFilter != null){
					// remove the filter that is no longer needed
					viewer.removeFilter(tagFilter);
				}
			}
		}
	}

	private TagAssociationFilter findTagFilter(final TreeViewer viewer) {
		TagAssociationFilter tagFilter = null;
		final ViewerFilter[] filters = viewer.getFilters();
		for (ViewerFilter filter : filters) {
			if(filter instanceof TagAssociationFilter){
				tagFilter = (TagAssociationFilter)filter;
				break;
			}
		}
		return(tagFilter);
	}

	public void selectionChanged(IAction action, ISelection selection) {}
}
