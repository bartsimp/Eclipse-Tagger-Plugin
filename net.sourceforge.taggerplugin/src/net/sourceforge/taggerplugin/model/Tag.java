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

import java.util.UUID;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 *	Model object representing a single tag.
 *
 * 	@author Christopher J. Stehno (chris@stehno.com)
 */
public class Tag {

	private TagSet parent;
	private String id,name,description;

	Tag(){
		super();
	}

	Tag(String id, String name, String description){
		this();
		this.id = id;
		this.name = name;
		this.description = description;
	}
	
	/**
	 * Creates a new tag with a randomly generated id and the specified name
	 * and description.
	 * 
	 * @param name
	 * @param description
	 */
	Tag(String name, String description){
		this(UUID.randomUUID().toString(),name,description);
	}

	public String getDescription() {
		return description;
	}

	void setDescription(String description) {
		this.description = description;
	}

	public String getId() {
		return id;
	}

	void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	void setName(String name) {
		this.name = name;
	}
	
	void setParent(TagSet parent){this.parent = parent;}
	
	TagSet getParent(){return(parent);}

	@Override
	public int hashCode() {
		return(new HashCodeBuilder(7,13).append(id).append(name).append(description).toHashCode());
	}

	@Override
	public boolean equals(Object obj) {
		boolean eq = false;
		if(obj instanceof Tag){
			final Tag tobj = (Tag)obj;
			eq = new EqualsBuilder().append(tobj.id,id).append(tobj.name,name).append(tobj.description,description).isEquals();
		}
		return(eq);
	}

	@Override
	public String toString() {
		return(new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE).append("id",id,true).append("name",name,true).append("description",description,true).toString());
	}
}
