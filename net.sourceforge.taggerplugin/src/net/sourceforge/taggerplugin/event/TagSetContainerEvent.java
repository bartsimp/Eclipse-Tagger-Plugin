package net.sourceforge.taggerplugin.event;

import java.util.EventObject;

import net.sourceforge.taggerplugin.model.ITagSetContainer;

public abstract class TagSetContainerEvent<E> extends EventObject {
	
	public enum Type {ADDED,REMOVED,UPDATED};

	private static final long serialVersionUID = -8979186221791925175L;
	private final Type type;
	private final E element;
	
	protected TagSetContainerEvent(ITagSetContainer container, Type type, E element){
		super(container);
		this.type = type;
		this.element = element;
	}

	public E getElements() {
		return element;
	}
	
	public E getElement(){
		return(element);
	}
	
	public Type getType() {
		return type;
	}
	
	public ITagSetContainer getContainer(){return((ITagSetContainer)getSource());}
}
