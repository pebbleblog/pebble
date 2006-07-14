package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.TrackBack;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

import java.util.List;

/**
 * Hides unapproved responses (comments and TrackBacks) if the current user is
 * not a blog contributor.
 * 
 * @author Simon Brown
 */
public class HideUnapprovedResponsesDecorator extends ContentDecoratorSupport {

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
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
  }

}
