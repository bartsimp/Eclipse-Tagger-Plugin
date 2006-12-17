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

import org.eclipse.core.resources.IResource;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.SearchResultEvent;

public class TagSearchResultEvent extends SearchResultEvent {

	public static enum Type {ADDED,REMOVED};

	private static final long serialVersionUID = -5424223750735617727L;
	private final IResource[] resources;
	private final Type type;

	public TagSearchResultEvent(ISearchResult searchResult, IResource[] resources, Type type){
		super(searchResult);
		this.resources = resources;
		this.type = type;
	}

	public Type getType() {
		return this.type;
	}

	public IResource[] getResources() {
		return this.resources;
	}
}