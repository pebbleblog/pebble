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

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.api.permalink.PermalinkProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Tests for the DefaultPermalinkProvider class.
 *
 * @author    Simon Brown
 */
public class DefaultPermalinkProviderTest extends PermalinkProviderSupportTestCase {

  /**
   * Gets a PermalinkProvider instance.
   *
   * @return a PermalinkProvider instance
   */
  protected PermalinkProvider getPermalinkProvider() {
    return new DefaultPermalinkProvider();
  }

  /**
   * Tests that a permalink can be generated for a blog entry.
   */
  public void testBlogEntryPermalink() throws Exception {
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    service.putBlogEntry(blogEntry);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy'/'MM'/'dd'/'");
    sdf.setTimeZone(blog.getTimeZone());
    StringBuffer buf = new StringBuffer();
    buf.append("/");
    buf.append(sdf.format(blogEntry.getDate()));
    buf.append(blogEntry.getId());
    buf.append(".html");
    assertEquals(buf.toString(), permalinkProvider.getPermalink(blogEntry));
  }

  /**
   * Tests that a blog entry permalink is recognised.
   */
  public void testIsBlogEntryPermalink() {
    assertTrue(permalinkProvider.isBlogEntryPermalink("/2004/01/01/1234567890123.html"));
    assertTrue(permalinkProvider.isBlogEntryPermalink("/2004/01/01/123456789012.html"));
    assertFalse(permalinkProvider.isBlogEntryPermalink("/1234567890123.html"));
    assertFalse(permalinkProvider.isBlogEntryPermalink("/someotherpage.html"));
    assertFalse(permalinkProvider.isBlogEntryPermalink(""));
    assertFalse(permalinkProvider.isBlogEntryPermalink(null));
  }

  /**
   * Tests that the correct blog entry can be found from a permalink.
   */
  public void testGetBlogEntry() throws Exception {
    BlogService service = new BlogService();
    BlogEntry blogEntry1 = new BlogEntry(blog);
    BlogEntry blogEntry2 = new BlogEntry(blog);
    service.putBlogEntry(blogEntry1);

    Calendar cal = blog.getCalendar();
    cal.set(Calendar.YEAR, 2000);
    blogEntry2.setDate(cal.getTime());
    service.putBlogEntry(blogEntry2);

    String uri = permalinkProvider.getPermalink(blogEntry1);
    assertEquals(blogEntry1, permalinkProvider.getBlogEntry(uri));
    uri = permalinkProvider.getPermalink(blogEntry2);
    assertEquals(blogEntry2, permalinkProvider.getBlogEntry(uri));
  }

}
