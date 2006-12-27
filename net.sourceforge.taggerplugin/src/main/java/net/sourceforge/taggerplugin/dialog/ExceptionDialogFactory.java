package net.sourceforge.taggerplugin.dialog;

import net.sourceforge.taggerplugin.TaggerActivator;
import net.sourceforge.taggerplugin.TaggerMessages;

import org.eclipse.swt.widgets.Shell;

public class ExceptionDialogFactory {

	private ExceptionDialogFactory(){super();}
	
	public static final ExceptionDetailsDialog create(Shell shell, Exception ex){
		return(create(shell,TaggerMessages.ExceptionDialogFactory_Title,ex.getMessage(),ex));
	}
	
	public static final ExceptionDetailsDialog create(Shell shell, String title, String msg, Exception ex){
		return(new ExceptionDetailsDialog(shell,title,null,msg,ex,TaggerActivator.getDefault()));
	}
}
