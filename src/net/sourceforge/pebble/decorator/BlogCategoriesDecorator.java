package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Generates category links for inclusion in the body of blog entries,
 * when rendered as HTML.
 * 
 * @author Simon Brown
 */
public class BlogCategoriesDecorator extends ContentDecoratorSupport {

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
  if (context.getMedia() == ContentDecoratorContext.HTML_PAGE) {
      ResourceBundle bundle = ResourceBundle.getBundle("resources", blogEntry.getBlog().getLocale());
      Iterator categories = blogEntry.getCategories().iterator();

      if (categories.hasNext()) {
        StringBuffer buf = new StringBuffer();

        buf.append("<div class=\"categories\"><span>");
        buf.append(bundle.getString("category.categories"));
        buf.append(" : </span>");
        while (categories.hasNext()) {
          Category category = (Category)categories.next();
          buf.append("<a href=\"");
          buf.append(category.getPermalink());
          buf.append("\">");
          buf.append(category.getName());
          buf.append("</a>");

          if (categories.hasNext()) {
            buf.append(", ");
          }
        }
        buf.append("</div>");

        // now add the HTML to the body and excerpt, if they aren't empty
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

}
