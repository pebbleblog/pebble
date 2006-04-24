package net.sourceforge.pebble.plugin.decorator;

import net.sourceforge.pebble.domain.BlogException;

/**
 * Represents an exception thrown when executing a decorator.
 * 
 * @author Simon Brown
 */
public class BlogEntryDecoratorException extends BlogException {

  /**
   * Creates a new exception instance with the specified message.
   *
   * @param message   the message as a String
   */
  public BlogEntryDecoratorException(String message) {
    super(message);
  }

}
