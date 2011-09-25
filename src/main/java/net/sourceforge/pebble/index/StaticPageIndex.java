package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.StaticPage;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: jroper
 * Date: 25.09.11
 * Time: 20:04
 * To change this template use File | Settings | File Templates.
 */
public interface StaticPageIndex {
    /**
     * Indexes one or more blog entries.
     *
     * @param staticPages   a List of Page instances
     */
    void reindex(Collection<StaticPage> staticPages);

    /**
     * Indexes a single page.
     *
     * @param staticPage    a Page instance
     */
    void index(StaticPage staticPage);

    /**
     * Unindexes a single page.
     *
     * @param staticPage    a Page instance
     */
    void unindex(StaticPage staticPage);

    /**
     * Gets the page ID for the specified named page.
     *
     * @param name    a String
     * @return  a String instance, or null if no page exists
     *          with the specified name
     */
    String getStaticPage(String name);

    /**
     * Gets the list of static page IDs.
     *
     * @return    a List<String>
     */
    List<String> getStaticPages();

    /**
     * Determines whether a page with the specified permalink exists.
     *
     * @param name   the name as a String
     * @return  true if the page exists, false otherwise
     */
    boolean contains(String name);

    /**
     * Gets the number of static pages.
     *
     * @return  an int
     */
    int getNumberOfStaticPages();
}
