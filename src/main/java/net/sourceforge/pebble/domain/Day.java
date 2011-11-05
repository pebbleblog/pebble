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

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Represents a blog at a daily level. This manages a collection of BlogEntry instances.
 *
 * @author    Simon Brown
 */
public class Day implements Comparable<Day> {

  private final Blog blog;
  private final Date date;
  private final int year;
  private final int month;
  private final int day;
  private final int numberOfBlogEntries;

  private Day(Blog blog, Date date, int year, int month, int day, int numberOfBlogEntries) {
    this.blog = blog;
    this.date = date;
    this.year = year;
    this.month = month;
    this.day = day;
    this.numberOfBlogEntries = numberOfBlogEntries;
  }

  public Blog getBlog() {
    return blog;
  }

  public int getYear() {
    return year;
  }

  /**
   * Gets the month this day is in
   *
   * @return  the month
   */
  public int getMonth() {
    return month;
  }

  /**
   * Gets the day that this Day is for.
   *
   * @return    an int representing the day in the month
   */
  public int getDay() {
    return day;
  }

  /**
   * Determines whether this day has entries.
   *
   * @return    true if this blog contains entries, false otherwise
   */
  public boolean hasBlogEntries() {
    return numberOfBlogEntries > 0;
  }

  /**
   * Get the number of blog entries for this day
   *
   * @return The number of blog entries
   */
  public int getNumberOfBlogEntries() {
    return numberOfBlogEntries;
  }

  public int compareTo(Day other) {
    if (year == other.year) {
      if (month == other.month) {
        return day - other.day;
      } else {
        return month - other.month;
      }
    } else {
      return year - other.year;
    }
  }

  /**
   * Determines if the this Day is before (in the calendar) the
   * specified Day.
   *
   * @param day The day to compare to
   * @return  true if this instance represents an earlier day than the
   *          specified Day instance, false otherwise
   */
  public boolean before(Day day) {
    return compareTo(day) < 0;
  }

  /**
   * Determines if the this Day is after (in the calendar) the
   * specified Day.
   *
   * @param day The day to compare to
   * @return  true if this instance represents an later day than the
   *          specified Day instance, false otherwise
   */
  public boolean after(Day day) {
    return compareTo(day) > 0;
  }

  public Date getDate() {
    return date;
  }

  public Date getDate(TimeZone timeZone, Locale locale) {
    return getDate(Calendar.getInstance(timeZone, locale));
  }

  public Date getDate(Calendar calendar) {
    calendar.setTimeInMillis(0);
    calendar.set(Calendar.YEAR, year);
    calendar.set(Calendar.MONTH, month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, day);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    return calendar.getTime();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Day day1 = (Day) o;

    if (day != day1.day) return false;
    if (month != day1.month) return false;
    if (year != day1.year) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = year;
    result = 31 * result + month;
    result = 31 * result + day;
    return result;
  }

  public static Builder builder(Blog blog, int year, int month, int day) {
    return new Builder(blog, year, month, day);
  }

  public static Builder builder(Day like) {
    return new Builder(like);
  }

  public static Day emptyDay(Blog blog, int year, int month, int day) {
    return builder(blog, year, month, day).build();
  }

  public static class Builder {
    private final Blog blog;
    private final int year;
    private final int month;
    private final int day;
    private final Date date;
    private int numberOfBlogEntries;

    private Builder(Blog blog, int year, int month, int day) {
      this.blog = blog;
      this.year = year;
      this.month = month;
      this.day = day;
      Calendar cal = blog.getCalendar();
      cal.set(Calendar.YEAR, year);
      cal.set(Calendar.MONTH, month - 1);
      cal.set(Calendar.DAY_OF_MONTH, day);
      cal.set(Calendar.HOUR_OF_DAY, 12);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      this.date = cal.getTime();
    }

    private Builder(Day day) {
      this.blog = day.blog;
      this.year = day.year;
      this.month = day.month;
      this.day = day.day;
      this.date = day.date;
      this.numberOfBlogEntries = day.numberOfBlogEntries;
    }

    public Day build() {
      return new Day(blog, date, year, month, day, numberOfBlogEntries);
    }

    public Builder setNumberOfBlogEntries(int numberOfBlogEntries) {
      this.numberOfBlogEntries = numberOfBlogEntries;
      return this;
    }
  }

}