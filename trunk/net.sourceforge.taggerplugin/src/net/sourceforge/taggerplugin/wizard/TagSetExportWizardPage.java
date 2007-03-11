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

import java.util.Arrays;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.model.TagSetContainerManager;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Tag externalization wizard page used by both the import and export wizard.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
class TagSetExportWizardPage extends WizardPage {

	private static final String EMPTY_STRING = "";
	private Text filePath;
	private Combo containerCbo;

	TagSetExportWizardPage(){
		super("TagSetExportWizardPage","Tag Set Export Wizard",null);
	}

	String getContainerId(){return(containerCbo.getText());}

	String getFilePath(){return(filePath.getText());}

	public void createControl(Composite parent) {
		final Composite panel = new Composite(parent, SWT.NULL);
		panel.setLayout(new GridLayout(3,false));

		// FIXME: externalize everything

		// container
		createLabel(panel,"Container:","Select the tag set container.",1);

		containerCbo = new Combo(panel,SWT.READ_ONLY);
		containerCbo.setLayoutData(horizFillGrid(2));

		final TagSetContainerManager manager = TaggerActivator.getDefault().getTagSetContainerManager();
		containerCbo.setItems(TagSetContainerManager.extractContainerNames(Arrays.asList(manager.getTagSetContainers()),true));
		containerCbo.setText(TagSetContainerManager.CONTAINERNAME_ALL);
		containerCbo.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e){dialogChanged();}
		});

		// path
		createLabel(panel,"File:","Select the target file.",1);

		filePath = new Text(panel,SWT.BORDER | SWT.SINGLE);
		filePath.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		filePath.setEditable(false);
		filePath.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e){dialogChanged();}
		});

		final Button directoryBtn = new Button(panel,SWT.PUSH);
		directoryBtn.setText("Browse...");
		directoryBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				final FileDialog dialog = new FileDialog(getShell(),SWT.SAVE);
				final String path = dialog.open();
				filePath.setText(path != null ? path : EMPTY_STRING);
			}
		});

		dialogChanged();
		setControl(panel);
	}

	private void dialogChanged(){
		if(StringUtils.isBlank(filePath.getText())){
			updateStatus("An export file must be selected.");
			return;
		}

		updateStatus(null);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	private Label createLabel(Composite container, String text, String tip, int hspan){
		final Label lbl = new Label(container,SWT.NULL);
		lbl.setText(text);
		lbl.setToolTipText(tip);
		lbl.setLayoutData(grid(hspan));
		return(lbl);
	}

	private static GridData grid(int hspan){
		final GridData gd = new GridData();
		gd.horizontalSpan = hspan;
		return(gd);
	}

	private static GridData horizFillGrid(int hspan){
		final GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = hspan;
		return(gd);
	}
}
