package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.StaticPage;

/**
 * Encloses the body or excerpt of the blog entry within a DIV (class=blogContentDecorated) making it
 * easier to target it with CSS selectors
 *
 * @author Pieroxy
 */
public class EncloseEntryInDivDecorator extends ContentDecoratorSupport {

  /**
   * Decorates the specified blog entry by putting the existing body and excerpt in a DIV.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    if (blogEntry.getExcerpt() != null && blogEntry.getExcerpt().length()>0) {
      blogEntry.setExcerpt("<div class=\"blogContentDecorated\">" + blogEntry.getExcerpt() + "</div>");
    }
    if (blogEntry.getBody() != null) {
      blogEntry.setBody("<div class=\"blogContentDecorated\">" + blogEntry.getBody() + "</div>");
    }
  }

  public void decorate(ContentDecoratorContext context, StaticPage staticPage) {
    if (staticPage.getBody() != null) {
      staticPage.setBody("<div class=\"pageContentDecorated\">" + staticPage.getBody() + "</div>");
    }
  }
}
