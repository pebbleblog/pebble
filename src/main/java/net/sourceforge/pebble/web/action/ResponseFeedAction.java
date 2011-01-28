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
import net.sourceforge.pebble.service.LastModifiedService;
import net.sourceforge.pebble.web.view.NotModifiedView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.*;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Gets a feed (RSS or Atom) for blog entry responses.
 *
 * @author Simon Brown
 */
public class ResponseFeedAction extends Action {

  @Inject
  private LastModifiedService lastModifiedService;

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog) getModel().get(Constants.BLOG_KEY);
    String flavor = request.getParameter("flavor");

    if (lastModifiedService.checkAndProcessLastModified(request, response, blog.getLastModified(), null)) {
      return new NotModifiedView();
    }
    List<Response> responses = new ArrayList<Response>();

    String entryId = request.getParameter("entry");
    if (entryId != null) {
      BlogService service = new BlogService();
      BlogEntry blogEntry;
      try {
        blogEntry = service.getBlogEntry(blog, entryId);
      } catch (BlogServiceException e) {
        throw new ServletException(e);
      }
      if (blogEntry != null && blogEntry.isPublished()) {
        getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
        for (Response r : blogEntry.getResponses()) {
          if (r.isApproved()) {
            responses.add(r);
          }
        }
      }
    } else {
      responses = new ArrayList<Response>(blog.getRecentApprovedResponses());
    }

    int numberOfResponses = blog.getRecentResponsesOnHomePage();

    if (responses.size() > numberOfResponses) {
      responses = responses.subList(0, numberOfResponses);
    }

    getModel().put(Constants.RESPONSES, responses);

    if (flavor != null && flavor.equalsIgnoreCase("atom")) {
      return new ResponseFeedView(AbstractRomeFeedView.FeedType.ATOM);
    } else {
      return new ResponseFeedView(AbstractRomeFeedView.FeedType.RSS);
    }
  }
}
