package net.sourceforge.taggerplugin.action;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sourceforge.taggerplugin.dialog.TagDialog;
import net.sourceforge.taggerplugin.model.ITagSetContainer;
import net.sourceforge.taggerplugin.model.ITaggableResource;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.model.TagSetContainerManager;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class CreateAndAddTagAssociationAction implements IObjectActionDelegate {
	
	private IWorkbenchPart activePart;
	private ISelection selection;
	
	/**
	 * @see IObjectActionDelegate#run(IAction action)
	 */
	public void run(IAction action) {
		if(!selection.isEmpty()){
			final Map<ITagSetContainer,Set<ITaggableResource>> map = new HashMap<ITagSetContainer, Set<ITaggableResource>>();
			
			final TreePath[] paths = ((ITreeSelection)selection).getPaths();
			for(TreePath path : paths){
				final Object lastSeg = path.getLastSegment();
				if(lastSeg instanceof IAdaptable){
					final ITaggableResource taggable = (ITaggableResource)((IAdaptable)lastSeg).getAdapter(ITaggableResource.class);
					if(taggable != null){
						final ITagSetContainer container = (ITagSetContainer)((IAdaptable)path.getFirstSegment()).getAdapter(ITagSetContainer.class);
						if(container == null){return;} // invalid selection
						
						if(map.containsKey(container)){
							map.get(container).add(taggable);
						} else {
							final Set<ITaggableResource> set = new HashSet<ITaggableResource>();
							set.add(taggable);
							map.put(container, set);
						}
					}
				} else {
					// invalid selection
					return;
				}
			}

			final TagDialog dialog = new TagDialog(activePart.getSite().getShell());
			dialog.setTagContainerEditable(true);
			dialog.setTagContainers(TagSetContainerManager.extractContainerNames(map.keySet(),true));
			dialog.setTagContainerId(TagSetContainerManager.CONTAINERNAME_ALL);

			if(dialog.showCreate() == TagDialog.OK){
				final String containerId = dialog.getTagContainerId();
				if(containerId.equals(TagSetContainerManager.CONTAINERNAME_ALL)){
					for(Entry<ITagSetContainer, Set<ITaggableResource>> entry : map.entrySet()){
						final Tag newTag = entry.getKey().addTag(dialog.getTagName(),dialog.getTagDescription());
						for(ITaggableResource itr : entry.getValue()){
							itr.addAssociation(newTag);
						}
					}	
				} else {
					for(Entry<ITagSetContainer, Set<ITaggableResource>> entry : map.entrySet()){
						if(entry.getKey().getName().equals(containerId)){
							final Tag newTag = entry.getKey().addTag(dialog.getTagName(),dialog.getTagDescription());
							for(ITaggableResource itr : entry.getValue()){
								itr.addAssociation(newTag);
							}
							break;
						}
					}						
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
}
