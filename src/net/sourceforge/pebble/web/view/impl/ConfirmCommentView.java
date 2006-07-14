package net.sourceforge.pebble.web.view.impl;

import net.sourceforge.pebble.web.view.HtmlView;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.confirmation.CommentConfirmationStrategy;

/**
 * Represents the confir comment page.
 *
 * @author    Simon Brown
 */
public class ConfirmCommentView extends HtmlView {

  /**
   * Prepares the view for presentation.
   */
  public void prepare() {
    getModel().put("confirmationAction", "confirmComment.action");
  }

  /**
   * Gets the title of this view.
   *
   * @return the title as a String
   */
  public String getTitle() {
    return null;
  }

  /**
   * Gets the URI that this view represents.
   *
   * @return the URI as a String
   */
  public String getUri() {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    CommentConfirmationStrategy strategy = blog.getCommentConfirmationStrategy();
    return strategy.getUri();
  }

}
