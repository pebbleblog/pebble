package net.sourceforge.pebble.plugin.decorator;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Tag;

import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Generates tag links for inclusion in the body of blog entries,
 * when rendered as HTML.
 * 
 * @author Simon Brown
 */
public abstract class AbstractTagsDecorator extends BlogEntryDecoratorSupport {

  /**
   * Executes the logic associated with this decorator.
   *
   * @param chain   the chain of BlogEntryDecorators to apply
   * @param context     the context in which the decoration is running
   * @throws BlogEntryDecoratorException
   *          if something goes wrong when running the decorator
   */
  public void decorate(BlogEntryDecoratorChain chain, BlogEntryDecoratorContext context)
      throws BlogEntryDecoratorException {

    if (context.getMedia() == BlogEntryDecoratorContext.HTML_PAGE) {
      BlogEntry blogEntry = context.getBlogEntry();
      ResourceBundle bundle = ResourceBundle.getBundle("resources", blogEntry.getBlog().getLocale());
      Iterator tags = blogEntry.getAllTags().iterator();

      String baseUrl = getBaseUrl(blogEntry);

      if (tags.hasNext()) {
        StringBuffer buf = new StringBuffer();

        buf.append("<div class=\"tags\">");
        buf.append(bundle.getString("tag.tags"));
        buf.append(" : ");
        while (tags.hasNext()) {
          Tag tag = (Tag)tags.next();
          buf.append("<a href=\"" + baseUrl);
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

    chain.decorate(context);
  }

  /**
   * Gets the base URL for tag links, complete with trailing slash.
   *
   * @param blogEntry   the owning BlogEntry
   * @return  a URL as a String
   */
  public abstract String getBaseUrl(BlogEntry blogEntry);

}
