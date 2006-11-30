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
