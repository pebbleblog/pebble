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
package net.sourceforge.pebble.web.view.impl;

import net.sourceforge.pebble.web.view.PlainTextView;
import net.sourceforge.pebble.logging.Log;
import net.sourceforge.pebble.logging.LogEntry;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.Constants;

import java.text.SimpleDateFormat;

/**
 * Represents the log file for today.
 *
 * @author    Simon Brown
 */
public class LogAsTabDelimitedView extends PlainTextView {

  private static final char SEPARATOR = '\t';

  /**
   * Prepares the view for presentation.
   */
  public void prepare() {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    Log log = (Log)getModel().get("log");
    StringBuffer buf = new StringBuffer();
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss Z");
    sdf.setTimeZone(blog.getTimeZone());

    buf.append("Host");
    buf.append(SEPARATOR);
    buf.append("Date/Time");
    buf.append(SEPARATOR);
    buf.append("Method");
    buf.append(SEPARATOR);
    buf.append("Request URI");
    buf.append(SEPARATOR);
    buf.append("Status");
    buf.append(SEPARATOR);
    buf.append("Referer");
    buf.append(SEPARATOR);
    buf.append("User-Agent");
    buf.append("\n");
    
    if (log != null) {
      for (LogEntry logEntry : log.getLogEntries()) {
        buf.append(logEntry.getHost());
        buf.append(SEPARATOR);
        buf.append(sdf.format(logEntry.getDate()));
        buf.append(SEPARATOR);
        buf.append(logEntry.getRequestMethod());
        buf.append(SEPARATOR);
        buf.append(logEntry.getRequestUri());
        buf.append(SEPARATOR);
        if (logEntry.getStatusCode() != -1) {
          buf.append(logEntry.getStatusCode());
        } else {
          buf.append("");
        }
        buf.append(SEPARATOR);
        if (logEntry.getReferer() != null) {
          buf.append(logEntry.getReferer());
        } else {
          buf.append("");
        }
        buf.append(SEPARATOR);
        buf.append(logEntry.getAgent());
        buf.append("\n");
      }
    }

    getModel().put("text", buf.toString());
  }

  /**
   * Gets the content type of this view.
   *
   * @return the content type as a String
   */
  public String getContentType() {
    return "application/vnd.ms-excel";
  }

}
