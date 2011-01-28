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
package net.sourceforge.pebble.domain;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a blog at a monthly level. This manages a collection of Day instances.
 *
 * @author    Simon Brown
 */
public class Month extends TimePeriod implements Permalinkable {

  /** the parent, Year instance */
  private Year year;

  /** an integer representing the month that this Month is for */
  private int month;

  /** the collection of Day instances that this blog is managing */
  private Day[] dailyBlogs;

  /** the last day in this month */
  private int lastDayInMonth;

  /**
   * Creates a new Month based upon the specified Year and month.
   *
   * @param year    the owning Year instance
   * @param month         the month as an int
   */
  Month(Year year, int month) {
    super(year.getBlog());

    this.year = year;
    this.month = month;
    setDate(getCalendar().getTime());

    Calendar cal = getBlog().getCalendar();
    cal.setTime(getDate());
    this.lastDayInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

    // and create all days
    dailyBlogs = new Day[lastDayInMonth];
    for (int day = 1; day <= lastDayInMonth; day++) {
      dailyBlogs[day-1] = new Day(this, day);
    }
  }

  private Calendar getCalendar() {
    // set the date corresponding to the 1st of the month
    // (this is used in determining whether another Month is
    // before or after this one)
    Calendar cal = getBlog().getCalendar();
    cal.set(Calendar.YEAR, year.getYear());
    cal.set(Calendar.MONTH, month - 1);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    return cal;
  }

  /**
   * Gets a reference to the parent Year instance.
   *
   * @return  a Year instance
   */
  public Year getYear() {
    return year;
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
   * Gets the permalink to display all entries for this Month.
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
   * Determines whether this monthly blog has entries.
   *
   * @return    true if this blog contains entries, false otherwise
   */
  public boolean hasBlogEntries() {
    for (int i = 1; i <= lastDayInMonth; i++) {
      if (getBlogForDay(i).hasBlogEntries()) {
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
  public List<String> getBlogEntries() {
    Day days[] = getAllDays();
    List blogEntries = new ArrayList();
    for (Day day : days) {
      blogEntries.addAll(day.getBlogEntries());
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
    Day days[] = getAllDays();
    for (Day day : days) {
      count += day.getNumberOfBlogEntries();
    }

    return count;
  }

  /**
   * Gets an array of all Days.
   *
   * @return  a Collection of Day instances for all those days
   *          that have entries (this can return an empty collection)
   */
  public Day[] getAllDays() {
    Day blogs[] = new Day[dailyBlogs.length];
    for (int day = 0; day < dailyBlogs.length; day++) {
      blogs[day] = getBlogForDay(day + 1);
    }

    return blogs;
  }

  /**
   * Gets a Day instance for the specified day. This lazy loads Day
   * instances as needed.
   *
   * @param day   the day as an int (i.e. 1 to 31)
   * @return  the corresponding Day instance
   */
  public synchronized Day getBlogForDay(int day) {
    // some bounds checking
    if (day < 1 || day > lastDayInMonth) {
      throw new IllegalArgumentException("Invalid day of " + day + " specified, should be between 1 and " + lastDayInMonth);
    }

    return dailyBlogs[day-1];
  }

  /**
   * Gets a Day instance for the first day of the month.
   *
   * @return  the Day instance representing the first day in the month
   */
  public Day getBlogForFirstDay() {
    return getBlogForDay(1);
  }

  /**
   * Gets a Day instance for the last day of the month.
   *
   * @return  the Day instance representing the last day in the month
   */
  public Day getBlogForLastDay() {
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
   * Gets the Month instance for the previous month.
   *
   * @return    a Month instance
   */
  public Month getPreviousMonth() {
    return year.getBlogForPreviousMonth(this);
  }

  /**
   * Gets the Month instance for the next month.
   *
   * @return    a Month instance
   */
  public Month getNextMonth() {
    return year.getBlogForNextMonth(this);
  }

  /**
   * Determines if the this Month is before (in the calendar) the
   * specified Month.
   *
   * @return  true if this instance represents an earlier month than the
   *          specified Month instance, false otherwise
   */
  public boolean before(Month month) {
    return getDate().before(month.getDate());
  }

  /**
   * Determines if the this Month is after (in the calendar) the
   * specified Month.
   *
   * @return  true if this instance represents a later month than the
   *          specified Month instance, false otherwise
   */
  public boolean after(Month month) {
    return getDate().after(month.getDate());
  }

  /**
   * Given a Day, this method returns the Day instance for the
   * previous day.
   *
   * @param day   a Day instance
   * @return  a Day instance representing the previous day
   */
  Day getBlogForPreviousDay(Day day) {
    if (day.getDay() > 1) {
      return this.getBlogForDay(day.getDay() - 1);
    } else {
      return year.getBlogForPreviousMonth(this).getBlogForLastDay();
    }
  }

  /**
   * Given a Day, this method returns the Day instance for the
   * next day.
   *
   * @param day   a Day instance
   * @return  a Day instance representing the next day
   */
  Day getBlogForNextDay(Day day) {
    if (day.getDay() < lastDayInMonth) {
      return this.getBlogForDay(day.getDay() + 1);
    } else {
      return year.getBlogForNextMonth(this).getBlogForFirstDay();
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