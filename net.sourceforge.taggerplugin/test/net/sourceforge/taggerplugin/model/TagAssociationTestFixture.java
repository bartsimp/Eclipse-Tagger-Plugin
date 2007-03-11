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

class TagAssociationTestFixture {

	static final String ASSOCA_ID = "/projecta";
	static final String ASSOCB_ID = "/projectb";

	TagAssociation assocA, assocB;

	TagAssociationTestFixture(){
		super();
	}

	void init(TagTestFixture ttf){
		assocA = new TagAssociation(ASSOCA_ID);
		assocA.addTag(ttf.tagA);

		assocB = new TagAssociation(ASSOCB_ID);
		assocB.addTag(ttf.tagA);
		assocB.addTag(ttf.tagB);
	}

	void destroy(){
		assocA = null;
		assocB = null;
	}
}
