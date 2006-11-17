package net.sourceforge.taggerplugin.event;

import java.util.EventObject;

import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;

/**
 * Event fired by the TagManager when managed tags set changes.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagManagerEvent extends EventObject {

	public static enum Type {ADDED};

	private static final long serialVersionUID = 1554238208483233563L;
	private final Tag[] tags;
	private final Type type;

	public TagManagerEvent(final TagManager manager,final Type type,final Tag[] tags){
		super(manager);
		this.tags = tags;
		this.type = type;
	}

	public Type getType() {return(type);}

	public Tag[] getTags(){return(tags);}

	public TagManager getTagManager(){return((TagManager)getSource());}
}
