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

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.model.TagSetContainerManager;
import net.sourceforge.taggerplugin.preference.PreferenceConstants;
import net.sourceforge.taggerplugin.view.TagSetView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class DeleteTagAction implements IViewActionDelegate {

	private IViewPart view;

	/**
	 * @see IViewActionDelegate#init(IViewPart)
	 */
	public void init(IViewPart view) {
		this.view = view;
	}

	/**
	 * @see IViewActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		final TagSetView tagSetView = (TagSetView)view;
		final ITreeSelection selection = (ITreeSelection)tagSetView.getSelection();
		if(!selection.isEmpty()){
			final List<Tag> tags = new LinkedList<Tag>();
			final TreePath[] paths = selection.getPaths();
			for(TreePath path : paths){
				final Object lastSeg = path.getLastSegment();
				if(lastSeg instanceof Tag){
					tags.add((Tag)lastSeg);
				} else {
					// all selected are not tags
					return;
				}
			}
			
			if(!tags.isEmpty() && (!showConfirmation() || confirm(tags.size()))){
				final TagSetContainerManager manager = TaggerActivator.getDefault().getTagSetContainerManager();
				for(Tag tag : tags){
					manager.getTagSetContainer(tag).removeTag(tag);
				}
			}
		}
	}
	
	/**
	 * @see IViewActionDelegate#selectionChanged(IAction,ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {}
	
	protected boolean showConfirmation(){
		final IPreferenceStore store = TaggerActivator.getDefault().getPreferenceStore();
		return store.getBoolean(PreferenceConstants.CONFIRM_DELETE_TAG.getKey());
	}
	
	protected boolean confirm(int count){
		return MessageDialog.openConfirm(
			view.getSite().getShell(),
			TaggerMessages.DeleteTagAction_Confirm_Title,
			TaggerMessages.bind(TaggerMessages.DeleteTagAction_Confirm_Text, count)
		);
	}
}