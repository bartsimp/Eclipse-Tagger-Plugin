package net.sourceforge.taggerplugin.wizard;

enum TagIoWizardType {

	IMPORT("TagImportWizardPage"),
	EXPORT("TagExportWizardPage");

	private final String pageId;

	private TagIoWizardType(final String pageId){
		this.pageId = pageId;
	}

	String getPageId(){return(pageId);}
}
