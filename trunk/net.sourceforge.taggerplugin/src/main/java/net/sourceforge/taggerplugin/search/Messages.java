package net.sourceforge.taggerplugin.search;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	
	private static final String BUNDLE_NAME = "net.sourceforge.taggerplugin.search.messages";

	public static String TagSearchPage_Label_TagList;
	public static String TagSearchPage_Label_AllRequired;
	public static String TagSearchPage_Error_Title;
	public static String TagSearchPage_Error_Message;
	
	public static String TagSearchQuery_Label;
	public static String TagSearchQuery_Status_Complete;
	public static String TagSearchQuery_Status_Error;
	
	public static String TagSearchResult_Label;
	public static String TagSearchResult_Tooltip;
	
	public static String TagSearchResultPage_Label;
	
	public static String TagSearchResultPage_Column_Name;
	public static String TagSearchResultPage_Column_Path;
	public static String TagSearchResultPage_Column_Tags;

	
	static {
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages(){}
}
