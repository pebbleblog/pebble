package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.BlogEntry;

import java.util.Collection;
import java.util.List;

/**
 * Index for blog entries
 */
public interface BlogEntryIndex {
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
     * Gets the number of blog entries for this blog.
     *
     * @return  an int
     */
    int getNumberOfBlogEntries();

    /**
     * Gets the number of published blog entries for this blog.
     *
     * @return  an int
     */
    int getNumberOfPublishedBlogEntries();

    /**
     * Gets the number of unpublished blog entries for this blog.
     *
     * @return  an int
     */
    int getNumberOfUnpublishedBlogEntries();

    /**
     * Gets the full list of blog entries.
     *
     * @return  a List of blog entry IDs
     */
    List<String> getBlogEntries();

    /**
     * Gets the full list of published blog entries.
     *
     * @return  a List of blog entry IDs
     */
    List<String> getPublishedBlogEntries();

    /**
     * Gets the full list of unpublished blog entries.
     *
     * @return  a List of blog entry IDs
     */
    List<String> getUnpublishedBlogEntries();
}
