package net.sourceforge.pebble.plugin.decorator;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Category;

import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Generates category links for inclusion in the body of blog entries,
 * when rendered as HTML.
 * 
 * @author Simon Brown
 */
public class BlogCategoriesDecorator extends BlogEntryDecoratorSupport {

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

    if (context.getMedia() == BlogEntryDecoratorContext.HTML_PAGE) {
      BlogEntry blogEntry = context.getBlogEntry();
      ResourceBundle bundle = ResourceBundle.getBundle("resources", blogEntry.getBlog().getLocale());
      Iterator categories = blogEntry.getCategories().iterator();

      if (categories.hasNext()) {
        StringBuffer buf = new StringBuffer();

        buf.append("<div class=\"categories\">");
        buf.append(bundle.getString("category.categories"));
        buf.append(" : ");
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

    chain.decorate(context);
  }

}
