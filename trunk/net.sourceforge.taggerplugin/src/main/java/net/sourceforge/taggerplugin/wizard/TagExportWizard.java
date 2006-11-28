package net.sourceforge.taggerplugin.wizard;

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
		// TODO Auto-generated method stub
		System.out.println("Directory: " + page.getExportDirectory()+ ", Format: " + page.getExportFormat());
		return false;
	}

}
