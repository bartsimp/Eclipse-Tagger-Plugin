package net.sourceforge.taggerplugin.ui;

import net.sourceforge.taggerplugin.TaggerActivator;

import org.eclipse.swt.widgets.Shell;

public class ExceptionDialogFactory {

	private ExceptionDialogFactory(){super();}
	
	public static final ExceptionDetailsDialog create(Shell shell, Exception ex){	// FIXME: externalize
		return(new ExceptionDetailsDialog(shell,"Exception",null,ex.getMessage(),ex,TaggerActivator.getDefault()));
	}
}
