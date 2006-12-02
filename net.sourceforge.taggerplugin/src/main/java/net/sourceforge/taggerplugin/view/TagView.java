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

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

/**
 * The view showing all available tags in the workspace. The view is rendered
 * as a table with a name and description field.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagView extends ViewPart {

	public static final String ID = "net.sourceforge.taggerplugin.TagView";
	private TableViewer viewer;

	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		viewer.setContentProvider(new TagViewContentProvider());
		viewer.setLabelProvider(new TagViewLabelProvider());
		viewer.setSorter(new TagViewSorter());

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
//		table.setLinesVisible(true);

		createTableColumn(table,TaggerMessages.TagView_Header_Name, 100);
		createTableColumn(table,TaggerMessages.TagView_Header_Description, 100);

		viewer.setInput(TagManager.getInstance());
	}

	/**
	 * Helper method used to create a table column for the table.
	 *
	 * @param table the table to which the column is being added
	 * @param name the name (header) of the column
	 * @param width the width of the column
	 */
	private void createTableColumn(Table table, String name, int width){
		final TableColumn col = new TableColumn(table,SWT.LEFT);
		col.setText(name);
		col.setWidth(width);
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public Tag getSelectedTag(){
		final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		return(!selection.isEmpty() ? (Tag)selection.getFirstElement() : null);
	}
}
