package net.sourceforge.pebble.api.confirmation;

import net.sourceforge.pebble.domain.Blog;

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
   * @param blog    the owning Blog
   * @return true if the confirmation is required, false otherwise
   */
  public boolean confirmationRequired(Blog blog);

}