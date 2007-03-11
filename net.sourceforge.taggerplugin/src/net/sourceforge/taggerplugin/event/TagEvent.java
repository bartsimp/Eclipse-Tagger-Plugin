package net.sourceforge.taggerplugin.event;

import net.sourceforge.taggerplugin.model.ITagSetContainer;
import net.sourceforge.taggerplugin.model.Tag;

public class TagEvent extends TagSetContainerEvent<Tag> {

	private static final long serialVersionUID = 3178092089126983813L;

	public TagEvent(ITagSetContainer container, Type type, Tag tag){
		super(container,type,tag);
	}
}
