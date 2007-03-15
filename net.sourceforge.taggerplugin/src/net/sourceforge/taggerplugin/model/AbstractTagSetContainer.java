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

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.dialog.CompareInput;
import net.sourceforge.taggerplugin.event.TagAssociationEvent;
import net.sourceforge.taggerplugin.event.TagEvent;
import net.sourceforge.taggerplugin.event.TagSetContainerEvent;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareUI;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;

abstract class AbstractTagSetContainer implements ITagSetContainer {

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
	private final TagSetContainerManager manager;
	private TagSet tagSet;
	private File tagSetFile;
	private long lastModified;

	protected AbstractTagSetContainer(String name){
		this.manager = TaggerActivator.getDefault().getTagSetContainerManager();
		this.tagSet = new TagSet(name);
	}

	public final Tag addTag(String name, String description) {
		final Tag newTag = new Tag(name,description);
		tagSet.addTag(newTag);

		save();
		manager.fireTagSetContainerEvent(new TagEvent(this,TagSetContainerEvent.Type.ADDED,newTag));

		return(newTag);
	}

	public void importTag(String id, String name, String description) {
		TagSetContainerEvent.Type type = TagSetContainerEvent.Type.ADDED;
		Tag tag = tagSet.getTag(id);
		if(tag != null){
			tag.setName(name);
			tag.setDescription(description);
			type = TagSetContainerEvent.Type.UPDATED;
		} else {
			tag = new Tag(id,name,description);
			tagSet.addTag(tag);
		}

		save();
		manager.fireTagSetContainerEvent(new TagEvent(this,type,tag));
	}

	public final TagAssociation[] getAssociations(Tag tag) {
		return(tagSet.getAssociations(tag));
	}

	public final String getName() {
		return(tagSet.getId());
	}

	public final Tag[] getTags() {
		return(tagSet.getTags());
	}

	public final boolean hasAssociations(Tag tag) {
		return(tagSet.hasAssociation(tag));
	}

	public final boolean hasTags() {
		return(tagSet.hasTags());
	}

	public final void removeTag(Tag tag) {
		// first remove the associations
		final TagAssociation[] assocs = tagSet.getAssociations(tag);
		for(int i=0; i<assocs.length; i++){
			removeAssociation(extractFile(assocs[i].getResourceId()), tag);
		}

		// remove the tag
		tagSet.removeTag(tag);
		manager.fireTagSetContainerEvent(new TagEvent(this,TagSetContainerEvent.Type.REMOVED,tag));
		save();
	}

	public final void updateTag(String tagid, String name, String description) {
		final Tag tag = tagSet.getTag(tagid);
		tag.setName(name);
		tag.setDescription(description);

		save();
		manager.fireTagSetContainerEvent(new TagEvent(this,TagSetContainerEvent.Type.UPDATED,tag));
	}

	public final void addAssociation(IResource resource, Tag tag) {
		final String resourceId = extractResourceId(resource);
		TagAssociation ta = tagSet.getAssociation(resourceId);
		if(ta == null){
			ta = new TagAssociation(resourceId);
			tagSet.addAssociation(ta);
		}
		ta.addTag(tag);

		save();
		manager.fireTagSetContainerEvent(new TagAssociationEvent(this,resource,TagSetContainerEvent.Type.ADDED,ta,tag));
	}

	public void importAssociations(String resourceId, String[] tagids) {
		final IFile file = extractFile(resourceId);
		if(file.exists()){
			TagAssociation assoc = tagSet.getAssociation(resourceId);
			if(assoc == null){
				assoc = new TagAssociation();
				tagSet.addAssociation(assoc);
			}
			final Tag[] tags = tagSet.getTags(tagids);
			assoc.addTags(tags);

			save();

			for(Tag tag : tags){
				manager.fireTagSetContainerEvent(new TagAssociationEvent(this,file,TagSetContainerEvent.Type.ADDED,assoc,tag));
			}
		}
	}

	public final void clearAssociations(IResource resource) {
		final TagAssociation ta = tagSet.removeAssociations(extractResourceId(resource));
		if(ta != null){
			save();
			manager.fireTagSetContainerEvent(new TagAssociationEvent(this,resource,TagSetContainerEvent.Type.REMOVED,ta,null));
		}
	}

	public final void removeAssociation(IResource resource, Tag tag){
		final TagAssociation ta = tagSet.removeAssociation(extractResourceId(resource),tag);
		save();
		manager.fireTagSetContainerEvent(new TagAssociationEvent(this,resource,TagSetContainerEvent.Type.REMOVED,ta,tag));
	}

	public final void load() throws IOException {
		if(!tagSetFile.exists()) return;

		Reader reader = null;
		try {
			reader = new BufferedReader(new FileReader(tagSetFile));
			final IMemento memento = XMLMemento.createReadRoot(reader);
			assertVersion(memento.getString(ATTR_VERSION));
			read(memento);
			
			this.lastModified = tagSetFile.lastModified();
			
		} catch(Exception ioe){
			throw new IOException(ioe.getMessage());
		} finally {
			IOUtils.closeQuietly(reader);
		}
	}

	public final void save() {
		if(tagSet == null) return;
		
		if(tagSetFile.lastModified() != lastModified){
			// file has been updated externally since last save
			System.out.println("TagSet file has been modified externally");
			
			/*
			 * two options:
			 * 		overwrite with my data
			 * 		merge file data with my data and then save
			 */
			
			// FIXME: externalize
			boolean overwrite = MessageDialog.openQuestion(
				TaggerActivator.getDefault().getWorkbench().getActiveWorkbenchWindow().getShell(),
				"Overwrite Modified TagSet File?", 
				"The tagset file has been modified externally, do you want to overwrite it with your data (Yes)" +
				" or load it and merge your data with it (No)?"
			);
			
			if(!overwrite){
//				CompareUI.openCompareDialog(new EditorInput(new CompareConfiguration()));
				final CompareConfiguration config = new CompareConfiguration();
				config.setRightEditable(true);
				config.setLeftEditable(true);
				CompareUI.openCompareDialog(new CompareInput(config));
				return;
			}
		}
		
		try {
			tagSetFile.createNewFile();

			final XMLMemento memento = XMLMemento.createWriteRoot(TAG_TAGSET);
			memento.putString(ATTR_VERSION,VERSION);
			write(memento);

			Writer writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(tagSetFile));
				memento.save(writer);
			} catch(IOException ioe){
				throw ioe;
			} finally {
				IOUtils.closeQuietly(writer);
			}
			
			this.lastModified = tagSetFile.lastModified();
			
		} catch(IOException ioe){
			throw new RuntimeException("Unable to save tagset: " + ioe.getMessage(),ioe);	// FIXME: externalize
		}
	}

	public void write(IMemento memento) {
		memento.putString(ATTR_ID,tagSet.getId());

		// tags
		final IMemento tags = memento.createChild(TAG_TAGS);
		for(Tag tag : tagSet.getTags()){
			final IMemento mem = tags.createChild(TAG_TAG);
			mem.putString(ATTR_ID, tag.getId());
			mem.putString(ATTR_NAME,tag.getName());
			mem.putTextData(tag.getDescription());
		}

		// associations
		final IMemento associations = memento.createChild(TAG_ASSOCIATIONS);
		for(TagAssociation assoc : tagSet.associations()){
			final IMemento association = associations.createChild(TAG_ASSOCIATION);
			association.putString(ATTR_RESOURCEID,assoc.getResourceId());

			for(Tag tag : assoc.tags()){
				final IMemento tagref = association.createChild(TAG_TAGREF);
				tagref.putString(ATTR_TAGID, tag.getId());
			}
		}
	}

	public void read(IMemento memento) {
		tagSet = new TagSet(getName());

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
	}

	protected final void setTagSetFile(File tagSetFile){
		this.tagSetFile = tagSetFile;
	}

	protected final String extractResourceId(IResource rc){
		return(rc.getFullPath().toPortableString());
	}

	protected final IFile extractFile(String resourceId){
		return(ResourcesPlugin.getWorkspace().getRoot().getFile(Path.fromPortableString(resourceId)));
	}

	public final boolean hasAssociations(IResource resource) {
		return(tagSet.hasAssociations(extractResourceId(resource)));
	}

	public final TagAssociation getAssociation(IResource resource) {
		return(tagSet.getAssociation(extractResourceId(resource)));
	}

	protected final TagSetContainerManager getManager(){return(manager);}

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
