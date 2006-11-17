package net.sourceforge.taggerplugin.action;

import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.ui.TagDialog;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

public class NewTagAction implements IViewActionDelegate {

	private IViewPart view;

	public void init(IViewPart view) {
		this.view = view;
	}

	public void run(IAction action) {
		final TagDialog dialog = new TagDialog(view.getSite().getShell());
		if(dialog.showCreate() == TagDialog.OK){
			TagManager.getInstance().addTag(dialog.getTag());
		}
	}

	public void selectionChanged(IAction action, ISelection selection) {}
}
