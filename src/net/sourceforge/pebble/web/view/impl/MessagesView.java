package net.sourceforge.pebble.web.view.impl;

import net.sourceforge.pebble.web.view.HtmlView;

/**
 * Represents the messages page.
 *
 * @author    Simon Brown
 */
public class MessagesView extends HtmlView {

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
    return "/WEB-INF/jsp/viewMessages.jsp";
  }

}
