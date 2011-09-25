package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Category;

import java.util.Collection;
import java.util.List;

/**
 * Index for categories
 */
public interface CategoryIndex {
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
     * Gets the the list of blog entries for a given category.
     *
     * @param category    a category
     * @return  a List of blog entry IDs
     */
    List<String> getRecentBlogEntries(Category category);
}
