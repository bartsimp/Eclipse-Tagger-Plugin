package net.sourceforge.taggerplugin;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * Commonly used logging methods.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public class TaggerLog {

	private TaggerLog(){super();}

	public static void info(String msg){
		log(IStatus.INFO, IStatus.OK, msg, null);
	}

	public static void error(Throwable exception) {
		error("Unexpected Exception: " + exception.getMessage(), exception);
	}

	public static void error(String message, Throwable exception) {
		log(IStatus.ERROR, IStatus.OK, message, exception);
	}

	public static void warn(Throwable exception) {
		warn("Unexpected Exception: " + exception.getMessage(), exception);
	}

	public static void warn(String message, Throwable exception) {
		log(IStatus.WARNING, IStatus.OK, message, exception);
	}

	public static void log(int severity, int code, String message,Throwable exception) {
		log(createStatus(severity, code, message, exception));
	}

	private static IStatus createStatus(int severity, int code, String message, Throwable exception) {
		return new Status(severity, TaggerActivator.PLUGIN_ID, code,message, exception);
	}

	private static void log(IStatus status) {
		TaggerActivator.getDefault().getLog().log(status);
		System.out.println(status.getMessage());
	}
}
