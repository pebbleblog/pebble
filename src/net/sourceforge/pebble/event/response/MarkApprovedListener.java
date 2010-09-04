package net.sourceforge.pebble.event.response;

import net.sourceforge.pebble.domain.Response;

/**
 * Sets the status of new comments and TrackBacks to approved.
 *
 * @author Simon Brown
 */
public class MarkApprovedListener extends BlogEntryResponseListenerSupport {

  /**
   * Called when a comment or TrackBack has been added.
   *
   * @param response a Response
   */
  protected void blogEntryResponseAdded(Response response) {
    response.setApproved();
  }

}
