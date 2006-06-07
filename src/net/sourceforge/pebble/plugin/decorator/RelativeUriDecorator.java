package net.sourceforge.pebble.plugin.decorator;

import net.sourceforge.pebble.domain.Attachment;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.StaticPage;

/**
 * Translates relative URIs in the blog entry body and excerpt into
 * absolute URLs.
 * 
 * @author Simon Brown
 */
public class RelativeUriDecorator extends ContentDecoratorSupport {


  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public BlogEntry decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    String body = blogEntry.getBody();
    body = body.replaceAll("\\./images/", getBlog().getUrl() + "images/");
    body = body.replaceAll("\\./files/", getBlog().getUrl() + "files/");
    blogEntry.setBody(body);

    String excerpt = blogEntry.getExcerpt();
    excerpt = excerpt.replaceAll("\\./images/", getBlog().getUrl() + "images/");
    excerpt = excerpt.replaceAll("\\./files/", getBlog().getUrl() + "files/");
    blogEntry.setExcerpt(excerpt);

    Attachment attachment = blogEntry.getAttachment();
    if (attachment != null) {
      String attachmentUrl = attachment.getUrl();
      if (attachmentUrl.startsWith("./")) {
        attachment.setUrl(getBlog().getUrl() + attachmentUrl.substring(2));
      }
    }

    return blogEntry;
  }

  /**
   * Decorates the specified static page.
   *
   * @param context    the context in which the decoration is running
   * @param staticPage the static page to be decorated
   */
  public void decorate(ContentDecoratorContext context, StaticPage staticPage) {
    String body = staticPage.getBody();
    body = body.replaceAll("\\./images/", getBlog().getUrl() + "images/");
    body = body.replaceAll("\\./files/", getBlog().getUrl() + "files/");
    staticPage.setBody(body);
  }
}
