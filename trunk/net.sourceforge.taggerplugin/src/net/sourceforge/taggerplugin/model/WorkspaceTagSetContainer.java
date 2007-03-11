package net.sourceforge.taggerplugin.model;

import java.io.File;

import net.sourceforge.taggerplugin.TaggerActivator;

import org.apache.commons.lang.builder.HashCodeBuilder;

class WorkspaceTagSetContainer extends AbstractTagSetContainer {

	public static final String NAME = "Workspace";
	
	private static final String WORKSPACE_TAGSETFILENAME = ".workspace-tagset";
	
	WorkspaceTagSetContainer(){
		super(NAME);
		setTagSetFile(new File(TaggerActivator.getDefault().getStateLocation().toFile(),WORKSPACE_TAGSETFILENAME)); 
	}
	
	public int hashCode(){
		return(new HashCodeBuilder(7,13).toHashCode());
	}
	
	public boolean equals(Object obj){
		return(obj instanceof WorkspaceTagSetContainer);
	}
}
