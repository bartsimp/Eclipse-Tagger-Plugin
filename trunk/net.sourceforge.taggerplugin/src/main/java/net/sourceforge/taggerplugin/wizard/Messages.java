package net.sourceforge.taggerplugin.wizard;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "net.sourceforge.taggerplugin.wizard.messages";

	public static String TagExportWizardPage_Title;
	public static String TagExportWizardPage_Tooltip_File;

	public static String TagImportWizardPage_Title;
	public static String TagImportWizardPage_Tooltip_File;

	public static String TagIoWizardPage_Button_Browse;
	public static String TagIoWizardPage_Label_File;
	public static String TagIoWizardPage_Label_Format;
	public static String TagIoWizardPage_Tooltip_Format;
	public static String TagIoWizardPage_Format_Xml;
	public static String TagIoWizardPage_Format_Csv;
	public static String TagIoWizardPage_Error_NoFile;

	public static String TagIo_Reading;
	public static String TagIo_Writing;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages(){}
}
