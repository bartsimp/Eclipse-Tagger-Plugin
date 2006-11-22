package net.sourceforge.taggerplugin.event;

import java.util.EventObject;

import net.sourceforge.taggerplugin.manager.TagAssociationManager;

import org.eclipse.core.resources.IResource;

public class TagAssociationEvent extends EventObject {

	public static enum Type {ADDED,REMOVED};
	
	private static final long serialVersionUID = -3523425086390468129L;
	private final Type type;
	private final IResource resource;
	
	public TagAssociationEvent(TagAssociationManager manager, Type type, IResource resource){
		super(manager);
		this.type = type;
		this.resource = resource;
	}
	
	public TagAssociationManager getManager(){return((TagAssociationManager)getSource());}
	
	public Type getType(){return(type);}
	
	public IResource getResource() {
		return this.resource;
	}
}
