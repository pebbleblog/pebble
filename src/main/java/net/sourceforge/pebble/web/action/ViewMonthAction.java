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
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.NotFoundView;
import net.sourceforge.pebble.web.view.impl.BlogEntriesByMonthView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;

/**
 * Finds all blog entries for a particular month, ready for them
 * to be displayed.
 *
 * @author    Simon Brown
 */
public class ViewMonthAction extends Action {

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    String year = request.getParameter("year");
    String month = request.getParameter("month");

    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    Month monthly;
    if (year != null && year.length() > 0 &&
        month != null && month.length() > 0) {
      monthly = blog.getBlogForMonth(Integer.parseInt(year), Integer.parseInt(month));
    } else {
      return new NotFoundView();
    }

    BlogService service = new BlogService();

    List<BlogEntry> blogEntries = null;
    try {
      blogEntries = service.getBlogEntries(blog, Integer.parseInt(year), Integer.parseInt(month));
    } catch (BlogServiceException e) {
      throw new ServletException(e);
    }
    getModel().put(Constants.BLOG_ENTRIES, filter(blog, blogEntries));
    getModel().put("displayMode", "month");
    getModel().put(Constants.MONTHLY_BLOG, monthly);

    // put the previous and next months in the model for navigation purposes
    Month firstMonth = blog.getBlogForFirstMonth();
    Month previousMonth = monthly.getPreviousMonth();
    Month nextMonth = monthly.getNextMonth();

    if (!previousMonth.before(firstMonth)) {
      getModel().put("previousMonth", previousMonth);
    }

    if (!nextMonth.getDate().after(blog.getCalendar().getTime()) || nextMonth.before(firstMonth)) {
      getModel().put("nextMonth", nextMonth);
    }

    return new BlogEntriesByMonthView();
  }

  private List<BlogEntry> filter(Blog blog, List<BlogEntry> blogEntries) {
    List<BlogEntry> filtered = new ArrayList<BlogEntry>();

    for (BlogEntry blogEntry : blogEntries) {
      if (
          blogEntry.isPublished() ||
          (
            (SecurityUtils.isUserAuthorisedForBlog(blog) && blogEntry.isUnpublished())
          )
         ) {
        filtered.add(blogEntry);
      }
    }

    return filtered;
  }

}