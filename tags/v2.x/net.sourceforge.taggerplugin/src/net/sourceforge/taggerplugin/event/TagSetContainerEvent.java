/*   ********************************************************************** **
**   Copyright (c) 2006-2007 Christopher J. Stehno (chris@stehno.com)       **
**   http://www.stehno.com                                                  **
**                                                                          **
**   All rights reserved                                                    **
**                                                                          **
**   This program and the accompanying materials are made available under   **
**   the terms of the Eclipse Public License v1.0 which accompanies this    **
**   distribution, and is available at:                                     **
**   http://www.stehno.com/legal/epl-1_0.html                               **
**                                                                          **
**   A copy is found in the file license.txt.                               **
**                                                                          **
**   This copyright notice MUST APPEAR in all copies of the file!           **
**  **********************************************************************  */
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
