package net.sourceforge.taggerplugin.ui;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.event.ITagAssociationManagerListener;
import net.sourceforge.taggerplugin.event.TagAssociationEvent;
import net.sourceforge.taggerplugin.manager.TagAssociationManager;
import net.sourceforge.taggerplugin.resource.ITaggable;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;

public class TaggedLabelDecorator implements ILightweightLabelDecorator,ITagAssociationManagerListener {

	private static final ImageDescriptor OVERLAY = TaggerActivator.imageDescriptorFromPlugin(TaggerActivator.getDefault().getBundle().getSymbolicName(),"icons/labeldeco.gif");

	private final List<ILabelProviderListener> labelProviderListeners = new LinkedList<ILabelProviderListener>();

	public TaggedLabelDecorator() {
		TagAssociationManager.getInstance().addTagAssociationListener(this);
	}

	public void dispose() {
		TagAssociationManager.getInstance().removeTagAssociationListener(this);
	}

	public void decorate(Object element, IDecoration decoration) {
		boolean decorate = false;
		if(element instanceof ITaggable){
			decorate = ((ITaggable)element).hasTags();
		} else if(element instanceof IResource){
			final IResource resource = (IResource)element;
			final ITaggable taggable = (ITaggable)resource.getAdapter(ITaggable.class);
			decorate = taggable.hasTags();
		}
		
		if(decorate){
			decoration.addOverlay(OVERLAY);
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

	public void handleTagAssociationEvent(TagAssociationEvent tme) {
		fireLabelProviderEvent(new LabelProviderChangedEvent(this,tme.getResource()));
	}
	
	private void fireLabelProviderEvent(LabelProviderChangedEvent evt){
		for(ILabelProviderListener listener : labelProviderListeners){
			listener.labelProviderChanged(evt);
		}
	}
}
