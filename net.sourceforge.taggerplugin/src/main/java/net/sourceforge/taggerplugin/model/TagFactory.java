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
package net.sourceforge.taggerplugin.model;

import java.util.UUID;

/**
 * Factory for creating Tag instances.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagFactory {

	private TagFactory(){}

	public static final Tag create(){
		return(new Tag(UUID.randomUUID()));
	}

	public static final Tag create(String name, String description){
		return(new Tag(UUID.randomUUID(),name,description));
	}

	public static final Tag create(String id, String name, String description){
		return(new Tag(UUID.fromString(id),name,description));
	}
}