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
package net.sourceforge.taggerplugin.event;

import java.util.EventObject;

import net.sourceforge.taggerplugin.model.TagSetContainerManager;

import org.eclipse.core.resources.IProject;

public class TagSetRegistrationEvent extends EventObject {

	public enum Type {REGISTERED, DEREGISTERED};

	private static final long serialVersionUID = 6440223502773442976L;
	private Type type;
	private IProject project;
	
	public TagSetRegistrationEvent(TagSetContainerManager manager, Type type, IProject project){
		super(manager);
		this.type = type;
		this.project = project;
	}
	
	public IProject getProject(){return(project);}
	
	public TagSetContainerManager getManager(){return((TagSetContainerManager)getSource());}
	
	public Type getType(){return(type);}
}
