/*
 * Copyright (c) 2003-2011, Simon Brown
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 *   - Neither the name of Pebble nor the names of its contributors may
 *     be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.trackback.TrackBackTokenManager;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.TrackBackResponseView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Adds a TrackBack to an existing blog entry.
 *
 * @author    Simon Brown
 */
public class AddTrackBackAction extends Action {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(AddTrackBackAction.class);

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    try {
      Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
      BlogEntry blogEntry = null;

      String entry = request.getParameter("entry");
      String token = request.getParameter("token");
      String title = request.getParameter("title");
      String excerpt = request.getParameter("excerpt");
      String url = request.getParameter("url");
      String blogName = request.getParameter("blog_name");
      String ipAddress = request.getRemoteAddr();

      if (url == null || url.length() == 0) {
        getModel().put("errorCode", new Integer(1));
        getModel().put("message", "The URL (permalink) must be specified for TrackBacks");
        return new TrackBackResponseView();
      } else if (!TrackBackTokenManager.getInstance().isValid(token)) {
        getModel().put("errorCode", new Integer(1));
        getModel().put("message", "The token has expired or is invalid");
        return new TrackBackResponseView();
      } else {
        BlogService service = new BlogService();
        blogEntry = service.getBlogEntry(blog, entry);

        // only add the TrackBack if they are enabled for the entry
        if (blogEntry.isTrackBacksEnabled()) {
          TrackBack trackBack = blogEntry.createTrackBack(title, excerpt, url, blogName, ipAddress);
          blogEntry.addTrackBack(trackBack);
          service.putBlogEntry(blogEntry);
          TrackBackTokenManager.getInstance().expire(token);

          getModel().put("errorCode", new Integer(0));
          return new TrackBackResponseView();
        } else {
          getModel().put("errorCode", new Integer(1));
          getModel().put("message", "TrackBacks are not enabled for this blog entry");
          return new TrackBackResponseView();
        }
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
      e.printStackTrace();
      throw new ServletException(e);
    }
  }

}