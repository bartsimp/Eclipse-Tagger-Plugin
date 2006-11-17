package net.sourceforge.taggerplugin.action;

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
		try {
			final TagDialog dialog = new TagDialog(view.getSite().getShell());
			if(dialog.showCreate() == TagDialog.OK){
				final String name = dialog.getNameValue();
				final String desc = dialog.getDescriptionValue();
				
				System.out.println(name + " --> " + desc);
			}
		} catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		/*
		 * TODO: implement
		 * 	-	will popup a dialog for inputting new tag information
		 * 	-	if(!cancel) addTag to manager
		 * 	-	close
		 */
//		final IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
//		try {
//			page.openEditor(new JournalEditorInput("Untitled",false), JournalEntryEditor.ID, true);
//		} catch(PartInitException pie){
//			pie.printStackTrace();
//		}
	}

	public void selectionChanged(IAction action, ISelection selection) {}
}
