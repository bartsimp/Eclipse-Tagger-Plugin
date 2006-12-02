package net.sourceforge.taggerplugin.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

public class TagAssociation implements Iterable<UUID> {

	private UUID resourceId;
	private Set<UUID> tagIds;
	
	public TagAssociation(){
		super();
		this.tagIds = new HashSet<UUID>();
	}
	
	public TagAssociation(UUID resourceId){
		this();
		this.resourceId = resourceId;
	}
	
	public UUID getResourceId(){
		return this.resourceId;
	}

	public Iterator<UUID> iterator() {
		return tagIds.iterator();
	}
	
	public UUID[] getAssociations(){
		return(tagIds.toArray(new UUID[tagIds.size()]));
	}
	
	public void addTagId(UUID tagid){
		tagIds.add(tagid);
	}
	
	public boolean hasAssociations(){
		return(!tagIds.isEmpty());
	}
	
	public boolean isEmpty(){
		return(tagIds.isEmpty());
	}
	
	public boolean containsAssociation(UUID tagid){
		return(tagIds.contains(tagid));
	}
	
	public boolean removeAssociation(UUID tagid){
		return(tagIds.remove(tagid));
	}
	
	public boolean removeAssociations(UUID[] tagids){
		return(tagIds.removeAll(Arrays.asList(tagids)));
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.resourceId == null) ? 0 : this.resourceId.hashCode());
		result = PRIME * result + ((this.tagIds == null) ? 0 : this.tagIds.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final TagAssociation other = (TagAssociation) obj;
		if (this.resourceId == null) {
			if (other.resourceId != null)
				return false;
		} else if (!this.resourceId.equals(other.resourceId))
			return false;
		if (this.tagIds == null) {
			if (other.tagIds != null)
				return false;
		} else if (!this.tagIds.equals(other.tagIds))
			return false;
		return true;
	}
}
