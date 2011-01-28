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
import net.sourceforge.pebble.comparator.BlogEntryComparator;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.service.LastModifiedService;
import net.sourceforge.pebble.web.view.NotModifiedView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.AbstractRomeFeedView;
import net.sourceforge.pebble.web.view.impl.FeedView;
import net.sourceforge.pebble.web.view.impl.RdfView;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Gets the RSS for a blog.
 *
 * @author Simon Brown
 */
public class FeedAction extends Action {

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
    AbstractBlog blog = (AbstractBlog) getModel().get(Constants.BLOG_KEY);
    String flavor = request.getParameter("flavor");

    if (lastModifiedService.checkAndProcessLastModified(request, response, blog.getLastModified(), null)) {
      return new NotModifiedView();
    }

    List<BlogEntry> blogEntries;
    String s = request.getParameter("includeAggregatedContent");
    boolean includeAggregatedContent = (s == null || s.equalsIgnoreCase("true"));

    if (blog instanceof Blog) {
      Tag tag = getTag((Blog) blog, request);
      Category category = getCategory((Blog) blog, request);
      String author = getAuthor(request);

      if (tag != null) {
        blogEntries = ((Blog) blog).getRecentPublishedBlogEntries(tag);
        getModel().put("tag", tag);
      } else if (category != null) {
        blogEntries = ((Blog) blog).getRecentPublishedBlogEntries(category);
        getModel().put("category", category);
      } else if (author != null) {
        blogEntries = ((Blog) blog).getRecentPublishedBlogEntries(author);
        getModel().put("author", author);
      } else {
        blogEntries = ((Blog) blog).getRecentPublishedBlogEntries();
      }
    } else {
      blogEntries = blog.getRecentBlogEntries();
    }

    List<BlogEntry> blogEntriesForFeed = new ArrayList<BlogEntry>();
    for (BlogEntry entry : blogEntries) {
      if (includeAggregatedContent || !entry.isAggregated()) {
        blogEntriesForFeed.add(entry);
      }
    }

    Collections.sort(blogEntriesForFeed, new BlogEntryComparator());

    getModel().put(Constants.BLOG_ENTRIES, blogEntriesForFeed);

    // set the locale of this feed request to be English
    javax.servlet.jsp.jstl.core.Config.set(
        request,
        javax.servlet.jsp.jstl.core.Config.FMT_LOCALE,
        Locale.ENGLISH);

    if (flavor != null && flavor.equalsIgnoreCase("atom")) {
      return new FeedView(AbstractRomeFeedView.FeedType.ATOM);
    } else if (flavor != null && flavor.equalsIgnoreCase("rdf")) {
      return new RdfView();
    } else {
      return new FeedView(AbstractRomeFeedView.FeedType.RSS);
    }
  }

  /**
   * Helper method to find a named tag from a request parameter.
   *
   * @param blog    the blog for which the feed is for
   * @param request the HTTP request containing the tag parameter
   * @return a Tag instance, or null if the tag isn't
   *         specified or can't be found
   */
  private Tag getTag(Blog blog, HttpServletRequest request) {
    String tag = request.getParameter("tag");
    if (tag != null) {
      return new Tag(tag, blog);
    } else {
      return null;
    }
  }

  /**
   * Helper method to find a named category from a request parameter.
   *
   * @param blog    the blog for which the feed is for
   * @param request the HTTP request containing the category parameter
   * @return a Category instance, or null if the category isn't
   *         specified or can't be found
   */
  private Category getCategory(Blog blog, HttpServletRequest request) {
    String categoryId = request.getParameter("category");
    if (categoryId != null) {
      return blog.getCategory(categoryId);
    } else {
      return null;
    }
  }

  /**
   * Helper method to find a named author from a request parameter.
   *
   * @param request the HTTP request containing the tag parameter
   * @return a String username, or null if the author isn't specified
   */
  private String getAuthor(HttpServletRequest request) {
    return request.getParameter("author");
  }

  public void setLastModifiedService(LastModifiedService lastModifiedService) {
    this.lastModifiedService = lastModifiedService;
  }
}
