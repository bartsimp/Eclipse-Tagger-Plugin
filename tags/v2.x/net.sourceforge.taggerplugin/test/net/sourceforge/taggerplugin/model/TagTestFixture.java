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

class TagTestFixture {

	static final String TAGA_ID = UUID.randomUUID().toString();
	static final String TAGA_NAME = "Tag-A";
	static final String TAGA_DESC = "This is tag a";

	Tag tagA, tagB;

	TagTestFixture(){
		super();
	}

	void init(){
		tagA = new Tag(TAGA_ID,TAGA_NAME,TAGA_DESC);
		tagB = new Tag(UUID.randomUUID().toString(),"Tag-B","This is tag b");
	}

	void destroy(){
		tagA = null;
		tagB = null;
	}
}
