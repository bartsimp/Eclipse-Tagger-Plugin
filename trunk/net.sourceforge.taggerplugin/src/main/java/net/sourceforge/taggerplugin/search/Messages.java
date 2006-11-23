package net.sourceforge.taggerplugin.search;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	
	private static final String BUNDLE_NAME = "net.sourceforge.taggerplugin.search.messages";

	public static String TagSearchPage_Label_TagList;

	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages(){}
}
