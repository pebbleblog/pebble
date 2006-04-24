package net.sourceforge.pebble.plugin.decorator;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.TrackBack;
import net.sourceforge.pebble.util.SecurityUtils;

import java.util.List;

/**
 * Hides unapproved responses (comments and TrackBacks) if the current user is
 * not a blog contributor.
 * 
 * @author Simon Brown
 */
public class HideUnapprovedResponsesDecorator extends BlogEntryDecoratorSupport {

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

    if (!SecurityUtils.isUserAuthorisedForBlogAsBlogContributor(blogEntry.getBlog())) {
      List comments = blogEntry.getComments();
      for (int i = comments.size()-1; i >= 0; i--) {
        Comment comment = (Comment)comments.get(i);
        if (!comment.isApproved()) {
          blogEntry.removeComment(comment.getId());
        }
      }

      List trackBacks = blogEntry.getTrackBacks();
      for (int i = trackBacks.size()-1; i >= 0; i--) {
        TrackBack trackBack = (TrackBack)trackBacks.get(i);
        if (!trackBack.isApproved()) {
          blogEntry.removeTrackBack(trackBack.getId());
        }
      }
    }

    chain.decorate(context);
  }

}
