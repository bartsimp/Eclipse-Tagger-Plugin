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
