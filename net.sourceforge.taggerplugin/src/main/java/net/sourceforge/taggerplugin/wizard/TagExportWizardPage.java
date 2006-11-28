package net.sourceforge.taggerplugin.wizard;

import net.sourceforge.taggerplugin.util.StringUtils;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

class TagExportWizardPage extends WizardPage {

	private Text exportPathTxt;
	private Button xmlFormatBtn,csvFormatBtn;

	TagExportWizardPage(){
		super("TagExportWizard","Resource Tag Export",null);	// TODO: externalize me
	}

	String getExportPath(){
		return(exportPathTxt.getText());
	}

	ExternalTagFormat getExportFormat(){
		if(xmlFormatBtn.getSelection()){
			return((ExternalTagFormat)xmlFormatBtn.getData());
		} else if(csvFormatBtn.getSelection()){
			return((ExternalTagFormat)csvFormatBtn.getData());
		} else {
			return(null);
		}
	}

	public void createControl(Composite parent) {
		final Composite panel = new Composite(parent, SWT.NULL);
		panel.setLayout(new GridLayout(3,false));

		// directory
		createLabel(panel, "File:", "Choose the file that the tags will be exported into.",1);	// TODO: externalize me

		exportPathTxt = new Text(panel,SWT.BORDER | SWT.SINGLE);
		exportPathTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		exportPathTxt.setEditable(false);
		exportPathTxt.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent e){dialogChanged();}
		});

		final Button directoryBtn = new Button(panel,SWT.PUSH);
		directoryBtn.setText("Browse");
		directoryBtn.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				final FileDialog dialog = new FileDialog(getShell(),SWT.SAVE);
				final String path = dialog.open();
				exportPathTxt.setText(path != null ? path : "");
			}
		});

		// export format
		createLabel(panel, "Export file format:", "Select a format for the export file data.",3);

		xmlFormatBtn = new Button(panel,SWT.RADIO);
		xmlFormatBtn.setLayoutData(createGridData(3));
		xmlFormatBtn.setText("XML");
		xmlFormatBtn.setData(ExternalTagFormat.XML);
		xmlFormatBtn.setSelection(true);

		csvFormatBtn = new Button(panel,SWT.RADIO);
		csvFormatBtn.setLayoutData(createGridData(3));
		csvFormatBtn.setText("Comma-separated values (CSV)");
		csvFormatBtn.setData(ExternalTagFormat.CSV);

		dialogChanged();
		setControl(panel);
	}

	private void dialogChanged(){
		// project
		final String directory = exportPathTxt.getText();
		if(StringUtils.isBlank(directory)){
			updateStatus("The export file must be specified.");
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
		lbl.setLayoutData(createGridData(hspan));
		return(lbl);
	}

	private GridData createGridData(int hspan){
		final GridData gd = new GridData();
		gd.horizontalSpan = hspan;
		return(gd);
	}
}
