package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.BlogEntry;

import java.util.Collection;
import java.util.List;

/**
 * Index for authors
 */
public interface AuthorIndex {
    /**
     * Clears the index.
     */
    void clear();

    /**
     * Indexes one or more blog entries.
     *
     * @param blogEntries   a List of BlogEntry instances
     */
    void index(Collection<BlogEntry> blogEntries);

    /**
     * Indexes a single blog entry.
     *
     * @param blogEntry   a BlogEntry instance
     */
    void index(BlogEntry blogEntry);

    /**
     * Unindexes a single blog entry.
     *
     * @param blogEntry   a BlogEntry instance
     */
    void unindex(BlogEntry blogEntry);

    /**
     * Gets the list of authors associated with this blog.
     */
    List<String> getAuthors();

    /**
     * Gets the blog entries for a given author.
     *
     * @param username    a username (String)
     * @return  a List of blog entry IDs
     */
    List<String> getRecentBlogEntries(String username);
}
