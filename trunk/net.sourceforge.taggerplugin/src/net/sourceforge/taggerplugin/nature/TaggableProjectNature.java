package net.sourceforge.taggerplugin.nature;

import net.sourceforge.taggerplugin.TaggerActivator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

public class TaggableProjectNature implements IProjectNature {

	public static final String ID = "net.sourceforge.taggerplugin.Nature";

	private IProject project;
	
	public void configure() throws CoreException {
		TaggerActivator.getDefault().getTagSetContainerManager().registerTagSet(getProject());
	}

	public void deconfigure() throws CoreException {
		TaggerActivator.getDefault().getTagSetContainerManager().deregisterTagSet(getProject());
	}

	public IProject getProject() {
		return(project);
	}

	public void setProject(IProject project) {
		this.project = project;
	}
}
