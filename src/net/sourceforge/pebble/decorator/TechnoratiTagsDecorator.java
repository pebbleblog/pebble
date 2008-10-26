package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.domain.PageBasedContent;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;


/**
 * Generates Technorati tag links for inclusion in the body of blog entries,
 * when rendered as HTML and newsfeeds.
 * 
 * @author Simon Brown
 */
public class TechnoratiTagsDecorator extends AbstractTagsDecorator {
  public TechnoratiTagsDecorator() {
	super("tag.technoratiTags", true);
  }
	
  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   *          if something goes wrong when running the decorator
   */
  @Override
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    String html = generateDecorationHtml(context, blogEntry);

    // now add the tags to the body and excerpt, if they aren't empty
    String body = blogEntry.getBody();
    if (body != null && body.trim().length() > 0) {
      blogEntry.setBody(body + html);
    }

    String excerpt = blogEntry.getExcerpt();
    if (excerpt != null && excerpt.trim().length() > 0) {
      blogEntry.setExcerpt(excerpt + html);
    }
  }

  /**
   * Gets the base URL for tag links, complete with trailing slash.
   *
   * @param content   the owning content
   * @return  a URL as a String
   */
  public String getBaseUrl(PageBasedContent content) {
    return "http://technorati.com/tag/";
  }

}
