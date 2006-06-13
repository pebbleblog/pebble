package net.sourceforge.pebble.api.comment;

import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.util.SecurityUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Starting point for CommentConfirmationStrategy implementations.
 *
 * @author    Simon Brown
 */
public abstract class AbstractCommentConfirmationStrategy implements CommentConfirmationStrategy {

  /**
   * Called to determine whether confirmation is required. This default
   * implementation returns true is the user isn't authenticated,
   * false otherwise.
   *
   * @param request the HttpServletRequest used in the confirmation
   * @param comment the Comment being confirmed
   * @return true if the comment should be confirmed, false otherwise
   */
  public boolean confirmationRequired(HttpServletRequest request, Comment comment) {
    Blog blog = comment.getBlogEntry().getBlog();
    return !(SecurityUtils.isUserAuthorisedForBlogAsBlogContributor(blog) || SecurityUtils.isUserAuthorisedForBlogAsBlogOwner(blog));
  }

  /**
   * Called before showing the confirmation page.
   *
   * @param request the HttpServletRequest used in the confirmation
   * @param comment the Comment being confirmed
   */
  public void setupConfirmation(HttpServletRequest request, Comment comment) {
  }

}
