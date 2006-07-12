package net.sourceforge.pebble.api.trackback;

import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.confirmation.ConfirmationStrategy;

import javax.servlet.http.HttpServletRequest;

/**
 * Represents an abstraction of the various ways in which TrackBack links
 * can be confirmed.
 *
 * @author    Simon Brown
 */
public interface TrackBackConfirmationStrategy extends ConfirmationStrategy {

  /**
   * Called to determine whether confirmation is required.
   *
   * @param request   the HttpServletRequest used in the confirmation
   * @return  true if the comment should be confirmed, false otherwise
   */
  public boolean confirmationRequired(HttpServletRequest request);

}