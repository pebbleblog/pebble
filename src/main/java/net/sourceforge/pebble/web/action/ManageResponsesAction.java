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
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.web.security.RequireSecurityToken;
import net.sourceforge.pebble.web.view.ForwardView;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Allows the user to manage recently added responses.
 *
 * @author    Simon Brown
 */
@RequireSecurityToken
public class ManageResponsesAction extends SecureAction {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(ManageResponsesAction.class);

  /** constant used to represent a comment */
  private static final String COMMENT = "c";

  /** constant used to represent a TrackBack */
  private static final String TRACKBACK = "t";

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    String ids[] = request.getParameterValues("response");
    String submit = request.getParameter("submit");
    BlogService service = new BlogService();

    if (ids != null && submit != null) {
      for (String id : ids) {
        Response ber = getBlogEntryResponse(blog, id);

        if (ber == null) {
          continue;
        }

        if (submit.equalsIgnoreCase("Approve")) {
          try {
            ber.setApproved();
            service.putBlogEntry(ber.getBlogEntry());
            blog.info("Response <a href=\"" + ber.getPermalink() + "\">" + ber.getTitle() + " (" + ber.getSourceName() + ")</a> approved.");
          } catch (BlogServiceException be) {
            log.error("Error updating state for response", be);
          }
        } else if (submit.equalsIgnoreCase("Reject")) {
          try {
            ber.setRejected();
            service.putBlogEntry(ber.getBlogEntry());
            blog.info("Response \"" + ber.getTitle() + "\" (" + ber.getSourceName() + ") rejected.");
          } catch (BlogServiceException be) {
            log.error("Error updating state for response", be);
          }
        } else if (submit.equalsIgnoreCase("Remove")) {
          try {
            ber.getBlogEntry().removeResponse(ber);
            service.putBlogEntry(ber.getBlogEntry());
            blog.info("Response \"" + ber.getTitle() + "\" (" + ber.getSourceName() + ") removed.");
          } catch (BlogServiceException be) {
            log.error("Error updating state for response", be);
          }
        }

      }
    }

    String redirectUrl = request.getParameter("redirectUrl");
    if (redirectUrl != null && redirectUrl.trim().length() > 0) {
      return new RedirectView(redirectUrl);
    } else {
      return new ForwardView("/viewResponses.secureaction");
    }
  }

  /**
   * Gets a list of all roles that are allowed to access this action.
   *
   * @return  an array of Strings representing role names
   * @param request
   */
  public String[] getRoles(HttpServletRequest request) {
    return new String[]{Constants.BLOG_CONTRIBUTOR_ROLE};
  }

  private Response getBlogEntryResponse(Blog blog, String guid) throws ServletException {
    // response key is of the form type/blogEntryId/responseId
    String type = guid.substring(0, 1);
    String blogEntryId = guid.substring(2, guid.indexOf("/", 2));
    String responseId = guid.substring(guid.lastIndexOf("/")+1);

    BlogService service = new BlogService();
    BlogEntry blogEntry = null;
    try {
      blogEntry = service.getBlogEntry(blog, blogEntryId);
    } catch (BlogServiceException e) {
      throw new ServletException(e);
    }
    if (blogEntry == null) {
      return null;
    }

    try {
      if (type != null && type.equalsIgnoreCase(COMMENT)) {
        return blogEntry.getComment(Long.parseLong(responseId));
      } else if (type != null && type.equalsIgnoreCase(TRACKBACK)) {
        return blogEntry.getTrackBack(Long.parseLong(responseId));
      }
    } catch (NumberFormatException e) {
      log.warn(e);
    }

    return null;
  }

}