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

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.taggerplugin.dialog.TagSelectionDialog;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.views.navigator.ResourceNavigator;

/**
 * 
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class FilterUsingTagsAction implements IViewActionDelegate {

	private IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		final ResourceNavigator navView = (ResourceNavigator)view;
		final TreeViewer viewer = navView.getViewer();
			
		final TagSelectionDialog dialog = new TagSelectionDialog(view.getSite().getShell(),TagManager.getInstance().getTags());
//			dialog.setInitialSelections(selectedElements);
		if(dialog.open() == TagSelectionDialog.OK){
			final Object[] results = dialog.getResult();
			final List<String> tagIds = new LinkedList<String>();
			for(Object obj : results){
				tagIds.add(((Tag)obj).getId());
			}
			viewer.addFilter(new TagAssociationFilter(tagIds));
		}
			
		
		/*
		 * 	-	open tag selection dialog with all tags
		 * 		-	current filter will be selected
		 * 	-	filter view using selected tags
		 * 		-	getViewer (treeviewer) ... addFilter() refresh?
		 */
	}

	public void selectionChanged(IAction action, ISelection selection) {}
	
	private static final class TagAssociationFilter extends ViewerFilter {
		
		private final List<String> tagIds;
		
		private TagAssociationFilter(final List<String> tagIds){
			this.tagIds = tagIds;
		}

		@Override
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			return false;
		}
	}
}
