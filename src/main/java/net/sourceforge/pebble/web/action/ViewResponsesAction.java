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
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.BlogServiceException;
import net.sourceforge.pebble.util.Pageable;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.ResponsesView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

/**
 * Allows the user to view all recently added responses.
 *
 * @author    Simon Brown
 */
public class ViewResponsesAction extends SecureAction {

  /** the number of responses to show per page */
  static final int PAGE_SIZE = 20;

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    final Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);

    String type = request.getParameter("type");
    if (type == null) {
      type = "approved";
    }

    String pageAsString = request.getParameter("page");
    int page = 1;
    if (pageAsString == null || pageAsString.length() == 0) {
      page = 1;
    } else {
      try {
        page = Integer.parseInt(pageAsString);
      } catch (NumberFormatException nfe) {
      }
    }

    List responses = null;
    if (type.equalsIgnoreCase("pending")) {
      responses = new ArrayList(blog.getPendingResponses());
    } else if (type.equalsIgnoreCase("rejected")) {
      responses = new ArrayList(blog.getRejectedResponses());
    } else {
      responses = new ArrayList(blog.getApprovedResponses());
    }

    Pageable pageable = new Pageable(responses) {
      public List getListForPage() {
        List responses = new ArrayList();
        BlogService service = new BlogService();
        Iterator it = super.getListForPage().iterator();
        while (it.hasNext()) {
          try {
            responses.add(service.getResponse(blog, (String)it.next()));
          } catch (BlogServiceException e) {
            // do nothing - some responses just won't get shown,
            // but a message will be sent to the blog
          }
        }
        return responses;
      }
    };
    pageable.setPageSize(PAGE_SIZE);
    pageable.setPage(page);
    getModel().put("pageable", pageable);

    getModel().put("type", type);

    return new ResponsesView();
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