package net.sourceforge.taggerplugin.wizard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.manager.TagManager;
import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.util.IoUtils;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IImportWizard;

public class TagImportWizard extends AbstractTagIoWizard implements IImportWizard {

	public TagImportWizard(){
		super(TagIoWizardType.IMPORT);
	}

	protected void doFinish(final IProgressMonitor monitor, final File file, final TagIoFormat format) throws CoreException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));

			final Tag[] tags = getTagIo(format).readTags(reader, monitor);

			final TagManager mgr = TagManager.getInstance();
			for(Tag tag : tags){
				if(!mgr.tagExists(tag.getId())){
					mgr.addTag(tag);
				}
			}

		} catch(Exception ex){
			throw new CoreException(new Status(IStatus.ERROR, TaggerActivator.PLUGIN_ID, IStatus.OK, ex.getMessage(), ex));
		} finally {
			IoUtils.closeQuietly(reader);
		}
	}
}
