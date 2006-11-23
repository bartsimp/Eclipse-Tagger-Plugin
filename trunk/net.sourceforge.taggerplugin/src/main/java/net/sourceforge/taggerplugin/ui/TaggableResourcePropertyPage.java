package net.sourceforge.taggerplugin.ui;

import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.resource.ITaggable;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.PropertyPage;

/**
 * Property page addition to resources that displays a list of all tags associated with the resource.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TaggableResourcePropertyPage extends PropertyPage {

	private static final String LIST_SEPARATOR = ", ";

	@Override
	protected Control createContents(Composite parent) {
		final Composite panel = new Composite(parent,SWT.NONE);
		panel.setLayout(new GridLayout(1,false));
		
		final Label tagsLbl = new Label(panel,SWT.LEFT);
		tagsLbl.setFont(parent.getFont());
		tagsLbl.setText(Messages.TaggableResourcePropertyPage_Label_Associations);
		
		final Text tagsTxt = new Text(panel,SWT.MULTI | SWT.WRAP);
		tagsTxt.setEditable(false);
		tagsTxt.setText(extractTagAssociations());
		
		return(panel);
	}

	/**
	 * Used to extract the tag associations from the resource and return the list of 
	 * tag names as a comma-separated string.
	 *
	 * @return a comma-separated list of tagnames associated with the selected resource.
	 */
	private String extractTagAssociations(){
		final ITaggable taggable = (ITaggable)getElement().getAdapter(ITaggable.class);
		if(taggable != null){
			final StringBuilder str = new StringBuilder();
			final Tag[] tags = TagManager.getInstance().findTags(taggable.listTags());
			for (Tag tag : tags){
				str.append(tag.getName()).append(LIST_SEPARATOR);
			}
			if(tags.length > 0){
				str.deleteCharAt(str.length()-1);
				str.deleteCharAt(str.length()-1);
				return(str.toString());
			} else {
				return(Messages.TaggableResourcePropertyPage_NoAssociations);
			}
		} else {
			return(Messages.TaggableResourcePropertyPage_NoAssociations);
		}
	}
}
