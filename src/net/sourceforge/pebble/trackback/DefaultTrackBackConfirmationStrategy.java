package net.sourceforge.pebble.trackback;

import net.sourceforge.pebble.api.trackback.TrackBackConfirmationStrategy;

import javax.servlet.http.HttpServletRequest;

/**
 * Default implementation that just asks the user to click a button to confirm
 * their comment.
 *
 * @author    Simon Brown
 */
public class DefaultTrackBackConfirmationStrategy implements TrackBackConfirmationStrategy {

  /**
   * Called to determine whether confirmation is required.
   *
   * @param request the HttpServletRequest used in the confirmation
   * @return true if the comment should be confirmed, false otherwise
   */
  public boolean confirmationRequired(HttpServletRequest request) {
    return true;
  }

  /**
   * Called before showing the confirmation page.
   *
   * @param request the HttpServletRequest used in the confirmation
   */
  public void setupConfirmation(HttpServletRequest request) {
  }

  /**
   * Called to confirm a comment.
   *
   * @param request the HttpServletRequest used in the confirmation
   * @return true if the comment has been successfully confirmed,
   *         false otherwise
   */
  public boolean confirm(HttpServletRequest request) {
    return true;  //To change body of implemented methods use File | Settings | File Templates.
  }

  /**
   * Gets the URI of the confirmation page.
   *
   * @return a URI, relative to the web application root.
   */
  public String getUri() {
    return "/WEB-INF/jsp/defaultTrackBackConfirmation.jsp";
  }

}
