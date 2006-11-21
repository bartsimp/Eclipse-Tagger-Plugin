package net.sourceforge.taggerplugin.view;

import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Provided the view labels for the TagView. Maps the Tag model fields with the
 * table columns of the view and provides any necessary conversion/translation.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	private static final int INDEX_NAME = 0;
	private static final int INDEX_DESCRIPTION = 1;

	public String getColumnText(Object obj, int index) {
		final Tag tag = (Tag)obj;
		String txt = null;
		switch(index){
			case INDEX_NAME:
				txt = tag.getName();
				break;
			case INDEX_DESCRIPTION:
				txt = tag.getDescription();
				break;
			default:
				txt = "???";
				break;
		};
		return(txt);
	}

	public Image getColumnImage(Object element, int columnIndex) {
		return null;	// no image
	}
}
