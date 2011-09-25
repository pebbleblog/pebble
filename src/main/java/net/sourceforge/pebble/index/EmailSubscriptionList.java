package net.sourceforge.pebble.index;

import java.util.List;

/**
 * Store for email subscriptions
 */
public interface EmailSubscriptionList {
    /**
     * Clears the index.
     */
    void clear();

    /**
     * Adds an e-mail address.
     *
     * @param emailAddress    an e-mail address
     */
    void addEmailAddress(String emailAddress);

    /**
     * Removes an e-mail address.
     *
     * @param emailAddress    an e-mail address
     */
    void removeEmailAddress(String emailAddress);

    /**
     * Gets the list of e-mail addresses.
     */
    List<String> getEmailAddresses();
}
