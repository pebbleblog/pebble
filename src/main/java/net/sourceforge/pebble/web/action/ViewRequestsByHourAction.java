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
import net.sourceforge.pebble.logging.Log;
import net.sourceforge.pebble.logging.LogEntry;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.RequestsByHourView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

/**
 * Gets the a breakdown of the requests for each hour of the day.
 *
 * @author    Simon Brown
 */
public class ViewRequestsByHourAction extends AbstractLogAction {

  protected Log getLog(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    return super.getLog(request, response);    //To change body of overridden methods use File | Settings | File Templates.
  }

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    Log log = getLog(request, response);

    // work out requests per hour
    int[] requestsPerHour = new int[24];
    Set<String>[] uniqueIpsPerHourAsSet = new Set[24];
    for (int hour = 0; hour < 24; hour++) {
      requestsPerHour[hour] = 0;
      uniqueIpsPerHourAsSet[hour] = new HashSet<String>();
    }
    for (LogEntry logEntry : log.getLogEntries()) {
      Calendar logTime = blog.getCalendar();
      logTime.setTime(logEntry.getDate());
      int hour = logTime.get(Calendar.HOUR_OF_DAY);
      requestsPerHour[hour] = requestsPerHour[hour]+1;
      uniqueIpsPerHourAsSet[hour].add(logEntry.getHost());

      if (logEntry.getRequestUri() != null &&
          logEntry.getRequestUri().indexOf("rss.xml") > -1 ||
          logEntry.getRequestUri().indexOf("feed.xml") > -1 ||
          logEntry.getRequestUri().indexOf("feed.action") > -1 ||
          logEntry.getRequestUri().indexOf("rdf.xml") > -1 ||
          logEntry.getRequestUri().indexOf("atom.xml") > -1) {
      }
    }

    int[] uniqueIpsPerHour = new int[24];
    for (int hour = 0; hour < 24; hour++) {
      uniqueIpsPerHour[hour] = uniqueIpsPerHourAsSet[hour].size();
    }

    getModel().put("logAction", "viewRequestsByHour");
    getModel().put("totalRequests", log.getTotalLogEntries());
    getModel().put("requestsPerHour", requestsPerHour);
    getModel().put("uniqueIpsPerHour", uniqueIpsPerHour);

    return new RequestsByHourView();
  }

}
