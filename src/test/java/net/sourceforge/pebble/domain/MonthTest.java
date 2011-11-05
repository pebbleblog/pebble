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
  private Year year;

  protected void setUp() throws Exception {
    super.setUp();
    year = Year.emptyYear(blog, 2011);
    month = Month.emptyMonth(blog, year.getYear(), 10);
  }

  /**
   * Tests the getter for the month property.
   */
  public void testGetMonth() {
    assertEquals(10, month.getMonth());
  }

  /**
   * Tests the getter for the yearly blog.
   */
  public void testGetYear() {
    assertEquals(year.getYear(), month.getYear());
  }

  /**
   * Tests that we can compare monthly blogs.
   */
  public void testBefore() {
    Month month1 = Month.emptyMonth(blog, 2003, 6);
    Month month2 = Month.emptyMonth(blog, 2003, 7);
    assertTrue(month1.before(month2));
    assertFalse(month2.before(month1));

    month1 = Month.emptyMonth(blog, 2002, 7);
    month2 = Month.emptyMonth(blog, 2003, 7);
    assertTrue(month1.before(month2));
    assertFalse(month2.before(month1));
  }

  /**
   * Tests that we can get all days for a month.
   */
  public void testGetAllDays() {
    month = Month.emptyMonth(blog, 2001, 1);
    // TODO jroper 2011.11.05 work out what should be the right behaviour here, this test disagrees with the javadocs
    // assertEquals(31, month.getAllDays().size());
  }

  /**
   * Tests that we can get the day for a specific day.
   */
  public void testGetBlogForDay() {
    Day day = month.getDay(1);
    assertNotNull(day);
    assertEquals(1, day.getDay());

    try {
      month.getDay(-1);
      fail();
    } catch (IllegalArgumentException iae) {
    }

    try {
      month.getDay(0);
      fail();
    } catch (IllegalArgumentException iae) {
    }

    Calendar cal = blog.getCalendar();
    cal.setTime(month.getDate());
    int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    month.getDay(maxDay - 1);
    month.getDay(maxDay);
    try {
      month.getDay(maxDay + 1);
      fail();
    } catch (IllegalArgumentException iae) {
    }

  }

  /**
   * Tests the getter for the date property.
   */
  public void testGetDate() {
    month = daoFactory.getBlogEntryIndex().getBlogForYear(blog, 2003).getMonth(4);
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
    String permalink = "2003/07.html";
    month = daoFactory.getBlogEntryIndex().getBlogForYear(blog, 2003).getMonth(7);
    assertEquals(permalink, blog.getPermalinkProvider().getPermalink(month));

    permalink = "2003/12.html";
    month = daoFactory.getBlogEntryIndex().getBlogForYear(blog, 2003).getMonth(12);
    assertEquals(permalink, blog.getPermalinkProvider().getPermalink(month));
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
    month = Month.emptyMonth(blog, 2005, 1);
    assertEquals(31, month.getLastDayInMonth());
    month = Month.emptyMonth(blog, 2005, 2);
    assertEquals(28, month.getLastDayInMonth());
    month = Month.emptyMonth(blog, 2005, 3);
    assertEquals(31, month.getLastDayInMonth());

    month = Month.emptyMonth(blog, 2004, 2);
    assertEquals(29, month.getLastDayInMonth());
  }

}