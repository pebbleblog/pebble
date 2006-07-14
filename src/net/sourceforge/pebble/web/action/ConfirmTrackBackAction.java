package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.confirmation.TrackBackConfirmationStrategy;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.web.view.NotFoundView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.ConfirmTrackBackView;
import net.sourceforge.pebble.web.view.impl.TrackBackLinkView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Confirms a TrackBack.
 *
 * @author    Simon Brown
 */
public class ConfirmTrackBackAction extends AbstractTrackBackAction {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(ConfirmTrackBackAction.class);

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    BlogEntry blogEntry;

    String entry = (String)request.getSession().getAttribute(BLOG_ENTRY_ID);
    if (entry == null) {
      return new NotFoundView();
    }

    BlogService service = new BlogService();
    try {
      blogEntry = service.getBlogEntry(blog, entry);
    } catch (BlogServiceException e) {
      throw new ServletException(e);
    }
    if (blogEntry == null) {
      // just send back a 404 - this is probably somebody looking for a way
      // to send comment spam ;-)
      return new NotFoundView();
    } else if (!blogEntry.isTrackBacksEnabled()) {
      return new NotFoundView();
    }
    getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);

    TrackBackConfirmationStrategy strategy = blog.getTrackBackConfirmationStrategy();

    if (strategy.isConfirmed(request)) {
      generateTrackBackLink(blogEntry);
      request.getSession().removeAttribute(BLOG_ENTRY_ID);
      return new TrackBackLinkView();
    } else {
      // try again!
      strategy.setupConfirmation(request);
      return new ConfirmTrackBackView();
    }
  }

}
