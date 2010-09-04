package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.MessagesView;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.Blog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;

/**
 * Allows the user to clear all messages for the current blog.
 *
 * @author    Simon Brown
 */
public class ClearMessagesAction extends SecureAction {

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    blog.clearMessages();
    getModel().put("messages", blog.getMessages());

    return new MessagesView();
  }

  /**
   * Gets a list of all roles that are allowed to access this action.
   *
   * @return  an array of Strings representing role names
   * @param request
   */
  public String[] getRoles(HttpServletRequest request) {
    return new String[]{Constants.BLOG_ADMIN_ROLE, Constants.BLOG_OWNER_ROLE};
  }

}
