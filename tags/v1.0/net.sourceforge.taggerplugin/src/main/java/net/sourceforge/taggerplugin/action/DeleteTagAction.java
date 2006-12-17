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

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.dialog.ExceptionDialogFactory;
import net.sourceforge.taggerplugin.manager.TagAssociationException;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.preferences.PreferenceConstants;
import net.sourceforge.taggerplugin.view.TagView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 *
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class DeleteTagAction implements IViewActionDelegate {

	private IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		final TagView tagView = (TagView)view;
		final Tag[] deletedTags = tagView.getSelectedTags();
		if(deleteConfirmed(deletedTags.length)){
			if(deletedTags != null && deletedTags.length != 0){
				try {
					TagManager.getInstance().deleteTags(deletedTags);
				} catch(TagAssociationException te){
					ExceptionDialogFactory.create(view.getSite().getShell(), te).open();
				}
			}
		}
	}

	private boolean deleteConfirmed(int tagCnt){
		if(tagCnt == 0){return(false);}

		final IPreferenceStore store = TaggerActivator.getDefault().getPreferenceStore();
		if(store.getBoolean(PreferenceConstants.CONFIRM_DELETE_TAG.getKey())){
			return(MessageDialog.openConfirm(view.getSite().getShell(),"Confirm Tag Deletion","Are you sure you want to delete the selected tag(s)?"));
		} else {
			return(true);
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {}
}
