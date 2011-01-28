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

/**
 * Tests for the Month class.
 *
 * @author    Simon Brown
 */
public class MonthTest extends SingleBlogTestCase {

  private Month month;

  protected void setUp() throws Exception {
    super.setUp();
    month = new Month(blog.getBlogForThisYear(), 7);
  }

  /**
   * Tests that the root blog is setup correctly.
   */
  public void testGetRootBlog() {
    assertEquals(blog, month.getBlog());
  }

  /**
   * Tests the getter for the month property.
   */
  public void testGetMonth() {
    assertEquals(7, month.getMonth());
  }

  /**
   * Tests the getter for the yearly blog.
   */
  public void testGetYear() {
    assertEquals(blog.getBlogForThisYear(), month.getYear());
  }

  /**
   * Tests that we can access the previous month.
   */
  public void testGetPreviousMonth() {
    month = blog.getBlogForMonth(2003, 7);
    Month previousMonth = month.getPreviousMonth();
    assertEquals(2003, previousMonth.getYear().getYear());
    assertEquals(6, previousMonth.getMonth());

    month = blog.getBlogForMonth(2003, 1);
    previousMonth = month.getPreviousMonth();
    assertEquals(2002, previousMonth.getYear().getYear());
    assertEquals(12, previousMonth.getMonth());
  }

  /**
   * Tests that we can access the next month.
   */
  public void testGetNextMonth() {
    month = blog.getBlogForMonth(2003, 7);
    Month nextMonth = month.getNextMonth();
    assertEquals(2003, nextMonth.getYear().getYear());
    assertEquals(8, nextMonth.getMonth());

    month = blog.getBlogForMonth(2002, 12);
    nextMonth = month.getNextMonth();
    assertEquals(2003, nextMonth.getYear().getYear());
    assertEquals(1, nextMonth.getMonth());
  }

  /**
   * Tests that we can compare monthly blogs.
   */
  public void testBefore() {
    Year year2002 = new Year(blog, 2002);
    Year year2003 = new Year(blog, 2003);
    Month month1 = new Month(year2003, 6);
    Month month2 = new Month(year2003, 7);
    assertTrue(month1.before(month2));
    assertFalse(month2.before(month1));

    month1 = new Month(year2002, 7);
    month2 = new Month(year2003, 7);
    assertTrue(month1.before(month2));
    assertFalse(month2.before(month1));
  }

  /**
   * Tests that we can get all days for a month.
   */
  public void testGetAllDays() {
    month = new Month(blog.getBlogForThisYear(), 1);
    assertEquals(31, month.getAllDays().length);
  }

  /**
   * Tests that we can get the day for a specific day.
   */
  public void testGetBlogForDay() {
    Day day = month.getBlogForDay(1);
    assertNotNull(day);
    assertEquals(1, day.getDay());

    try {
      day = month.getBlogForDay(-1);
      fail();
    } catch (IllegalArgumentException iae) {
    }

    try {
      day = month.getBlogForDay(0);
      fail();
    } catch (IllegalArgumentException iae) {
    }

    Calendar cal = blog.getCalendar();
    cal.setTime(month.getDate());
    int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    day = month.getBlogForDay(maxDay - 1);
    day = month.getBlogForDay(maxDay);
    try {
      day = month.getBlogForDay(maxDay + 1);
      fail();
    } catch (IllegalArgumentException iae) {
    }

  }

  /**
   * Tests the getter for the date property.
   */
  public void testGetDate() {
    month = blog.getBlogForMonth(2003, 4);
    Date date = month.getDate();
    Calendar cal = blog.getCalendar();
    cal.setTime(date);
    assertEquals(2003, cal.get(Calendar.YEAR));
    assertEquals(4, cal.get(Calendar.MONTH) + 1);
    assertEquals(1, cal.get(Calendar.DAY_OF_MONTH));
    assertEquals(0, cal.get(Calendar.HOUR));
    assertEquals(0, cal.get(Calendar.MINUTE));
    assertEquals(0, cal.get(Calendar.SECOND));
    assertEquals(0, cal.get(Calendar.MILLISECOND));
  }

  /**
   * Tests the permalink.
   */
  public void testGetPermalink() {
    String permalink = blog.getUrl() + "2003/07.html";
    month = blog.getBlogForMonth(2003, 7);
    assertEquals(permalink, month.getPermalink());

    permalink = blog.getUrl() + "2003/12.html";
    month = blog.getBlogForMonth(2003, 12);
    assertEquals(permalink, month.getPermalink());
  }

//  /**
//   * Tests that all blog entries for a month can be accessed.
//   */
//  public void testGetAllBlogEntries() {
//    assertTrue(month.getBlogEntries().isEmpty());
//
//    // now add an entry
//    BlogEntry blogEntry1 = new BlogEntry(blog);
//    BlogEntry blogEntry1 = month.getBlogForFirstDay().createBlogEntry();
//    month.getBlogForFirstDay().addEntry(blogEntry1);
//    assertTrue(month.getBlogEntries().size() == 1);
//    assertTrue(month.getBlogEntries().contains(blogEntry1));
//
//    // now add a second
//    BlogEntry blogEntry2 = month.getBlogForLastDay().createBlogEntry();
//    month.getBlogForLastDay().addEntry(blogEntry2);
//    assertTrue(month.getBlogEntries().size() == 2);
//
//    // check they are reverse ordered by date
//    assertTrue(month.getBlogEntries().get(0) == blogEntry2);
//    assertTrue(month.getBlogEntries().get(1) == blogEntry1);
//  }

  public void testLastDayInMonth() {
    month = new Month(blog.getBlogForYear(2005), 1);
    assertEquals(31, month.getLastDayInMonth());
    month = new Month(blog.getBlogForYear(2005), 2);
    assertEquals(28, month.getLastDayInMonth());
    month = new Month(blog.getBlogForYear(2005), 3);
    assertEquals(31, month.getLastDayInMonth());

    month = new Month(blog.getBlogForYear(2004), 2);
    assertEquals(29, month.getLastDayInMonth());
  }

}