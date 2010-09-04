package net.sourceforge.pebble.web.view.impl;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.confirmation.TrackBackConfirmationStrategy;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.web.view.HtmlView;

/**
 * Represents the confirm TrackBack page.
 *
 * @author    Simon Brown
 */
public class ConfirmTrackBackView extends HtmlView {

  /**
   * Prepares the view for presentation.
   */
  public void prepare() {
    getModel().put("confirmationAction", "confirmTrackBack.action");
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
    TrackBackConfirmationStrategy strategy = blog.getTrackBackConfirmationStrategy();
    return strategy.getUri();
  }

}
