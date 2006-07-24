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
package net.sourceforge.pebble.permalink;

import net.sourceforge.pebble.domain.Day;
import net.sourceforge.pebble.domain.Month;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.api.permalink.PermalinkProvider;

/**
 * Tests for the DefaultPermalinkProvider class.
 *
 * @author    Simon Brown
 */
public abstract class PermalinkProviderSupportTestCase extends SingleBlogTestCase {

  protected PermalinkProvider permalinkProvider;

  protected void setUp() throws Exception {
    super.setUp();

    permalinkProvider = getPermalinkProvider();
    blog.setPermalinkProvider(permalinkProvider);
  }

  /**
   * Gets a PermalinkProvider instance.
   *
   * @return  a PermalinkProvider instance
   */
  protected abstract PermalinkProvider getPermalinkProvider();

  /**
   * Tests that a monthly blog permalink can be generated.
   */
  public void testGetPermalinkForMonthlyBlog() {
    Month month = blog.getBlogForMonth(2004, 01);
    assertEquals("/2004/01.html", permalinkProvider.getPermalink(month));
  }

  /**
   * Tests that a monthly blog permalink is recognised.
   */
  public void testMonthlyBlogPermalink() {
    assertTrue(permalinkProvider.isMonthlyBlogPermalink("/2004/01.html"));
    assertFalse(permalinkProvider.isMonthlyBlogPermalink("/2004/01/01.html"));
    assertFalse(permalinkProvider.isMonthlyBlogPermalink("/someotherpage.html"));
    assertFalse(permalinkProvider.isMonthlyBlogPermalink(""));
    assertFalse(permalinkProvider.isMonthlyBlogPermalink(null));
  }

  /**
   * Tests thet the correct monthly blog can be found from a permalink.
   */
  public void testGetMonthlyBlog() {
    Month month = permalinkProvider.getMonthlyBlog("/2004/07.html");
    assertEquals(2004, month.getYearlyBlog().getYear());
    assertEquals(7, month.getMonth());
  }

  /**
   * Tests that a daily blog permalink can be generated.
   */
  public void testGetPermalinkForDailyBlog() {
    Day day = blog.getBlogForDay(2004, 07, 14);
    assertEquals("/2004/07/14.html", permalinkProvider.getPermalink(day));
  }

  /**
   * Tests that a daily blog permalink is recognised.
   */
  public void testDailyBlogPermalink() {
    assertTrue(permalinkProvider.isDailyBlogPermalink("/2004/01/01.html"));
    assertFalse(permalinkProvider.isDailyBlogPermalink("/2004/01.html"));
    assertFalse(permalinkProvider.isDailyBlogPermalink("/someotherpage.html"));
    assertFalse(permalinkProvider.isDailyBlogPermalink(""));
    assertFalse(permalinkProvider.isDailyBlogPermalink(null));
  }

  /**
   * Tests thet the correct daily blog can be found from a permalink.
   */
  public void testGetDailyBlog() {
    Day day = permalinkProvider.getDailyBlog("/2004/07/14.html");
    assertEquals(2004, day.getMonthlyBlog().getYearlyBlog().getYear());
    assertEquals(7, day.getMonthlyBlog().getMonth());
    assertEquals(14, day.getDay());
  }

}
