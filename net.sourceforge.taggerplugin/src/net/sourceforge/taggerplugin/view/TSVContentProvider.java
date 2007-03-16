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
package net.sourceforge.taggerplugin.view;

import net.sourceforge.taggerplugin.event.ITagSetContainerListener;
import net.sourceforge.taggerplugin.event.ITagSetRegistrationListener;
import net.sourceforge.taggerplugin.event.TagAssociationEvent;
import net.sourceforge.taggerplugin.event.TagEvent;
import net.sourceforge.taggerplugin.event.TagSetContainerEvent;
import net.sourceforge.taggerplugin.event.TagSetRegistrationEvent;
import net.sourceforge.taggerplugin.model.ITagSetContainer;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.model.TagAssociation;
import net.sourceforge.taggerplugin.model.TagSetManager;
import net.sourceforge.taggerplugin.util.SynchWithDisplay;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

class TSVContentProvider implements ITreeContentProvider, ITagSetContainerListener, ITagSetRegistrationListener {

	private TreeViewer viewer;
	private TagSetManager manager;

	TSVContentProvider(){
		super();
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer) viewer;
		if (manager != null){
			manager.removeTagSetContainerListener(this);
			manager.removeTagSetRegistrationListener(this);
		}

		manager = (TagSetManager) newInput;

		if (manager != null){
			manager.addTagSetContainerListener(this);
			manager.addTagSetRegistrationListener(this);
		}
	}

	public Object[] getElements(Object inputElement) {
		return(manager.getTagSetContainers());
	}

	public boolean hasChildren(Object element) {
		boolean has = false;
		if(element instanceof ITagSetContainer){
			has = ((ITagSetContainer)element).hasTags();
		} else if(element instanceof Tag){
			final ITagSetContainer tsc = (ITagSetContainer)getParent(element);
			if(tsc != null){
				has = tsc.hasAssociations((Tag)element);
			}
		}
		return(has);
	}

	public Object[] getChildren(Object parentElement) {
		Object[] children = null;
		if(parentElement instanceof ITagSetContainer){
			children = ((ITagSetContainer)parentElement).getTags();
		} else if(parentElement instanceof Tag){
			children = ((ITagSetContainer)getParent(parentElement)).getAssociations((Tag)parentElement);
		}
		return(children);
	}

	public Object getParent(Object element) {
		Object parent = null;
		if(element instanceof Tag){
			parent = manager.getTagSetContainer((Tag)element);
		}
		return parent;
	}

	public void dispose() {/* nothing */}

	public void handleEvent(final TagSetContainerEvent trme) {
		viewer.getTree().setRedraw(false);
		try {
			if(trme.getType().equals(TagSetContainerEvent.Type.ADDED)){
				if(trme instanceof TagEvent){
					final TagEvent tevt = (TagEvent)trme;
					viewer.add(tevt.getContainer(),tevt.getElement());
				} else if(trme instanceof TagAssociationEvent){
					final TagAssociationEvent taevt = (TagAssociationEvent)trme;
					viewer.add(new TreePath(new Object[]{taevt.getContainer(),taevt.getTag()}),taevt.getElement());
				}
			} else if(trme.getType().equals(TagSetContainerEvent.Type.REMOVED)){
				if(trme instanceof TagEvent){
					viewer.remove(trme.getContainer(),new Object[]{trme.getElement()});
				} else if(trme instanceof TagAssociationEvent){
					final TagAssociationEvent taevt = (TagAssociationEvent)trme;
					final TagAssociation assoc = taevt.getElement();
					final Tag tag = taevt.getTag();
					if(tag == null){
						for(Tag t : assoc.tags()){
							viewer.refresh(t);
						}
					} else {
						viewer.refresh(tag);
//						viewer.remove(new TreePath(new Object[]{taevt.getContainer(),tag}),new Object[]{assoc});
					}
				}
			} else if(trme.getType().equals(TagSetContainerEvent.Type.UPDATED)){
				if(trme instanceof TagEvent){
					viewer.refresh(trme.getContainer());
				}
			}
		} finally {
			viewer.getTree().setRedraw(true);
		}
	}

	public void handleEvent(final TagSetRegistrationEvent tsre) {
		SynchWithDisplay.synch(new Runnable(){
			public void run() {
				viewer.getTree().setRedraw(false);
				try {
					// both registration and deregistration call refresh
					viewer.refresh();
				} finally {
					viewer.getTree().setRedraw(true);
				}
			}
		});
	}
}