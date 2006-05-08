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

import java.util.Calendar;

/**
 * Represents a blog at a yearly level. This manages a collection of MonthlyBlog instances.
 *
 * @author    Simon Brown
 */
public class YearlyBlog extends TimePeriod implements Comparable {

  /** the year that this blog is for */
  private int year;

  /** a collection of the monthly blogs that this instance is managing */
  private MonthlyBlog[] monthlyBlogs;

  /**
   * Creates a new YearlyBlog instance for the specified year.
   *
   * @param blog    the Blog on which this YearlyBlog is based
   * @param year    the year that this YearlyBlog is for
   */
  public YearlyBlog(Blog blog, int year) {
    super(blog);

    this.year = year;
    init();
  }

  /**
   * Initialises internal data, such as the collection of MonthlyBlog instances.
   */
  private void init() {
    setDate(getCalendar().getTime());
    this.monthlyBlogs = new MonthlyBlog[12];

    for (int i = 1; i <= 12; i++) {
      monthlyBlogs[i-1] = new MonthlyBlog(this, i);
    }
  }

  private Calendar getCalendar() {
    // set the date corresponding to the 1st of January of the specified year
    Calendar cal = getBlog().getCalendar();
    cal.set(Calendar.YEAR, year);
    cal.set(Calendar.MONTH, 0);
    cal.set(Calendar.DAY_OF_MONTH, 2);
    cal.set(Calendar.HOUR_OF_DAY, 0);
    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    cal.set(Calendar.MILLISECOND, 0);

    return cal;
  }

  /**
   * Gets an integer representing the year that this yearly blog is for.
   *
   * @return  an int representing the year (e.g. 2003)
   */
  public int getYear() {
    return year;
  }

  /**
   * Gets the MonthlyBlog for the specified month. Months are lazy
   * loaded as needed.
   *
   * @param month   the month as an int
   * @return  a MonthlyBlog instance
   */
  public synchronized MonthlyBlog getBlogForMonth(int month) {

    // some bounds checking
    if (month < 1 || month > 12) {
      throw new IllegalArgumentException("Invalid month of " + month + " specified, should be between 1 and 12");
    }

    return monthlyBlogs[month-1];
  }

  /**
   * Given a MonthlyBlog, this method returns the MonthlyBlog instance for the
   * previous month.
   *
   * @param monthlyBlog   a MonthlyBlog instance
   * @return  a MonthlyBlog instance representing the previous month
   */
  MonthlyBlog getBlogForPreviousMonth(MonthlyBlog monthlyBlog) {
    if (monthlyBlog.getMonth() > 1) {
      return this.getBlogForMonth(monthlyBlog.getMonth() - 1);
    } else {
      return getBlog().getBlogForPreviousYear(this).getBlogForMonth(12);
    }
  }

  /**
   * Given a MonthlyBlog, this method returns the MonthlyBlog instance for the
   * next month.
   *
   * @param monthlyBlog   a MonthlyBlog instance
   * @return  a MonthlyBlog instance representing the next month
   */
  MonthlyBlog getBlogForNextMonth(MonthlyBlog monthlyBlog) {
    if (monthlyBlog.getMonth() < 12) {
      return this.getBlogForMonth(monthlyBlog.getMonth() + 1);
    } else {
      return getBlog().getBlogForNextYear(this).getBlogForMonth(1);
    }
  }

  /**
   * Gets the first MonthlyBlog that actually contains blog entries.
   *
   * @return  a MonthlyBlog instance
   */
  public MonthlyBlog getBlogForFirstMonth() {
    return getBlogForMonth(1);
  }

  /**
   * Gets a collection of all MonthlyBlogs managed by this blog.
   *
   * @return  a Collection of MonthlyBlog instances
   */
  public MonthlyBlog[] getMonthlyBlogs() {
    MonthlyBlog[] months = new MonthlyBlog[12];
    for (int i = 1; i <= 12; i++) {
      months[i-1] = getBlogForMonth(i);
    }

    return months;
  }

  /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the specified object.<p>
   *
   * @param o the Object to be compared.
   * @return a negative integer, zero, or a positive integer as this object
   *         is less than, equal to, or greater than the specified object.
   * @throws ClassCastException if the specified object's type prevents it
   *                            from being compared to this Object.
   */
  public int compareTo(Object o) {
    return this.getYear() - ((YearlyBlog)o).getYear();
  }

  /**
   * Gets a string representation of this object.
   *
   * @return  a String
   */
  public String toString() {
    return "" + this.year;
  }

}