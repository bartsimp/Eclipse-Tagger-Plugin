package net.sourceforge.taggerplugin.event;

import java.util.EventObject;

import net.sourceforge.taggerplugin.model.TagSetContainerManager;

import org.eclipse.core.resources.IProject;

public class TagSetRegistrationEvent extends EventObject {

	public enum Type {REGISTERED, DEREGISTERED};

	private static final long serialVersionUID = 6440223502773442976L;
	private Type type;
	private IProject project;
	
	public TagSetRegistrationEvent(TagSetContainerManager manager, Type type, IProject project){
		super(manager);
		this.type = type;
		this.project = project;
	}
	
	public IProject getProject(){return(project);}
	
	public TagSetContainerManager getManager(){return((TagSetContainerManager)getSource());}
	
	public Type getType(){return(type);}
}
