package net.sourceforge.pebble.plugin.decorator;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.TrackBack;
import net.sourceforge.pebble.util.StringUtils;

import java.util.Iterator;

/**
 * Decorates blog entries and comments by rendering them as HTML.
 * 
 * @author Simon Brown
 */
public class HtmlDecorator extends ContentDecoratorSupport {

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public BlogEntry decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    // render comments if in detail mode
    if (context.getView() == ContentDecoratorContext.DETAIL_VIEW) {
      Iterator it = blogEntry.getComments().iterator();
      while (it.hasNext()) {
        Comment comment = (Comment)it.next();
        decorate(context, comment);
      }

      it = blogEntry.getTrackBacks().iterator();
      while (it.hasNext()) {
        TrackBack trackBack = (TrackBack)it.next();
        decorate(context, trackBack);
      }
    }

    return blogEntry;
  }

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
