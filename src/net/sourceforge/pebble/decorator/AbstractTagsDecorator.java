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
  private final String resourceKey;
  private final String target;

  /**
   * Extended Parameters for generating Links to different Tagging sites - like Technorati.
   * @param resourceKey is used to determine the label for the tags from pebbles resource files
   * @param openLinkInNewWindow set to true to generate links with 'target="_blank"' 
   */

  public AbstractTagsDecorator(String resourceKey, boolean openLinkInNewWindow) {
	this.resourceKey = resourceKey;
	target=openLinkInNewWindow ? " target=\"_blank\"":"";
  }
	
  /**
   * Default constructors makes Tags use the standard label (key tag.tags) and open links
   * in the same browser window. 
   */

  public AbstractTagsDecorator() {
	this.resourceKey = "tag.tags";
	target="";
  }
	
  protected String generateDecorationHtml(ContentDecoratorContext context, PageBasedContent content) {
    StringBuffer buf = new StringBuffer();

    if (context.getMedia() == ContentDecoratorContext.HTML_PAGE) {
      ResourceBundle bundle = ResourceBundle.getBundle("resources", content.getBlog().getLocale());
      Iterator<Tag> tags = content.getAllTags().iterator();

      String baseUrl = getBaseUrl(content);

      if (tags.hasNext()) {
        buf.append("<div class=\"tags\"><span>");
		buf.append(bundle.getString(resourceKey));
        buf.append(" : </span>");

        while (tags.hasNext()) {

          Tag tag = tags.next();

		  if (tag.getName() != null && !tag.getName().equals("")) {

			  buf.append("<a href=\"");
			  buf.append(baseUrl);
			  buf.append(tag.getName() + "\"");
			  buf.append(target);
			  buf.append(" rel=\"tag\">");
			  buf.append(tag.getName());
			  buf.append("</a>");

			  if (tags.hasNext()) {
				buf.append(", ");
			  }
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
