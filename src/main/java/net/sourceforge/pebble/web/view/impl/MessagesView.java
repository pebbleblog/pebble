package net.sourceforge.pebble.web.view.impl;

import net.sourceforge.pebble.web.view.HtmlView;

import java.util.List;
import java.util.Collections;

/**
 * Represents the messages page.
 *
 * @author    Simon Brown
 */
public class MessagesView extends HtmlView {

  /**
   * Prepares the view for presentation.
   */
  public void prepare() {
    List list = (List)getModel().get("messages");
    Collections.reverse(list);
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
    return "/WEB-INF/jsp/viewMessages.jsp";
  }

}
