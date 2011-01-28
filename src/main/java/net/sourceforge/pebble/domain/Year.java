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
import java.util.List;
import java.util.LinkedList;

/**
 * Represents a blog at a yearly level. This manages a collection of Month instances.
 *
 * @author    Simon Brown
 */
public class Year extends TimePeriod implements Comparable {

  /** the year that this blog is for */
  private int year;

  /** a collection of the monthly blogs that this instance is managing */
  private Month[] months;

  /**
   * Creates a new Year instance for the specified year.
   *
   * @param blog    the Blog on which this Year is based
   * @param year    the year that this Year is for
   */
  public Year(Blog blog, int year) {
    super(blog);

    this.year = year;
    init();
  }

  /**
   * Initialises internal data, such as the collection of Month instances.
   */
  private void init() {
    setDate(getCalendar().getTime());
    this.months = new Month[12];

    for (int i = 1; i <= 12; i++) {
      months[i-1] = new Month(this, i);
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
   * Gets the Month for the specified month. Months are lazy
   * loaded as needed.
   *
   * @param month   the month as an int
   * @return  a Month instance
   */
  public synchronized Month getBlogForMonth(int month) {

    // some bounds checking
    if (month < 1 || month > 12) {
      throw new IllegalArgumentException("Invalid month of " + month + " specified, should be between 1 and 12");
    }

    return months[month-1];
  }

  /**
   * Given a Month, this method returns the Month instance for the
   * previous month.
   *
   * @param month   a Month instance
   * @return  a Month instance representing the previous month
   */
  Month getBlogForPreviousMonth(Month month) {
    if (month.getMonth() > 1) {
      return this.getBlogForMonth(month.getMonth() - 1);
    } else {
      return getBlog().getBlogForPreviousYear(this).getBlogForMonth(12);
    }
  }

  /**
   * Given a Month, this method returns the Month instance for the
   * next month.
   *
   * @param month   a Month instance
   * @return  a Month instance representing the next month
   */
  Month getBlogForNextMonth(Month month) {
    if (month.getMonth() < 12) {
      return this.getBlogForMonth(month.getMonth() + 1);
    } else {
      return getBlog().getBlogForNextYear(this).getBlogForMonth(1);
    }
  }

  /**
   * Gets the first Month that actually contains blog entries.
   *
   * @return  a Month instance
   */
  public Month getBlogForFirstMonth() {
    return getBlogForMonth(1);
  }

  /**
   * Gets a collection of all Months managed by this blog.
   *
   * @return  a Collection of Month instances
   */
  public Month[] getMonths() {
    Month[] months = new Month[12];
    for (int i = 1; i <= 12; i++) {
      months[i-1] = getBlogForMonth(i);
    }

    return months;
  }

  /**
   * Gets a collection of all Months, to date and in reverse order.
   *
   * @return  a Collection of Month instances
   */
  public List<Month> getArchives() {
    List<Month> list = new LinkedList<Month>();
    Month thisMonth = getBlog().getBlogForThisMonth();
    Month firstMonth = getBlog().getBlogForFirstMonth();
    for (int i = 12; i >=1; i--) {
      Month month = getBlogForMonth(i);
      if (!month.after(thisMonth) && !month.before(firstMonth)) {
        list.add(month);
      }
    }

    return list;
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
    return this.getYear() - ((Year)o).getYear();
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