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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.InvocationTargetException;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.dialog.ExceptionDialogFactory;
import net.sourceforge.taggerplugin.model.ITagSetContainer;
import net.sourceforge.taggerplugin.model.TagSetContainerManager;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.XMLMemento;

/**
 * Wizard used to export the available tag set to a file on the filesystem.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagSetExportWizard extends Wizard implements IExportWizard {

	private TagSetExportWizardPage page;

	public void init(IWorkbench workbench, IStructuredSelection selection) {/* nothing */}

	/**
	 * @see Wizard#addPages()
	 */
	@Override
	public void addPages() {
		this.page = new TagSetExportWizardPage();
		addPage(page);
	}

	/**
	 * @see Wizard#performFinish()
	 */
	@Override
	public boolean performFinish() {
		final ITagSetContainer[] containers = tagSetContainers(page.getContainerId());
		final File file = new File(page.getFilePath());

		final IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(monitor,containers,file);
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

	private ITagSetContainer[] tagSetContainers(String containerId){
		final TagSetContainerManager manager = TaggerActivator.getDefault().getTagSetContainerManager();
		return(containerId.equals(TagSetContainerManager.CONTAINERNAME_ALL) ? manager.getTagSetContainers() : new ITagSetContainer[]{manager.getTagSetContainer(containerId)});
	}

	/**
	 * @see AbstractTagExternalizationWizard#doFinish(IProgressMonitor, File, TagIoFormat)
	 */
	protected void doFinish(final IProgressMonitor monitor, final ITagSetContainer[] containers, final File exportFile) throws CoreException {
		monitor.beginTask("", containers.length + 2);
		try {
			exportFile.createNewFile();
			monitor.worked(1);

			final XMLMemento memento = XMLMemento.createWriteRoot("tagsets");
			for(ITagSetContainer tsc : containers){
				tsc.write(memento.createChild("tag-set"));
				monitor.worked(1);
			}

			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter(new FileWriter(exportFile));
				memento.save(writer);
			} finally {
				IOUtils.closeQuietly(writer);
				monitor.done();
			}
		} catch(Exception ex){
			throw new CoreException(new Status(IStatus.ERROR,TaggerActivator.PLUGIN_ID,IStatus.OK,ex.getMessage(),ex));
		}
	}
}
