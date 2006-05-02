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
package net.sourceforge.pebble.domain;

import net.sourceforge.pebble.comparator.DailyBlogComparator;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a blog at a monthly level. This manages a collection of DailyBlog instances.
 *
 * @author    Simon Brown
 */
public class MonthlyBlog extends TimePeriod implements Permalinkable {

  /** the parent, YearlyBlog instance */
  private YearlyBlog yearlyBlog;

  /** an integer representing the month that this MonthlyBlog is for */
  private int month;

  /** the collection of DailyBlog instances that this blog is managing */
  private DailyBlog[] dailyBlogs;

  /** the last day in this month */
  private int lastDayInMonth;

  /**
   * Creates a new MonthlyBlog based upon the specified YearlyBlog and month.
   *
   * @param yearlyBlog    the owning YearlyBlog instance
   * @param month         the month as an int
   */
  MonthlyBlog(YearlyBlog yearlyBlog, int month) {
    super(yearlyBlog.getBlog());

    this.yearlyBlog = yearlyBlog;
    this.month = month;
    setDate(getCalendar().getTime());

    Calendar cal = getBlog().getCalendar();
    cal.setTime(getDate());
    this.lastDayInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

    // and load all daily blogs
    dailyBlogs = new DailyBlog[lastDayInMonth];
    for (int day = 1; day <= lastDayInMonth; day++) {
      dailyBlogs[day-1] = new DailyBlog(this, day);
    }
  }

  private Calendar getCalendar() {
    // set the date corresponding to the 1st of the month
    // (this is used in determining whether another MonthlyBlog is
    // before or after this one)
    Calendar cal = getBlog().getCalendar();
    cal.set(Calendar.YEAR, yearlyBlog.getYear());
    cal.set(Calendar.MONTH, month - 1);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    return cal;
  }

  /**
   * Gets a reference to the parent YearlyBlog instance.
   *
   * @return  a YearlyBlog instance
   */
  public YearlyBlog getYearlyBlog() {
    return yearlyBlog;
  }

  /**
   * Gets an integer representing the month that this monthly blog is for.
   *
   * @return  an int representing the month (i.e. 1 to 12)
   */
  public int getMonth() {
    return month;
  }

  /**
   * Gets the permalink to display all entries for this MonthlyBlog.
   *
   * @return  an absolute URL
   */
  public String getPermalink() {
    String s = getBlog().getPermalinkProvider().getPermalink(this);
    if (s != null && s.length() > 0) {
      return getBlog().getUrl() + s.substring(1);
    } else {
      return "";
    }
  }

  /**
   * Gets a collection containing daily blogs that contain blog entries.
   *
   * @return  a Collection of DailyBlog instances for all those daily blogs
   *          that have entries (this can return an empty collection)
   */
  public Collection getDailyBlogs() {
    List blogs = new ArrayList();

    DailyBlog daily;
    for (int i = 1; i <= lastDayInMonth; i++) {
      daily = getBlogForDay(i);

      if (daily.hasEntries()) {
        blogs.add(daily);
      }
    }

    Collections.sort(blogs, new DailyBlogComparator());
    return blogs;
  }

  /**
   * Determines whether this monthly blog has entries.
   *
   * @return    true if this blog contains entries, false otherwise
   */
  public boolean hasBlogEntries() {
    for (int i = 1; i <= lastDayInMonth; i++) {
      if (getBlogForDay(i).hasEntries()) {
        return true;
      }
    }

    return false;
  }

  /**
   * Gets all blog entries for this month.
   *
   * @return  a List of BlogEntry instances, reverse ordered by date
   */
  public List getBlogEntries() {
    Iterator it = getDailyBlogs().iterator();
    List blogEntries = new ArrayList();
    while (it.hasNext()) {
      DailyBlog dailyBlog = (DailyBlog)it.next();
      blogEntries.addAll(dailyBlog.getEntries());
    }

    return blogEntries;
  }

  /**
   * Gets the number of blog entries for this month.
   *
   * @return  an int
   */
  public int getNumberOfBlogEntries() {
    int count = 0;
    Iterator it = getDailyBlogs().iterator();
    while (it.hasNext()) {
      DailyBlog dailyBlog = (DailyBlog)it.next();
      count += dailyBlog.getEntries().size();
    }

    return count;
  }

  /**
   * Gets a collection containing daily blogs that contain blog entries.
   *
   * @return  a Collection of DailyBlog instances for all those daily blogs
   *          that have entries (this can return an empty collection)
   */
  public DailyBlog[] getAllDailyBlogs() {
    DailyBlog blogs[] = new DailyBlog[dailyBlogs.length];
    for (int day = 0; day < dailyBlogs.length; day++) {
      blogs[day] = getBlogForDay(day + 1);
    }
    //System.arraycopy(dailyBlogs, 0, blogs, 0, dailyBlogs.length);
    return blogs;
  }

  /**
   * Gets a DailyBlog instance for the specified day. This lazy loads DailyBlog
   * instances as needed.
   *
   * @param day   the day as an int (i.e. 1 to 31)
   * @return  the corresponding DailyBlog instance
   */
  public synchronized DailyBlog getBlogForDay(int day) {
    // some bounds checking
    if (day < 1 || day > lastDayInMonth) {
      throw new IllegalArgumentException("Invalid day of " + day + " specified, should be between 1 and " + lastDayInMonth);
    }

    return dailyBlogs[day-1];
  }

  /**
   * Gets a DailyBlog instance for the first day of the month.
   *
   * @return  the DailyBlog instance representing the first day in the month
   */
  public DailyBlog getBlogForFirstDay() {
    return getBlogForDay(1);
  }

  /**
   * Gets a DailyBlog instance for the last day of the month.
   *
   * @return  the DailyBlog instance representing the last day in the month
   */
  public DailyBlog getBlogForLastDay() {
    return getBlogForDay(lastDayInMonth);
  }

  /**
   * Gets the last day of the month.
   *
   * @return  an int representing the last day in the month
   */
  public int getLastDayInMonth() {
    return lastDayInMonth;
  }

  /**
   * Gets the MonthlyBlog instance for the previous month.
   *
   * @return    a MonthlyBlog instance
   */
  public MonthlyBlog getPreviousMonth() {
    return yearlyBlog.getBlogForPreviousMonth(this);
  }

  /**
   * Gets the MonthlyBlog instance for the next month.
   *
   * @return    a MonthlyBlog instance
   */
  public MonthlyBlog getNextMonth() {
    return yearlyBlog.getBlogForNextMonth(this);
  }

  /**
   * Determines if the this MonthlyBlog is before (in the calendar) the
   * specified MonthlyBlog.
   *
   * @return  true if this instance represents an earlier month than the
   *          specified MonthlyBlog instance, false otherwise
   */
  public boolean before(MonthlyBlog monthlyBlog) {
    return getDate().before(monthlyBlog.getDate());
  }

  /**
   * Given a DailyBlog, this method returns the DailyBlog instance for the
   * previous day.
   *
   * @param dailyBlog   a DailyBlog instance
   * @return  a DailyBlog instance representing the previous day
   */
  DailyBlog getBlogForPreviousDay(DailyBlog dailyBlog) {
    if (dailyBlog.getDay() > 1) {
      return this.getBlogForDay(dailyBlog.getDay() - 1);
    } else {
      return yearlyBlog.getBlogForPreviousMonth(this).getBlogForLastDay();
    }
  }

  /**
   * Given a DailyBlog, this method returns the DailyBlog instance for the
   * next day.
   *
   * @param dailyBlog   a DailyBlog instance
   * @return  a DailyBlog instance representing the next day
   */
  DailyBlog getBlogForNextDay(DailyBlog dailyBlog) {
    if (dailyBlog.getDay() < lastDayInMonth) {
      return this.getBlogForDay(dailyBlog.getDay() + 1);
    } else {
      return yearlyBlog.getBlogForNextMonth(this).getBlogForFirstDay();
    }
  }

  /**
   * Gets a string representation of this object.
   *
   * @return  a String
   */
  public String toString() {
    SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
    return sdf.format(getDate());
  }

}