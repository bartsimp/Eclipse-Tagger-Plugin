package net.sourceforge.taggerplugin.action;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.dialog.TagSelectionDialog;
import net.sourceforge.taggerplugin.model.ITagSetContainer;
import net.sourceforge.taggerplugin.model.ITaggableResource;
import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class AddTagAssociationAction implements IObjectActionDelegate {
	
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
			if(!sel.isEmpty()){
				final ITagSetContainer workspaceContainer = (ITagSetContainer)(ResourcesPlugin.getWorkspace()).getAdapter(ITagSetContainer.class);
				final Set<ITagSetContainer> containers = new HashSet<ITagSetContainer>();
				final List<ITaggableResource> taggables = new LinkedList<ITaggableResource>();
				final Set<Tag> sharedTags = new HashSet<Tag>();

				// find the containers and taggables selected
				final TreePath[] paths = sel.getPaths();
				for(TreePath path : paths){
					if(path.getLastSegment() instanceof IFile && path.getFirstSegment() instanceof IProject){
						containers.add((ITagSetContainer)((IProject)path.getFirstSegment()).getAdapter(ITagSetContainer.class));

						final ITaggableResource itr = (ITaggableResource)((IFile)path.getLastSegment()).getAdapter(ITaggableResource.class);
						taggables.add(itr);

						sharedTags.addAll(Arrays.asList(itr.getAssociations()));
					}
				}

				// cull the shared tags list to be only shared tags
				final Iterator<Tag> tags = sharedTags.iterator();
				while(tags.hasNext()){
					final Tag t = tags.next();
					for(ITaggableResource itr : taggables){
						if(!itr.hasAssociation(t)){
							tags.remove();
							break;
						}
					}
				}

				final Set<Tag> taglist = new HashSet<Tag>();
				taglist.addAll(Arrays.asList(workspaceContainer.getTags()));
				if(containers.size() == 1){
					// if there is more than one container (project) only the workspace tags are available
					taglist.addAll(Arrays.asList(containers.iterator().next().getTags()));
				}

				// remove tags that are already shared by all selected taggables
				taglist.removeAll(sharedTags);

				final TagSelectionDialog dialog = new TagSelectionDialog(
						activePart.getSite().getShell(),
						taglist.toArray(new Tag[taglist.size()]),
						TaggerMessages.AddTagAssociationAction_Title,
						TaggerMessages.AddTagAssociationAction_Message
				);

				if(dialog.open() == TagSelectionDialog.OK){
					final Object[] selectedTags = dialog.getResult();
					for (Object tag : selectedTags) {
						final Tag t = (Tag)tag;
						for(ITaggableResource taggable : taggables){
							taggable.addAssociation(t);
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
