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

import java.util.Iterator;

import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.ViewPart;

/**
 * The view showing all available tags in the workspace. The view is rendered
 * as a table with a name and description field.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagView extends ViewPart {

	private static final String TAG_COLWIDTH_DESC = "col-width-desc";
	private static final String TAG_COLWIDTH_NAME = "col-width-name";
	private static final String TAG_VIEWSTATE = "tag-view-state";
	public static final String ID = "net.sourceforge.taggerplugin.TagView";
	private TableViewer viewer;
	private TableColumn nameCol, descCol;
	private int nameColWidth = 100,descColWidth = 100;
	
	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		super.init(site, memento);
		
		if(memento != null){
			final IMemento mem = memento.getChild(TAG_VIEWSTATE);
			if(mem != null){
				nameColWidth = extractWidth(mem,TAG_COLWIDTH_NAME);
				descColWidth = extractWidth(mem,TAG_COLWIDTH_DESC);
			}
		}
	}
	
	private int extractWidth(IMemento m, String key){
		int val = 100;
		if(m != null){
			final int x = m.getInteger(key);
			val = x > 5 ? x : val;
		}
		return(val);
	}

	public void createPartControl(Composite parent) {
		viewer = new TableViewer(parent, SWT.MULTI | SWT.FULL_SELECTION);
		viewer.setContentProvider(new TagViewContentProvider());
		viewer.setLabelProvider(new TagViewLabelProvider());
		viewer.setSorter(new TagViewSorter());

		final Table table = viewer.getTable();
		table.setHeaderVisible(true);

		this.nameCol = createTableColumn(table,TaggerMessages.TagView_Header_Name,nameColWidth);
		this.descCol = createTableColumn(table,TaggerMessages.TagView_Header_Description,descColWidth);

		viewer.setInput(TagManager.getInstance());
	}

	/**
	 * Helper method used to create a table column for the table.
	 *
	 * @param table the table to which the column is being added
	 * @param name the name (header) of the column
	 * @param width the width of the column
	 */
	private TableColumn createTableColumn(Table table, String name, int width){
		final TableColumn col = new TableColumn(table,SWT.LEFT);
		col.setText(name);
		col.setWidth(width);
		return(col);
	}

	public void setFocus() {
		viewer.getControl().setFocus();
	}

	public Tag getSelectedTag(){
		final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		return(!selection.isEmpty() ? (Tag)selection.getFirstElement() : null);
	}
	
	public Tag[] getSelectedTags(){
		final IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
		final Tag[] tags = new Tag[selection.size()];
		if(!selection.isEmpty()){
			int idx = 0;
			final Iterator sels = selection.iterator();
			while(sels.hasNext()){
				tags[idx++] = (Tag)sels.next();
			}
		}
		return(tags);
	}
	
	@Override
	public void saveState(IMemento memento) {
		final IMemento mem = memento.createChild(TAG_VIEWSTATE);
		mem.putInteger(TAG_COLWIDTH_NAME, nameCol.getWidth());
		mem.putInteger(TAG_COLWIDTH_DESC, descCol.getWidth());
		
		super.saveState(memento);
	}
}
