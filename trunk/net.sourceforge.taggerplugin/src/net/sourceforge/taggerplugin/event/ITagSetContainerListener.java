package net.sourceforge.taggerplugin.event;

import java.util.EventListener;


public interface ITagSetContainerListener extends EventListener {

	public void handleEvent(TagSetContainerEvent trme);
}
