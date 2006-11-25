package net.sourceforge.taggerplugin.action;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.manager.TaggedMarker;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.resource.ITaggable;
import net.sourceforge.taggerplugin.view.TagView;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;

/**
 * Action to create a new working set from the resources marked with the selected tag (from tag view).
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class CreateWorkingSetFromTagsAction implements IViewActionDelegate {

	private IViewPart viewPart;
	
	public void init(IViewPart view) {
		this.viewPart = view;
	}

	public void run(IAction action) {
		final TagView tagView = (TagView)viewPart;
		final Tag selectedTag = tagView.getSelectedTag();
		if(selectedTag != null){
			try {
				final IWorkingSetManager workingSetMgr = PlatformUI.getWorkbench().getWorkingSetManager();

				final IWorkingSet workingSet = workingSetMgr.createWorkingSet(selectedTag.getName() + " Working Set",findResourcesWithTag(selectedTag));
				workingSetMgr.addWorkingSet(workingSet);
				
				// FIXME: needs externalization
				MessageDialog.openInformation(viewPart.getSite().getShell(), "Working Set Created","Working Set '" + workingSet.getName() + "' has been created.");
				
			} catch(CoreException ce){
				// FIXME: send to user
				TaggerLog.error("Unable to create working set: " + ce.getMessage(),ce);
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {}
	
	private IResource[] findResourcesWithTag(final Tag selectedTag) throws CoreException {
		final IMarker[] markers = ResourcesPlugin.getWorkspace().getRoot().findMarkers(TaggedMarker.MARKER_TYPE,false,IResource.DEPTH_INFINITE);
		final List<IResource> resources = new LinkedList<IResource>();
		for(IMarker marker : markers){
			final IResource resource = marker.getResource();
			final ITaggable taggable = (ITaggable)resource.getAdapter(ITaggable.class);
			if(taggable.hasTag(selectedTag.getId())){
				resources.add(resource);
			}
		}
		return(resources.toArray(new IResource[resources.size()]));
	}
}
