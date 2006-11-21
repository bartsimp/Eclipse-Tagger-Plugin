package net.sourceforge.taggerplugin.ui;

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
		super(parentShell,tags, new TagListContentProvider(),new TagLabelProvider(),Messages.TagSelectionDialog_Message);
		setTitle(Messages.TagSelectionDialog_Title);
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
