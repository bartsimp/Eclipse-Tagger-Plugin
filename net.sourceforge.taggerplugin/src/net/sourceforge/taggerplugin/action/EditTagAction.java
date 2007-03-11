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

import java.util.Arrays;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.dialog.TagDialog;
import net.sourceforge.taggerplugin.model.ITagSetContainer;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.model.TagSetContainerManager;
import net.sourceforge.taggerplugin.view.TagSetView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class EditTagAction implements IViewActionDelegate {
	
	private IViewPart view;

	/**
	 * @see IViewActionDelegate#init(IViewPart)
	 */
	public void init(IViewPart view) {
		this.view = view;
	}

	/**
	 * @see IViewActionDelegate#run(IAction action)
	 */
	public void run(IAction action) {
		final TagSetView tagView = (TagSetView)view;
		final ITreeSelection selection = (ITreeSelection)tagView.getSelection();
		if(!selection.isEmpty()){
			final TreePath[] paths = selection.getPaths();
			final Object lastSeg = paths[0].getLastSegment(); 
			if(lastSeg instanceof Tag){
				final Tag tag = (Tag)lastSeg;
				final ITagSetContainer container = (ITagSetContainer)paths[0].getFirstSegment();
				if(container != null){
					final TagDialog dialog = new TagDialog(view.getSite().getShell());
					dialog.setTagContainerEditable(false);
					dialog.setTagContainers(containerNames());
					dialog.setTagContainerId(container.getName());
					dialog.setTagName(tag.getName());
					dialog.setTagDescription(tag.getDescription());
					
					if(dialog.showModify() == TagDialog.OK){
						container.updateTag(tag.getId(),dialog.getTagName(),dialog.getTagDescription());
					}		
				}
			}
		}
	}

	/**
	 * @see IViewActionDelegate#selectionChanged(IAction,ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {}
	
	private String[] containerNames(){
		return TagSetContainerManager.extractContainerNames(Arrays.asList(TaggerActivator.getDefault().getTagSetContainerManager().getTagSetContainers()),false);
	}
}
