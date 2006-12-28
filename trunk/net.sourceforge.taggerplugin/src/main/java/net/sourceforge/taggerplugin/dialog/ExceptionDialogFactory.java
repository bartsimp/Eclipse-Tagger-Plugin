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
