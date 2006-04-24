package net.sourceforge.pebble.plugin.decorator;

import net.sourceforge.pebble.domain.Attachment;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;

/**
 * Translates relative URIs in the blog entry body and excerpt into
 * absolute URLs.
 * 
 * @author Simon Brown
 */
public class RelativeUriDecorator extends BlogEntryDecoratorSupport {

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
    if (context.getMedia() != BlogEntryDecoratorContext.DESKTOP_UI) {
      BlogEntry blogEntry = context.getBlogEntry();
      Blog blog = blogEntry.getBlog();
      String body = blogEntry.getBody();
      body = body.replaceAll("\\./images/", blog.getUrl() + "images/");
      body = body.replaceAll("\\./files/", blog.getUrl() + "files/");
      blogEntry.setBody(body);

      String excerpt = blogEntry.getExcerpt();
      excerpt = excerpt.replaceAll("\\./images/", blog.getUrl() + "images/");
      excerpt = excerpt.replaceAll("\\./files/", blog.getUrl() + "files/");
      blogEntry.setExcerpt(excerpt);

      Attachment attachment = blogEntry.getAttachment();
      if (attachment != null) {
        String attachmentUrl = attachment.getUrl();
        if (attachmentUrl.startsWith("./")) {
          attachment.setUrl(blog.getUrl() + attachmentUrl.substring(2));
        }
      }
    }

    chain.decorate(context);
  }

}
