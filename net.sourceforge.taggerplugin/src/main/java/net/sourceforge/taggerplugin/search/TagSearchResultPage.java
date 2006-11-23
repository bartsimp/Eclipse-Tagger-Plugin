package net.sourceforge.taggerplugin.search;

import net.sourceforge.taggerplugin.event.TagSearchResultEvent;

import org.eclipse.search.ui.ISearchResult;
import org.eclipse.search.ui.ISearchResultListener;
import org.eclipse.search.ui.ISearchResultPage;
import org.eclipse.search.ui.ISearchResultViewPart;
import org.eclipse.search.ui.SearchResultEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.IPageSite;

public class TagSearchResultPage implements ISearchResultPage,ISearchResultListener {
	
	private String id;
	private Object uiState;
	private TagSearchResult result;
	private Composite control;
	private IPageSite site;
	private ISearchResultViewPart viewPart;
	private Table resultTable;
	
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

	public void setInput(ISearchResult search, Object uiState) {
		// TODO: what to do with state
		this.result = (TagSearchResult)search;
		result.addListener(this);
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
		
		resultTable = new Table(panel,SWT.V_SCROLL | SWT.SINGLE);
		final GridData tableGridData = new GridData(GridData.FILL_HORIZONTAL);
		tableGridData.heightHint = 100;
		resultTable.setLayoutData(tableGridData);
		
		this.control = panel;
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

	public void searchResultChanged(SearchResultEvent e) {
		final TagSearchResultEvent evt = (TagSearchResultEvent)e;
		
		final TableItem item = new TableItem(resultTable,SWT.NONE);
		item.setText(evt.getResource().getFullPath().toString());
	}
}
