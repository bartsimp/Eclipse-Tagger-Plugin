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
package net.sourceforge.taggerplugin;

import org.eclipse.osgi.util.NLS;

public class TaggerMessages extends NLS {

	private static final String BUNDLE_NAME = "net.sourceforge.taggerplugin.messages";

	public static String TagDialog_Title_Create;
	public static String TagDialog_Title_Modify;
	public static String TagDialog_Label_Name;
	public static String TagDialog_Label_Description;
	public static String TagDialog_Error_NoName;
	public static String TagDialog_Error_NoDescription;

	public static String TagSelectionDialog_Title;
	public static String TagSelectionDialog_Message;

	public static String TaggableResourcePropertyPage_NoAssociations;
	public static String TaggableResourcePropertyPage_Label_Associations;

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

	public static String TagView_Header_Name;
	public static String TagView_Header_Description;

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
		NLS.initializeMessages(BUNDLE_NAME, TaggerMessages.class);
	}

	private TaggerMessages(){super();}
}
