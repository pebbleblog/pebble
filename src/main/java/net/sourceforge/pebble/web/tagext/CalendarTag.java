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
package net.sourceforge.pebble.web.tagext;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.Day;
import net.sourceforge.pebble.domain.Month;
import net.sourceforge.pebble.util.I18n;
import net.sourceforge.pebble.util.UrlRewriter;

/**
 * A custom tag that outputs a calendar control.
 *
 * @author    Simon Brown
 */
public class CalendarTag extends TagSupport {

  /**
   * Implementation from the Tag interface - this is called when the opening tag
   * is encountered.
   *
   * @return  an integer specifying what to do afterwards
   * @throws  JspException    if something goes wrong
   */
  public int doStartTag() throws JspException {

    HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
    Blog blog = (Blog)request.getAttribute(Constants.BLOG_KEY);
    Month month = (Month)request.getAttribute(Constants.MONTHLY_BLOG);
    Day today = blog.getBlogForToday();
    Calendar now = blog.getCalendar();

    if (month == null) {
      month = today.getMonth();
    }

    Calendar firstDayOfMonth = blog.getCalendar();
    firstDayOfMonth.setTime(month.getBlogForDay(1).getDate());

    SimpleDateFormat monthAndYearFormatter = new SimpleDateFormat("MMMM yyyy", blog.getLocale());
    monthAndYearFormatter.setTimeZone(blog.getTimeZone());
    SimpleDateFormat monthFormatter = new SimpleDateFormat("MMM", blog.getLocale());
    monthFormatter.setTimeZone(blog.getTimeZone());
    NumberFormat numberFormatter = NumberFormat.getIntegerInstance(blog.getLocale());

    Month firstMonth = blog.getBlogForFirstMonth();

    try {
      JspWriter out = pageContext.getOut();

      out.write("<div class=\"calendar\">");
      out.write("<table width=\"100%\">");
      out.write("<tr>");
      out.write("<td colspan=\"7\" align=\"center\">");
      if (month.before(firstMonth)) {
        out.write("<b>");
        out.write(monthAndYearFormatter.format(month.getDate()));
        out.write("</b>");
      } else {
        out.write("<b><a href=\"");
        out.write(UrlRewriter.doRewrite(month.getPermalink()));
        out.write("\">");
        out.write(monthAndYearFormatter.format(month.getDate()));
        out.write("</a></b>");
      }
      out.write("</td>");
      out.write("</tr>");

      int firstDayOfWeek = now.getFirstDayOfWeek();

      // write out the calendar header
      DateFormatSymbols symbols = new DateFormatSymbols(blog.getLocale());
      String[] days = symbols.getShortWeekdays();
      out.write("<tr>");
      for (int i = firstDayOfWeek; i <= 7; i++) {
        out.write("<td class=\"calendarDayHeader\" width=\"14%\">" + days[i] + "</td>");
      }
      for (int i = 1; i < firstDayOfWeek; i++) {
        out.write("<td class=\"calendarDayHeader\">" + days[i] + "</td>");
      }
      out.write("</tr>");

      // write out the body of the calendar
      Iterator it = getDatesForCompleteWeeks(blog, month).iterator();
      Calendar cal;
      int count = 0;
      while (it.hasNext()) {
        cal = (Calendar)it.next();
        Day daily = blog.getBlogForDay(cal.getTime());

        String formattedNumber = numberFormatter.format(cal.get(Calendar.DAY_OF_MONTH));
        if (formattedNumber.length() == 1) {
          formattedNumber = "&nbsp;" + formattedNumber;
        }

        if (count % 7 == 0) {
          out.write("<tr>");
        }

        // output padding if the date to display isn't in the month
        if (cal.get(Calendar.MONTH) != firstDayOfMonth.get(Calendar.MONTH)) {
          out.write("<td class=\"calendarDay\">&nbsp;");
        } else if (now.get(Calendar.YEAR) == cal.get(Calendar.YEAR) &&
          now.get(Calendar.MONTH) == cal.get(Calendar.MONTH) &&
          now.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {
          out.write("<td class=\"calendarToday\">");
          if (daily.hasBlogEntries()) {
            out.write("&nbsp;<a href=\"" + UrlRewriter.doRewrite(daily.getPermalink()) + "\">" + formattedNumber + "</a>&nbsp;");
          } else {
            out.write("&nbsp;" + formattedNumber + "&nbsp;");
          }
        } else {
          if (daily.hasBlogEntries()) {
            out.write("<td class=\"calendarDayWithEntries\">");
            out.write("&nbsp;<a href=\"" + UrlRewriter.doRewrite(daily.getPermalink()) + "\">" + formattedNumber + "</a>&nbsp;");
          } else {
            out.write("<td class=\"calendarDay\">");
            out.write("&nbsp;" + formattedNumber + "&nbsp;");
          }
        }
        out.write("</td>");

        if (count % 7 == 6) {
          out.write("</tr>");
        }

        count++;
      }

      // write out the footer of the calendar
      Month previous = month.getPreviousMonth();
      Month next = month.getNextMonth();

      out.write("<tr>");
      out.write("<td colspan=\"7\" align=\"center\">");

      // only display the previous month link if there are blog entries
      if (previous.before(firstMonth)) {
        out.write(monthFormatter.format(previous.getDate()));
      } else {
        out.write("<a href=\"" + UrlRewriter.doRewrite(previous.getPermalink()) + "\">" + monthFormatter.format(previous.getDate()) + "</a>");
      }

      String todayText = I18n.getMessage(blog, "common.today");
      out.write("&nbsp; | &nbsp;");
      out.write("<a href=\"" + UrlRewriter.doRewrite(today.getPermalink()) + "\">" + todayText + "</a>");
      out.write("&nbsp; | &nbsp;");

      // only display the next month date if it's not in the future
      if (next.getDate().after(now.getTime()) || next.before(firstMonth)) {
        out.write(monthFormatter.format(next.getDate()));
      } else {
        out.write("<a href=\"" + UrlRewriter.doRewrite(next.getPermalink()) + "\">" + monthFormatter.format(next.getDate()) + "</a>");
      }
      out.write("</td>");
      out.write("</tr>");

      out.write("</table>");
      out.write("</div>");
    } catch (IOException ioe) {
      throw new JspTagException(ioe.getMessage());
    }

    return SKIP_BODY;
  }

  /**
   * Gets a list of dates that should be displayed for the given month. This
   * method adds dates either side of the month, padding the list so that it
   * contains a number of complete weeks. For example, if the first day of the
   * month for the blog's locale is Monday and the given month starts on a
   * Tuesday, this method will add in that previous Monday to present
   * back a complete week. The same happens for the end of the month. This
   * makes rendering easier since we just have a 7xN grid.
   *
   * @param blog    a Blog instance
   * @param month   the month
   * @return  a List of Calendar instances
   */
  private List getDatesForCompleteWeeks(Blog blog, Month month) {
    List dates = new ArrayList();
    Calendar start = blog.getCalendar();
    start.setTime(month.getBlogForDay(1).getDate());
    Calendar end = blog.getCalendar();
    end.setTime(month.getBlogForDay(month.getLastDayInMonth()).getDate());
    Calendar cal;

    // put all days in month into a list
    for (int i = 1; i <= month.getLastDayInMonth(); i++) {
      cal = (Calendar)start.clone();
      cal.set(Calendar.DAY_OF_MONTH, i);
      dates.add(cal);
    }

    // pad out before the start of the month, until the first day of the week
    cal = (Calendar)start.clone();
    while (cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek()) {
      cal.add(Calendar.DATE, -1);
      dates.add(0, cal.clone());
    }

    // pad out after month, until the last day of the week
    cal = (Calendar)end.clone();
    cal.add(Calendar.DATE, 1);
    while (cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek()) {
      dates.add(cal.clone());
      cal.add(Calendar.DATE, 1);
    }

    return dates;
  }

}