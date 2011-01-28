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
import java.util.*;

/**
 * Interface that all loggers implement.
 *
 * @author    Simon Brown
 */
public abstract class AbstractLogger {

  /** the blog that this instance is associated with, and logging for */
  protected Blog blog;

  /**
   * Creates a new log associated with the given blog.
   *
   * @param blog    a Blog instance
   */
  public AbstractLogger(Blog blog) {
    this.blog = blog;
  }

  /**
   * Logs a HTTP request.
   *
   * @param request   a HttpServletRequest
   */
  public abstract void log(HttpServletRequest request, int status);

  /**
   * Called to start this logger.
   */
  public abstract void start();

  /**
   * Called to stop this logger.
   */
  public abstract void stop();

  /**
   * Gets a copy of the log file for a given year, month and day.
   *
   * @param year    the year to get entries for
   * @param month   the month to get entries for
   * @param day     the day to get entries for
   * @return    a String containing the contents of the requested log file
   */
  public abstract String getLogFile(int year, int month, int day);

  /**
   * Gets a copy of the log file for today.
   *
   * @return    a String containing the contents of the requested log file
   */
  public String getLogFile() {
    Calendar cal = blog.getCalendar();
    return getLogFile(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
  }

  /**
   * Gets a copy of the log file for a given year and month.
   *
   * @param year    the year to get entries for
   * @param month   the month to get entries for
   * @return    a String containing the contents of the requested log file
   */
  public String getLogFile(int year, int month) {
    StringBuffer buf = new StringBuffer();
    Calendar cal = blog.getCalendar();
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.MONTH, month-1);
    for (int day = 1; day <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
      buf.append(getLogFile(year, month, day));
    }

    return buf.toString();
  }

  /**
   * Gets the log for a given year, month and day.
   *
   * @param year    the year to get entries for
   * @param month   the month to get entries for
   * @param day     the day to get entries for
   * @return    a Log object
   */
  public abstract Log getLog(int year, int month, int day);

  /**
   * Gets the log for today.
   *
   * @return    a Log object
   */
  public Log getLog() {
    Calendar cal = blog.getCalendar();
    return getLog(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
  }

  /**
   * Gets the log for a given year and month.
   *
   * @param year    the year to get entries for
   * @param month   the month to get entries for
   * @return    a Log object
   */
  public Log getLog(int year, int month) {
    Collection logEntries = new HashSet();
    Calendar cal = blog.getCalendar();
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.MONTH, month-1);
    for (int day = 1; day <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
      logEntries.addAll(getLog(year, month, day).getLogEntries());
    }

    return new Log(blog, logEntries);
  }

  /**
   * Gets the log summary information for the given year, month and day.
   *
   * @param year    the year to get entries for
   * @param month   the month to get entries for
   * @param day     the day to get entries for
   * @return    a LogSummary object
   */
  public abstract LogSummary getLogSummary(int year, int month, int day);

  /**
   * Gets the log summary for today.
   *
   * @return    a LogSummary object
   */
  public LogSummary getLogSummary() {
    Calendar cal = blog.getCalendar();
    return getLogSummary(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH));
  }

  /**
   * Gets the log summary information for the given year and month.
   *
   * @param year  the year to get entries for
   * @param month the month to get entries for
   * @return a LogSummary object
   */
  public LogSummary getLogSummary(int year, int month) {
    Calendar cal = blog.getCalendar();
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.MONTH, month-1);

    List logSummaries = new ArrayList();
    for (int day = 1; day <= cal.getActualMaximum(Calendar.DAY_OF_MONTH); day++) {
      logSummaries.add(getLogSummary(year, month, day));
    }

    return new LogSummaryContainer(blog, cal.getTime(), logSummaries);
  }

  /**
   * Gets the log summary information for the given year.
   *
   * @param year  the year to get entries for
   * @return a LogSummary object
   */
  public LogSummary getLogSummary(int year) {
    Calendar cal = blog.getCalendar();
    cal.set(Calendar.YEAR, year);

    List logSummaries = new ArrayList();
    for (int month = 1; month <= 12; month++) {
      logSummaries.add(getLogSummary(year, month));
    }

    return new LogSummaryContainer(blog, cal.getTime(), logSummaries);
  }

}
