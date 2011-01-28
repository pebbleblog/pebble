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
package net.sourceforge.pebble.logging;

import net.sourceforge.pebble.domain.Blog;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Represents a log entry in the combined log file format.
 *
 * @author Simon Brown
 */
public class CombinedFormatLogEntryFormat {

  /** the format used for dates */
  SimpleDateFormat dateFormatter = new SimpleDateFormat("[dd/MMM/yyyy:HH:mm:ss Z]");

  /**
   * Default, no args constructor.
   */
  public CombinedFormatLogEntryFormat(Blog blog) {
    dateFormatter.setTimeZone(blog.getTimeZone());
  }

  /**
   * Formats a given entry into the combined log file format.
   *
   * @param entry   a log entry
   * @return  a formatted String
   */
  public String format(LogEntry entry) {
    StringBuffer buf = new StringBuffer();
    if (entry.getHost() != null) {
      buf.append(entry.getHost());
    } else {
      buf.append("-");
    }
    buf.append(" ");
    buf.append("-");
    buf.append(" ");
    buf.append("-");
    buf.append(" ");
    buf.append(dateFormatter.format(entry.getDate()));
    buf.append(" ");
    buf.append("\"" + entry.getRequest() + "\"");
    buf.append(" ");
    buf.append(entry.getStatusCode());
    buf.append(" ");
    buf.append("-");
    buf.append(" ");
    if (entry.getReferer() != null) {
      buf.append("\"" + entry.getReferer() + "\"");
    } else {
      buf.append("-");
    }
    buf.append(" ");
    if (entry.getAgent() != null) {
      buf.append("\"" + entry.getAgent() + "\"");
    } else {
      buf.append("-");
    }

    return buf.toString();
  }

  /**
   * Parses a string in the combined log file format into a log entry.
   *
   * @param s   the String to parse
   * @return  a LogEntry instance
   */
  public LogEntry parse(String s) {
    LogEntry logEntry = new LogEntry();

    // there are 9 tokens in the combined log file format
    int start = 0;
    int end = s.indexOf(" ", start);
    String host = s.substring(start, end);
    start = end + 1;
    end = s.indexOf(" ", start);
    String rfc931 = s.substring(start, end);
    start = end + 1;
    end = s.indexOf(" ", start);
    String authuser = s.substring(start, end);
    start = end + 1;
    end = s.indexOf("]", start);
    String date = s.substring(start, end+1);
    start = end + 2;
    end = s.indexOf("\"", start+1);
    String request = s.substring(start+1, end);
    start = end + 2;
    end = s.indexOf(" ", start);
    String statusCode = s.substring(start, end);
    start = end + 1;
    end = s.indexOf(" ", start);
    String bytes = s.substring(start, end);
    start = end + 1;
    String referer;
    if (s.charAt(start) == '-') {
      referer = "-";
      start = start + 2;
    } else {
      end = s.indexOf("\"", start+1);
      referer = s.substring(start+1, end);
      start = end + 2;
    }
    end = s.length();
    String agent;
    if (s.charAt(start) == '-') {
      agent = "-";
      start = start + 2;
    } else {
      end = s.indexOf("\"", start+1);
      agent = s.substring(start+1, end);
      start = end + 2;
    }

    if (!host.equals("-")) {
      logEntry.setHost(host);
    }

    if (!rfc931.equals("-")) {
      //logEntry.setHost(host);
    }

    if (!authuser.equals("-")) {
      //logEntry.setUser(authuser);
    }

    try {
      logEntry.setDate(dateFormatter.parse(date));
    } catch (ParseException e) {
      // ignore
    }

    logEntry.setRequest(request);

    if (!statusCode.equals("-")) {
      try {
        logEntry.setStatusCode(Integer.parseInt(statusCode));
      } catch (NumberFormatException e) {
        // ignore
      }
    }

    if (!bytes.equals("-")) {
      try {
        logEntry.setBytes(Long.parseLong(bytes));
      } catch (NumberFormatException e) {
        // ignore
      }
    }

    if (!referer.equals("-")) {
      logEntry.setReferer(referer);
    }

    if (!agent.equals("-")) {
      logEntry.setAgent(agent);
    }

    return logEntry;
  }

}


