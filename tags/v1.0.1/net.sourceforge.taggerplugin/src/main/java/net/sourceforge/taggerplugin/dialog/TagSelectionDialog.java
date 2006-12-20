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
package net.sourceforge.taggerplugin.dialog;

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListSelectionDialog;

/**
 * Dialog used to selecte one or more tags using check-boxes.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagSelectionDialog extends ListSelectionDialog {

	public TagSelectionDialog(Shell parentShell, Tag[] tags){
		super(parentShell,tags, new TagListContentProvider(),new TagLabelProvider(),TaggerMessages.TagSelectionDialog_Message);
		setTitle(TaggerMessages.TagSelectionDialog_Title);
	}

	private static final class TagListContentProvider implements IStructuredContentProvider {

		private Tag[] input;

		public Object[] getElements(Object inputElement) {
			return(input);
		}

		public void dispose(){}

		@SuppressWarnings("unchecked")
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.input = (Tag[])newInput;
		}
	}

	private static final class TagLabelProvider implements ILabelProvider {

		public Image getImage(Object element) {
			return null;
		}

		public String getText(Object element) {
			return(((Tag)element).getName());
		}

		public void addListener(ILabelProviderListener listener) {}

		public void dispose() {}

		public boolean isLabelProperty(Object element, String property) {
			return false;
		}

		public void removeListener(ILabelProviderListener listener) {}
	}
}
