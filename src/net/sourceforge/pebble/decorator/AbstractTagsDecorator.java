package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Tag;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Generates tag links for inclusion in the body of blog entries,
 * when rendered as HTML.
 * 
 * @author Simon Brown
 */
public abstract class AbstractTagsDecorator extends ContentDecoratorSupport {

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   *          if something goes wrong when running the decorator
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    if (context.getMedia() == ContentDecoratorContext.HTML_PAGE) {
      ResourceBundle bundle = ResourceBundle.getBundle("resources", blogEntry.getBlog().getLocale());
      Iterator tags = blogEntry.getAllTags().iterator();

      String baseUrl = getBaseUrl(blogEntry);

      if (tags.hasNext()) {
        StringBuffer buf = new StringBuffer();

        buf.append("<div class=\"tags\"><span>");
        buf.append(bundle.getString("tag.tags"));
        buf.append(" : </span>");
        while (tags.hasNext()) {
          Tag tag = (Tag)tags.next();
          buf.append("<a href=\"");
          buf.append(baseUrl);
          buf.append(tag.getName());
          buf.append("\" rel=\"tag\">");
          buf.append(tag.getName());
          buf.append("</a>");

          if (tags.hasNext()) {
            buf.append(" ");
          }
        }
        buf.append("</div>");

        // now add the tags to the body and excerpt, if they aren't empty
        String body = blogEntry.getBody();
        if (body != null && body.trim().length() > 0) {
          blogEntry.setBody(body + buf.toString());
        }

        String excerpt = blogEntry.getExcerpt();
        if (excerpt != null && excerpt.trim().length() > 0) {
          blogEntry.setExcerpt(excerpt + buf.toString());
        }
      }
    }
  }

  /**
   * Gets the base URL for tag links, complete with trailing slash.
   *
   * @param blogEntry   the owning BlogEntry
   * @return  a URL as a String
   */
  public abstract String getBaseUrl(BlogEntry blogEntry);

}
