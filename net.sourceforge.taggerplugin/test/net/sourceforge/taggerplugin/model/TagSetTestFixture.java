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
