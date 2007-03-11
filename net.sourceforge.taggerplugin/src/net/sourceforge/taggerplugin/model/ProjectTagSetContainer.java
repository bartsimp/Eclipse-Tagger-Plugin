package net.sourceforge.taggerplugin.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

class ProjectTagSetContainer extends AbstractTagSetContainer {
	
	private static final String PROJECT_TAGSETFILENAME = ".tagset";
	private final IProject project;
	
	ProjectTagSetContainer(final IProject project){
		super(project.getName());
		this.project = project;
		setTagSetFile(((IResource)project.getAdapter(IResource.class)).getLocation().append(PROJECT_TAGSETFILENAME).toFile());
	}
	
	public int hashCode(){
		return(new HashCodeBuilder(7,13).append(project).toHashCode());
	}
	
	public boolean equals(Object obj){
		boolean eq = false;
		if(obj instanceof ProjectTagSetContainer){
			eq = new EqualsBuilder().append(project,((ProjectTagSetContainer)obj).project).isEquals();
		}
		return(eq);
	}
}
