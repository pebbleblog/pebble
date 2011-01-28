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
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.BlogServiceException;
import net.sourceforge.pebble.web.view.NotFoundView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.TrackBackFormView;
import net.sourceforge.pebble.web.view.impl.TrackBackSentView;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Adds a comment to an existing blog entry.
 *
 * @author    Simon Brown
 */
public class SendTrackBackAction extends SecureAction {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(SendTrackBackAction.class);

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);

    String entryId = request.getParameter("entry");
    String trackBackUrl = request.getParameter("url");
    String excerpt = request.getParameter("excerpt");
    String trackBackResponseMessage;
    Integer trackBackResponseCode;

    BlogService service = new BlogService();
    BlogEntry blogEntry = null;
    if (entryId != null) {
      try {
        blogEntry = service.getBlogEntry(blog, entryId);
      } catch (BlogServiceException e) {
        throw new ServletException(e);
      }
    }

    if (blogEntry == null) {
      // the entry cannot be found - it may have been removed or the
      // requesting URL was wrong

      return new NotFoundView();
    } else if (trackBackUrl == null || trackBackUrl.trim().length() == 0) {
      getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
      return new TrackBackFormView();
    } else {
      // extract the information to send in the trackback
      String title = blogEntry.getTitle();
      String blogName = blogEntry.getBlog().getName();
      String url = blogEntry.getPermalink();

      // now send the trackback
      try {
        HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(trackBackUrl);
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=" + blog.getCharacterEncoding());
        NameValuePair[] data = {
          new NameValuePair("title", title),
          new NameValuePair("url", url),
          new NameValuePair("excerpt", excerpt),
          new NameValuePair("blog_name", blogName)
        };
        postMethod.addParameters(data);

        trackBackResponseCode = new Integer(httpClient.executeMethod(postMethod));
        trackBackResponseMessage = postMethod.getResponseBodyAsString();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
        throw new ServletException(e);
      }

      getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
      getModel().put("trackBackUrl", trackBackUrl);
      getModel().put("trackBackResponseCode", trackBackResponseCode);
      getModel().put("trackBackResponseMessage", trackBackResponseMessage);

      return new TrackBackSentView();
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

}