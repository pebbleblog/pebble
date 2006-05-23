package net.sourceforge.pebble.plugin.decorator;

import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.domain.BlogEntry;

/**
 * Hides unpublished blog entries if the current user is
 * not a blog contributor or blog owner.
 * 
 * @author Simon Brown
 */
public class HideUnpublishedBlogEntriesDecorator extends BlogEntryDecoratorSupport {

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

    BlogEntry blogEntry = context.getBlogEntry();

    if (blogEntry.isPublished()) {
      chain.decorate(context);
    } else {
      if (SecurityUtils.isUserAuthorisedForBlogAsBlogOwner(blogEntry.getBlog()) ||
          SecurityUtils.isUserAuthorisedForBlogAsBlogContributor(blogEntry.getBlog())) {
        // decorate as normal
        chain.decorate(context);
      } else {
        // hide the blog entry and don't call other decorators
        context.setBlogEntry(null);
      }
    }
  }

}
