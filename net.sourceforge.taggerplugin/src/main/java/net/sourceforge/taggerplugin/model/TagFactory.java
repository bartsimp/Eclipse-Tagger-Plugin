package net.sourceforge.taggerplugin.model;

import java.util.UUID;

/**
 * Factory for creating Tag instances.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagFactory {

	private TagFactory(){}

	public static final Tag create(){
		return(new Tag(UUID.randomUUID()));
	}

	public static final Tag create(String name, String description){
		return(new Tag(UUID.randomUUID(),name,description));
	}

	public static final Tag create(String id, String name, String description){
		return(new Tag(UUID.fromString(id),name,description));
	}
}
