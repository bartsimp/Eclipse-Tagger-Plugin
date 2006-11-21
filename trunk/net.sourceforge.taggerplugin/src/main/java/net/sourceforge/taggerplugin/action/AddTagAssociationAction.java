package net.sourceforge.taggerplugin.action;

import java.util.Set;
import java.util.UUID;

import net.sourceforge.taggerplugin.manager.TagAssociationManager;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.resource.ITaggable;
import net.sourceforge.taggerplugin.ui.TagSelectionDialog;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * This action is used to add the same tag associations to all selected resources.
 * Tag associations that are common to all selected resources will not be available 
 * for selection; however, associations common to only some of the resources will 
 * be available.
 * 
 * This action will display the TagSelectionDialog
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class AddTagAssociationAction implements IObjectActionDelegate {
	
	private IWorkbenchPart activePart;
	private ISelection selection;
	
	public void run(IAction action) {
		if(selection instanceof IStructuredSelection){
			final IStructuredSelection sel = (IStructuredSelection)selection;
			if(!sel.isEmpty()){
				try {
					final Object[] resources = sel.toArray();
					final ITaggable[] taggables = new ITaggable[resources.length];
					final String[] resourceIds = new String[resources.length];
					for(int i=0; i<resources.length; i++){
						taggables[i] = (ITaggable)((IResource)(resources[i])).getAdapter(ITaggable.class);
						resourceIds[i] = taggables[i].getResourceId();
					}
					
					final Set<UUID> sharedAssociations = TagAssociationManager.getInstance().findSharedAssociations(resourceIds);
					final Tag[] taglist = TagManager.getInstance().findTagsNotIn(sharedAssociations.toArray(new UUID[sharedAssociations.size()]));
				
					final TagSelectionDialog dialog = new TagSelectionDialog(activePart.getSite().getShell(),taglist);
					if(dialog.open() == TagSelectionDialog.OK){
						final Object[] selectedTags = dialog.getResult();
						for (Object tag : selectedTags) {
							final Tag t = (Tag)tag;
							for(ITaggable taggable : taggables){
								taggable.setTag(t.getId());
							}
						}
					}
				} catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}
	}

	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.activePart = targetPart;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}
}
