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
package net.sourceforge.taggerplugin.ui;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.event.ITagSetRegistrationListener;
import net.sourceforge.taggerplugin.event.TagSetRegistrationEvent;
import net.sourceforge.taggerplugin.nature.TaggableProjectNature;
import net.sourceforge.taggerplugin.preference.PreferenceConstants;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;

public class TaggableProjectLabelDecorator implements ILightweightLabelDecorator,ITagSetRegistrationListener {

	private static final ImageDescriptor OVERLAY = TaggerActivator.imageDescriptorFromPlugin(TaggerActivator.getDefault().getBundle().getSymbolicName(),"icons/tagger_labeldeco.gif");

	private final List<ILabelProviderListener> labelProviderListeners = new LinkedList<ILabelProviderListener>();

	public TaggableProjectLabelDecorator() {
		TaggerActivator.getDefault().getTagSetContainerManager().addTagSetRegistrationListener(this);
	}

	public void dispose() {
		TaggerActivator.getDefault().getTagSetContainerManager().removeTagSetRegistrationListener(this);
	}

	public void decorate(Object element, IDecoration decoration) {
		final IProject project = (IProject)element;
		try {
			if(project.isOpen() && project.hasNature(TaggableProjectNature.ID)){
				decoration.addOverlay(OVERLAY,TaggerActivator.getDefault().getPreferenceStore().getInt(PreferenceConstants.POSITION_LABEL_DECORATION.getKey()));
			}
		} catch(CoreException ce){
			TaggerLog.error("Unable to decorate project: " + ce.getMessage(),ce);
		}
	}

	public void addListener(ILabelProviderListener listener) {
		if (!labelProviderListeners.contains(listener)){
			labelProviderListeners.add(listener);
		}
	}

	public void removeListener(ILabelProviderListener listener) {
		labelProviderListeners.remove(listener);
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	private void fireLabelProviderEvent(LabelProviderChangedEvent evt){
		for(ILabelProviderListener listener : labelProviderListeners){
			listener.labelProviderChanged(evt);
		}
	}

	public void handleEvent(TagSetRegistrationEvent tsre) {
		fireLabelProviderEvent(new LabelProviderChangedEvent(this,tsre.getProject()));
	}
}
