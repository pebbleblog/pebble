package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.domain.PageBasedContent;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

/**
 * Generates tag links for inclusion in the body of static pages,
 * when rendered as HTML and newsfeeds.
 *
 * @author Simon Brown
 */
public class StaticPageTagsDecorator extends AbstractTagsDecorator {

  /**
   * Decorates the specified static page.
   *
   * @param context    the context in which the decoration is running
   * @param staticPage the static page to be decorated
   */
  @Override
  public void decorate(ContentDecoratorContext context, StaticPage staticPage) {
    String html = generateDecorationHtml(context, staticPage);

    // now add the tags to the body, if not empty
    String body = staticPage.getBody();
    if (body != null && body.trim().length() > 0) {
      staticPage.setBody(body + html);
    }
  }

  /**
   * Gets the base URL for tag links, complete with trailing slash.
   *
   * @param content   the owning content
   * @return  a URL as a String
   */
  public String getBaseUrl(PageBasedContent content) {
    return content.getBlog().getUrl() + "tags/";
  }

}
