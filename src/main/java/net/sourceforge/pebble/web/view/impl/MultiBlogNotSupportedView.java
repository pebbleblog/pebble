package net.sourceforge.pebble.web.view.impl;

import net.sourceforge.pebble.web.view.HtmlView;

import javax.servlet.http.HttpServletResponse;

/**
 * Returned when an action that's only intended for a single blog is called in a multi blog context
 * @author James Roper
 */
public class MultiBlogNotSupportedView extends HtmlView {

  /**
   * Gets the title of this view.
   *
   * @return the title as a String
   */
  public String getTitle() {
    return null;
  }

  protected int getStatus() {
    return HttpServletResponse.SC_NOT_FOUND;
  }

  /**
   * Gets the URI that this view represents.
   *
   * @return the URI as a String
   */
  public String getUri() {
    return "/WEB-INF/jsp/multiblogNotSupported.jsp";
  }
}
