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
import net.sourceforge.pebble.web.view.impl.BlogEntriesByDayView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;

/**
 * Finds all blog entries for a particular day, ready for them to be displayed.
 *
 * @author    Simon Brown
 */
public class ViewDayAction extends Action {

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)request.getAttribute(Constants.BLOG_KEY);
    String year = request.getParameter("year");
    String month = request.getParameter("month");
    String day = request.getParameter("day");

    Day daily;
    if (year != null && year.length() > 0 &&
        month != null && month.length() > 0 &&
        day != null && day.length() > 0) {
      daily = blog.getBlogForDay(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    } else {
      daily = blog.getBlogForToday();
    }

    BlogService service = new BlogService();
    List<BlogEntry> blogEntries;
    try {
      blogEntries = service.getBlogEntries(blog, Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
    } catch (BlogServiceException e) {
      throw new ServletException(e);
    }

    getModel().put(Constants.MONTHLY_BLOG, daily.getMonth());
    getModel().put(Constants.DAILY_BLOG, daily);
    getModel().put(Constants.BLOG_ENTRIES, filter(blog, blogEntries));
    getModel().put("displayMode", "day");

    // put the previous and next days in the model for navigation purposes
    Day firstDay = blog.getBlogForFirstMonth().getBlogForFirstDay();
    Day previousDay = daily.getPreviousDay();
    Day nextDay = daily.getNextDay();

    if (!previousDay.before(firstDay)) {
      getModel().put("previousDay", previousDay);
    }

    if (!nextDay.getDate().after(blog.getCalendar().getTime()) || nextDay.before(firstDay)) {
      getModel().put("nextDay", nextDay);
    }

    return new BlogEntriesByDayView();
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