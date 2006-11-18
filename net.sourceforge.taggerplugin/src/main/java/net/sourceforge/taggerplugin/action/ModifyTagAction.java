package net.sourceforge.taggerplugin.action;

import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.ui.TagDialog;
import net.sourceforge.taggerplugin.view.TagView;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

/**
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class ModifyTagAction implements IViewActionDelegate {

	private IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		final TagView tagView = (TagView)view;
		final Tag selectedTag = tagView.getSelectedTag();
		if(selectedTag != null){
			final TagDialog dialog = new TagDialog(view.getSite().getShell());
			dialog.setTag(selectedTag);
			if(dialog.showModify() == TagDialog.OK){
				TagManager.getInstance().updateTag(selectedTag);
			}
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {}
}
