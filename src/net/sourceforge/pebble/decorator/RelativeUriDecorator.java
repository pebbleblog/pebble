package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.domain.Attachment;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

/**
 * Translates relative URIs in blog entries and static pages into absolute URLs.
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
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    blogEntry.setBody(replaceCommonUris(blogEntry.getBody()));
    blogEntry.setExcerpt(replaceCommonUris(blogEntry.getExcerpt()));

    Attachment attachment = blogEntry.getAttachment();
    if (attachment != null) {
      String attachmentUrl = attachment.getUrl();
      if (attachmentUrl.startsWith("./")) {
        attachment.setUrl(getBlog().getUrl() + attachmentUrl.substring(2));
      }
    }
  }

  /**
   * Decorates the specified static page.
   *
   * @param context    the context in which the decoration is running
   * @param staticPage the static page to be decorated
   */
  public void decorate(ContentDecoratorContext context, StaticPage staticPage) {
    staticPage.setBody(replaceCommonUris(staticPage.getBody()));
  }

  /**
   * Helper method to replace common relative URIs with their absolute values.
   *
   * @param s   the String containing relative URIs
   * @return    a new String containing absolute URLs
   */
  private String replaceCommonUris(String s) {
    s = s.replaceAll("href=\"\\./", "href=\"" + getBlog().getUrl());
    s = s.replaceAll("href='\\./", "href='" + getBlog().getUrl());
    s = s.replaceAll("src=\"\\./", "src=\"" + getBlog().getUrl());
    s = s.replaceAll("src='\\./", "src='" + getBlog().getUrl());
    return s;
  }

}