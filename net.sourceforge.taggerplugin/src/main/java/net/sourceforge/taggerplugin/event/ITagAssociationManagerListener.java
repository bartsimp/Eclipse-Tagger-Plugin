package net.sourceforge.taggerplugin.event;

import java.util.EventListener;

public interface ITagAssociationManagerListener extends EventListener {

	public void handleTagAssociationEvent(TagAssociationEvent tme);
}
