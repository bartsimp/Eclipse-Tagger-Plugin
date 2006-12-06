package net.sourceforge.taggerplugin.preferences;

public enum PreferenceConstants {

	CONFIRM_CLEAR_ASSOCIATIONS("confirm.clearassociations"),
	CONFIRM_DELETE_TAG("confirm.deletetag"),
	POSITION_LABEL_DECORATION("position.labeldecoration");

	private String key;

	private PreferenceConstants(String key){
		this.key = key;
	}

	public String getKey(){return(key);}
}
