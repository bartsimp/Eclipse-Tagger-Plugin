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
package net.sourceforge.taggerplugin.wizard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.lang.reflect.InvocationTargetException;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.dialog.ExceptionDialogFactory;
import net.sourceforge.taggerplugin.model.ITagSetContainer;
import net.sourceforge.taggerplugin.model.TagSetManager;
import net.sourceforge.taggerplugin.util.SynchWithDisplay;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.XMLMemento;

public class TagSetImportWizard  extends Wizard implements IImportWizard {

	private TagSetImportWizardPage page;

	public void init(IWorkbench workbench, IStructuredSelection selection) {/* nothing */}

	/**
	 * @see Wizard#addPages()
	 */
	@Override
	public void addPages() {
		this.page = new TagSetImportWizardPage();
		addPage(page);
	}

	/**
	 * @see Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		final File file = new File(page.getFilePath());

		final IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(monitor,file);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}
		};

		try {
			getContainer().run(true, false, op);
		} catch (InterruptedException e) {
			return false;
		} catch (InvocationTargetException e) {
			ExceptionDialogFactory.create(getShell(), e.getTargetException()).open();
			return false;
		}
		return true;
	}

	/**
	 * @see AbstractTagExternalizationWizard#doFinish(IProgressMonitor, File, TagIoFormat)
	 */
	protected void doFinish(final IProgressMonitor monitor, final File importFile) throws CoreException {
		if(!importFile.exists()) return;	// TODO: should probably be an error

		final TagSetManager manager = TaggerActivator.getDefault().getTagSetManager();

		SynchWithDisplay.synch(new Runnable(){
			public void run() {
				Reader reader = null;
				try {
					reader = new BufferedReader(new FileReader(importFile));
					final IMemento memento = XMLMemento.createReadRoot(reader);
					final IMemento[] mems = memento.getChildren("tag-set");
					for(IMemento m : mems){
						// will only import tags into existing open tagsetcontainers
						final ITagSetContainer tsc = manager.getTagSetContainer(m.getString("id"));
						if(tsc != null){
							final IMemento[] tags = m.getChild("tags").getChildren("tag");
							for(IMemento t : tags){
								tsc.importTag(t.getString("id"),t.getString("name"),t.getTextData());
							}

							final IMemento[] associations = m.getChild("associations").getChildren("association");
							for(IMemento a : associations){
								final String resourceId = a.getString("resource-id");

								final IMemento[] refs = a.getChildren("tag-ref");
								final String[] tagIds = new String[refs.length];
								for(int i=0; i<refs.length; i++){
									tagIds[i] = refs[i].getString("tag-id");
								}

								tsc.importAssociations(resourceId, tagIds);
							}
						}
					}
				} catch(Exception ex){
					throw new RuntimeException(ex.getMessage(),ex);
//					throw new CoreException(new Status(IStatus.ERROR,TaggerActivator.PLUGIN_ID,IStatus.OK,ex.getMessage(),ex));
				} finally {
					IOUtils.closeQuietly(reader);
				}
			}
		});
	}
}
