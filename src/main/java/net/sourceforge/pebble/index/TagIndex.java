package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Tag;

import java.util.Collection;
import java.util.List;

/**
 * Index for tags
 */
public interface TagIndex {
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
     * Gets the list of tags associated with this blog.
     *
     * @return The list of tags
     */
    Collection<Tag> getTags();

    /**
     * Gets the blog entries for a given tag.
     *
     * @param tag   a tag
     * @return  a List of blog entry IDs
     */
    List<String> getRecentBlogEntries(Tag tag);
}
