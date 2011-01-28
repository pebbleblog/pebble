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
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.util.Pageable;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.BlogEntriesView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Views blog entries page by page. The page size is the same as the "number of
 * blog entries shown on the home page".
 *
 * @author    Simon Brown
 */
public class ViewBlogEntriesByPageAction extends Action {

  /**
   * Peforms the processing associated with this action.
   *
   * @param request     the HttpServletRequest instance.
   * @param response    the HttpServletResponse instance.
   * @return            the next View
   */
  public View process(HttpServletRequest request,
                      HttpServletResponse response)
      throws ServletException {

    AbstractBlog abstractBlog = (AbstractBlog)getModel().get(Constants.BLOG_KEY);

    if (abstractBlog instanceof MultiBlog) {
      List publicBlogs = BlogManager.getInstance().getPublicBlogs();
      if (publicBlogs.size() == 1) {
        Blog blog = (Blog)publicBlogs.get(0);
        return new RedirectView(blog.getUrl());
      } else {
        getModel().put(Constants.BLOG_ENTRIES, abstractBlog.getRecentBlogEntries());

        return new BlogEntriesView();
      }
    } else {
      Blog blog = (Blog)abstractBlog;
      int page = 1;
      try {
        page = Integer.parseInt(request.getParameter("page"));
      } catch (NumberFormatException nfe) {
        page = 1;
      }
      boolean publishedOnly = true;
      if (SecurityUtils.isUserAuthorisedForBlog(blog)) {
        publishedOnly = false;
      }

      getModel().put(Constants.MONTHLY_BLOG, blog.getBlogForThisMonth());
      getModel().put("displayMode", "page");
      getModel().put("page", page);

      Pageable<String> pageable;
      if (publishedOnly) {
        pageable = new Pageable<String>(blog.getBlogEntryIndex().getPublishedBlogEntries());
      } else {
        pageable = new Pageable<String>(blog.getBlogEntryIndex().getBlogEntries());
      }

      pageable.setPageSize(blog.getRecentBlogEntriesOnHomePage());
      pageable.setPage(page);
      List<String> blogEntryIds = pageable.getListForPage();
      List<BlogEntry> blogEntries = blog.getBlogEntries(blogEntryIds);

      getModel().put(Constants.BLOG_ENTRIES, blogEntries);
      getModel().put("pageable", pageable);

      return new BlogEntriesView();
    }
  }

}
