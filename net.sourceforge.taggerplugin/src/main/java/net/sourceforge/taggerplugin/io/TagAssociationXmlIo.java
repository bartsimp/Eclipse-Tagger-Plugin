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
package net.sourceforge.taggerplugin.io;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import net.sourceforge.taggerplugin.model.TagAssociation;

import org.eclipse.core.runtime.IProgressMonitor;

public class TagAssociationXmlIo implements ITagAssociationIo {

	public Map<String, TagAssociation> readTagAssociations(Reader reader, IProgressMonitor monitor) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	public void writeTagAssociations(Writer writer, Map<String, TagAssociation> associations, IProgressMonitor monitor) throws IOException {
		// TODO Auto-generated method stub

	}

}
