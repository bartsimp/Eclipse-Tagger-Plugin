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

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.model.ITaggableResource;
import net.sourceforge.taggerplugin.model.Tag;

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

	private static final String TAG_SEPARATOR = ", ";

	@Override
	protected Control createContents(Composite parent) {
		final Composite panel = new Composite(parent,SWT.NONE);
		panel.setLayout(new GridLayout(1,false));

		final Label tagsLbl = new Label(panel,SWT.LEFT);
		tagsLbl.setFont(parent.getFont());
		tagsLbl.setText(TaggerMessages.TaggableResourcePropertyPage_Label_Associations);

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
		final ITaggableResource taggable = (ITaggableResource)getElement().getAdapter(ITaggableResource.class);
		if(taggable != null && taggable.hasAssociations()){
			final StringBuilder str = new StringBuilder();
			for(Tag tag : taggable.getAssociations()){
				str.append(tag.getName()).append(TAG_SEPARATOR);
			}
			str.delete(str.length()-TAG_SEPARATOR.length(),str.length());
			return(str.toString());
		}
		return(TaggerMessages.TaggableResourcePropertyPage_NoAssociations);
	}
}
