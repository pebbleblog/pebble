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
package net.sourceforge.pebble.logging;

import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.file.FileDAOFactory;
import net.sourceforge.pebble.domain.DailyBlog;
import net.sourceforge.pebble.domain.MonthlyBlog;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.YearlyBlog;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.text.DecimalFormat;
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

  /** the format of the log filenames */
  private SimpleDateFormat filenameFormat = new SimpleDateFormat("'blog-'yyyyMMdd'.log'");

  private List entries = new ArrayList();

  private LoggingThread loggingThread;

  private boolean dirty = false;

  /** a cached copy of the log for today */
  private Log logForToday;

  public CombinedLogFormatLogger(Blog blog) {
    super(blog);
    filenameFormat.setTimeZone(blog.getTimeZone());
    logForToday = super.getLog();
  }

  /**
   * Logs a HTTP request.
   *
   * @param request   a HttpServletRequest
   */
  public synchronized void log(HttpServletRequest request) {
    LogEntry entry = new LogEntry();
    entry.setHost(request.getRemoteHost());
    entry.setDate(blog.getCalendar().getTime());
    StringBuffer buf = new StringBuffer();
    buf.append(request.getMethod());
    buf.append(" ");
    buf.append(request.getRequestURI());
    if (request.getQueryString() != null) {
      buf.append("?");
      buf.append(request.getQueryString());
    }
    entry.setRequest(buf.toString());
    entry.setReferer(request.getHeader(REFERER_HEADER));
    entry.setAgent(request.getHeader(USER_AGENT_HEADER));
    entries.add(entry);
    dirty = true;
  }

  /**
   * Called to start this logger.
   */
  public void start() {
    loggingThread = new LoggingThread();
    loggingThread.start();
  }

  /**
   * Called to stop this logger.
   */
  public void stop() {
    loggingThread.setActive(false);
  }

  /**
   * Gets the log for today - overridden to use the cache copy of todays log.
   *
   * @return a Log object
   */
  public Log getLog() {
    return logForToday;
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
    } finally {
      return new LogSummaryItem(blog, cal.getTime(), totalRequests);
    }
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

  /**
   * This is a thread that logs referers and visited pages to the enclosing
   * Blog instance.
   */
  class LoggingThread extends Thread {

    private boolean active = true;

    public LoggingThread() {
      super("pebble/" + blog.getId() + "/log");
    }

    public void setActive(boolean active) {
      this.active = active;
    }

    public void run() {
      while (active) {
        try {
          Thread.sleep(1000 * 60); // 1 minute
        } catch (InterruptedException e) {
        }

        synchronized (CombinedLogFormatLogger.this) {
          if (dirty) {
            try {
              write(entries);
              //logForToday.addLogEntries(entries);
              entries.clear();
              logForToday = CombinedLogFormatLogger.super.getLog();
            } catch (IOException ioe) {
              ioe.printStackTrace();
            }
            dirty = false;
          }
        }
      }
    }

  }

  /**
   * Standalone program to convert logs from previous versions of Pebble
   * to the new combined format.
   *
   * @param args    the command line arguments
   * @throws Exception    if something goes wrong
   */
  public static void main(String[] args) throws Exception {
    DAOFactory.setConfiguredFactory(new FileDAOFactory());
    Blog blog = new Blog(args[0]);
    CombinedLogFormatLogger logger = new CombinedLogFormatLogger(blog);
    for (int year = 0; year < blog.getYearlyBlogs().size(); year++) {
      YearlyBlog yearlyBlog = (YearlyBlog)blog.getYearlyBlogs().get(year);
      MonthlyBlog[] months = yearlyBlog.getMonthlyBlogs();
      for (int month = 0; month < 12; month++) {
        DailyBlog days[] = months[month].getAllDailyBlogs();
        for (int day = 0; day < days.length; day++) {
          try {
            String pathToDay = getPath(days[day]);
            File visitedPagesFile = new File(pathToDay, "visited-pages.txt");
            File referersFile = new File(pathToDay, "referers.txt");
            BufferedReader reader = new BufferedReader(new FileReader(visitedPagesFile));
            ArrayList entries = new ArrayList();
            LogEntry entry;

            String line = reader.readLine();
            while (line != null) {
              entry = new LogEntry();
              entry.setDate(days[day].getDate());
              entry.setRequest("GET ");
              String uri = line;
              int index = uri.indexOf("/");
              index = uri.indexOf("/", index+1);
              index = uri.indexOf("/", index+1);
              uri = uri.substring(index);
              entry.setRequest("GET " + uri);
              entries.add(entry);
              line = reader.readLine();
            }
            reader.close();

            reader = new BufferedReader(new FileReader(referersFile));

            line = reader.readLine();
            int count = 0;
            while (line != null) {
              entry = (LogEntry)entries.get(count);
              if (line.length() > 0) {
                entry.setReferer(line);
              }
              count++;
              line = reader.readLine();
            }
            reader.close();

            logger.write(entries);

            referersFile.delete();
            visitedPagesFile.delete();
          } catch (Exception e) {
            System.out.println(e.getMessage());
          }
        }
      }
    }
  }

  private static String getPath(DailyBlog dailyBlog) {
    DecimalFormat format = new DecimalFormat("00");
    StringBuffer path = new StringBuffer();
    path.append(dailyBlog.getBlog().getRoot());
    path.append(File.separator);
    path.append(dailyBlog.getMonthlyBlog().getYearlyBlog().getYear());
    path.append(File.separator);
    path.append(format.format(dailyBlog.getMonthlyBlog().getMonth()));
    path.append(File.separator);
    path.append(format.format(dailyBlog.getDay()));
    path.append(File.separator);

    return path.toString();
  }

}
