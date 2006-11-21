package net.sourceforge.taggerplugin.event;

import java.util.EventObject;

import net.sourceforge.taggerplugin.manager.TagAssociationManager;

public class TagAssociationEvent extends EventObject {

	private static final long serialVersionUID = -3523425086390468129L;

	public TagAssociationEvent(TagAssociationManager manager){
		super(manager);
	}
	
	public TagAssociationManager getManager(){return((TagAssociationManager)getSource());}
}
