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

import java.util.HashSet;
import java.util.Set;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TagSetTest {

	private TagTestFixture tagFixture;
	private TagAssociationTestFixture assocFixture;
	private TagSetTestFixture setFixture;

	public TagSetTest(){
		tagFixture = new TagTestFixture();
		assocFixture = new TagAssociationTestFixture();
		setFixture = new TagSetTestFixture();
	}

	@Before
	public void init(){
		tagFixture.init();
		assocFixture.init(tagFixture);
		setFixture.init(tagFixture,assocFixture);
	}

	@Test
	public void properties(){
		Assert.assertEquals(TagSetTestFixture.TAGSETA_ID,setFixture.tagSetA.getId());
	}

	@Test
	public void equals(){
		Assert.assertSame(setFixture.tagSetA, setFixture.tagSetA);
		Assert.assertEquals(setFixture.tagSetA, setFixture.tagSetA);
		Assert.assertFalse(setFixture.tagSetA.equals(setFixture.tagSetB));
	}

	@Test
	public void collections(){
		Set<TagSet> set = new HashSet<TagSet>();
		set.add(setFixture.tagSetA);
		set.add(setFixture.tagSetA);
		set.add(setFixture.tagSetB);
		set.add(setFixture.tagSetB);

		Assert.assertEquals(2, set.size());
		Assert.assertTrue(set.contains(setFixture.tagSetA));
		Assert.assertTrue(set.contains(setFixture.tagSetB));
	}

	@After
	public void destroy(){
		tagFixture.destroy();
		assocFixture.destroy();
		setFixture.destroy();
	}
}
