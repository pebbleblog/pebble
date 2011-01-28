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

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * A no-op logger that can be used when Pebble logging isn't required, such
 * as when something like Apache provides the necessary logging already.
 *
 * @author    Simon Brown
 */
public class NullLogger extends AbstractLogger {

  /**
   * Creates a new instance associated with the specified blog.
   *                                                 
   * @param blog    a Blog instance
   */
  public NullLogger(Blog blog) {
    super(blog);
  }

  /**
   * Logs a HTTP request.
   *
   * @param request   a HttpServletRequest
   */
  public void log(HttpServletRequest request, int status) {
  }

  /**
   * Called to start this logger.
   */
  public void start() {
  }

  /**
   * Called to stop this logger.
   */
  public void stop() {
  }

  /**
   * Gets a copy of the log file for a given year, month and day.
   *
   * @param year    the year to get entries for
   * @param month   the month to get entries for
   * @param day     the day to get entries for
   * @return    a String containing the contents of the requested log file
   */
  public String getLogFile(int year, int month, int day) {
    return "";
  }

  /**
   * Gets the log for a given year, month and day.
   *
   * @param year    the year to get entries for
   * @param month   the month to get entries for
   * @param day     the day to get entries for
   * @return    a Log object
   */
  public Log getLog(int year, int month, int day) {
    return new Log(blog, new ArrayList());
  }

  /**
   * Gets the log summary information for the given year, month and day.
   *
   * @param year  the year to get entries for
   * @param month the month to get entries for
   * @param day   the day to get entries for
   * @return a LogSummary object
   */
  public LogSummary getLogSummary(int year, int month, int day) {
    Calendar cal = blog.getCalendar();
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.MONTH, month-1);
    cal.set(Calendar.DAY_OF_MONTH, day);
    int totalRequests = 0;

    return new LogSummaryItem(blog, cal.getTime(), totalRequests);
  }

}
