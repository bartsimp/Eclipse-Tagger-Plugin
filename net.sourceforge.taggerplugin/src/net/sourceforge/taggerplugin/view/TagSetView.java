package net.sourceforge.taggerplugin.view;

import net.sourceforge.taggerplugin.TaggerActivator;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class TagSetView extends ViewPart implements ISelectionProvider {

	private TreeViewer viewer;
	
	@Override
	public void createPartControl(Composite parent) {
		viewer = new TreeViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		viewer.setContentProvider(new TSVContentProvider());
		viewer.setLabelProvider(new TSVLabelProvider());
		
		/////
		
		viewer.setInput(TaggerActivator.getDefault().getTagSetContainerManager());
	}
	
	@Override
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		viewer.addSelectionChangedListener(listener);
	}

	public ISelection getSelection() {
		return(viewer.getSelection());
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		viewer.removeSelectionChangedListener(listener);
	}

	public void setSelection(ISelection selection) {
		viewer.setSelection(selection);
	}
}
