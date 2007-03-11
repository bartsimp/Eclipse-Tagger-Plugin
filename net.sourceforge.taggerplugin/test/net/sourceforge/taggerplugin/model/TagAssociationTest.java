package net.sourceforge.taggerplugin.model;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TagAssociationTest {

	private TagTestFixture tagFixture;
	private TagAssociationTestFixture assocFixture;

	public TagAssociationTest(){
		tagFixture = new TagTestFixture();
		assocFixture = new TagAssociationTestFixture();
	}

	@Before
	public void init(){
		tagFixture.init();
		assocFixture.init(tagFixture);
	}

	@Test
	public void properties(){
		Assert.assertEquals(TagAssociationTestFixture.ASSOCA_ID, assocFixture.assocA.getResourceId());
		Assert.assertTrue(assocFixture.assocA.hasTag(tagFixture.tagA));

		Assert.assertEquals(TagAssociationTestFixture.ASSOCB_ID, assocFixture.assocB.getResourceId());
		Assert.assertTrue(assocFixture.assocB.hasTag(tagFixture.tagA));
		Assert.assertTrue(assocFixture.assocB.hasTag(tagFixture.tagB));
	}

	@Test
	public void equals(){
		Assert.assertSame(assocFixture.assocA, assocFixture.assocA);
		Assert.assertEquals(assocFixture.assocA, assocFixture.assocA);
		Assert.assertFalse(assocFixture.assocA.equals(assocFixture.assocB));
	}

	@Test
	public void collections(){
		Set<TagAssociation> set = new HashSet<TagAssociation>();
		set.add(assocFixture.assocA);
		set.add(assocFixture.assocB);
		set.add(assocFixture.assocA);
		set.add(assocFixture.assocB);

		Assert.assertEquals(2, set.size());
		Assert.assertTrue(set.contains(assocFixture.assocA));
		Assert.assertTrue(set.contains(assocFixture.assocB));
	}

	@After
	public void destroy(){
		tagFixture.destroy();
		assocFixture.destroy();
	}
}
