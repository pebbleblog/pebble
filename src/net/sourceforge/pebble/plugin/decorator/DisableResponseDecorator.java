package net.sourceforge.pebble.plugin.decorator;

import net.sourceforge.pebble.domain.BlogEntry;

/**
 * Disables comments and TrackBacks for the blog entry.
 * 
 * @author Simon Brown
 */
public class DisableResponseDecorator extends ContentDecoratorSupport {

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public BlogEntry decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    blogEntry.setCommentsEnabled(false);
    blogEntry.setTrackBacksEnabled(false);

    return blogEntry;
  }

}
