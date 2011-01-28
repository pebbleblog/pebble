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
package net.sourceforge.pebble.logging;

import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.permalink.TitlePermalinkProvider;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.SingleBlogTestCase;

/**
 * Tests for the Request class.
 *
 * @author    Simon Brown
 */
public class RequestTest extends SingleBlogTestCase {

  private Request url;

  protected void setUp() throws Exception {
    super.setUp();

    url = new Request("http://www.somedomain.com");
    url.addLogEntry(new LogEntry());
  }

  public void testConstruction() {
    assertEquals("http://www.somedomain.com", url.getName());
    assertEquals("http://www.somedomain.com", url.getUrl());
    assertEquals(1, url.getCount());
  }

  public void testIncrementingCount() {
    assertEquals(1, url.getCount());
    url.addLogEntry(new LogEntry());
    assertEquals(2, url.getCount());
  }

  public void testShortUrlIsNotTruncated() {
    String s = "http://www.somedomain.com";
    url = new Request(s);
    assertEquals("http://www.somedomain.com", url.getTruncatedName());
  }

  public void testLongUrlIsTruncated() {
    String s = "http://www.somedomain.com/here/is/a/long/url/abcdefghijklmnopqrstuvwxyz012345678012345678901234567890123456789";
    url = new Request(s);
    assertEquals(s.substring(0, Request.NAME_LENGTH_LIMIT - 3) + "...", url.getTruncatedName());
  }

  public void testEmptyUrl() {
    url = new Request("");
    assertEquals("", url.getUrl());
    assertEquals("None", url.getName());
  }

  public void testNullUrl() {
    url = new Request(null);
    assertEquals(null, url.getUrl());
    assertEquals("None", url.getName());
  }

  public void testHashCode() {
    url = new Request(null);
    assertEquals(0, url.hashCode());
    url = new Request("http://www.somedomain.com");
    assertEquals("http://www.somedomain.com".hashCode(), url.hashCode());
  }

  public void testEquals() {
    Request url1 = new Request("http://www.somedomain.com");
    Request url2 = new Request("http://www.yahoo.com");
    Request url3 = new Request(null);
    Request url4 = new Request(null);

    assertFalse(url1.equals(null));
    assertFalse(url1.equals("http://www.somedomain.com"));
    assertTrue(url1.equals(url1));
    assertFalse(url1.equals(url2));
    assertFalse(url2.equals(url1));
    assertFalse(url1.equals(url3));
    assertFalse(url3.equals(url1));
    assertTrue(url3.equals(url4));
  }

  /**
   * Test that friendly names are used for news feeds.
   */
  public void testFriendlyNamesForNewsFeeds() {
    url = new Request("http://...rss.xml");
    assertEquals("Feed : Blog Entries", url.getName());
    url = new Request("http://...rss.xml?category=java");
    assertEquals("Feed : Blog Entries", url.getName());

    url = new Request("http://...feed.xml");
    assertEquals("Feed : Blog Entries", url.getName());
    url = new Request("http://...feed.xml?category=java");
    assertEquals("Feed : Blog Entries", url.getName());

    url = new Request("http://...feed.action");
    assertEquals("Feed : Blog Entries", url.getName());
    url = new Request("http://...feed.action?category=java");
    assertEquals("Feed : Blog Entries", url.getName());

    url = new Request("http://...rdf.xml");
    assertEquals("Feed : Blog Entries", url.getName());
    url = new Request("http://...rdf.xml?category=java");
    assertEquals("Feed : Blog Entries", url.getName());

    url = new Request("http://...atom.xml");
    assertEquals("Feed : Blog Entries", url.getName());
    url = new Request("http://...atom.xml?category=java");
    assertEquals("Feed : Blog Entries", url.getName());
  }

  public void testFriendlyNamesForHomePage() throws Exception {
    url = new Request("/", blog);
    assertEquals("Home", url.getName());
  }

  public void testFriendlyNamesForBlogEntries() throws Exception {
    BlogEntry be = new BlogEntry(blog);
    be.setTitle("Test blog entry");
    BlogService service = new BlogService();
    service.putBlogEntry(be);
    String permalink = "/" + be.getPermalink().substring(PebbleContext.getInstance().getConfiguration().getUrl().length());
    url = new Request(permalink, blog);
    assertEquals("Blog Entry : Test blog entry", url.getName());
  }

  public void testFriendlyNamesForBlogEntriesUsingDefaultPermalinkProvider() throws Exception {
    BlogEntry be = new BlogEntry(blog);
    be.setTitle("Test blog entry");
    BlogService service = new BlogService();
    service.putBlogEntry(be);
    String permalink = "/" + be.getPermalink().substring(PebbleContext.getInstance().getConfiguration().getUrl().length());
    blog.setPermalinkProvider(new TitlePermalinkProvider());
    url = new Request(permalink, blog);
    assertEquals("Blog Entry : Test blog entry", url.getName());
  }

  public void testFriendlyNamesForTagPage() throws Exception {
    url = new Request("/tags/", blog);
    assertEquals("Tags", url.getName());
    url = new Request("/tags", blog);
    assertEquals("Tags", url.getName());

    url = new Request("/tags/java", blog);
    assertEquals("Tag : java", url.getName());
    url = new Request("/tags/java/", blog);
    assertEquals("Tag : java", url.getName());
  }

  public void testFriendlyNamesForCategoryPage() throws Exception {
    url = new Request("/categories/", blog);
    assertEquals("Categories", url.getName());
    url = new Request("/categories", blog);
    assertEquals("Categories", url.getName());

    url = new Request("/categories/java", blog);
    assertEquals("Category : java", url.getName());
    url = new Request("/categories/java/", blog);
    assertEquals("Category : java", url.getName());
  }

  public void testFriendlyNamesForFile() throws Exception {
    url = new Request("/files/", blog);
    assertEquals("Files", url.getName());
    url = new Request("/files", blog);
    assertEquals("Files", url.getName());

    url = new Request("/files/test.txt", blog);
    assertEquals("File : test.txt", url.getName());
    url = new Request("/files/directory/test.txt", blog);
    assertEquals("File : directory/test.txt", url.getName());
  }

  public void testFriendlyNamesForBlogEntriesByPage() throws Exception {
    url = new Request("/blogentries/1.html", blog);
    assertEquals("Blog Entries : Page 1", url.getName());
  }

  public void testFriendlyNamesForMonthPages() throws Exception {
    url = new Request("/2007/07.html", blog);
    assertEquals("Month : July 2007", url.getName());
  }

  public void testFriendlyNamesForDayPages() throws Exception {
    url = new Request("/2007/07/01.html", blog);
    assertEquals("Day : 01 July 2007", url.getName());
  }

  public void testFriendlyNamesForResponsesFeed() throws Exception {
    url = new Request("/responses/rss.xml", blog);
    assertEquals("Feed : Responses", url.getName());
    url = new Request("/responses/atom.xml", blog);
    assertEquals("Feed : Responses", url.getName());
  }

  public void testFriendlyNamesForCategoryFeeds() throws Exception {
    url = new Request("/categories/java/rss.xml", blog);
    assertEquals("Feed : category=java", url.getName());
    url = new Request("/categories/java/atom.xml", blog);
    assertEquals("Feed : category=java", url.getName());

    url = new Request("/categories/java/junit/rss.xml", blog);
    assertEquals("Feed : category=java/junit", url.getName());
    url = new Request("/categories/java/junit/atom.xml", blog);
    assertEquals("Feed : category=java/junit", url.getName());
  }

  public void testFriendlyNamesForTagFeeds() throws Exception {
    url = new Request("/tags/java/rss.xml", blog);
    assertEquals("Feed : tag=java", url.getName());
    url = new Request("/tags/java/atom.xml", blog);
    assertEquals("Feed : tag=java", url.getName());
  }

  public void testFriendlyNamesForAuthorFeeds() throws Exception {
    url = new Request("/authors/sbrown/rss.xml", blog);
    assertEquals("Feed : author=sbrown", url.getName());
    url = new Request("/authors/sbrown/atom.xml", blog);
    assertEquals("Feed : author=sbrown", url.getName());
  }

  public void testFriendlyNamesForAuthorPage() throws Exception {
    url = new Request("/authors/sbrown/", blog);
    assertEquals("Author : sbrown", url.getName());
    url = new Request("/authors/sbrown", blog);
    assertEquals("Author : sbrown", url.getName());
  }

  public void testFriendlyNamesForStaticPage() throws Exception {
    url = new Request("/pages/sbrown.html", blog);
    assertEquals("Static Page : sbrown.html", url.getName());
    url = new Request("/pages/authors/sbrown.html", blog);
    assertEquals("Static Page : authors/sbrown.html", url.getName());
  }

  public void testFriendlyNamesForSearches() throws Exception {
    url = new Request("/search.action", blog);
    assertEquals("Search", url.getName());
    url = new Request("/search.action?query=java", blog);
    assertEquals("Search", url.getName());
  }

}