package net.sourceforge.taggerplugin.action;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.model.ITaggableResource;
import net.sourceforge.taggerplugin.preference.PreferenceConstants;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ClearTagAssociationsAction implements IObjectActionDelegate {
	
	private IWorkbenchPart activePart;
	private ISelection selection;
	
	/*
	 * if all files in same project
	 * 		tags = project-tags + ws-tags
	 * else
	 * 		tags = ws-tags
	 */

	/**
	 * @see IObjectActionDelegate#run(IAction action)
	 */
	public void run(IAction action) {
		if(selection instanceof IStructuredSelection){
			final ITreeSelection sel = (ITreeSelection)selection;
			if(!sel.isEmpty() && (!showConfirmation() || confirm())){
				for(Object elt : sel.toArray()){
					final IAdaptable adaptable = (IAdaptable)elt;
					final ITaggableResource taggable = (ITaggableResource)adaptable.getAdapter(ITaggableResource.class);
					taggable.clearAssociations();
				}
			}
		}
	}

	/**
	 * @see IObjectActionDelegate#setActivePart(IAction, IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.activePart = targetPart;
	}

	/**
	 * @see IObjectActionDelegate#selectionChanged(IAction,ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}
	
	protected boolean showConfirmation(){
		return (TaggerActivator.getDefault().getPreferenceStore()).getBoolean(PreferenceConstants.CONFIRM_CLEAR_ASSOCIATIONS.getKey());
	}
	
	protected boolean confirm(){
		return MessageDialog.openConfirm(
			activePart.getSite().getShell(),
			TaggerMessages.ClearTagAssociationsAction_Confirm_Title,
			TaggerMessages.ClearTagAssociationsAction_Confirm_Text
		);
	}
}
