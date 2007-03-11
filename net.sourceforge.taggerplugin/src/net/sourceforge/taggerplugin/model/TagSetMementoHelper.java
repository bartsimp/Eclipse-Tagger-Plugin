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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.XMLMemento;

class TagSetMementoHelper {

	private static final String VERSION = "2.0";
	private static final String ATTR_TAGID = "tag-id";
	private static final String TAG_TAGREF = "tag-ref";
	private static final String TAG_TAG = "tag";
	private static final String TAG_TAGS = "tags";
	private static final String TAG_ASSOCIATIONS = "associations";
	private static final String TAG_ASSOCIATION = "association";
	private static final String TAG_TAGSET = "tag-set";
	private static final String ATTR_ID = "id";
	private static final String ATTR_RESOURCEID = "resource-id";
	private static final String ATTR_NAME = "name";
	private static final String ATTR_VERSION = "version";
	
	static void save(final TagSet tagSet, final File file) throws IOException {
		file.createNewFile();
		
		Writer writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			save(tagSet,writer);
		} catch(IOException ioe){
			throw ioe;
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	/**
	 * Used to write the tagset data into the writer in memento format.
	 * The writer is NOT closed within this method.
	 *
	 * @param tagSet the tag set to be saved
	 * @param writer the writer that the tag set data is to be written into
	 * @throws IOException if there is a problem writing the data.
	 */
	static void save(final TagSet tagSet, final Writer writer) throws IOException {
		if(tagSet == null || writer == null) return;

		final XMLMemento memento = XMLMemento.createWriteRoot(TAG_TAGSET);
		memento.putString(ATTR_VERSION,VERSION);
		memento.putString(ATTR_ID,tagSet.getId());

		// tags
		final IMemento tags = memento.createChild(TAG_TAGS);
		for(Tag tag : tagSet.getTags()){
			final IMemento mem = tags.createChild(TAG_TAG);
			mem.putString(ATTR_ID, tag.getId());
			mem.putString(ATTR_NAME,tag.getName());
			mem.putTextData(tag.getDescription());
		}

		// tags
		final IMemento associations = memento.createChild(TAG_ASSOCIATIONS);
		for(TagAssociation assoc : tagSet.associations()){
			final IMemento association = associations.createChild(TAG_ASSOCIATION);
			association.putString(ATTR_RESOURCEID,assoc.getResourceId());

			for(Tag tag : assoc.tags()){
				final IMemento tagref = association.createChild(TAG_TAGREF);
				tagref.putString(ATTR_TAGID, tag.getId());
			}
		}

		memento.save(writer);
	}
	
	static TagSet load(final File file) throws IOException {
		if(!file.exists()){
			return(null);
		}
		
		TagSet tagSet = null;
		Reader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			tagSet = load(reader);
		} catch(IOException ioe){
			throw ioe;
		} finally {
			IOUtils.closeQuietly(reader);
		}
		return(tagSet);
	}

	/**
	 * Used to read the tagset data from the specified reader to create a TagSet.
	 * The reader is NOT closed within this method.
	 *
	 * @param reader the reader containing the tagset data
	 * @return the populated TagSet object
	 * @throws IOException if there is a problem
	 */
	static TagSet load(final Reader reader) throws IOException {
		final TagSet tagSet = new TagSet();
		try {
			final IMemento memento = XMLMemento.createReadRoot(reader);
			assertVersion(memento.getString(ATTR_VERSION));
			tagSet.setId(memento.getString(ATTR_ID));	// FIXME: validate

			// NOTE: depending on how the tagassociation goes, I may be able to remove this
			Map<String, Tag> tags = new HashMap<String, Tag>();

			for(IMemento tag : memento.getChild(TAG_TAGS).getChildren(TAG_TAG)){
				final Tag t = new Tag();
				t.setId(tag.getString(ATTR_ID));
				t.setName(tag.getString(ATTR_NAME));
				t.setDescription(tag.getTextData());
				tagSet.addTag(t);

				tags.put(t.getId(), t);
			}

			for(IMemento assoc : memento.getChild(TAG_ASSOCIATIONS).getChildren(TAG_ASSOCIATION)){
				final TagAssociation a = new TagAssociation();
				a.setResourceId(assoc.getString(ATTR_RESOURCEID));

				for(IMemento ref : assoc.getChildren(TAG_TAGREF)){
					final String tagRef = ref.getString(ATTR_TAGID);
					final Tag t = tags.get(tagRef);
					if(t != null){
						a.addTag(t);
					}
				}

				tagSet.addAssociation(a);
			}

		} catch(WorkbenchException we){
			throw new IOException(we.getMessage());
		}
		return(tagSet);
	}

	/**
	 * Used to assert the the version of the incoming data is that required by the loader.
	 *
	 * @param version the version of the incoming data
	 */
	protected static void assertVersion(String version){
		// FIXME: will want to do data conversion from 1.x to 2.x here
		if(!StringUtils.equals(version, VERSION)){
			throw new IllegalArgumentException("Incorrect input data version: requires: " + VERSION + ", found: " + version);	// FIXME: externalize
		}
	}
}