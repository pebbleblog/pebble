package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.api.decorator.ContentDecoratorSupport;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.TrackBack;
import net.sourceforge.pebble.util.StringUtils;

/**
 * Decorates blog entries and comments by rendering them as HTML.
 * 
 * @author Simon Brown
 */
public class HtmlDecorator extends ContentDecoratorSupport {

  /**
   * Decorates the specified comment.
   *
   * @param context the context in which the decoration is running
   * @param comment the comment to be decorated
   */
  public void decorate(ContentDecoratorContext context, Comment comment) {
    comment.setBody(StringUtils.transformToHTMLSubset(StringUtils.transformHTML(comment.getBody())));
  }

  /**
   * Decorates the specified TrackBack.
   *
   * @param context   the context in which the decoration is running
   * @param trackBack the TrackBack to be decorated
   */
  public void decorate(ContentDecoratorContext context, TrackBack trackBack) {
    trackBack.setExcerpt(StringUtils.transformToHTMLSubset(StringUtils.transformHTML(trackBack.getExcerpt())));
  }
}
