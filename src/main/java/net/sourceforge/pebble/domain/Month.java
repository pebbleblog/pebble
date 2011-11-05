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

import com.google.common.collect.ImmutableList;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Represents a blog at a monthly level. This manages a collection of Day instances.
 *
 * @author    Simon Brown
 */
public class Month extends TimePeriod implements Permalinkable {

  /** the year this month is for */
  private int year;

  /** an integer representing the month that this Month is for */
  private int month;

  /** the collection of Day instances that this blog is managing */
  private List<Day> days;

  /** the last day in this month */
  private int lastDayInMonth;

  /** the first day this month has a blog for **/
  private int firstDay;

  private Month(Blog blog, Date date, int year, int month, int lastDayInMonth, List<Day> days, int firstDay) {
    super(blog, date);

    this.year = year;
    this.month = month;
    this.lastDayInMonth = lastDayInMonth;
    this.days = days;
    this.firstDay = firstDay;
  }

  /**
   * Gets a reference to the parent Year instance.
   *
   * @return  a Year instance
   */
  public int getYear() {
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
    for (Day day : days) {
      if (day.hasBlogEntries()) {
        return true;
      }
    }

    return false;
  }

  /**
   * Gets the number of blog entries for this month.
   *
   * @return  an int
   */
  public int getNumberOfBlogEntries() {
    int count = 0;
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
  public List<Day> getAllDays() {
    return days;
  }

  /**
   * Gets a Day instance for the specified day. This lazy loads Day
   * instances as needed.
   *
   * @param day   the day as an int (i.e. 1 to 31)
   * @return  the corresponding Day instance
   */
  public Day getBlogForDay(int day) {
    // some bounds checking
    if (day < 1 || day > lastDayInMonth) {
      throw new IllegalArgumentException("Invalid day of " + day + " specified, should be between 1 and " + lastDayInMonth);
    }

    // Calculate index
    int index = day - firstDay;
    if (index < 0 || index >= days.size()) {
      return Day.emptyDay(getBlog(), year, month, day);
    }

    return days.get(index);
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
    return getBlog().getBlogForYear(year).getBlogForPreviousMonth(this);
  }

  /**
   * Gets the Month instance for the next month.
   *
   * @return    a Month instance
   */
  public Month getNextMonth() {
    return getBlog().getBlogForYear(year).getBlogForNextMonth(this);
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
      return getBlog().getBlogForYear(year).getBlogForPreviousMonth(this).getBlogForLastDay();
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
      return getBlog().getBlogForYear(year).getBlogForNextMonth(this).getBlogForFirstDay();
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Month month1 = (Month) o;

    if (month != month1.month) return false;
    if (year != month1.year) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = year;
    result = 31 * result + month;
    return result;
  }

  public static Builder builder(Blog blog, int year, int month) {
    return new Builder(blog, year, month);
  }

  public static Builder builder(Month like) {
    return new Builder(like);
  }

  public static Month emptyMonth(Blog blog, int year, int month) {
    return builder(blog, year, month).build();
  }

  public static class Builder {
    private final Blog blog;
    private final int year;
    private final int month;
    private final LinkedList<Day> days;
    private final Date date;
    private final int lastDayInMonth;
    private int firstDay;

    private Builder(Blog blog, int year, int month) {
      this.blog = blog;
      this.year = year;
      this.month = month;
      this.days = new LinkedList<Day>();

      Calendar cal = blog.getCalendar();
      cal.set(Calendar.YEAR, year);
      cal.set(Calendar.MONTH, month - 1);
      cal.set(Calendar.DAY_OF_MONTH, 1);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      this.date = cal.getTime();
      this.lastDayInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

      this.firstDay = lastDayInMonth + 1;
    }

    private Builder(Month month) {
      this.blog = month.getBlog();
      this.year = month.year;
      this.month = month.month;
      this.days = new LinkedList<Day>(month.days);
      this.date = month.getDate();
      this.lastDayInMonth = month.lastDayInMonth;
      this.firstDay = month.firstDay;
    }

    public Month build() {
      return new Month(blog, date, year, month, lastDayInMonth, ImmutableList.copyOf(days), firstDay);
    }

    public Builder putDay(Day day) {
      if (day.getMonth() != month || day.getYear() != year) {
        throw new IllegalArgumentException("Cannot add day from year " + day.getYear()  + " and month " + day.getMonth() + " to month " + month + "in year " + year);
      }
      if (day.getDay() < 1 || day.getDay() > lastDayInMonth) {
        throw new IllegalArgumentException("Day must be between 1 and " + lastDayInMonth);
      }
      // First insert needed days
      while (firstDay > day.getDay()) {
        firstDay--;
        days.addFirst(Day.emptyDay(blog, year, month, firstDay));
      }
      days.set(day.getDay() - firstDay, day);
      return this;
    }

  }

}