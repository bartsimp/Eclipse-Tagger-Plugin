package net.sourceforge.taggerplugin.dialog;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerMessages;

import org.eclipse.swt.widgets.Shell;

public class ExceptionDialogFactory {

	private ExceptionDialogFactory(){super();}
	
	public static final ExceptionDetailsDialog create(Shell shell, Exception ex){
		return(new ExceptionDetailsDialog(shell,TaggerMessages.ExceptionDialogFactory_Title,null,ex.getMessage(),ex,TaggerActivator.getDefault()));
	}
}
