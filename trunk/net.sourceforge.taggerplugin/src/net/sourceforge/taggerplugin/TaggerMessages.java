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

/**
 * Externalized strings accessor for the Resource Tagger plug-in.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TaggerMessages extends NLS {

	private static final String BUNDLE_NAME = "net.sourceforge.taggerplugin.messages";
	
	public static String TagRepositoryManager_LoadWs_Error;
	public static String TagRepositoryManager_LoadProj_Error;
	public static String TagRepositoryManager_SaveWs_Error;
	public static String TagRepositoryManager_SaveProj_Error;
	
	public static String ExceptionDetailsDialog_Title;
	public static String ExceptionDetailsDialog_Label_Provider;
	public static String ExceptionDetailsDialog_Label_PluginName;
	public static String ExceptionDetailsDialog_Label_PluginId;
	public static String ExceptionDetailsDialog_Label_Version;
	
	public static String AddTaggableNatureAction_Error_Title;
	public static String AddTaggableNatureAction_Error_Text;
	
	public static String RemoveTaggableNatureAction_Error_Title;
	public static String RemoveTaggableNatureAction_Error_Text;
	
	public static String TagDialog_Title_Create;
	public static String TagDialog_Title_Modify;
	public static String TagDialog_Label_Name;
	public static String TagDialog_Label_Description;
	public static String TagDialog_Label_Container;
	public static String TagDialog_Error_NoContainer;
	public static String TagDialog_Error_NoName;
	public static String TagDialog_Error_NoDescription;
	
	public static String AddTagAssociationAction_Title;
	public static String AddTagAssociationAction_Message;
	
	public static String TaggerPreferencePage_Description;
	public static String TaggerPreferencePage_Label_ConfirmClear;
	public static String TaggerPreferencePage_Label_ConfirmDelete;
	public static String TaggerPreferencePage_Label_LabelDecoration;
	public static String TaggerPreferencePage_Label_LabelDecoration_TopLeft;
	public static String TaggerPreferencePage_Label_LabelDecoration_TopRight;
	public static String TaggerPreferencePage_Label_LabelDecoration_BottomLeft;
	public static String TaggerPreferencePage_Label_LabelDecoration_BottomRight;
	
	public static String DeleteTagAction_Confirm_Title;
	public static String DeleteTagAction_Confirm_Text;
	
	public static String TaggableResourcePropertyPage_Label_Associations;
	public static String TaggableResourcePropertyPage_NoAssociations;
	
	public static String ClearTagAssociationsAction_Confirm_Title;
	public static String ClearTagAssociationsAction_Confirm_Text; 
	
	static {
		NLS.initializeMessages(BUNDLE_NAME, TaggerMessages.class);
	}

	private TaggerMessages(){super();}
}