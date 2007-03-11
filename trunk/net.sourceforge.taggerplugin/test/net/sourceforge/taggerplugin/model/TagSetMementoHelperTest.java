package net.sourceforge.taggerplugin.model;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class TagSetMementoHelperTest {

	private TagTestFixture tagFixture;
	private TagAssociationTestFixture assocFixture;
	private TagSetTestFixture setFixture;
	private XPath xpath;

	public TagSetMementoHelperTest(){
		tagFixture = new TagTestFixture();
		assocFixture = new TagAssociationTestFixture();
		setFixture = new TagSetTestFixture();
	}

	@Before
	public void init(){
		xpath = XPathFactory.newInstance().newXPath();

		tagFixture.init();
		assocFixture.init(tagFixture);
		setFixture.init(tagFixture,assocFixture);
	}

	@Test
	public void saveNull() throws IOException {
		final TagSet tagSet = null;
		final Writer writer = new StringWriter();

		TagSetMementoHelper.save(tagSet, writer);

		writer.close();
	}

	@Test
	public void save() throws Exception {
		final Document doc = parse(saveToString());

		Assert.assertEquals(TagSetTestFixture.TAGSETA_ID,xpath.evaluate("//tag-set/@id", doc));
		Assert.assertEquals("2.0",xpath.evaluate("//tag-set/@version", doc));

		Assert.assertEquals(TagTestFixture.TAGA_NAME,xpath.evaluate("//tag-set/tags/tag[@id='" + TagTestFixture.TAGA_ID + "']/@name",doc));
		Assert.assertEquals(TagTestFixture.TAGA_DESC,xpath.evaluate("//tag-set/tags/tag[@id='" + TagTestFixture.TAGA_ID + "']/text()",doc));

		Assert.assertEquals(TagTestFixture.TAGA_ID,xpath.evaluate("//tag-set/associations/association[@resource-id='" + TagAssociationTestFixture.ASSOCA_ID + "']/tag-ref/@tag-id", doc));
	}

	@Test
	public void loadFromSaved() throws Exception {
		final StringReader reader = new StringReader(saveToString());
		final TagSet tagSet = TagSetMementoHelper.load(reader);
		reader.close();

		Assert.assertEquals(setFixture.tagSetA, tagSet);
	}

	@After
	public void destroy(){
		tagFixture.destroy();
		assocFixture.destroy();
		setFixture.destroy();
	}

	private String saveToString() throws Exception {
		final Writer writer = new StringWriter();
		TagSetMementoHelper.save(setFixture.tagSetA, writer);
		writer.close();
		return(writer.toString());
	}

	private Document parse(String xml) throws Exception {
		return(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xml))));
	}
}
