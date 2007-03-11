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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

class TagSet {

	private String id;
	private Map<String,Tag> tags;
	private Map<String,TagAssociation> associations;

	TagSet(){
		super();
		this.tags = new HashMap<String,Tag>();
		this.associations = new HashMap<String,TagAssociation>();
	}

	TagSet(String id){
		this();
		this.id = id;
	}

	Tag[] getTags(String[] tagIds){
		final List<Tag> list = new LinkedList<Tag>();
		for(String tagid : tagIds){
			final Tag tag = tags.get(tagid);
			if(tag != null){
				list.add(tag);
			}
		}
		return(list.toArray(new Tag[list.size()]));
	}

	public String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	void addTag(Tag tag){
		tag.setParent(this);
		tags.put(tag.getId(),tag);
	}

	boolean containsTag(String tagId){
		return(tags.containsKey(tagId));
	}

	boolean hasTags(){
		return(!tags.isEmpty());
	}

	/**
	 * Used to remove the tag from the tagset. The tag will also be removed from all tag
	 * assocaitions that it has.
	 *
	 * @param tag
	 */
	void removeTag(Tag tag){
		for(TagAssociation ta : getAssociations(tag)){
			ta.removeTag(tag);
			if(!ta.hasTags()){
				// remove the assocation when you remove the last tag
				associations.remove(ta.getResourceId());
			}
		}
		tags.remove(tag.getId());
		tag.setParent(null);
	}

	Tag getTag(String tagid){
		return(tags.get(tagid));
	}

	TagAssociation removeAssociations(String resourceId){
		return(associations.remove(resourceId));
	}

	TagAssociation removeAssociation(String resourceId, Tag tag){
		final TagAssociation ta = getAssociation(resourceId);
		if(ta != null){
			ta.removeTag(tag);
			if(!ta.hasTags()){
				associations.remove(resourceId);
			}
		}
		return(ta);
	}

	void addAssociation(TagAssociation association){
		association.setParent(this);
		associations.put(association.getResourceId(),association);
	}

	boolean hasAssociations(String resourceId){
		return(associations.containsKey(resourceId));
	}

	boolean hasAssociation(Tag tag){
		boolean has = false;
		for(TagAssociation assoc : associations.values()){
			if(assoc.hasTag(tag)){
				has = true;
				break;
			}
		}
		return(has);
	}

	public Tag[] getTags(){return(tags.values().toArray(new Tag[tags.size()]));}

	public TagAssociation[] getAssociations(Tag tag){
		List<TagAssociation> assocs = new LinkedList<TagAssociation>();
		for(TagAssociation assoc : associations.values()){
			if(assoc.hasTag(tag)){
				assocs.add(assoc);
			}
		}
		return(assocs.toArray(new TagAssociation[assocs.size()]));
	}

	TagAssociation getAssociation(String resourceId){
		TagAssociation assoc = null;
		for(TagAssociation ta : associations.values()){
			if(ta.getResourceId().equals(resourceId)){
				assoc = ta;
				break;
			}
		}
		return(assoc);
	}

	public Iterable<TagAssociation> associations(){
		return(associations.values());
	}

	@Override
	public int hashCode() {
		return(new HashCodeBuilder(7,13).append(id).append(tags).append(associations).toHashCode());
	}

	@Override
	public boolean equals(Object obj) {
		boolean eq = false;
		if(obj instanceof TagSet){
			final TagSet ts = (TagSet)obj;
			eq = new EqualsBuilder().append(ts.id,id).append(ts.tags,tags).append(ts.associations,associations).isEquals();
		}
		return(eq);
	}

	@Override
	public String toString() {
		return(new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE).append("id",id,true).append("tags",tags,true).append("associations",associations,true).toString());
	}
}
