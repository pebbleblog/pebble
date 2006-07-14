package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

import java.util.ResourceBundle;

/**
 * Adds a read more link :
 *  - when the entry is aggregated
 *  - when an excerpt is present, in the summary view
 *
 * @author Simon Brown
 */
public class ReadMoreDecorator extends ContentDecoratorSupport {


  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    Blog blog = blogEntry.getBlog();
    ResourceBundle bundle = ResourceBundle.getBundle("resources", blog.getLocale());

    if ((blogEntry.getExcerpt() != null && blogEntry.getExcerpt().length() > 0 && context.getView() == ContentDecoratorContext.SUMMARY_VIEW)) {
      StringBuffer buf = new StringBuffer();
      buf.append(blogEntry.getExcerpt());

      buf.append("<p><a href=\"");
      buf.append(blogEntry.getPermalink());
      buf.append("\">");
      buf.append(bundle.getString("common.readMore"));
      buf.append("</a></p>");

      blogEntry.setExcerpt(buf.toString());
    } else if (blogEntry.isAggregated()) {
      StringBuffer buf = new StringBuffer();
      buf.append(blogEntry.getBody());

      buf.append("<p><a href=\"");
      buf.append(blogEntry.getPermalink());
      buf.append("\">");
      buf.append(bundle.getString("common.readMore"));
      buf.append("</a></p>");

      blogEntry.setBody(buf.toString());
    }
  }

}