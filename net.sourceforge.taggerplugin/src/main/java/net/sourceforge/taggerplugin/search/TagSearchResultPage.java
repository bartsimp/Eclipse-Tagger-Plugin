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
package net.sourceforge.taggerplugin.search;


import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerMessages;
import net.sourceforge.taggerplugin.action.CreateWorkingSetFromResultsAction;
import net.sourceforge.taggerplugin.util.MementoUtils;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.actions.OpenFileAction;
import org.eclipse.ui.part.IPageSite;

/**
 * Search results page viewer for the Tag Search.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagSearchResultPage implements ISearchResultPage {

	private static final String TAG_COLWIDTH_TAGS = "col-width-tags";
	private static final String TAG_COLWIDTH_PATH = "col-width-path";
	private static final String TAG_COLWIDTH_NAME = "col-width-name";
	private static final String TAG_SEARCHVIEWSTATE = "tag-search-view-state";
	private String id;
	private Object uiState;
	private Composite control;
	private IPageSite site;
	private TagSearchResultsViewContentProvider viewContentProvider;
	private ISearchResult result;
	private TableViewer resultViewer;
	private TableColumn nameCol,pathCol,tagsCol;
	private int nameColWidth = 100,pathColWidth = 100,tagsColWidth = 100;
	
	public String getID() {return id;}

	public String getLabel() {return(TaggerMessages.TagSearchResultPage_Label);}

	public Object getUIState() {return uiState;}

	public void saveState(IMemento memento){
		final IMemento mem = memento.createChild(TAG_SEARCHVIEWSTATE);
		mem.putInteger(TAG_COLWIDTH_NAME,nameCol.getWidth());
		mem.putInteger(TAG_COLWIDTH_PATH,pathCol.getWidth());
		mem.putInteger(TAG_COLWIDTH_TAGS,tagsCol.getWidth());
	}
	
	public void restoreState(IMemento memento) {
		if(memento != null){
			final IMemento mem = memento.getChild(TAG_SEARCHVIEWSTATE);
			if(mem != null){
				this.nameColWidth = MementoUtils.getInt(mem,TAG_COLWIDTH_NAME,100);
				this.pathColWidth = MementoUtils.getInt(mem,TAG_COLWIDTH_PATH,100);
				this.tagsColWidth = MementoUtils.getInt(mem,TAG_COLWIDTH_TAGS,100);
			}
		}
	}

	public void setID(String id) {this.id = id;}

	public void setInput(ISearchResult newSearch, Object uiState) {
		if(newSearch == null){
			((TagSearchResult)result).clearMatches();
			return;
		}

		this.result = newSearch;

		if(viewContentProvider == null){
			this.viewContentProvider = new TagSearchResultsViewContentProvider();
		}

		viewContentProvider.inputChanged(resultViewer,result,newSearch);

		// TODO: what to do with state
	}

	public void setViewPart(ISearchResultViewPart part) {}

	public IPageSite getSite() {return site;}

	public void init(IPageSite site) throws PartInitException {
		this.site = site;
	}

	public void createControl(Composite parent) {
		final Composite panel = new Composite(parent,SWT.NONE);
		panel.setLayout(new GridLayout(1,false));

		resultViewer = new TableViewer(panel, SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		resultViewer.setContentProvider(new TagSearchResultsViewContentProvider());
		resultViewer.setLabelProvider(new TagSearchResultsViewLabelProvider());

		final Table table = resultViewer.getTable();
		table.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		table.setHeaderVisible(true);

		this.nameCol = createTableColumn(table,TaggerMessages.TagSearchResultPage_Column_Name,nameColWidth);
		this.pathCol = createTableColumn(table,TaggerMessages.TagSearchResultPage_Column_Path,pathColWidth);
		this.tagsCol = createTableColumn(table,TaggerMessages.TagSearchResultPage_Column_Tags,tagsColWidth);

		// FIXME: this currently does not work - when col is clicked, content disappears
//		resultViewer.setSorter(
//			new GenericViewSorter(
//				"tagsearch",
//				resultViewer,
//				new TableColumn[]{nameCol,pathCol},
//				new Comparator[]{new ResourceComparator(ResourceComparator.Field.NAME),new ResourceComparator(ResourceComparator.Field.PATH)}
//			)
//		);
		
		resultViewer.setInput(null);

		resultViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
				final ISelection selection = event.getSelection();
				if(!selection.isEmpty() && selection instanceof IStructuredSelection){
					final IStructuredSelection iss = (IStructuredSelection)selection;

					final OpenFileAction action = new OpenFileAction(site.getWorkbenchWindow().getActivePage());
					action.selectionChanged(iss);
					action.run();
				}
			}
		});

		this.control = panel;
	}
	
//	This goes with the commented out sorter code above
//	private static final class ResourceComparator implements Comparator<IResource> {
//
//		private static enum Field {NAME,PATH};
//		private final Field field;
//		
//		private ResourceComparator(Field field){
//			this.field = field;
//		}
//		
//		public int compare(IResource r1, IResource r2){
//			if(field.equals(Field.NAME))
//				return(r1.getName().compareTo(r2.getName()));
//			} else {
//				return(String.valueOf(r1.getLocation()).compareTo(String.valueOf(r2.getLocation())));
//			}
//		}
//	}

	private TableColumn createTableColumn(Table table, String name, int width){
		final TableColumn col = new TableColumn(table,SWT.LEFT);
		col.setText(name);
		col.setWidth(width);
		return(col);
	}

	public void dispose() {
		viewContentProvider.dispose();
		control.dispose();
	}

	public Control getControl() {return control;}

	public void setActionBars(IActionBars actionBars) {
		// FIXME: externalize
		final CreateWorkingSetFromResultsAction action = new CreateWorkingSetFromResultsAction();
		action.setId("net.sourceforge.taggerplugin.action.CreateWorkingSetFromResultsAction");
		action.setText("Create Working Set...");
		action.setToolTipText("Create working set from search results.");
		action.setImageDescriptor(TaggerActivator.imageDescriptorFromPlugin(TaggerActivator.PLUGIN_ID, "icons/workset.gif"));
		action.setViewer(resultViewer);
		
		actionBars.getToolBarManager().appendToGroup("additions", action);
		actionBars.getMenuManager().appendToGroup("additions", action);
	}

	public void setFocus() {resultViewer.getTable().setFocus();}
}
