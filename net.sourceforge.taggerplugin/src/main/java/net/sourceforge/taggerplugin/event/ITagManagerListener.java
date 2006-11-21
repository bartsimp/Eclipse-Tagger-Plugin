package net.sourceforge.taggerplugin.event;

import java.util.EventListener;

/**
 * Listener for objects that need to be notified about tag manager events.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public interface ITagManagerListener extends EventListener {

	public void handleTagManagerEvent(TagManagerEvent tme);
}
