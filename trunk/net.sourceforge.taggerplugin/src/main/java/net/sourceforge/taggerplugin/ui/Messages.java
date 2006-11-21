package net.sourceforge.taggerplugin.ui;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	
	private static final String BUNDLE_NAME = "net.sourceforge.taggerplugin.ui.messages";

	public static String TagDialog_Title_Create;
	public static String TagDialog_Title_Modify;
	public static String TagDialog_Label_Name;
	public static String TagDialog_Label_Description;
	public static String TagDialog_Error_NoName;
	public static String TagDialog_Error_NoDescription;
	
	public static String TagSelectionDialog_Title;
	public static String TagSelectionDialog_Message;
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages(){}
}
