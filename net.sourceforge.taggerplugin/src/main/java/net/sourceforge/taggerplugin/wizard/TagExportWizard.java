package net.sourceforge.taggerplugin.wizard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.util.IoUtils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Wizard used to export the available tag set to a file on the filesystem.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagExportWizard extends Wizard implements IExportWizard {

	private TagExportWizardPage page;

	public void init(IWorkbench workbench, IStructuredSelection selection) {/* nothing for now */}

	@Override
	public void addPages() {
		this.page = new TagExportWizardPage();
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		final File exportFile = new File(page.getExportPath());
		final ExternalTagFormat exportFormat = page.getExportFormat();

		final IRunnableWithProgress op = new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException {
				try {
					doFinish(monitor,exportFile,exportFormat);
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
			MessageDialog.openError(getShell(), "Error", e.getTargetException().getMessage());
			return false;
		}
		return true;
	}

	private void doFinish(final IProgressMonitor monitor, final File exportFile, final ExternalTagFormat exportFormat) throws CoreException {
		final Tag[] tags = TagManager.getInstance().getTags();
		monitor.beginTask("Export Resource Tags",tags.length);

		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(exportFile));
			if(exportFormat.equals(ExternalTagFormat.XML)){
				writeTagsAsXml(writer,tags,monitor);
			} else if(exportFormat.equals(ExternalTagFormat.CSV)){
				writeTagsAsCsv(writer,tags,monitor);
			}
		} catch(Exception ex){
			throw new CoreException(new Status(IStatus.ERROR, TaggerActivator.PLUGIN_ID, IStatus.OK, ex.getMessage(), ex));
		} finally {
			IoUtils.closeQuietly(writer);
		}
	}

	private void writeTagsAsXml(final BufferedWriter writer, final Tag[] tags, IProgressMonitor monitor) throws IOException {
		writer.write("<tags>");
		for(Tag tag : tags){
			writer.write("<tag id='" + tag.getId().toString() + "'><name><![CDATA[" + tag.getName() + "]]></name><description><![CDATA[" + tag.getDescription() + "]]></description></tag>");
			writer.newLine();
			monitor.worked(1);
		}
		writer.write("</tags>");
	}

	private void writeTagsAsCsv(final BufferedWriter writer, final Tag[] tags, IProgressMonitor monitor) throws IOException {
		writer.write("ID,Name,Description");
		for(Tag tag : tags){
			writer.write('"' + tag.getId().toString() + "\",\"" + tag.getName() + "\".\"" + tag.getDescription() + '"');
			writer.newLine();
			monitor.worked(1);
		}
	}
}
