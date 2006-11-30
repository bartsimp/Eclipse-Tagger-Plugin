package net.sourceforge.taggerplugin.wizard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.util.IoUtils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IExportWizard;

/**
 * Wizard used to export the available tag set to a file on the filesystem.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TagExportWizard extends AbstractTagIoWizard implements IExportWizard {

	public TagExportWizard(){
		super(TagIoWizardType.EXPORT);
	}

	protected void doFinish(final IProgressMonitor monitor, final File exportFile, final TagIoFormat exportFormat) throws CoreException {
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(exportFile));
			getTagIo(exportFormat).writeTags(writer, TagManager.getInstance().getTags(), monitor);
		} catch(Exception ex){
			throw new CoreException(new Status(IStatus.ERROR, TaggerActivator.PLUGIN_ID, IStatus.OK, ex.getMessage(), ex));
		} finally {
			IoUtils.closeQuietly(writer);
		}
	}
}
