package net.sourceforge.taggerplugin.resource;

import java.util.LinkedList;
import java.util.List;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.event.ITagAssociationManagerListener;
import net.sourceforge.taggerplugin.event.TagAssociationEvent;
import net.sourceforge.taggerplugin.manager.TagAssociationManager;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ILightweightLabelDecorator;
import org.eclipse.jface.viewers.LabelProviderChangedEvent;

public class TaggedLabelDecorator implements ILightweightLabelDecorator,ITagAssociationManagerListener {

	private static final ImageDescriptor OVERLAY = TaggerActivator.imageDescriptorFromPlugin(TaggerActivator.getDefault().getBundle().getSymbolicName(),"icons/labeldeco.png");

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

//	public void favoritesChanged(FavoritesManagerEvent favoritesEvent) {
//		Collection elements = new HashSet();
//		addResourcesTo(favoritesEvent.getItemsAdded(), elements);
//		addResourcesTo(favoritesEvent.getItemsRemoved(), elements);
//		LabelProviderChangedEvent labelEvent = new LabelProviderChangedEvent(this, elements.toArray());
//		Iterator iter = labelProviderListeners.iterator();
//		while (iter.hasNext())
//			((ILabelProviderListener) iter.next()).labelProviderChanged(labelEvent);
//	}
//
//	private void addResourcesTo(IFavoriteItem[] items,Collection elements){
//		for (int i = 0; i < items.length; i++) {
//			IFavoriteItem item = items[i];
//			Object res = item.getAdapter(IResource.class);
//			if (res != null)
//				elements.add(res);
//		}
//	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void handleTagAssociationEvent(TagAssociationEvent tme) {
		// TODO Auto-generated method stub
		
	}
	
	private void fireLabelProviderEvent(LabelProviderChangedEvent evt){
		for(ILabelProviderListener listener : labelProviderListeners){
			listener.labelProviderChanged(evt);
		}
	}
}
