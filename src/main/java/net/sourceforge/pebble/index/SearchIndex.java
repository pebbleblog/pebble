package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.search.SearchException;
import net.sourceforge.pebble.search.SearchResults;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: jroper
 * Date: 25.09.11
 * Time: 20:03
 * To change this template use File | Settings | File Templates.
 */
public interface SearchIndex {
    /**
     * Clears the index.
     */
    void clear();

    /**
     * Allows a collection of blog entries to be indexed.
     */
    void indexBlogEntries(Collection<BlogEntry> blogEntries);

    /**
     * Allows a collection of static pages to be indexed.
     */
    void indexStaticPages(Collection<StaticPage> staticPages);

    /**
     * Allows a single blog entry to be (re)indexed. If the entry is already
     * indexed, this method deletes the previous index before adding the new
     * one.
     *
     * @param blogEntry   the BlogEntry instance to index
     */
    void index(BlogEntry blogEntry);

    /**
     * Allows a single static page to be (re)indexed. If the page is already
     * indexed, this method deletes the previous index before adding the new
     * one.
     *
     * @param staticPage    the StaticPage instance to index
     */
    void index(StaticPage staticPage);

    /**
     * Removes the index for a single blog entry to be removed.
     *
     * @param blogEntry   the BlogEntry instance to be removed
     */
    void unindex(BlogEntry blogEntry);

    /**
     * Removes the index for a single blog entry to be removed.
     *
     * @param staticPage    the StaticPage instance to be removed
     */
    void unindex(StaticPage staticPage);

    SearchResults search(String queryString) throws SearchException;
}
