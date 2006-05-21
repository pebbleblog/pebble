package net.sourceforge.pebble.plugin.decorator;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.Attachment;

import java.util.ResourceBundle;

/**
 * Adds a read more link :
 *  - when the entry is aggregated
 *  - when an excerpt is present, in the summary view
 *
 * @author Simon Brown
 */
public class ReadMoreDecorator extends BlogEntryDecoratorSupport {

  /**
   * Executes the logic associated with this decorator.
   *
   * @param chain   the chain of BlogEntryDecorators to apply
   * @param context     the context in which the decoration is running
   * @throws net.sourceforge.pebble.plugin.decorator.BlogEntryDecoratorException
   *          if something goes wrong when running the decorator
   */
  public void decorate(BlogEntryDecoratorChain chain, BlogEntryDecoratorContext context)
    throws BlogEntryDecoratorException {
    BlogEntry blogEntry = context.getBlogEntry();

    if (blogEntry.isAggregated() ||
        (blogEntry.getExcerpt() != null && blogEntry.getExcerpt().length() > 0 && context.getView() == BlogEntryDecoratorContext.SUMMARY_VIEW)) {
      Blog blog = blogEntry.getBlog();
      ResourceBundle bundle = ResourceBundle.getBundle("resources", blog.getLocale());

      StringBuffer buf = new StringBuffer();
      buf.append(blogEntry.getBody());

      buf.append("<p><a href=\"");
      buf.append(blogEntry.getPermalink());
      buf.append("\">");
      buf.append(bundle.getString("common.readMore"));
      buf.append("</a></p>");

      blogEntry.setBody(buf.toString());
    }

    chain.decorate(context);
  }

}
