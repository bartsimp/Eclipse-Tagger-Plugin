/**
 * 
 */
package net.sourceforge.taggerplugin.view;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.model.ITagSetContainer;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.model.TagAssociation;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

class TSVLabelProvider extends LabelProvider {
	
	private static final String ICON_TAG = "icons/tag_blue.png";
	private static final String ICON_TAGASSOC = "icons/attach.png";
	private static final String ICON_FOLDER = "icons/folder.png";

	TSVLabelProvider() {
		super();
	}
	
	@Override
	public Image getImage(Object element) {
		Image image = null;
		if(element instanceof ITagSetContainer){
			return(TaggerActivator.imageDescriptorFromPlugin(TaggerActivator.getDefault().getBundle().getSymbolicName(),ICON_FOLDER).createImage());
		} else if(element instanceof Tag){
			return(TaggerActivator.imageDescriptorFromPlugin(TaggerActivator.getDefault().getBundle().getSymbolicName(),ICON_TAG).createImage());
		} else if(element instanceof TagAssociation){
			return(TaggerActivator.imageDescriptorFromPlugin(TaggerActivator.getDefault().getBundle().getSymbolicName(),ICON_TAGASSOC).createImage());
		} else {
			image = super.getImage(element);
		}
		return(image);
	}
	
	@Override
	public String getText(Object element) {
		String name = null;
		if(element instanceof ITagSetContainer){
			name = ((ITagSetContainer)element).getName();
		} else if(element instanceof Tag){
			final Tag tag = (Tag)element;
			name = tag.getName() + " (" + tag.getDescription() + ")";
		} else if(element instanceof TagAssociation){
			name = ((TagAssociation)element).getResourceId();
		} else {
			name = super.getText(element);
		}
		return name;
	}
}