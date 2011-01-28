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
import net.sourceforge.pebble.domain.BlogServiceException;
import net.sourceforge.pebble.web.security.RequireSecurityToken;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Saves the security properties associated with the current Blog.
 *
 * @author    Simon Brown
 */
@RequireSecurityToken
public class SaveBlogSecurityAction extends SecureAction {

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);

    String privateBlog = request.getParameter("private");
    blog.setProperty(Blog.PRIVATE_KEY, privateBlog);

    String values[] = request.getParameterValues("blogOwners");
    StringBuffer blogOwners = new StringBuffer();
    if (values != null) {
      for (String value : values) {
        blogOwners.append(value);
        blogOwners.append(",");
      }
    }
    blog.setProperty(Blog.BLOG_OWNERS_KEY, blogOwners.toString());

    values = request.getParameterValues("blogPublishers");
    StringBuffer blogPublishers = new StringBuffer();
    if (values != null) {
      for (String value : values) {
        blogPublishers.append(value);
        blogPublishers.append(",");
      }
    }
    blog.setProperty(Blog.BLOG_PUBLISHERS_KEY, blogPublishers.toString());

    values = request.getParameterValues("blogContributors");
    StringBuffer blogContributors = new StringBuffer();
    if (values != null) {
      for (String value : values) {
        blogContributors.append(value);
        blogContributors.append(",");
      }
    }
    blog.setProperty(Blog.BLOG_CONTRIBUTORS_KEY, blogContributors.toString());

    values = request.getParameterValues("blogReaders");
    StringBuffer blogReaders = new StringBuffer();
    if (values != null) {
      for (String value : values) {
        blogReaders.append(value);
        blogReaders.append(",");
      }
    }
    blog.setProperty(Blog.BLOG_READERS_KEY, blogReaders.toString());

    try {
      blog.storeProperties();
      blog.info("Blog security settings saved.");
    } catch (BlogServiceException e) {
      throw new ServletException(e);
    }

    return new RedirectView(blog.getUrl() + "viewBlogSecurity.secureaction");
  }

  /**
   * Gets a list of all roles that are allowed to access this action.
   *
   * @return  an array of Strings representing role names
   * @param request   the originating request
   */
  public String[] getRoles(HttpServletRequest request) {
    return new String[]{
        Constants.BLOG_ADMIN_ROLE,
        Constants.BLOG_OWNER_ROLE
    };
  }

}