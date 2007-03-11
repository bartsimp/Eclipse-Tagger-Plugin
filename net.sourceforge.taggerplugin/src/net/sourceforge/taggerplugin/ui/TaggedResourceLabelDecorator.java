package net.sourceforge.taggerplugin.ui;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.event.ITagSetContainerListener;
import net.sourceforge.taggerplugin.event.TagAssociationEvent;
import net.sourceforge.taggerplugin.event.TagSetContainerEvent;
import net.sourceforge.taggerplugin.model.ITaggableResource;
import net.sourceforge.taggerplugin.preference.PreferenceConstants;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;

public class TaggedResourceLabelDecorator implements ILightweightLabelDecorator,ITagSetContainerListener {
	
	private static final ImageDescriptor OVERLAY = TaggerActivator.imageDescriptorFromPlugin(TaggerActivator.getDefault().getBundle().getSymbolicName(),"icons/tagger_labeldeco.gif");

	private final List<ILabelProviderListener> labelProviderListeners = new LinkedList<ILabelProviderListener>();

	public TaggedResourceLabelDecorator() {
		TaggerActivator.getDefault().getTagSetContainerManager().addTagSetContainerListener(this);
	}

	public void dispose() {
		TaggerActivator.getDefault().getTagSetContainerManager().removeTagSetContainerListener(this);
	}

	public void decorate(Object element, IDecoration decoration) {
		final IResource resource = (IResource)element;
		final ITaggableResource taggable = (ITaggableResource)resource.getAdapter(ITaggableResource.class);
		if(taggable != null && taggable.hasAssociations()){
			decoration.addOverlay(OVERLAY,TaggerActivator.getDefault().getPreferenceStore().getInt(PreferenceConstants.POSITION_LABEL_DECORATION.getKey()));
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

	public void handleEvent(TagSetContainerEvent trme) {
		if(trme instanceof TagAssociationEvent){
			final TagAssociationEvent tevt = (TagAssociationEvent)trme;
			fireLabelProviderEvent(new LabelProviderChangedEvent(this,tevt.getResource()));
		}
	}
}
