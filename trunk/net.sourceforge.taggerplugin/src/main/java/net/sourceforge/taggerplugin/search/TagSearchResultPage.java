package net.sourceforge.taggerplugin.search;

import net.sourceforge.taggerplugin.event.TagSearchResultEvent;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.resource.ITaggable;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.IPageSite;

public class TagSearchResultPage implements ISearchResultPage {
	
	private String id;
	private Object uiState;
	private Composite control;
	private IPageSite site;
	private ISearchResultViewPart viewPart;
	private TableViewer resultViewer;
	private TagSearchResultsViewContentProvider viewContentProvider;
	
	public String getID() {
		return id;
	}

	public String getLabel() {
		return "Tag Search Results";
	}

	public Object getUIState() {
		return uiState;
	}

	public void restoreState(IMemento memento) {
		// TODO Auto-generated method stub
	}

	public void saveState(IMemento memento) {
		// TODO Auto-generated method stub
	}

	public void setID(String id) {
		this.id = id;
	}

	public void setInput(ISearchResult newSearch, Object uiState) {
		if(newSearch == null){return;}

		if(viewContentProvider == null){
			this.viewContentProvider = new TagSearchResultsViewContentProvider();	
		}
		
		viewContentProvider.inputChanged(resultViewer,null,newSearch);
		
		// TODO: what to do with state
	}

	public void setViewPart(ISearchResultViewPart part) {
		this.viewPart = part;
	}

	public IPageSite getSite() {
		return site;
	}

	public void init(IPageSite site) throws PartInitException {
		this.site = site;
	}

	public void createControl(Composite parent) {
		final Composite panel = new Composite(parent,SWT.NONE);
		panel.setLayout(new GridLayout(1,false));
		
		final GridData viewerGridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		
		resultViewer = new TableViewer(panel, SWT.SINGLE | SWT.FULL_SELECTION);
		resultViewer.setContentProvider(new TagSearchResultsViewContentProvider());
		resultViewer.setLabelProvider(new TagSearchResultsViewLabelProvider());
		resultViewer.setSorter(new ViewerSorter(){});

		final Table table = resultViewer.getTable();
		table.setLayoutData(viewerGridData);
		table.setHeaderVisible(true);

		createTableColumn(table,"Name", 100);
		createTableColumn(table,"Path", 300);
		createTableColumn(table,"Tags", 100);

		resultViewer.setInput(null);
		
		this.control = panel;
	}
	
	private static final class TagSearchResultsViewContentProvider implements IStructuredContentProvider,ISearchResultListener {

		private TagSearchResult result;
		private TableViewer viewer;
		
		public Object[] getElements(Object inputElement) {
			return(result.getMatches());
		}

		public void dispose() {}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			this.viewer = (TableViewer)viewer;	
			
			if(result != null){
				// remove the listener from the old input
				result.removeListener(this);
			}
			
			if(newInput != null){
				result = (TagSearchResult)newInput;
				result.addListener(this);
			}
		}
		
		public void searchResultChanged(SearchResultEvent e) {
			final TagSearchResultEvent evt = (TagSearchResultEvent)e;

			// NOTE: this is a bit hacky but there are NO good search examples on line and SWT is single-threaded and 
			// throws an error if you access the ui from another thread
			Display.getDefault().asyncExec(new Runnable(){
				public void run() {
					viewer.getTable().setRedraw(false);
					try {
						viewer.add(evt.getResource());
					} finally {
						viewer.getTable().setRedraw(true);
					}		
				}
			});
		}
	}
	
	private static final class TagSearchResultsViewLabelProvider extends LabelProvider implements ITableLabelProvider {

		private static final int COLUMN_NAME = 0;
		private static final int COLUMN_PATH = 1;
		private static final int COLUMN_TAGS = 2;
		
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			final IResource resource = (IResource)element;
			switch(columnIndex){
				case COLUMN_NAME:
					return(resource.getName());
				case COLUMN_PATH:
					return(String.valueOf(resource.getLocation()));
				case COLUMN_TAGS:
					final ITaggable taggable = (ITaggable)resource.getAdapter(ITaggable.class);
					final Tag[] tags = TagManager.getInstance().findTags(taggable.listTags());
					
					final StringBuilder str = new StringBuilder();
					for (Tag tag : tags) {
						str.append(tag.getName()).append(", ");
					}
					
					if(tags.length > 0){
						str.delete(str.length() - 2, str.length());
					}
					
					return(str.toString());
				default:
					return("???");
			}
		}
	}
	
	private void createTableColumn(Table table, String name, int width){
		final TableColumn col = new TableColumn(table,SWT.LEFT);
		col.setText(name);
		col.setWidth(width);
	}

	public void dispose() {
		// TODO: should I clean up results?
	}

	public Control getControl() {
		return control;
	}

	public void setActionBars(IActionBars actionBars) {
		// nothing for now
	}

	public void setFocus() {
		// TODO: more?
		control.setFocus();
	}
}
