package net.sourceforge.taggerplugin.action;

import net.sourceforge.taggerplugin.resource.ITaggable;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This action is used to clear all tag associations from all selected files.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class ClearTagAssociationsAction implements IObjectActionDelegate {

	private ISelection selection;
	
	public void run(IAction action) {
		if(selection instanceof IStructuredSelection){
			final IStructuredSelection sel = (IStructuredSelection)selection;
			if(!sel.isEmpty()){
				final Object[] selectedObjs = sel.toArray();
				for (Object obj : selectedObjs) {
					final IResource resource = (IResource)obj;
					final ITaggable taggable = (ITaggable)resource.getAdapter(ITaggable.class);
					taggable.clearTags();
				}
			}
		}
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart){}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}
}
