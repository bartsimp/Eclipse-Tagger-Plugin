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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class TagAssociation {

	private TagSet parent;
	private String resourceId;
	private Set<Tag> tags;

	TagAssociation(){
		super();
		this.tags = new HashSet<Tag>();
	}

	TagAssociation(String resourceId){
		this();
		this.resourceId = resourceId;
	}

	TagSet getParent(){return(parent);}

	void setParent(TagSet parent){this.parent = parent;}

	void removeTag(Tag tag){
		tags.remove(tag);
	}

	boolean hasTags(){
		return(!tags.isEmpty());
	}

	public String getResourceId() {
		return resourceId;
	}

	Tag[] getTags(){return(tags.toArray(new Tag[tags.size()]));}

	public Iterable<Tag> tags(){return(tags);}

	@Override
	public int hashCode() {
		return(new HashCodeBuilder(7,13).append(resourceId).append(tags).toHashCode());
	}

	@Override
	public boolean equals(Object obj) {
		boolean eq = false;
		if(obj instanceof TagAssociation){
			final TagAssociation ta = (TagAssociation)obj;
			eq = new EqualsBuilder().append(ta.resourceId,resourceId).isEquals();
		}
		return(eq);
	}

	@Override
	public String toString() {
		return(new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE).append(resourceId).toString());
	}

	void addTag(Tag tag){
		tags.add(tag);
	}

	void addTags(Tag[] tags){
		this.tags.addAll(Arrays.asList(tags));
	}

	void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	boolean hasTag(Tag tag){
		return(tags.contains(tag));
	}
}
