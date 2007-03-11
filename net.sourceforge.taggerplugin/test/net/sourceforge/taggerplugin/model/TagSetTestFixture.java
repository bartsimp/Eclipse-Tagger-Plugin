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

class TagSetTestFixture {

	static final String TAGSETA_ID = "TagSet-A";
	static final String TAGSETB_ID = "TagSet-B";
	TagSet tagSetA, tagSetB;

	TagSetTestFixture(){
		super();
	}

	void init(TagTestFixture ttf, TagAssociationTestFixture tatf){
		tagSetA = new TagSet(TagSetTestFixture.TAGSETA_ID);
		tagSetA.addTag(ttf.tagA);
		tagSetA.addTag(ttf.tagB);
		tagSetA.addAssociation(tatf.assocA);
		tagSetA.addAssociation(tatf.assocB);

		tagSetB = new TagSet(TagSetTestFixture.TAGSETB_ID);
		tagSetB.addTag(ttf.tagA);
		tagSetB.addTag(ttf.tagB);
		tagSetB.addAssociation(tatf.assocA);
	}

	void destroy(){
		tagSetA = null;
		tagSetB = null;
	}
}
