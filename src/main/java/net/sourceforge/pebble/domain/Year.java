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
import com.google.common.collect.Lists;

import java.util.*;

/**
 * Represents a blog at a yearly level. This manages a collection of Month instances.
 *
 * @author Simon Brown
 */
public class Year extends TimePeriod implements Comparable {

  /**
   * the year that this blog is for
   */
  private final int year;

  /**
   * The first month in the list.  If this year is the first year that contains blog entries, than this might be
   * greater than 1, otherwise it will equal 1.
   */
  private final int firstMonth;

  /**
   * a collection of the monthly blogs that this instance is managing
   */
  private final List<Month> months;

  private Year(Blog blog, Date date, int year, int firstMonth, List<Month> months) {
    super(blog, date);
    if (firstMonth < 1 && firstMonth > 13) {
      throw new IllegalArgumentException("First month must be from 1 to 13 (13 meaning no months in this year)");
    }
    if (months.size() > 13 - firstMonth) {
      throw new IllegalArgumentException("Too many months for year " + months.size() + " when first month is " + firstMonth);
    }
    this.year = year;
    this.firstMonth = firstMonth;
    this.months = months;
  }

  /**
   * Gets an integer representing the year that this yearly blog is for.
   *
   * @return an int representing the year (e.g. 2003)
   */
  public int getYear() {
    return year;
  }

  /**
   * Gets the Month for the specified month. Months are lazy
   * loaded as needed.
   *
   * @param month the month as an int
   * @return a Month instance
   */
  public Month getBlogForMonth(int month) {
    // some bounds checking
    if (month < 1 || month > 12) {
      throw new IllegalArgumentException("Invalid month of " + month + " specified, should be between 1 and 12");
    }

    // Offset month by first month to get the index in the array
    int index = month - firstMonth;

    // Check if we have this month or not
    if (index < 0 || index >= months.size()) {
      return Month.emptyMonth(getBlog(), year, month);
    }

    return months.get(index);
  }

  /**
   * Given a Month, this method returns the Month instance for the
   * previous month.
   *
   * @param month a Month instance
   * @return a Month instance representing the previous month
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
   * @param month a Month instance
   * @return a Month instance representing the next month
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
   * @return a Month instance
   */
  public Month getBlogForFirstMonth() {
    return getBlogForMonth(1);
  }

  /**
   * Gets a collection of all Months, to date and in reverse order.
   *
   * @return a Collection of Month instances
   */
  public List<Month> getArchives() {
    return Lists.reverse(months);
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
    return this.getYear() - ((Year) o).getYear();
  }

  /**
   * Gets a string representation of this object.
   *
   * @return a String
   */
  public String toString() {
    return "" + this.year;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Year year1 = (Year) o;

    if (year != year1.year) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return year;
  }

  public static Builder builder(Blog blog, int year) {
    return new Builder(blog, year);
  }

  public static Builder builder(Year like) {
    return new Builder(like);
  }

  public static Year emptyYear(Blog blog, int year) {
    return new Builder(blog, year).build();
  }

  public static class Builder {
    private final Blog blog;
    private final int year;
    private final LinkedList<Month> months;
    private int firstMonth;

    private Builder(Blog blog, int year) {
      this.blog = blog;
      this.year = year;
      months = new LinkedList<Month>();
      firstMonth = 13;
    }

    private Builder(Year year) {
      this.blog = year.getBlog();
      this.year = year.year;
      this.months = new LinkedList<Month>(year.months);
      this.firstMonth = year.firstMonth;
    }

    public Year build() {
      Calendar cal = blog.getCalendar();
      cal.set(Calendar.YEAR, year);
      cal.set(Calendar.MONTH, 0);
      cal.set(Calendar.DAY_OF_MONTH, 2);
      cal.set(Calendar.HOUR_OF_DAY, 0);
      cal.set(Calendar.MINUTE, 0);
      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MILLISECOND, 0);
      Date date = cal.getTime();
      return new Year(blog, date, year, firstMonth, ImmutableList.copyOf(months));
    }

    public Builder putMonth(Month month) {
      if (month.getYear() != year) {
        throw new IllegalArgumentException("Cannot add month from year " + month.getYear() + " to year " + year);
      }
      if (month.getMonth() < 1 || month.getMonth() > 12) {
        throw new IllegalArgumentException("Month must be between 1 and 12");
      }
      // First insert needed months
      while (firstMonth > month.getMonth()) {
        firstMonth--;
        months.addFirst(Month.emptyMonth(blog, year, firstMonth));
      }
      months.set(month.getMonth() - firstMonth, month);
      return this;
    }
  }
}