package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

/**
 * Disables comments for the blog entry.
 * 
 * @author Simon Brown
 */
public class DisableCommentsDecorator extends ContentDecoratorSupport {

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    blogEntry.setCommentsEnabled(false);
  }

}
