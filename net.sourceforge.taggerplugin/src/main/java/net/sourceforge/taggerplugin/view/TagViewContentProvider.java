package net.sourceforge.taggerplugin.view;

import net.sourceforge.taggerplugin.event.ITagManagerListener;
import net.sourceforge.taggerplugin.event.TagManagerEvent;
import net.sourceforge.taggerplugin.manager.TagManager;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * Provides the content for the TagView. Maps the manager operations to the view
 * operations.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagViewContentProvider implements IStructuredContentProvider, ITagManagerListener {

	private TableViewer viewer;
	private TagManager manager;

	/**
	 * Notifies this content provider that the given viewer's input
	 * has been switched to a different element.
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TableViewer) viewer;
		if (manager != null){
			manager.removeTagManagerListener(this);
		}

		manager = (TagManager) newInput;

		if (manager != null){
			manager.addTagManagerListener(this);
		}
	}

	public void dispose(){}

	/**
	 * Returns the elements to display in the viewer when its input is
	 * set to the given element. In our case, the partent is assumed to
	 * be the FavoritesManager and the items returned, the Favorite
	 * items in that manager.
	 */
	public Object[] getElements(Object parent) {
		return(manager.getTags());
	}

	public void handleTagManagerEvent(TagManagerEvent tme) {
		viewer.getTable().setRedraw(false);
		try {
			if(tme.getType().equals(TagManagerEvent.Type.ADDED)){
				viewer.add(tme.getTags());
			}
		} finally {
			viewer.getTable().setRedraw(true);
		}
	}
}
