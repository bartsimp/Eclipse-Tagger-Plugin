package net.sourceforge.taggerplugin.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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
import net.sourceforge.taggerplugin.util.IoUtils;

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
	private Map<String, Set<UUID>> associations;
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
	public Set<UUID> findSharedAssociations(String[] resourceIds){
		ensureAssociations();
		
		final Set<UUID> masterSet = new HashSet<UUID>();
		for (String rcid : resourceIds){
			Set<UUID> vals = associations.get(rcid);
			if(vals != null && !vals.isEmpty()){
				masterSet.addAll(vals);	
			}
		}
		
		final Set<UUID> shared = new HashSet<UUID>();
		for(UUID uuid : masterSet){
			boolean allhave = false;
			for(String rid : resourceIds){
				final Set<UUID> assocs = associations.get(rid);
				if(assocs != null && !assocs.isEmpty()){
					allhave = assocs.contains(uuid);
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
		return(shared);
	}
	
	public boolean hasAssociations(String resourceId){
		ensureAssociations();
		
		final Set<UUID> set = associations.get(resourceId);
		return(set != null && !set.isEmpty());
	}
	
	public void clearAssociations(String resourceId){
		ensureAssociations();
		
		associations.remove(resourceId);
	}
	
	public void clearAssociation(String resourceId, UUID tagid){
		ensureAssociations();
		
		final Set<UUID> tags = associations.get(resourceId);
		if(tags != null && !tags.isEmpty()){
			tags.remove(tagid);
		}
	}
	
	public boolean hasAssociation(String resourceId, UUID tagid){
		ensureAssociations();
		
		final Set<UUID> tags = associations.get(resourceId);
		return(tags != null && !tags.isEmpty() && tags.contains(tagid));
	}
	
	public void addAssociation(String resourceId, UUID tagid){
		ensureAssociations();
		
		Set<UUID> tags = associations.get(resourceId);
		if(tags == null){
			tags = new HashSet<UUID>();
			associations.put(resourceId, tags);
		}
		tags.add(tagid);
	}
	
	public UUID[] getAssociations(String resourceId){
		ensureAssociations();
		
		return(associations.get(resourceId).toArray(new UUID[0]));
	}
	
	private void ensureAssociations(){
		if(associations == null){
			associations = new HashMap<String, Set<UUID>>();
			loadAssociations();
		}
	}
	
	private void loadAssociations(){
		final File file = getTagAssociationFile();
		if(file.exists()){
			Reader reader = null;
			try {
				reader = new BufferedReader(new FileReader(file));

				final IMemento[] children = XMLMemento.createReadRoot(reader).getChildren(TAG_ASSOCIATION);
				for (IMemento mem : children) {
					final String resourceId = mem.getID();
					
					Set<UUID> list = associations.get(resourceId);
					if(list == null){
						list = new HashSet<UUID>();
						associations.put(resourceId, list);
					}
					
					final IMemento[] resourceChildren = mem.getChildren(TAG_TAG); 
					for(IMemento rchild : resourceChildren){
						list.add(UUID.fromString(rchild.getString(TAG_REFID)));
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
		for (Entry<String, Set<UUID>> entry : associations.entrySet()) {
			final IMemento mem = memento.createChild(TAG_ASSOCIATION,String.valueOf(entry.getKey()));
			
			for (UUID tagid : entry.getValue()) {
				final IMemento tagMem = mem.createChild(TAG_TAG);
				tagMem.putString(TAG_REFID, tagid.toString());
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
