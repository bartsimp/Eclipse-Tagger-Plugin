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
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.event.ITagSetContainerListener;
import net.sourceforge.taggerplugin.event.ITagSetRegistrationListener;
import net.sourceforge.taggerplugin.event.TagSetContainerEvent;
import net.sourceforge.taggerplugin.event.TagSetRegistrationEvent;
import net.sourceforge.taggerplugin.nature.TaggableProjectNature;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;

public class TagSetManager implements ITagSetManager,IResourceChangeListener {

	public static final String CONTAINERNAME_ALL = "All Listed Containers";
	
	private static final String TAGATTR_WORKSPACE = "workspace";
	private static final String TAGATTR_FILE = "file";
	private static final String TAGATTR_NAME = "name";
	private static final String TAG_TAGSET = "tagset";
	private static final String TAG_TAGSETS = "tagsets";
	private final File tagsetsFile;
	private final Map<String, ITagSetContainer> tagSetContainers;
	private final Set<ITagSetRegistrationListener> tagSetRegistrationListeners;
	private final Set<ITagSetContainerListener> tagSetContainerListeners;

	public TagSetManager(){
		super();
		this.tagSetContainers = new HashMap<String, ITagSetContainer>();
		this.tagSetRegistrationListeners = new HashSet<ITagSetRegistrationListener>();
		this.tagSetContainerListeners = new HashSet<ITagSetContainerListener>();
		this.tagsetsFile = TaggerActivator.getDefault().getStateLocation().append("/.tagsets").toFile();
	}

	public static String[] extractContainerNames(final Collection<ITagSetContainer> tscs, boolean includeAll) {
		final List<String> names = new LinkedList<String>();
		if(includeAll) names.add(CONTAINERNAME_ALL);
		for(ITagSetContainer tsc : tscs){
			names.add(tsc.getName());
		}
		return names.toArray(new String[names.size()]);
	}

	public void init(){
		loadState();
		
		// load the tagsets
		for(ITagSetContainer tsc : tagSetContainers.values()){
			try {
				tsc.load();
			} catch(Exception ioe){
				final String msg = TaggerMessages.bind(TaggerMessages.TagRepositoryManager_LoadProj_Error,tsc.getName(),ioe.getMessage());
				TaggerLog.error(msg, ioe);
				throw new RuntimeException(msg,ioe);
			}
		}
		
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.POST_CHANGE);
	}
	
	public void destroy(){
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		
		saveState();
		
		// save all tagsets
		for(ITagSetContainer tsc : tagSetContainers.values()){
			try {
				tsc.save();
			} catch(Exception ioe){
				final String msg = TaggerMessages.bind(TaggerMessages.TagRepositoryManager_SaveWs_Error, ioe.getMessage());
				TaggerLog.error(msg, ioe);
				throw new RuntimeException(msg,ioe);
			}
		}
	}

	public void addTagSetContainerListener(ITagSetContainerListener trml){
		tagSetContainerListeners.add(trml);
	}

	public void removeTagSetContainerListener(ITagSetContainerListener trml){
		tagSetContainerListeners.remove(trml);
	}

	public void addTagSetRegistrationListener(ITagSetRegistrationListener trml){
		tagSetRegistrationListeners.add(trml);
	}

	public void removeTagSetRegistrationListener(ITagSetRegistrationListener trml){
		tagSetRegistrationListeners.remove(trml);
	}
	
	public Tag[] getAssociations(IResource resource){
		final List<Tag> tags = new LinkedList<Tag>();
		for(ITagSetContainer tsc : tagSetContainers.values()){
			final TagAssociation assoc = tsc.getAssociation(resource);
			if(assoc != null && assoc.hasTags()){
				tags.addAll(Arrays.asList(assoc.getTags()));	
			}
		}
		return(tags.toArray(new Tag[tags.size()]));
	}
	
	public boolean hasAssociations(IResource resource){
		boolean has = false;
		for(ITagSetContainer tsc : tagSetContainers.values()){
			has = tsc.hasAssociations(resource);
			if(has){
				break;
			}
		}
		return(has);
	}
	
	public void clearAssociations(IResource resource){
		for(ITagSetContainer tsc : tagSetContainers.values()){
			tsc.clearAssociations(resource);
		}
	}

	public void registerTagSet(IProject project){
		try {
			final ITagSetContainer tsc = new ProjectTagSetContainer(project);
			tsc.load();
			tagSetContainers.put(tsc.getName(), tsc);

			fireTagSetRegistrationEvent(new TagSetRegistrationEvent(this,TagSetRegistrationEvent.Type.REGISTERED,project));
		} catch(IOException ioe){
			throw new RuntimeException("Unable to register tag set: " + ioe.getMessage());	// FIXME: externalize
		}
	}

	public void deregisterTagSet(IProject project){
		final ITagSetContainer tsc = tagSetContainers.get(project.getName());
		if(tsc != null){
			if(tagSetContainers.remove(project.getName()) != null){
				fireTagSetRegistrationEvent(new TagSetRegistrationEvent(this,TagSetRegistrationEvent.Type.DEREGISTERED,project));
			}
		}
	}

	/**
	 * Used to retrieve all registered TagSetContainers, which includes all projects and the workspace tagset.
	 *
	 * @return all registered tagsetcontainers.
	 */
	public ITagSetContainer[] getTagSetContainers(){
		return(tagSetContainers.values().toArray(new ITagSetContainer[tagSetContainers.size()]));
	}

	public ITagSetContainer getTagSetContainer(String containerName){
		return(tagSetContainers.get(containerName));
	}

	/**
	 * Used to retrieve the tagsetcontainer for the specified tag.
	 *
	 * @param tag
	 * @return
	 */
	public ITagSetContainer findTagSetContainer(Tag tag){
		return(tag.getParent() != null ? tagSetContainers.get(tag.getParent().getId()) : null);
	}

	void fireTagSetContainerEvent(TagSetContainerEvent trme){
		for(ITagSetContainerListener listener : tagSetContainerListeners){
			listener.handleEvent(trme);
		}
	}

	private void fireTagSetRegistrationEvent(TagSetRegistrationEvent trme){
		for(ITagSetRegistrationListener listener : tagSetRegistrationListeners){
			listener.handleEvent(trme);
		}
	}

	public void resourceChanged(IResourceChangeEvent event) {
		try {
			//NOTE: http://help.eclipse.org/help32/index.jsp?topic=/org.eclipse.platform.doc.isv/guide/resAdv_events.htm
			switch (event.getType()) {
				case IResourceChangeEvent.POST_CHANGE:
					event.getDelta().accept(new DeltaVisitor(this));
					event.getDelta().accept(new DeltaPrinter());
					break;
			}
		} catch(CoreException ce){
			TaggerLog.error("Unable to handle resource change event: " + ce.getMessage(), ce);
		}
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(7,13).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return(obj instanceof TagSetManager);
	}
	
	private void loadState(){
		if(tagsetsFile.exists()){
			Reader reader = null;
			try {
				reader = new BufferedReader(new FileReader(tagsetsFile));
				
				final XMLMemento memento = XMLMemento.createReadRoot(reader);
				for(IMemento mem : memento.getChild(TAG_TAGSETS).getChildren(TAG_TAGSET)){
					final String name = mem.getString(TAGATTR_NAME);
					final boolean workspaceRel = BooleanUtils.toBoolean(mem.getString(TAGATTR_WORKSPACE));
					
					final String filepath = mem.getString(TAGATTR_FILE);
					final File file = workspaceRel ? new File(TaggerActivator.getDefault().getStateLocation().toFile(),filepath) : new File(filepath);
					
					tagSetContainers.put(name, new TagSetContainer(this,name,file,workspaceRel));
				}
				
			} catch(Exception ioe){
				// FIXME: handle
				ioe.printStackTrace();
			} finally {
				IOUtils.closeQuietly(reader);
			}	
		}
	}
	
	private void saveState(){
		// ensure that the tagset manager file exists
		if(!tagsetsFile.exists()){
			// TODO: would it be better to use eclipse filesys? 
			try {tagsetsFile.createNewFile();} 
			catch(IOException ioe){ioe.printStackTrace();} // FIXME: do better
		}
		
		// save tagset file
		final XMLMemento memento = XMLMemento.createWriteRoot(TAG_TAGSETS);
		for(ITagSetContainer tsc: tagSetContainers.values()){
			final IMemento mem = memento.createChild(TAG_TAGSET);
			mem.putString(TAGATTR_NAME,tsc.getName());
			mem.putString(TAGATTR_FILE, tsc.isWorkspaceRelative() ? tsc.getFile().getFullPath().toPortableString() : tsc.getFile().getLocation().toPortableString());
			mem.putString(TAGATTR_WORKSPACE, String.valueOf(tsc.isWorkspaceRelative()));
		}
		
		Writer writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(tagsetsFile));
			memento.save(writer);
		} catch(IOException ioe){
			// FIXME: handle
			ioe.printStackTrace();
		} finally {
			IOUtils.closeQuietly(writer);
		}
	}

	private static class DeltaVisitor implements IResourceDeltaVisitor {

		private final TagSetManager manager;

		private DeltaVisitor(TagSetManager manager){
			super();
			this.manager = manager;
		}

		public boolean visit(IResourceDelta delta) throws CoreException {
			switch (delta.getKind()){
				case IResourceDelta.REMOVED:
					projectRemoved(delta);
					break;
				case IResourceDelta.CHANGED:
					projectChanged(delta);
					break;
			}
			return(true);
		}

		private void projectRemoved(final IResourceDelta delta) throws CoreException {
			final IProject project = (IProject)(delta.getResource()).getAdapter(IProject.class);
			if(project != null){
				manager.deregisterTagSet(project);
			}
		}

		private void projectChanged(final IResourceDelta delta) throws CoreException {
			final IProject project = (IProject)(delta.getResource()).getAdapter(IProject.class);
			if(project != null){
				int flags = delta.getFlags();
				if ((flags & IResourceDelta.OPEN) != 0) {
					if(project.isOpen()){
						// taggable project opened, register the tag set
						manager.registerTagSet(project);
					} else if(!project.isOpen()){
						// project closed, deregister tagset (might not be taggable)
						// FIXME: wondering if this exposes a flaw... file needs to be hacked in or tagset dereg without save
						manager.deregisterTagSet(project);
					}
				}
			}
		}
	}
	
	private static class DeltaPrinter implements IResourceDeltaVisitor {
		public boolean visit(IResourceDelta delta) {
			IResource res = delta.getResource();
			int flags = delta.getFlags();
			switch (delta.getKind()) {
			case IResourceDelta.CHANGED:
				System.out.println("Resource-Changed: " + delta.getFullPath());
				if ((flags & IResourceDelta.CONTENT) != 0) {
					System.out.println("--> Content Change");
				}
				break;
			}
			return true; // visit the children
		}
	}
}
