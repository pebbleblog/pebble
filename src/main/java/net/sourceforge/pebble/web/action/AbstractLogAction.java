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
import net.sourceforge.pebble.domain.Month;
import net.sourceforge.pebble.domain.Day;
import net.sourceforge.pebble.logging.Log;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Superclass for all log related actions.
 *
 * @author    Simon Brown
 */
public abstract class AbstractLogAction extends SecureAction {

  protected Log getLog(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);

    String yearAsString = request.getParameter("year");
    String monthAsString = request.getParameter("month");
    String dayAsString = request.getParameter("day");

    Calendar cal = blog.getCalendar();
    Log log = null;
    String logPeriod = "";

    if (yearAsString != null && yearAsString.length() > 0 &&
        monthAsString != null && monthAsString.length() > 0 &&
        dayAsString != null && dayAsString.length() > 0) {
      int year = Integer.parseInt(yearAsString);
      int month = Integer.parseInt(monthAsString);
      int day = Integer.parseInt(dayAsString);
      cal.set(Calendar.YEAR, year);
      cal.set(Calendar.MONTH, month-1);
      cal.set(Calendar.DAY_OF_MONTH, day);
      log = blog.getLogger().getLog(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", blog.getLocale());
      dateFormat.setTimeZone(blog.getTimeZone());
      registerObjectsForNavigation(blog, blog.getBlogForDay(year, month, day));
      logPeriod = dateFormat.format(cal.getTime());
    } else if (yearAsString != null && yearAsString.length() > 0 &&
          monthAsString != null && monthAsString.length() > 0) {
      int year = Integer.parseInt(yearAsString);
      int month = Integer.parseInt(monthAsString);
      cal.set(Calendar.YEAR, year);
      cal.set(Calendar.MONTH, month-1);
      log = blog.getLogger().getLog(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1);
      SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", blog.getLocale());
      dateFormat.setTimeZone(blog.getTimeZone());
      registerObjectsForNavigation(blog, blog.getBlogForMonth(year, month));
      logPeriod = dateFormat.format(cal.getTime());
    } else {
      // get the log for today
      log = blog.getLogger().getLog();
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", blog.getLocale());
      dateFormat.setTimeZone(blog.getTimeZone());
      registerObjectsForNavigation(blog, blog.getBlogForToday());
      logPeriod = dateFormat.format(cal.getTime());
    }

    getModel().put("logPeriod", logPeriod);

    return log;
  }

  protected String getLogFile(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);

    String yearAsString = request.getParameter("year");
    String monthAsString = request.getParameter("month");
    String dayAsString = request.getParameter("day");

    Calendar cal = blog.getCalendar();
    String log = null;
    String logPeriod = "";

    if (yearAsString != null && yearAsString.length() > 0 &&
        monthAsString != null && monthAsString.length() > 0 &&
        dayAsString != null && dayAsString.length() > 0) {
      int year = Integer.parseInt(yearAsString);
      int month = Integer.parseInt(monthAsString);
      int day = Integer.parseInt(dayAsString);
      cal.set(Calendar.YEAR, year);
      cal.set(Calendar.MONTH, month-1);
      cal.set(Calendar.DAY_OF_MONTH, day);
      log = blog.getLogger().getLogFile(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", blog.getLocale());
      dateFormat.setTimeZone(blog.getTimeZone());
      registerObjectsForNavigation(blog, blog.getBlogForDay(year, month, day));
      logPeriod = dateFormat.format(cal.getTime());
    } else if (yearAsString != null && yearAsString.length() > 0 &&
          monthAsString != null && monthAsString.length() > 0) {
      int year = Integer.parseInt(yearAsString);
      int month = Integer.parseInt(monthAsString);
      cal.set(Calendar.YEAR, year);
      cal.set(Calendar.MONTH, month-1);
      log = blog.getLogger().getLogFile(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1);
      SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM yyyy", blog.getLocale());
      dateFormat.setTimeZone(blog.getTimeZone());
      registerObjectsForNavigation(blog, blog.getBlogForMonth(year, month));
      logPeriod = dateFormat.format(cal.getTime());
    } else {
      // get the log for today
      log = blog.getLogger().getLogFile();
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy", blog.getLocale());
      dateFormat.setTimeZone(blog.getTimeZone());
      registerObjectsForNavigation(blog, blog.getBlogForToday());
      logPeriod = dateFormat.format(cal.getTime());
    }

    getModel().put("logPeriod", logPeriod);

    return log;
  }

  private void registerObjectsForNavigation(Blog blog, Month month) {
    Month firstMonth = blog.getBlogForFirstMonth();
    Month previousMonth = month.getPreviousMonth();
    Month nextMonth = month.getNextMonth();

    if (!previousMonth.before(firstMonth)) {
      getModel().put("previousMonth", previousMonth);
    }

    if (!nextMonth.getDate().after(blog.getCalendar().getTime()) || nextMonth.before(firstMonth)) {
      getModel().put("nextMonth", nextMonth);
    }
    getModel().put("displayMode", "logSummaryForMonth");
  }

  private void registerObjectsForNavigation(Blog blog, Day day) {
    Day firstDay = blog.getBlogForFirstMonth().getBlogForFirstDay();
    Day previousDay = day.getPreviousDay();
    Day nextDay = day.getNextDay();

    if (!previousDay.before(firstDay)) {
      getModel().put("previousDay", previousDay);
    }

    if (!nextDay.getDate().after(blog.getCalendar().getTime()) || nextDay.before(firstDay)) {
      getModel().put("nextDay", nextDay);
    }
    getModel().put("displayMode", "logSummaryForDay");
  }

  /**
   * Gets a list of all roles that are allowed to access this action.
   *
   * @return  an array of Strings representing role names
   * @param request
   */
  public String[] getRoles(HttpServletRequest request) {
    return new String[]{Constants.BLOG_ADMIN_ROLE, Constants.BLOG_OWNER_ROLE, Constants.BLOG_PUBLISHER_ROLE, Constants.BLOG_CONTRIBUTOR_ROLE};
  }

}