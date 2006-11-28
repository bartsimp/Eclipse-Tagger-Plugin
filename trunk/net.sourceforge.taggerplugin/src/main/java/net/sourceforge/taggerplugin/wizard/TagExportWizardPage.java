package net.sourceforge.taggerplugin.wizard;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

class TagExportWizardPage extends WizardPage {

	private Text directoryTxt;
	private Button xmlFormatBtn,csvFormatBtn;

	TagExportWizardPage(){
		super("TagExportWizard","Resource Tag Export",null);	// TODO: externalize me
	}

	String getExportDirectory(){
		return(directoryTxt.getText());
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
		createLabel(panel, "Directory:", "Choose the directory where the file will be saved.",1);	// TODO: externalize me

		directoryTxt = new Text(panel,SWT.BORDER | SWT.SINGLE);
		directoryTxt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		directoryTxt.setEditable(false);

		final Button directoryBtn = new Button(panel,SWT.PUSH);
		directoryBtn.setText("Browse");

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

		setControl(panel);
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
