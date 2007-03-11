package net.sourceforge.taggerplugin.event;

import java.util.EventListener;


public interface ITagSetRegistrationListener extends EventListener {

	public void handleEvent(TagSetRegistrationEvent tsre);
}
