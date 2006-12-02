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
package net.sourceforge.taggerplugin.wizard;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import net.sourceforge.taggerplugin.model.Tag;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * Import/export helper for reading and writing tag information to and from external formats.
 *
 * @author Christopher J. Stehno (chris@stehno.com)
 */
public interface ITagIo {

	public Tag[] readTags(Reader reader, IProgressMonitor monitor) throws IOException;

	public void writeTags(Writer writer, Tag[] tags, IProgressMonitor monitor) throws IOException;
}
