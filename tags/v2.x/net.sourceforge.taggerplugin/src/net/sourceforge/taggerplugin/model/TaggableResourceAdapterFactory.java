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
package net.sourceforge.taggerplugin.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IAdapterFactory;

public class TaggableResourceAdapterFactory implements IAdapterFactory {

	private static final Class[] ADAPTERS = {ITaggableResource.class};
	
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		ITaggableResource taggable = null;
		if(adaptableObject instanceof IFile){
			taggable = adapt((IFile)adaptableObject);
		}
		return(taggable);
	}
	
	protected ITaggableResource adapt(final IFile file){
		return(new TaggableResource(file));
	}

	public Class[] getAdapterList() {return(ADAPTERS);}
}
