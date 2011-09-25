package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Response;

import java.util.Collection;
import java.util.List;

/**
 * Index for responses
 */
public interface ResponseIndex {
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
     * Indexes a single response.
     *
     * @param response    a Response instance
     */
    void index(Response response);

    /**
     * Unindexes a single response.
     *
     * @param response    a Response instance
     */
    void unindex(Response response);

    /**
     * Gets the number of approved responses for this blog.
     *
     * @return  an int
     */
    int getNumberOfApprovedResponses();

    /**
     * Gets the number of pending responses for this blog.
     *
     * @return  an int
     */
    int getNumberOfPendingResponses();

    /**
     * Gets the number of rejected responses for this blog.
     *
     * @return  an int
     */
    int getNumberOfRejectedResponses();

    /**
     * Gets the number of responses for this blog.
     *
     * @return  an int
     */
    int getNumberOfResponses();

    /**
     * Gets the most recent N approved responses.
     *
     * @return  a List of response IDs
     */
    List<String> getRecentResponses(int number);

    /**
     * Gets the most recent N approved responses.
     *
     * @return  a List of response IDs
     */
    List<String> getRecentApprovedResponses(int number);

    /**
     * Gets the list of approved responses.
     *
     * @return  a List of response IDs
     */
    List<String> getApprovedResponses();

    /**
     * Gets the list of pending responses.
     *
     * @return  a List of response IDs
     */
    List<String> getPendingResponses();

    /**
     * Gets the list of rejected responses.
     *
     * @return  a List of response IDs
     */
    List<String> getRejectedResponses();
}
