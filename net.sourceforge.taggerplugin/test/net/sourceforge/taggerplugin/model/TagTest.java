package net.sourceforge.taggerplugin.model;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TagTest {

	private TagTestFixture fixture;

	public TagTest(){
		fixture = new TagTestFixture();
	}

	@Before
	public void init(){
		fixture.init();
	}

	@Test
	public void properties(){
		Assert.assertEquals(TagTestFixture.TAGA_NAME,fixture.tagA.getName());
		Assert.assertEquals(TagTestFixture.TAGA_ID,fixture.tagA.getId());
		Assert.assertEquals(TagTestFixture.TAGA_DESC,fixture.tagA.getDescription());
	}

	@Test
	public void equals(){
		Assert.assertSame(fixture.tagA,fixture.tagA);
		Assert.assertNotSame(fixture.tagA,fixture.tagB);
		Assert.assertFalse(fixture.tagA.equals(fixture.tagB));
		Assert.assertTrue(fixture.tagA.equals(fixture.tagA));
	}

	@Test
	public void collection(){
		Set<Tag> set = new HashSet<Tag>();
		set.add(fixture.tagA);
		set.add(fixture.tagB);
		set.add(fixture.tagA);

		Assert.assertEquals(2, set.size());
		Assert.assertTrue(set.contains(fixture.tagA));
		Assert.assertTrue(set.contains(fixture.tagB));
	}

	@After
	public void destroy(){
		fixture.destroy();
	}
}
