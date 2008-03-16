package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.PageBasedContent;
import net.sourceforge.pebble.domain.Tag;

import java.util.Iterator;
import java.util.ResourceBundle;

/**
 * Generates tag links for inclusion in the body of blog entries,
 * when rendered as HTML.
 * 
 * @author Simon Brown
 */
public abstract class AbstractTagsDecorator extends ContentDecoratorSupport {

  protected String generateDecorationHtml(ContentDecoratorContext context, PageBasedContent content) {
    StringBuffer buf = new StringBuffer();

    if (context.getMedia() == ContentDecoratorContext.HTML_PAGE) {
      ResourceBundle bundle = ResourceBundle.getBundle("resources", content.getBlog().getLocale());
      Iterator tags = content.getAllTags().iterator();

      String baseUrl = getBaseUrl(content);

      if (tags.hasNext()) {
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

      }
    }

    return buf.toString();
  }

  /**
   * Gets the base URL for tag links, complete with trailing slash.
   *
   * @param content   the owning content
   * @return  a URL as a String
   */
  public abstract String getBaseUrl(PageBasedContent content);

}
