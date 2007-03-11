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
