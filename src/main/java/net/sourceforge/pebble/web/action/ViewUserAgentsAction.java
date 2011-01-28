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
import net.sourceforge.pebble.logging.UserAgentConsolidator;
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

      String consolidatedUserAgent = UserAgentConsolidator.consolidate(userAgent);
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
