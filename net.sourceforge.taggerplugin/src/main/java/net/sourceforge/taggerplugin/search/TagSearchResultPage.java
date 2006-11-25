package net.sourceforge.taggerplugin.search;


import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
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
	
	private String id;
	private Object uiState;
	private Composite control;
	private IPageSite site;
	private TableViewer resultViewer;
	private TagSearchResultsViewContentProvider viewContentProvider;
	private ISearchResult result;
	
	public String getID() {return id;}

	public String getLabel() {return(Messages.TagSearchResultPage_Label);}

	public Object getUIState() {return uiState;}

	public void restoreState(IMemento memento) {
		// TODO Auto-generated method stub
		System.out.println("Restoring: " + memento);
	}

	public void saveState(IMemento memento) {
		// TODO Auto-generated method stub
		System.out.println("Saving: " + memento);
	}

	public void setID(String id) {
		this.id = id;
	}

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
		
		resultViewer = new TableViewer(panel, SWT.SINGLE | SWT.FULL_SELECTION | SWT.BORDER);
		resultViewer.setContentProvider(new TagSearchResultsViewContentProvider());
		resultViewer.setLabelProvider(new TagSearchResultsViewLabelProvider());
		resultViewer.setSorter(new ViewerSorter(){});

		final Table table = resultViewer.getTable();
		table.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		table.setHeaderVisible(true);

		createTableColumn(table,Messages.TagSearchResultPage_Column_Name, 100);
		createTableColumn(table,Messages.TagSearchResultPage_Column_Path, 300);
		createTableColumn(table,Messages.TagSearchResultPage_Column_Tags, 100);

		resultViewer.setInput(null);
		
		resultViewer.addDoubleClickListener(new IDoubleClickListener(){
			public void doubleClick(DoubleClickEvent event) {
				final ISelection selection = event.getSelection();
				if(!selection.isEmpty() && selection instanceof IStructuredSelection){
					final IStructuredSelection iss = (IStructuredSelection)selection;
					
					// FIXME: pull this out and share the instance
					final OpenFileAction action = new OpenFileAction(site.getWorkbenchWindow().getActivePage());
					action.selectionChanged(iss);
					action.run();
				}
			}
		});
		
		this.control = panel;
	}
	
	private void createTableColumn(Table table, String name, int width){
		final TableColumn col = new TableColumn(table,SWT.LEFT);
		col.setText(name);
		col.setWidth(width);
	}

	public void dispose() {
		viewContentProvider.dispose();
		control.dispose();
	}

	public Control getControl() {return control;}

	public void setActionBars(IActionBars actionBars) {/* nothing for now */}

	public void setFocus() {resultViewer.getTable().setFocus();}
}
