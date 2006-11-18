package net.sourceforge.taggerplugin.action;

import java.util.Iterator;

import net.sourceforge.taggerplugin.resource.ITaggable;
import net.sourceforge.taggerplugin.ui.TagSelectionDialog;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class AddTagAction implements IViewActionDelegate {
	
	private ISelection selection;
	private IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		/*
		 * open dialog with tags
		 * 	-	tags set on all selected are bold (selected)
		 * 	-	tags set on some selected are normal (selected)
		 * 	-	available tags are normal (unselected)
		 *  
		 */
		
		if(selection instanceof IStructuredSelection){
			final IStructuredSelection sel = (IStructuredSelection)selection;
			if(!sel.isEmpty()){
				final TagSelectionDialog dialog = new TagSelectionDialog(view.getSite().getShell());
				if(dialog.open() == TagSelectionDialog.OK){
					// FIXME: Still working in here
					final Iterator items = sel.iterator();
					while(items.hasNext()){
						final IResource resource = (IResource)items.next();
						final ITaggable taggable = (ITaggable)resource.getAdapter(ITaggable.class);
						System.out.println("Tag count: " + taggable.listTags().length);
					}
					
				}
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
		action.setEnabled(!selection.isEmpty());
	}
}
