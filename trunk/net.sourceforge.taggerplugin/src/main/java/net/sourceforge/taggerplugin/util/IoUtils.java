package net.sourceforge.taggerplugin.util;

import java.io.Reader;
import java.io.Writer;

/**
 * FIXME: may want to import equivalent jakarta-commons api, but for now...
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class IoUtils {

	private IoUtils(){super();}

	public static void closeQuietly(final Reader reader){
		if(reader != null){
			try {reader.close();} catch(Exception e){/* ignored */}
		}
	}

	public static void closeQuietly(final Writer writer) {
		if(writer != null){
			try {writer.close();} catch(Exception e){/* ignored */}
		}
	}
}
