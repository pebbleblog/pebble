package net.sourceforge.pebble.api.trackback;

import net.sourceforge.pebble.domain.Comment;

import javax.servlet.http.HttpServletRequest;

/**
 * Represents an abstraction of the various ways in which TrackBack links
 * can be confirmed.
 *
 * @author    Simon Brown
 */
public interface TrackBackConfirmationStrategy {

  /**
   * Called to determine whether confirmation is required.
   *
   * @param request   the HttpServletRequest used in the confirmation
   * @return  true if the comment should be confirmed, false otherwise
   */
  public boolean confirmationRequired(HttpServletRequest request);

  /**
   * Called before showing the confirmation page.
   *
   * @param request   the HttpServletRequest used in the confirmation
   */
  public void setupConfirmation(HttpServletRequest request);

  /**
   * Gets the URI of the confirmation page.
   *
   * @return  a URI, relative to the web application root.
   */
  public String getUri();

  /**
   * Called to confirm a comment.
   *
   * @param request   the HttpServletRequest used in the confirmation
   * @return  true if the comment has been successfully confirmed,
   *          false otherwise
   */
  public boolean confirm(HttpServletRequest request);

}