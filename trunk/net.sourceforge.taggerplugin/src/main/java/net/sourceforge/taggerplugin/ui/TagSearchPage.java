package net.sourceforge.taggerplugin.ui;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import net.sourceforge.taggerplugin.TaggerLog;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.resource.ITaggable;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.search.ui.ISearchPage;
import org.eclipse.search.ui.ISearchPageContainer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class TagSearchPage extends DialogPage implements ISearchPage {

	private Table tagList;
	private TableItem[] tableItems;
	private Button requireAllBtn;
	private ISearchPageContainer container;
	
	public void setContainer(ISearchPageContainer container) {
		this.container = container;
	}

	public void createControl(Composite parent) {
		final Group panel = new Group(parent,SWT.SHADOW_ETCHED_IN);
		panel.setLayout(new GridLayout(1,false));
		panel.setText("Tag Associations:");
		
		// list of tags
		tagList = new Table(panel,SWT.CHECK | SWT.MULTI | SWT.V_SCROLL);
		final GridData listData = new GridData(GridData.FILL_HORIZONTAL);
		listData.heightHint = 100;
		tagList.setLayoutData(listData);
		
		final Tag[] tags = TagManager.getInstance().getTags();
		Arrays.sort(tags);
		
		tableItems = new TableItem[tags.length];
		for (int t=0; t<tags.length; t++) {
			tableItems[t] = new TableItem(tagList,SWT.NONE);
			tableItems[t].setText(tags[t].getName());
			tableItems[t].setData(tags[t].getId());
		}
		
		// require all
		requireAllBtn = new Button(panel,SWT.CHECK);
		requireAllBtn.setText("All tags required for match.");
		
		setControl(panel);
	}
	
	public boolean performAction() {
		try {
			final UUID[] selectedTagids = extractSelectedTagIds();
			if(selectedTagids.length != 0){
		        container.getRunnableContext().run(true, true, new SearchOperation(selectedTagids,requireAllBtn.getSelection()));
			}
		} catch(Exception ie){
			TaggerLog.error(ie);
		}
        return true;
	}
	
	private UUID[] extractSelectedTagIds(){
		final List<UUID> ids = new LinkedList<UUID>();
		for(TableItem item : tableItems){
			if(item.getChecked()){
				ids.add((UUID)item.getData());
			}
		}
		return(ids.toArray(new UUID[ids.size()]));
	}
	
	private static class SearchOperation extends WorkspaceModifyOperation implements IResourceProxyVisitor {
		
		private final UUID[] tagids;
		private final boolean required;
		
		private SearchOperation(final UUID[] tagids, final boolean required){
			super();
			this.tagids = tagids;
			this.required = required;
		}
		
		public void execute(IProgressMonitor monitor) {
			try {
				ResourcesPlugin.getWorkspace().getRoot().accept(this, IResource.DEPTH_INFINITE);
			} catch(CoreException ce){
				TaggerLog.error(ce);
			}
		}
		
		public boolean visit(IResourceProxy proxy) {
			final ITaggable taggable = (ITaggable)proxy.requestResource().getAdapter(ITaggable.class);
			if(taggable != null){
				if(required){
					boolean hasAll = false;
					for(UUID tagid : tagids){
						if(taggable.hasTag(tagid)){
							hasAll = true;
						} else {
							hasAll = false;
							break;
						}
					}
					
					if(hasAll){
						markMatch(proxy.requestResource());
					}
					
				} else {
					for(UUID tagid : tagids){
						if(taggable.hasTag(tagid)){
							markMatch(proxy.requestResource());
							break;	// abort of first match
						}
					}
				}
			}
			return(true);	// TODO: verify that this is what I want to do
		}
		
		private void markMatch(IResource resource){
			try {
				resource.createMarker(NewSearchUI.SEARCH_MARKER);
			} catch(CoreException ce){
				TaggerLog.error(ce);
			}
		}
	}
}
