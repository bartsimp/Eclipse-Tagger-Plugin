package net.sourceforge.taggerplugin.manager;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.taggerplugin.event.ITagManagerListener;

/**
 * Manages the tag set available to the plugin.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagManager {

	private static TagManager instance;
	private List<ITagManagerListener> listeners;

	private TagManager(){
		super();

		this.listeners = new LinkedList<ITagManagerListener>();
	}

	/**
	 * Used to retrieve the shared instance of the TagManager.
	 *
	 * @return the shared instance of the TagManager
	 */
	public static TagManager getInstance(){
		if(instance == null){instance = new TagManager();}
		return(instance);
	}

	/**
	 * Used to add a TagManagerListener to the listener list. If the listener
	 * is already in the list, it will not be added again.
	 *
	 * @param listener the listener to be added
	 */
	public void addTagManagerListener(ITagManagerListener listener){
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}

	/**
	 * Used to remove the specified listener from the listener list. If the
	 * listener is not in the list, no action will be taken.
	 *
	 * @param listener the listener to be removed.
	 */
	public void removeTagManagerListener(ITagManagerListener listener){
		listeners.remove(listener);
	}
}
