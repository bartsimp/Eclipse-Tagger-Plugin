package net.sourceforge.taggerplugin.view;

import net.sourceforge.taggerplugin.manager.TagManager;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.part.ViewPart;

// FIXME: just a stub
public class TagView extends ViewPart {

	public static final String ID = "";	// FIXME: need id
	private TableViewer viewer;

	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.SINGLE | SWT.FULL_SELECTION);
		viewer.setContentProvider(new TagViewContentProvider());
		viewer.setLabelProvider(new TagViewLabelProvider());
		viewer.setSorter(new TagViewSorter());

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
//		table.setLinesVisible(true);

		createTableColumn(table, "Name", 100);
		createTableColumn(table, "Description", 100);

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
}
