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
import net.sourceforge.pebble.Constants;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Supports the <a href="http://httpd.apache.org/docs/logs.html#combined">Combined Log Format</a>.
 *
 * @author    Simon Brown
 */
public class CombinedLogFormatLogger extends AbstractLogger {

  private static final String REFERER_HEADER = "Referer";
  private static final String USER_AGENT_HEADER = "User-Agent";
  private static final int FLUSH_SIZE = 0;

  /** the format of the log filenames */
  private SimpleDateFormat filenameFormat = new SimpleDateFormat("'blog-'yyyyMMdd'.log'");

  private List entries = new ArrayList();

  public CombinedLogFormatLogger(Blog blog) {
    super(blog);
    filenameFormat.setTimeZone(blog.getTimeZone());
  }

  /**
   * Logs a HTTP request.
   *
   * @param request   a HttpServletRequest
   */
  public synchronized void log(HttpServletRequest request, int status) {
    String externalUri = (String)request.getAttribute(Constants.EXTERNAL_URI);
    LogEntry entry = new LogEntry();
    entry.setHost(request.getRemoteAddr());
    entry.setDate(blog.getCalendar().getTime());
    entry.setStatusCode(status);
    StringBuffer buf = new StringBuffer();
    buf.append(request.getMethod());
    buf.append(" ");
    buf.append(externalUri);
    entry.setRequest(buf.toString());
    entry.setReferer(request.getHeader(REFERER_HEADER));
    entry.setAgent(request.getHeader(USER_AGENT_HEADER));
    entries.add(entry);

    if (entries.size() >= FLUSH_SIZE) {
      flush();
    }
  }

  private void flush() {
    try {
      write(entries);
      entries.clear();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /**
   * Called to start this logger.
   */
  public void start() {
  }

  /**
   * Called to stop this logger.
   */
  public synchronized void stop() {
    flush();
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
    StringBuffer buf = new StringBuffer();
    try {
      // read the file a line at a time, creating a String as we go
      File file = new File(blog.getLogsDirectory(), getFilename(year, month, day));
      if (file.exists()) {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
          buf.append(line);
          buf.append(System.getProperty("line.separator"));
          line = reader.readLine();
        }
        reader.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      return buf.toString();
    }
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
    List logEntries = new ArrayList();
    CombinedFormatLogEntryFormat format = new CombinedFormatLogEntryFormat(blog);

    try {
      // read the file a line at a time, parsing into LogEntry objects
      File file = new File(blog.getLogsDirectory(), getFilename(year, month, day));
      if (file.exists()) {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
          logEntries.add(format.parse(line));
          line = reader.readLine();
        }
        reader.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      return new Log(blog, logEntries);
    }
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

    try {
      // read the file a line at a time
      File file = new File(blog.getLogsDirectory(), getFilename(year, month, day));
      if (file.exists()) {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null) {
          totalRequests++;
          line = reader.readLine();
        }
        reader.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return new LogSummaryItem(blog, cal.getTime(), totalRequests);
  }

  /**
   * Determines the name of the log file.
   *
   * @param year    the year to get entries for
   * @param month   the month to get entries for
   * @param day     the day to get entries for
   * @return    the name of the log file for the given year, month and day
   */
  private String getFilename(int year, int month, int day) {
    Calendar cal = blog.getCalendar();
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.MONTH, month-1);
    cal.set(Calendar.DAY_OF_MONTH, day);

    return filenameFormat.format(cal.getTime());
  }

  /**
   * Writes the given list of entries to the log file, creating it if
   * necessary.
   *
   * @param entries   the list of entries to write
   */
  private void write(List entries) throws IOException {
    CombinedFormatLogEntryFormat format = new CombinedFormatLogEntryFormat(blog);
    File file;
    BufferedWriter writer = null;
    String currentFilename = "";
    String filename;
    Iterator it = entries.iterator();
    while (it.hasNext()) {
      LogEntry entry = (LogEntry)it.next();
        filename = filenameFormat.format(entry.getDate());
        if (!filename.equals(currentFilename)) {
          // close the old file (if there is one)
          if (writer != null) {
            writer.flush();
            writer.close();
          }

          // and open a new file
          currentFilename = filename;
          file = new File(blog.getLogsDirectory(), currentFilename);
          writer = new BufferedWriter(new FileWriter(file, true));
        }

      writer.write(format.format(entry));
      writer.newLine();
    }

    if (writer != null) {
      writer.flush();
      writer.close();
    }
  }

}
