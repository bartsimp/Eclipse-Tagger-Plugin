package net.sourceforge.taggerplugin.search;

import org.eclipse.core.resources.IResource;
import org.eclipse.search.ui.ISearchResult;

public interface ITagSearchResult extends ISearchResult {

	public void addMatch(IResource resource);

	public int getMatchCount();

	public IResource[] getMatches();

	public void clearMatches();
}
