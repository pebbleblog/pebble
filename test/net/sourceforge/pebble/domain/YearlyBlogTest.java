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




/**
 * Tests for the YearlyBlog class.
 *
 * @author    Simon Brown
 */
public class YearlyBlogTest extends SingleBlogTestCase {

  private YearlyBlog yearlyBlog;

  protected void setUp() throws Exception {
    super.setUp();
    yearlyBlog = new YearlyBlog(blog, 2003);
  }

  /**
   * Tests that the root blog is setup correctly.
   */
  public void testGetRootBlog() {
    assertEquals(blog, yearlyBlog.getBlog());
  }

  /**
   * Tests the getter for the year property.
   */
  public void testGetYear() {
    assertEquals(2003, yearlyBlog.getYear());
  }

  /**
   * Tests that we can get the first month containing blog entries.
   */
  public void testFirstMonth() {
    assertEquals(1, yearlyBlog.getBlogForFirstMonth().getMonth());
  }

  /**
   * Tests that we can get a specific month from a year.
   */
  public void testGetMonth() {
    MonthlyBlog monthlyBlog = yearlyBlog.getBlogForMonth(1);
    assertEquals(yearlyBlog, monthlyBlog.getYearlyBlog());
    assertEquals(1, monthlyBlog.getMonth());

    monthlyBlog = yearlyBlog.getBlogForMonth(12);
    assertEquals(yearlyBlog, monthlyBlog.getYearlyBlog());
    assertEquals(12, monthlyBlog.getMonth());

    try {
      monthlyBlog = yearlyBlog.getBlogForMonth(-1);
      fail();
    } catch (IllegalArgumentException iae) {
    }

    try {
      monthlyBlog = yearlyBlog.getBlogForMonth(0);
      fail();
    } catch (IllegalArgumentException iae) {
    }

    try {
      monthlyBlog = yearlyBlog.getBlogForMonth(13);
      fail();
    } catch (IllegalArgumentException iae) {
    }
  }

  /**
   * Tests that toString() works.
   */
  public void testToString() {
    assertEquals("2003", yearlyBlog.toString());
  }

  /**
   * Tests the compareTo method.
   */
  public void testCompareTo() {
    YearlyBlog y1 = new YearlyBlog(blog, 2004);
    YearlyBlog y2 = new YearlyBlog(blog, 2005);
    assertTrue(y1.compareTo(y1) == 0);
    assertTrue(y1.compareTo(y2) < 0);
    assertTrue(y1.compareTo(y2) < 0);
    assertTrue(y2.compareTo(y1) > 0);
  }

}