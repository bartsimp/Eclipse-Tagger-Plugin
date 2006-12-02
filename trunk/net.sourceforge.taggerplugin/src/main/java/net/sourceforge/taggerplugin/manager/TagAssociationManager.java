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
package net.sourceforge.taggerplugin.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.event.ITagAssociationManagerListener;
import net.sourceforge.taggerplugin.event.TagAssociationEvent;
import net.sourceforge.taggerplugin.model.TagAssociation;
import net.sourceforge.taggerplugin.resource.ITaggedMarker;
import net.sourceforge.taggerplugin.resource.TaggedMarkerHelper;
import net.sourceforge.taggerplugin.util.IoUtils;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;

/**
 * Manager used to load and store the tag/resource associations in the workspace.
 * The tag/resource associations are stored as part of the plugin state.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagAssociationManager {

	private static final String TAG_ASSOCIATIONS = "associations";
	private static final String TAG_REFID = "ref-id";
	private static final String TAG_TAG = "tag";
	private static final String TAGASSOCIATIONFILENAME = "tag-associations.xml";
	private static final String TAG_ASSOCIATION = "assoc";
	private static TagAssociationManager instance;
	private Map<UUID, TagAssociation> associations;
	private final List<ITagAssociationManagerListener> listeners;

	private TagAssociationManager(){
		super();
		this.listeners = new LinkedList<ITagAssociationManagerListener>();
	}

	public static final TagAssociationManager getInstance(){
		if(instance == null){
			instance = new TagAssociationManager();
		}
		return(instance);
	}

	/**
	 * Used to find all associations (tag ids) that are shared by all of the resources with
	 * the given ids. An association will ONLY appear in the resulting set if it is shared by
	 * all resources in the id list.
	 *
	 * @param resourceIds
	 * @return
	 */
	public Set<UUID> findSharedAssociations(Object[] resources){
		ensureAssociations();

		final Set<UUID> shared = new HashSet<UUID>();
		try {
			final Set<UUID> masterSet = new HashSet<UUID>();
			for (Object resource : resources){
				TagAssociation vals = associations.get(TaggedMarkerHelper.getMarker((IResource)resource).getResourceId());
				if(vals != null && vals.hasAssociations()){
					masterSet.addAll(Arrays.asList(vals.getAssociations()));
				}
			}

			for(UUID uuid : masterSet){
				boolean allhave = false;
				for(Object resource : resources){
					final TagAssociation assocs = associations.get(TaggedMarkerHelper.getMarker((IResource)resource).getResourceId());
					if(assocs != null && assocs.hasAssociations()){
						allhave = assocs.containsAssociation(uuid);
						if(!allhave){
							break;
						}
					} else {
						allhave = false;
						break;
					}
				}

				if(allhave){
					shared.add(uuid);
				}
			}
		} catch(CoreException ce){
			// FIXME: send to user
			TaggerLog.error("Unable to find associations: " + ce.getMessage(),ce);
		}
		return(shared);
	}

	/**
	 * Used to clear all associations of the given resource.
	 *
	 * @param resource the resource
	 */
	public void clearAssociations(IResource resource){
		ensureAssociations();

		try {
			final ITaggedMarker marker = TaggedMarkerHelper.getMarker(resource);
			if(marker != null){
				final UUID resourceId = marker.getResourceId();
				final TagAssociation tags = associations.get(resourceId);
				if(tags != null && tags.hasAssociations()){
					associations.remove(resourceId);

					TaggedMarkerHelper.deleteMarker(resource);

					fireTagAssociationEvent(new TagAssociationEvent(this,TagAssociationEvent.Type.REMOVED,resource));
				}
			}
		} catch(CoreException ce){
			// FIXME: send to user
			TaggerLog.error("Unable to clear tag associations: " + ce.getMessage(), ce);
		}
	}

	/**
	 * Used to clear (remove) the association of the tag with the given id
	 * with the specified resource.
	 *
	 * @param resource the resource
	 * @param tagid the id of the tag association being cleared
	 */
	public void clearAssociation(IResource resource, UUID tagid){
		ensureAssociations();

		try {
			final ITaggedMarker marker = TaggedMarkerHelper.getMarker(resource);
			if(marker != null){
				final TagAssociation tags = associations.get(marker.getResourceId());
				if(tags != null && tags.hasAssociations()){
					tags.removeAssociation(tagid);

					if(tags.isEmpty()){
						// there are no more associations, clear the marker
						TaggedMarkerHelper.deleteMarker(resource);
					}

					fireTagAssociationEvent(new TagAssociationEvent(this,TagAssociationEvent.Type.REMOVED,resource));
				}
			}
		} catch(CoreException ce){
			// FIXME: pass to user
			TaggerLog.error("Unable to clear tag association: " + ce.getMessage(), ce);
		}
	}

	public boolean hasAssociation(IResource resource, UUID tagid){
		ensureAssociations();

		boolean hasAssoc = false;
		try {
			final ITaggedMarker marker = TaggedMarkerHelper.getMarker(resource);
			if(marker != null){
				final TagAssociation tags = associations.get(marker.getResourceId());
				hasAssoc = tags != null && tags.hasAssociations() && tags.containsAssociation(tagid);
			}
		} catch(CoreException ce){
			// FIXME: send to user
			TaggerLog.error("Unable to determine association: " + ce.getMessage(), ce);
		}
		return(hasAssoc);
	}

	public boolean hasAssociations(IResource resource){
		ensureAssociations();
		boolean hasAssoc = false;
		try {
			hasAssoc = TaggedMarkerHelper.getMarker(resource) != null;
		} catch(CoreException ce){
			// FIXME: send to user
			TaggerLog.error("Unable to determine association: " + ce.getMessage(), ce);
		}
		return(hasAssoc);
	}

	/**
	 * Used to associate the tag with the specified id to the given resource.
	 *
	 * @param resource the resource
	 * @param tagid the id of the tag being associated with the resource
	 */
	public void addAssociation(IResource resource, UUID tagid){
		ensureAssociations();
		try {
			final TagAssociation tags = getOrCreateAssociation(TaggedMarkerHelper.getOrCreateMarker(resource).getResourceId());
			tags.addTagId(tagid);

			fireTagAssociationEvent(new TagAssociationEvent(this,TagAssociationEvent.Type.ADDED,resource));

		} catch(CoreException ce){
			// FIXME: this should be passed to user
			TaggerLog.error("Unable to create tag association: " + ce.getMessage(),ce);
		}
	}

	public UUID[] getAssociations(IResource resource){
		ensureAssociations();
		try {
			final ITaggedMarker marker = TaggedMarkerHelper.getMarker(resource);
			if(marker != null){
				return(associations.get(marker.getResourceId()).getAssociations());
			}
		} catch(CoreException ce){
			// FIXME: send to user
			TaggerLog.error("Unable to retrive association: " + ce.getMessage(), ce);
		}
		return(new UUID[0]);
	}

	private void ensureAssociations(){
		if(associations == null){
			associations = new HashMap<UUID,TagAssociation>();
			loadAssociations();
		}
	}

	/**
	 * Used to retrieve the set of tag ids associated with the given resource id. If the set
	 * does not exist, one will be created, added to the associations map, and returned
	 * for use.
	 *
	 * @param resourceId
	 * @return
	 */
	private TagAssociation getOrCreateAssociation(UUID resourceId){
		TagAssociation tagAssoc = associations.get(resourceId);
		if(tagAssoc == null){
			tagAssoc = new TagAssociation(resourceId);
			associations.put(resourceId, tagAssoc);
		}
		return(tagAssoc);
	}

	private void loadAssociations(){
		final File file = getTagAssociationFile();
		if(file.exists()){
			Reader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));

				final IMemento[] children = XMLMemento.createReadRoot(reader).getChildren(TAG_ASSOCIATION);
				for (IMemento mem : children) {
					final UUID resourceId = UUID.fromString(mem.getID());

					TagAssociation tagAssoc = associations.get(resourceId);
					if(tagAssoc == null){
						tagAssoc = new TagAssociation(resourceId);
						associations.put(resourceId, tagAssoc);
					}

					final IMemento[] resourceChildren = mem.getChildren(TAG_TAG);
					for(IMemento rchild : resourceChildren){
						tagAssoc.addTagId(UUID.fromString(rchild.getString(TAG_REFID)));
					}
				}

			} catch(Exception ex){
				TaggerLog.error(ex);
			} finally {
				IoUtils.closeQuietly(reader);
			}
		}
	}

	public void saveAssociations(){
		if(associations == null){return;}

		final XMLMemento memento = XMLMemento.createWriteRoot(TAG_ASSOCIATIONS);
		for (Entry<UUID,TagAssociation> entry : associations.entrySet()) {
			final IMemento mem = memento.createChild(TAG_ASSOCIATION,String.valueOf(entry.getKey()));

			for (UUID tagId : entry.getValue()){
				final IMemento tagMem = mem.createChild(TAG_TAG);
				tagMem.putString(TAG_REFID, tagId.toString());
			}
		}

		Writer writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(getTagAssociationFile()));
			memento.save(writer);
		} catch (IOException e) {
			TaggerLog.error(e);
		} finally {
			IoUtils.closeQuietly(writer);
		}
	}

	/**
	 * Used to retrieve the file in the plugin state directory used to store the tag association
	 * information.
	 *
	 * @return the tag association persistance file
	 */
	private File getTagAssociationFile() {
		return(TaggerActivator.getDefault().getStateLocation().append(TAGASSOCIATIONFILENAME).toFile());
	}

	public void addTagAssociationListener(ITagAssociationManagerListener listener){
		if(!listeners.contains(listener)){
			listeners.add(listener);
		}
	}

	public void removeTagAssociationListener(ITagAssociationManagerListener listener){
		listeners.remove(listener);
	}

	private void fireTagAssociationEvent(TagAssociationEvent event){
		for(ITagAssociationManagerListener listener : listeners){
			listener.handleTagAssociationEvent(event);
		}
	}
}
