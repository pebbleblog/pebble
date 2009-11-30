/*
 * Copyright (c) 2003-2006, Simon Brown
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
import net.sourceforge.pebble.web.view.impl.UserAgentsView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Gets the user agent information for the specified time period.
 *
 * @author    Simon Brown
 */
public class ViewUserAgentsAction extends AbstractLogAction {

  private static final String MSIE_50 = "MSIE 5.0";
  private static final String MSIE_60 = "MSIE 6.0";
  private static final String MSIE_70 = "MSIE 7.0";
  private static final String MSIE_80 = "MSIE 8.0";
  private static final String MSIE_90 = "MSIE 9.0";
  private static final String FIREFOX_1X = "Firefox/1.";
  private static final String FIREFOX_2X = "Firefox/2.";
  private static final String FIREFOX_30 = "Firefox/3.0";
  private static final String FIREFOX_35 = "Firefox/3.5";
  private static final String FIREFOX_36 = "Firefox/3.6";
  private static final String SAFARI = "Safari";
  private static final String BLOGLINES = "Bloglines";
  private static final String GOOGLEBOT = "Googlebot";
  private static final String GOOGLE_FEEDFETCHER = "Feedfetcher-Google";
  private static final String OTHER = "Other";

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

    Map<String, Integer> userAgents = new TreeMap<String, Integer>(new Comparator<String>() {
      public int compare(String s1, String s2) {
        return s1 != null ? s1.compareToIgnoreCase(s2) : -1;
      }
    });

    Map<String, Integer> consolidatedUserAgents = new TreeMap<String, Integer>(new Comparator<String>() {
      public int compare(String s1, String s2) {
        return s1 != null ? s1.compareToIgnoreCase(s2) : -1;
      }
    });

    for (LogEntry logEntry : log.getLogEntries()) {
      String userAgent = logEntry.getAgent();
      if (userAgent == null) {
        userAgent = "";
      }

      Integer count = userAgents.get(userAgent);
      if (count == null) {
        count = 0;
      }
      count = count+1;
      userAgents.put(userAgent, count);

      String consolidatedUserAgent = OTHER;
      if (userAgent.contains(MSIE_50)) {
        consolidatedUserAgent = MSIE_50;
      } else if (userAgent.contains(MSIE_60)) {
        consolidatedUserAgent = MSIE_60;
      } else if (userAgent.contains(MSIE_70)) {
        consolidatedUserAgent = MSIE_70;
      } else if (userAgent.contains(MSIE_80)) {
        consolidatedUserAgent = MSIE_70;
      } else if (userAgent.contains(MSIE_90)) {
        consolidatedUserAgent = MSIE_70;
      } else if (userAgent.contains(FIREFOX_1X)) {
        consolidatedUserAgent = FIREFOX_1X;
      } else if (userAgent.contains(FIREFOX_2X)) {
        consolidatedUserAgent = FIREFOX_2X;
      } else if (userAgent.contains(FIREFOX_30)) {
        consolidatedUserAgent = FIREFOX_2X;
      } else if (userAgent.contains(FIREFOX_35)) {
        consolidatedUserAgent = FIREFOX_2X;
      } else if (userAgent.contains(FIREFOX_36)) {
        consolidatedUserAgent = FIREFOX_2X;
      } else if (userAgent.contains(SAFARI)) {
        consolidatedUserAgent = SAFARI;
      } else if (userAgent.contains(BLOGLINES)) {
        consolidatedUserAgent = BLOGLINES;
      } else if (userAgent.contains(GOOGLEBOT)) {
        consolidatedUserAgent = GOOGLEBOT;
      } else if (userAgent.contains(GOOGLE_FEEDFETCHER)) {
        consolidatedUserAgent = GOOGLE_FEEDFETCHER;
      }
      Integer consolidatedCount = consolidatedUserAgents.get(consolidatedUserAgent);
      if (consolidatedCount == null) {
        consolidatedCount = 0;
      }
      consolidatedCount = consolidatedCount+1;
      consolidatedUserAgents.put(consolidatedUserAgent, consolidatedCount);
    }

    getModel().put("logAction", "viewUserAgents");
    getModel().put("userAgents", userAgents);
    getModel().put("consolidatedUserAgents", consolidatedUserAgents);

    return new UserAgentsView();
  }

}
