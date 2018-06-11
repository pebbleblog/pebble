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

package net.sourceforge.pebble.permalink;

import net.sourceforge.pebble.api.permalink.PermalinkProvider;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.Day;
import net.sourceforge.pebble.domain.Month;

import java.util.Calendar;

/**
 * Tests for the Latin1SeoPermalinkProvider class.
 *
 * @author Mattias Reichel
 */
public class Latin1SeoPermalinkProviderTest extends PermalinkProviderSupportTestCase {

  /**
   * Gets a PermalinkProvider instance.
   *
   * @return a PermalinkProvider instance
   */
  protected PermalinkProvider getPermalinkProvider() {
    return new Latin1SeoPermalinkProvider();
  }

  /**
   * Tests that a monthly blog permalink can be generated.
   */
  public void testGetPermalinkForMonth() {
    Month month = blog.getBlogForMonth(2004, 01);
    assertEquals("/2004/01", permalinkProvider.getPermalink(month));
  }

  /**
   * Tests that a monthly blog permalink is recognised.
   */
  public void testMonthPermalink() {
    assertTrue(permalinkProvider.isMonthPermalink("/2004/01"));
    assertFalse(permalinkProvider.isMonthPermalink("/2004/01/01"));
    assertFalse(permalinkProvider.isMonthPermalink("/someotherpage"));
    assertFalse(permalinkProvider.isMonthPermalink(""));
    assertFalse(permalinkProvider.isMonthPermalink(null));
  }

  /**
   * Tests that a day permalink can be generated.
   */
  public void testGetPermalinkForDay() {
    Day day = blog.getBlogForDay(2004, 07, 14);
    assertEquals("/2004/07/14", permalinkProvider.getPermalink(day));
  }

  /**
   * Tests that a day permalink is recognised.
   */
  public void testDayPermalink() {
    assertTrue(permalinkProvider.isDayPermalink("/2004/01/01"));
    assertFalse(permalinkProvider.isDayPermalink("/2004/01"));
    assertFalse(permalinkProvider.isDayPermalink("/someotherpage"));
    assertFalse(permalinkProvider.isDayPermalink(""));
    assertFalse(permalinkProvider.isDayPermalink(null));
  }

  /**
   * Tests that a permalink can be generated for a blog entry.
   */
  public void testBlogEntryPermalink() throws Exception {
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    service.putBlogEntry(blogEntry);

    String prefix = "/";
    String suffix = "";

    blogEntry.setTitle("Here is a title");
    assertEquals(prefix + "here-is-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here's a title");
    assertEquals(prefix + "heres-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here's a title!");
    assertEquals(prefix + "heres-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here_is_a_title");
    assertEquals(prefix + "here-is-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here-is-a-title");
    assertEquals(prefix + "here-is-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here     is    a         title");
    assertEquals(prefix + "here-is-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is : a title");
    assertEquals(prefix + "here-is-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is/a title");
    assertEquals(prefix + "here-is-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is\\a title");
    assertEquals(prefix + "here-is-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is, a title");
    assertEquals(prefix + "here-is-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is; a title");
    assertEquals(prefix + "here-is-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is a title.");
    assertEquals(prefix + "here-is-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle(":Here is a title.");
    assertEquals(prefix + "here-is-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is. a title");
    assertEquals(prefix + "here-is-a-title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle(null);
    assertEquals(prefix + blogEntry.getId() + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("");
    assertEquals(prefix + blogEntry.getId() + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("./:!@�$%^&*()");
    assertEquals(prefix + blogEntry.getId() + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is a title with chars from the latin1 supplemental charset: \u00a0 \u00a1 \u00a2 \u00a3 \u00a4 \u00a5 \u00a6 \u00a7 \u00a8 \u00a9 \u00aa \u00ab \u00ac \u00ad \u00ae \u00af");
    assertEquals(prefix + "here-is-a-title-with-chars-from-the-latin1-supplemental-charset" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is a title with chars from the latin1 supplemental charset: \u00b0 \u00b1 \u00b2 \u00b3 \u00b4 \u00b5 \u00b6 \u00b7 \u00b8 \u00b9 \u00ba \u00bb \u00bc \u00bd \u00be \u00bf");
    assertEquals(prefix + "here-is-a-title-with-chars-from-the-latin1-supplemental-charset-2-3" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is a title with chars from the latin1 supplemental charset: \u00c0 \u00c1 \u00c2 \u00c3 \u00c4 \u00c5 \u00c6 \u00c7 \u00c8 \u00c9 \u00ca \u00cb \u00cc \u00cd \u00ce \u00cf");
    assertEquals(prefix + "here-is-a-title-with-chars-from-the-latin1-supplemental-charset-a-a-a-a-a-a-ae-c-e-e-e-e-i-i-i-i" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is a title with chars from the latin1 supplemental charset: \u00d0 \u00d1 \u00d2 \u00d3 \u00d4 \u00d5 \u00d6 \u00d7 \u00d8 \u00d9 \u00da \u00db \u00dc \u00dd \u00de \u00df");
    assertEquals(prefix + "here-is-a-title-with-chars-from-the-latin1-supplemental-charset-d-n-o-o-o-o-o-x-o-u-u-u-u-y-p-ss" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is a title with chars from the latin1 supplemental charset: \u00e0 \u00e1 \u00e2 \u00e3 \u00e4 \u00e5 \u00e6 \u00e7 \u00e8 \u00e9 \u00ea \u00eb \u00ec \u00ed \u00ee \u00ef");
    assertEquals(prefix + "here-is-a-title-with-chars-from-the-latin1-supplemental-charset-a-a-a-a-a-a-ae-c-e-e-e-e-i-i-i-i" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is a title with chars from the latin1 supplemental charset: \u00f0 \u00f1 \u00f2 \u00f3 \u00f4 \u00f5 \u00f6 \u00f7 \u00f8 \u00f9 \u00fa \u00fb \u00fc \u00fd \u00fe \u00ff");
    assertEquals(prefix + "here-is-a-title-with-chars-from-the-latin1-supplemental-charset-d-n-o-o-o-o-o-o-u-u-u-u-y-p-y" + suffix, permalinkProvider.getPermalink(blogEntry));
  }

  /**
   * Tests that a permalink can be generated for a blog entry when there are
   * duplicate titles for the same day.
   */
  public void testBlogEntryPermalinkForEntriesWithSameTitle() throws Exception {
    BlogService service = new BlogService();

    BlogEntry blogEntry1 = new BlogEntry(blog);
    blogEntry1.setTitle("A Title");
    service.putBlogEntry(blogEntry1);

    String prefix = "/";
    String suffix = "";
    assertEquals(prefix + "a-title" + suffix, permalinkProvider.getPermalink(blogEntry1));

    // now add another with the same name
    BlogEntry blogEntry2 = new BlogEntry(blog);
    blogEntry2.setTitle("A Title");
    service.putBlogEntry(blogEntry2);

    assertEquals(prefix + "a-title" + suffix, permalinkProvider.getPermalink(blogEntry1));
    assertEquals(prefix + "a-title_" + blogEntry2.getId() + suffix, permalinkProvider.getPermalink(blogEntry2));

    // now add another with the same name a year ahead
    BlogEntry blogEntry3 = new BlogEntry(blog);
    blogEntry3.setTitle("A Title");
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, 1);
    blogEntry3.setDate(cal.getTime());
    service.putBlogEntry(blogEntry3);

    assertEquals(prefix + "a-title" + suffix, permalinkProvider.getPermalink(blogEntry1));
    assertEquals(prefix + "a-title_" + blogEntry2.getId() + suffix, permalinkProvider.getPermalink(blogEntry2));
    assertEquals(prefix + "a-title_" + blogEntry3.getId() + suffix, permalinkProvider.getPermalink(blogEntry3));
  }

  /**
   * Tests that a blog entry permalink is recognised.
   */
  public void testIsBlogEntryPermalink() {
    assertTrue(permalinkProvider.isBlogEntryPermalink("/blog-entry-title"));
    assertFalse(permalinkProvider.isBlogEntryPermalink("/2010/05/01/someotherpage.html"));
    assertFalse(permalinkProvider.isBlogEntryPermalink(""));
    assertFalse(permalinkProvider.isBlogEntryPermalink(null));
  }

  /**
   * Tests that the correct blog entry can be found from a permalink.
   */
  public void testGetBlogEntry() throws Exception {
    BlogService service = new BlogService();

    BlogEntry blogEntry1 = new BlogEntry(blog);
    blogEntry1.setTitle("A Title");
    service.putBlogEntry(blogEntry1);

    BlogEntry blogEntry2 = new BlogEntry(blog);
    blogEntry2.setTitle("Some other title");
    service.putBlogEntry(blogEntry2);

    BlogEntry blogEntry3 = new BlogEntry(blog);
    blogEntry3.setTitle("Some other itle");
    service.putBlogEntry(blogEntry3);

    BlogEntry blogEntry4 = new BlogEntry(blog);
    blogEntry4.setTitle("������");
    service.putBlogEntry(blogEntry4);

    String uri = permalinkProvider.getPermalink(blogEntry1);
    assertEquals(blogEntry1, permalinkProvider.getBlogEntry(uri));
    uri = permalinkProvider.getPermalink(blogEntry2);
    assertEquals(blogEntry2, permalinkProvider.getBlogEntry(uri));
    uri = permalinkProvider.getPermalink(blogEntry3);
    assertEquals(blogEntry3, permalinkProvider.getBlogEntry(uri));
    uri = permalinkProvider.getPermalink(blogEntry4);
    assertEquals(blogEntry4, permalinkProvider.getBlogEntry(uri));
  }

  /**
   * Tests that the correct aggregated blog entry can be found from a permalink.
   */
  public void testGetAggregatedBlogEntry() throws Exception {
    BlogService service = new BlogService();

    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setTitle("A Title");
    blogEntry.setOriginalPermalink("http://www.someotherdomain.com/blog/abc.html");
    service.putBlogEntry(blogEntry);

    String uri = permalinkProvider.getPermalink(blogEntry);
    assertEquals(blogEntry, permalinkProvider.getBlogEntry(uri));
  }

  /**
   * Tests that a permalink is changed when the blog entry title changes.
   */
  public void testBlogEntryPermalinkChangesWithTitle() throws Exception {
    blog.setPermalinkProvider(new Latin1SeoPermalinkProvider());

    BlogService service = new BlogService();

    BlogEntry blogEntry = new BlogEntry(blog);
    service.putBlogEntry(blogEntry);

    String prefix = blog.getUrl();
    String suffix = "";

    blogEntry.setTitle("Here is a title");
    assertEquals(prefix + "here-is-a-title" + suffix, blogEntry.getPermalink());

    blogEntry.setTitle("Here is a new title");
    assertEquals(prefix + "here-is-a-new-title" + suffix, blogEntry.getPermalink());
  }

}