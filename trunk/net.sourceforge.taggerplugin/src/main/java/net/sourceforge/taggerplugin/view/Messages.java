package net.sourceforge.taggerplugin.view;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "net.sourceforge.taggerplugin.view.messages";

	public static String TagView_Header_Name;
	public static String TagView_Header_Description;

	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages(){}
}
