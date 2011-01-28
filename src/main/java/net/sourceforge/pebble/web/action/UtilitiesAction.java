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
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.util.Utilities;
import net.sourceforge.pebble.web.security.RequireSecurityToken;
import net.sourceforge.pebble.web.security.SecurityTokenValidatorCondition;
import net.sourceforge.pebble.web.view.ForwardView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.UtilitiesView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utilities for the current blog, such as those useful for moving
 * between versions of Pebble.
 *
 * @author    Simon Brown
 */
@RequireSecurityToken(UtilitiesAction.UtilitiesCondition.class)
public class UtilitiesAction extends SecureAction {

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    String action = request.getParameter("action");
    if (action == null) {
      // do nothing
    } else if (action.equalsIgnoreCase("ipAddressListener")) {
      Utilities.buildIpAddressLists(blog);
      return new ForwardView("/reloadBlog.secureaction");
    } else if (action.equalsIgnoreCase("fixHtmlInResponses")) {
      Utilities.fixHtmlInResponses(blog);
      return new ForwardView("/reloadBlog.secureaction");
    } else if (action.equalsIgnoreCase("buildIndexes")) {
      Utilities.buildIndexes(blog);
      return new ForwardView("/reloadBlog.secureaction");
    } else if (action.equalsIgnoreCase("convertCategories")) {
      Utilities.convertCategories(blog);
      return new ForwardView("/reloadBlog.secureaction");
    } else if (action.equalsIgnoreCase("restructureBlogToGMT")) {
      Utilities.restructureBlogToGMT(blog);
      Utilities.buildIndexes(blog);
      return new ForwardView("/reloadBlog.secureaction");
    } else if (action.equalsIgnoreCase("moveBlogEntriesFromCategory")) {
      Category from = blog.getCategory(request.getParameter("from"));
      Category to = blog.getCategory(request.getParameter("to"));
      if (from != null && to != null) {
        Utilities.moveBlogEntriesFromCategory(blog, from, to);
      }
    }

    return new UtilitiesView();
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

  public static class UtilitiesCondition implements SecurityTokenValidatorCondition {
    public boolean shouldValidate(HttpServletRequest request) {
      // Only validate if we have an action, so return true if the action is not null
      return request.getParameter("action") != null;
    }
  }
}