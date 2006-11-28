package net.sourceforge.taggerplugin.util;

public class StringUtils {

	private StringUtils(){super();}

	public static final boolean isEmpty(String str){
		return(str == null || str.length() == 0);
	}

	public static final boolean isBlank(String str){
		return(str == null || isEmpty(str.trim()));
	}
}
