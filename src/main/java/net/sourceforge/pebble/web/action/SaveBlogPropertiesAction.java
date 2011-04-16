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
import net.sourceforge.pebble.web.view.ForwardView;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

/**
 * Saves the properties associated with the current Blog.
 *
 * @author    Simon Brown
 */
@RequireSecurityToken
public class SaveBlogPropertiesAction extends SecureAction {

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);

    String submit = request.getParameter("submit");
    if (submit != null && submit.length() > 0) {
      String currentTimeZone = blog.getProperty(Blog.TIMEZONE_KEY);

      Enumeration params = request.getParameterNames();
      while (params.hasMoreElements()) {
        String key = (String)params.nextElement();
        String value = request.getParameter(key);

        if (key.equals("submit")) {
          // this is the parameter representing the submit button - do nothing
        } else {
          // this is an existing parameter - save or remove it
          if (value == null || value.length() == 0) {
            blog.removeProperty(key);
          } else {
            blog.setProperty(key, value);
          }
        }
      }

      // and now the checkboxes
      String name = "richTextEditorForBlogEntriesEnabled";
      String checkbox = request.getParameter(name);
      if (checkbox == null) {
        blog.setProperty(name, "false");
      }
      name = "richTextEditorForStaticPagesEnabled";
      checkbox = request.getParameter(name);
      if (checkbox == null) {
        blog.setProperty(name, "false");
      }
      name = "richTextEditorForCommentsEnabled";
      checkbox = request.getParameter(name);
      if (checkbox == null) {
        blog.setProperty(name, "false");
      }
      name = "gravatarSupportForCommentsEnabled";
      checkbox = request.getParameter(name);
      if (checkbox == null) {
        blog.setProperty(name, "false");
      }

      try {
        blog.storeProperties();
        blog.info("Blog properties saved.");
      } catch (BlogServiceException e) {
        throw new ServletException(e);
      }

      // if the following properties have changed, reload the blog
      //  - timezone
      String newTimeZone = blog.getProperty(Blog.TIMEZONE_KEY);

      if (!currentTimeZone.equals(newTimeZone)) {
        return new ForwardView("/reindexBlog.secureaction");
      }

    }

    return new RedirectView(blog.getUrl() + "viewBlogProperties.secureaction");
  }

  /**
   * Gets a list of all roles that are allowed to access this action.
   *
   * @return  an array of Strings representing role names
   * @param request
   */
  public String[] getRoles(HttpServletRequest request) {
    return new String[]{
        Constants.BLOG_ADMIN_ROLE,
        Constants.BLOG_OWNER_ROLE
    };
  }

}