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
import java.util.Date;

/**
 * Tests for the MonthlyBlog class.
 *
 * @author    Simon Brown
 */
public class MonthlyBlogTest extends SingleBlogTestCase {

  private MonthlyBlog monthlyBlog;

  protected void setUp() throws Exception {
    super.setUp();
    monthlyBlog = new MonthlyBlog(blog.getBlogForThisYear(), 7);
  }

  /**
   * Tests that the root blog is setup correctly.
   */
  public void testGetRootBlog() {
    assertEquals(blog, monthlyBlog.getBlog());
  }

  /**
   * Tests the getter for the month property.
   */
  public void testGetMonth() {
    assertEquals(7, monthlyBlog.getMonth());
  }

  /**
   * Tests the getter for the yearly blog.
   */
  public void testGetYearlyBlog() {
    assertEquals(blog.getBlogForThisYear(), monthlyBlog.getYearlyBlog());
  }

  /**
   * Tests that we can access the previous month.
   */
  public void testGetPreviousMonth() {
    monthlyBlog = blog.getBlogForMonth(2003, 7);
    MonthlyBlog previousMonthlyBlog = monthlyBlog.getPreviousMonth();
    assertEquals(2003, previousMonthlyBlog.getYearlyBlog().getYear());
    assertEquals(6, previousMonthlyBlog.getMonth());

    monthlyBlog = blog.getBlogForMonth(2003, 1);
    previousMonthlyBlog = monthlyBlog.getPreviousMonth();
    assertEquals(2002, previousMonthlyBlog.getYearlyBlog().getYear());
    assertEquals(12, previousMonthlyBlog.getMonth());
  }

  /**
   * Tests that we can access the next month.
   */
  public void testGetNextMonth() {
    monthlyBlog = blog.getBlogForMonth(2003, 7);
    MonthlyBlog nextMonthlyBlog = monthlyBlog.getNextMonth();
    assertEquals(2003, nextMonthlyBlog.getYearlyBlog().getYear());
    assertEquals(8, nextMonthlyBlog.getMonth());

    monthlyBlog = blog.getBlogForMonth(2002, 12);
    nextMonthlyBlog = monthlyBlog.getNextMonth();
    assertEquals(2003, nextMonthlyBlog.getYearlyBlog().getYear());
    assertEquals(1, nextMonthlyBlog.getMonth());
  }

  /**
   * Tests that we can compare monthly blogs.
   */
  public void testBefore() {
    Year yearlyBlog2002 = new Year(blog, 2002);
    Year yearlyBlog2003 = new Year(blog, 2003);
    MonthlyBlog monthlyBlog1 = new MonthlyBlog(yearlyBlog2003, 6);
    MonthlyBlog monthlyBlog2 = new MonthlyBlog(yearlyBlog2003, 7);
    assertTrue(monthlyBlog1.before(monthlyBlog2));
    assertFalse(monthlyBlog2.before(monthlyBlog1));

    monthlyBlog1 = new MonthlyBlog(yearlyBlog2002, 7);
    monthlyBlog2 = new MonthlyBlog(yearlyBlog2003, 7);
    assertTrue(monthlyBlog1.before(monthlyBlog2));
    assertFalse(monthlyBlog2.before(monthlyBlog1));
  }

  /**
   * Tests that we can get all daily blogs for a month.
   */
  public void testGetAllDailyBlogs() {
    monthlyBlog = new MonthlyBlog(blog.getBlogForThisYear(), 1);
    assertEquals(31, monthlyBlog.getAllDailyBlogs().length);
  }

  /**
   * Tests that we can get the daily blog for a specific day.
   */
  public void testGetBlogForDay() {
    DailyBlog dailyBlog = monthlyBlog.getBlogForDay(1);
    assertNotNull(dailyBlog);
    assertEquals(1, dailyBlog.getDay());

    try {
      dailyBlog = monthlyBlog.getBlogForDay(-1);
      fail();
    } catch (IllegalArgumentException iae) {
    }

    try {
      dailyBlog = monthlyBlog.getBlogForDay(0);
      fail();
    } catch (IllegalArgumentException iae) {
    }

    Calendar cal = blog.getCalendar();
    cal.setTime(monthlyBlog.getDate());
    int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    dailyBlog = monthlyBlog.getBlogForDay(maxDay - 1);
    dailyBlog = monthlyBlog.getBlogForDay(maxDay);
    try {
      dailyBlog = monthlyBlog.getBlogForDay(maxDay + 1);
      fail();
    } catch (IllegalArgumentException iae) {
    }

  }

  /**
   * Tests the getter for the date property.
   */
  public void testGetDate() {
    monthlyBlog = blog.getBlogForMonth(2003, 4);
    Date date = monthlyBlog.getDate();
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
    monthlyBlog = blog.getBlogForMonth(2003, 7);
    assertEquals(permalink, monthlyBlog.getPermalink());

    permalink = blog.getUrl() + "2003/12.html";
    monthlyBlog = blog.getBlogForMonth(2003, 12);
    assertEquals(permalink, monthlyBlog.getPermalink());
  }

//  /**
//   * Tests that all blog entries for a month can be accessed.
//   */
//  public void testGetAllBlogEntries() {
//    assertTrue(monthlyBlog.getBlogEntries().isEmpty());
//
//    // now add an entry
//    BlogEntry blogEntry1 = new BlogEntry(blog);
//    BlogEntry blogEntry1 = monthlyBlog.getBlogForFirstDay().createBlogEntry();
//    monthlyBlog.getBlogForFirstDay().addEntry(blogEntry1);
//    assertTrue(monthlyBlog.getBlogEntries().size() == 1);
//    assertTrue(monthlyBlog.getBlogEntries().contains(blogEntry1));
//
//    // now add a second
//    BlogEntry blogEntry2 = monthlyBlog.getBlogForLastDay().createBlogEntry();
//    monthlyBlog.getBlogForLastDay().addEntry(blogEntry2);
//    assertTrue(monthlyBlog.getBlogEntries().size() == 2);
//
//    // check they are reverse ordered by date
//    assertTrue(monthlyBlog.getBlogEntries().get(0) == blogEntry2);
//    assertTrue(monthlyBlog.getBlogEntries().get(1) == blogEntry1);
//  }

  public void testLastDayInMonth() {
    monthlyBlog = new MonthlyBlog(blog.getBlogForYear(2005), 1);
    assertEquals(31, monthlyBlog.getLastDayInMonth());
    monthlyBlog = new MonthlyBlog(blog.getBlogForYear(2005), 2);
    assertEquals(28, monthlyBlog.getLastDayInMonth());
    monthlyBlog = new MonthlyBlog(blog.getBlogForYear(2005), 3);
    assertEquals(31, monthlyBlog.getLastDayInMonth());

    monthlyBlog = new MonthlyBlog(blog.getBlogForYear(2004), 2);
    assertEquals(29, monthlyBlog.getLastDayInMonth());
  }

}