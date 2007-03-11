package net.sourceforge.taggerplugin.action;

import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.dialog.ExceptionDialogFactory;
import net.sourceforge.taggerplugin.nature.TaggableProjectNature;

import org.apache.commons.lang.ArrayUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

public class ToggleTaggableNatureAction implements IObjectActionDelegate {

	private IWorkbenchPart activePart;
	private ISelection selection;
	
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
		this.activePart = targetPart;
	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}
	
	public void run(IAction action) {
		if(selection != null && !selection.isEmpty() && selection instanceof IStructuredSelection){
			final IStructuredSelection sel = (IStructuredSelection)selection;
			for(Object obj : sel.toArray()){
				final IProject project = (IProject)obj;
				if(project.isOpen()){
					try {
						final IProjectDescription desc = project.getDescription();
						if(project.hasNature(TaggableProjectNature.ID)){
							removeNature(desc);
						} else {
							addNature(desc);
						}
						project.setDescription(desc, null);
					} catch(CoreException ce){
						TaggerLog.error(ce);
						ExceptionDialogFactory.create(
							activePart.getSite().getShell(),
							TaggerMessages.AddTaggableNatureAction_Error_Title,
							TaggerMessages.bind(TaggerMessages.AddTaggableNatureAction_Error_Text,project.getName(),ce.getMessage()), 
							ce
						).open();
					}
				}
			}
		}
	}
	
	private void addNature(IProjectDescription desc){
		desc.setNatureIds((String[])ArrayUtils.add(desc.getNatureIds(), TaggableProjectNature.ID));
	}
	
	private void removeNature(IProjectDescription desc){
		final String[] natureIds = desc.getNatureIds();
		desc.setNatureIds((String[])ArrayUtils.remove(natureIds,ArrayUtils.indexOf(natureIds,TaggableProjectNature.ID)));
	}
}
