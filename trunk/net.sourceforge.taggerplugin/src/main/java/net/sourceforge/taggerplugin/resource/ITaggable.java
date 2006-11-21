package net.sourceforge.taggerplugin.resource;

import java.util.UUID;

/**
 *	Interface used for adapting resources into taggable resources.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public interface ITaggable {

	/**
	 * Used to associate the tag with the specified id on the taggable object.
	 *
	 * @param id the id of the tag
	 */
	public void setTag(UUID id);

	/**
	 * Used to remove the association with the tag with the specified id.
	 *
	 * @param id the tag id
	 */
	public void clearTag(UUID id);

	/**
	 * Used to remove all tag associations from the tagged object.
	 */
	public void clearTags();

	/**
	 * Used to retrieve an array of the ids for each tag associated with the taggable object.
	 *
	 * @return an array of all tag ids associated with the taggable object
	 */
	public UUID[] listTags();

	/**
	 * Used to determine whether the tag with a specified id is associated with the taggable
	 * object.
	 *
	 * @param id the tag id
	 * @return a value of true if the tag with the specified id is associated with the taggable object.
	 */
	public boolean hasTag(UUID id);
	
	public String getResourceId();
	
	public boolean hasTags();
}
