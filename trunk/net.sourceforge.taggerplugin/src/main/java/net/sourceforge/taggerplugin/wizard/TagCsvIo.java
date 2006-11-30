package net.sourceforge.taggerplugin.wizard;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.taggerplugin.model.Tag;
import net.sourceforge.taggerplugin.model.TagFactory;

import org.eclipse.core.runtime.IProgressMonitor;

public class TagCsvIo implements ITagIo {

	private static final char LINEBREAK = '\n';
	private static final String QUOTE_COMMA_QUOTE = "\",\"";
	private static final char QUOTE = '"';
	private static final String HEADER_LINE = "\"Id\",\"Name\",\"Description\"";

	public Tag[] readTags(Reader reader, IProgressMonitor monitor) throws IOException {
		monitor.beginTask(Messages.TagIo_Reading, 1);	// TODO: find a more accurate mesurement

		final List<Tag> tags = new LinkedList<Tag>();

		final BufferedReader breader = (BufferedReader)reader;
		for(String line = breader.readLine(); line != null; line = breader.readLine()){
			if(!line.equalsIgnoreCase(HEADER_LINE)){
				final String[] parts = (line.substring(1,line.length()-1)).split(QUOTE_COMMA_QUOTE);
				tags.add(TagFactory.create(parts[0],parts[1],parts[2]));
			}
		}

		monitor.worked(1);

		return(tags.toArray(new Tag[tags.size()]));
	}

	public void writeTags(Writer writer, Tag[] tags, IProgressMonitor monitor) throws IOException {
		monitor.beginTask(Messages.TagIo_Writing, tags.length);

		StringBuilder str = new StringBuilder(HEADER_LINE);

		for(Tag tag : tags){
			str.append(QUOTE).append(tag.getId().toString()).append(QUOTE_COMMA_QUOTE)
				.append(tag.getName()).append(QUOTE_COMMA_QUOTE)
				.append(tag.getDescription()).append(QUOTE).append(LINEBREAK);

			monitor.worked(1);
		}
	}
}
