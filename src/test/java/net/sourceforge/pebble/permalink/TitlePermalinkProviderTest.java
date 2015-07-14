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

/**
 * Tests for the TitlePermalinkProvider class.
 *
 * @author    Simon Brown
 */
public class TitlePermalinkProviderTest extends PermalinkProviderSupportTestCase {

  /**
   * Gets a PermalinkProvider instance.
   *
   * @return a PermalinkProvider instance
   */
  protected PermalinkProvider getPermalinkProvider() {
    return new TitlePermalinkProvider();
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
    String prefix = "/";
    prefix += sdf.format(blogEntry.getDate());
    String suffix = ".html";

    blogEntry.setTitle("Here is a title");
    assertEquals(prefix + "here_is_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here's a title");
    assertEquals(prefix + "heres_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here's a title!");
    assertEquals(prefix + "heres_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here_is_a_title");
    assertEquals(prefix + "here_is_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here-is-a-title");
    assertEquals(prefix + "here_is_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here     is    a         title");
    assertEquals(prefix + "here_is_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is : a title");
    assertEquals(prefix + "here_is_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is/a title");
    assertEquals(prefix + "here_is_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is\\a title");
    assertEquals(prefix + "here_is_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is, a title");
    assertEquals(prefix + "here_is_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is; a title");
    assertEquals(prefix + "here_is_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is a title.");
    assertEquals(prefix + "here_is_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle(":Here is a title.");
    assertEquals(prefix + "here_is_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is. a title");
    assertEquals(prefix + "here_is_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("Here is. \u00e0 title"); // Just to make sure diacritics are handled
    assertEquals(prefix + "here_is_a_title" + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle(null);
    assertEquals(prefix + blogEntry.getId() + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("");
    assertEquals(prefix + blogEntry.getId() + suffix, permalinkProvider.getPermalink(blogEntry));

    blogEntry.setTitle("./:!@�$%^&*()");
    assertEquals(prefix + blogEntry.getId() + suffix, permalinkProvider.getPermalink(blogEntry));
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

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy'/'MM'/'dd'/'");
    sdf.setTimeZone(blog.getTimeZone());
    String prefix = "/";
    prefix += sdf.format(blogEntry1.getDate());
    String suffix = ".html";
    assertEquals(prefix + "a_title" + suffix, permalinkProvider.getPermalink(blogEntry1));

    // now add another with the same name
    BlogEntry blogEntry2 = new BlogEntry(blog);
    blogEntry2.setTitle("A Title");
    service.putBlogEntry(blogEntry2);

    assertEquals(prefix + "a_title" + suffix, permalinkProvider.getPermalink(blogEntry1));
    assertEquals(prefix + "a_title_" + blogEntry2.getId() + suffix, permalinkProvider.getPermalink(blogEntry2));
  }

  /**
   * Tests that a blog entry permalink is recognised.
   */
  public void testIsBlogEntryPermalink() {
    assertTrue(permalinkProvider.isBlogEntryPermalink("/2004/01/01/blog_entry_title.html"));
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
    blogEntry1.setTitle("A Title");
    service.putBlogEntry(blogEntry1);

    BlogEntry blogEntry2 = new BlogEntry(blog);
    blogEntry2.setTitle("Some other title");
    service.putBlogEntry(blogEntry2);

    BlogEntry blogEntry3 = new BlogEntry(blog);
    blogEntry3.setTitle("Some other itle");
    service.putBlogEntry(blogEntry3);

    String uri = permalinkProvider.getPermalink(blogEntry1);
    assertEquals(blogEntry1, permalinkProvider.getBlogEntry(uri));
    uri = permalinkProvider.getPermalink(blogEntry2);
    assertEquals(blogEntry2, permalinkProvider.getBlogEntry(uri));
    uri = permalinkProvider.getPermalink(blogEntry3);
    assertEquals(blogEntry3, permalinkProvider.getBlogEntry(uri));
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
    blog.setPermalinkProvider(new TitlePermalinkProvider());

    BlogService service = new BlogService();

    BlogEntry blogEntry = new BlogEntry(blog);
    service.putBlogEntry(blogEntry);

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy'/'MM'/'dd'/'");
    sdf.setTimeZone(blog.getTimeZone());
    String prefix = blog.getUrl();
    prefix += sdf.format(blogEntry.getDate());
    String suffix = ".html";

    blogEntry.setTitle("Here is a title");
    assertEquals(prefix + "here_is_a_title" + suffix, blogEntry.getPermalink());

    blogEntry.setTitle("Here is a new title");
    assertEquals(prefix + "here_is_a_new_title" + suffix, blogEntry.getPermalink());
  }

}
