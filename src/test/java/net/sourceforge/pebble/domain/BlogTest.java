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

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.event.DefaultEventDispatcher;
import net.sourceforge.pebble.api.event.blog.BlogEvent;
import net.sourceforge.pebble.api.event.blog.BlogListener;
import net.sourceforge.pebble.logging.CombinedLogFormatLogger;
import net.sourceforge.pebble.permalink.DefaultPermalinkProvider;

import java.io.File;
import java.util.*;

/**
 * Tests for the Blog class.
 *
 * @author    Simon Brown
 */
public class BlogTest extends SingleBlogTestCase {

  public void testConstructionOfDefaultInstance() {
    assertEquals(new File(TEST_BLOG_LOCATION, "blogs/default").getAbsolutePath(), blog.getRoot());
    assertNull(blog.getBlog());
    assertEquals("My blog", blog.getName());
    assertEquals("", blog.getDescription());
    assertEquals("Blog Owner", blog.getAuthor());
    assertEquals("blog@yourdomain.com", blog.getEmail());
    assertEquals(TimeZone.getTimeZone("Europe/London"), blog.getTimeZone());
    assertEquals("en", blog.getLanguage());
    assertEquals("GB", blog.getCountry());
    assertEquals("UTF-8", blog.getCharacterEncoding());
    assertEquals(3, blog.getRecentBlogEntriesOnHomePage());
    assertEquals(3, blog.getRecentResponsesOnHomePage());
    assertTrue(blog.isPublic());
    assertFalse(blog.isPrivate());
    assertEquals("net.sourceforge.pebble.permalink.DefaultPermalinkProvider", blog.getPermalinkProviderName());
    assertTrue(blog.getPermalinkProvider() instanceof DefaultPermalinkProvider);
    assertEquals("net.sourceforge.pebble.event.DefaultEventDispatcher", blog.getEventDispatcherName());
    assertTrue(blog.getEventDispatcher() instanceof DefaultEventDispatcher);
    assertEquals("net.sourceforge.pebble.logging.CombinedLogFormatLogger", blog.getLoggerName());
    assertTrue(blog.getLogger() instanceof CombinedLogFormatLogger);
  }

  /**
   * Tests that we can get a specific property.
   */
  public void testGetProperty() {
    assertEquals(blog.getName(), blog.getProperty(Blog.NAME_KEY));
  }

  /**
   * Tests that we can get a specific property.
   */
  public void testGetProperties() {
    Properties props = blog.getProperties();
    assertNotNull(props);
    assertEquals(blog.getName(), props.getProperty(Blog.NAME_KEY));
  }

  /**
   * Tests that we can set a specific property.
   */
  public void testSetProperty() {
    blog.setProperty(Blog.NAME_KEY, "New name");
    assertEquals("New name", blog.getProperty(Blog.NAME_KEY));
    assertEquals("New name", blog.getName());

    // and a new property
    blog.setProperty("aNewPropertyKey", "A new property value");
    assertEquals("A new property value", blog.getProperty("aNewPropertyKey"));
  }

  /**
   * Tests that we can store properties.
   public void testStoreProperties() {
   blog.setProperty("aNewPropertyKey", "A new property value");
   try {
   blog.storeProperties();

   blog = new Blog(TEST_BLOG_LOCATION.getAbsolutePath());
   assertEquals("A new property value", blog.getProperty("aNewPropertyKey"));

   // and clean up
   blog.removeProperty("aNewPropertyKey");
   blog.storeProperties();
   } catch (BlogServiceException e) {
   fail();
   }
   }
   */

  /**
   * Tests that we can remove a specific property.
   */
  public void testRemoveProperty() {
    blog.setProperty("aNewPropertyKey", "A new property value");
    assertEquals("A new property value", blog.getProperty("aNewPropertyKey"));
    blog.removeProperty("aNewPropertyKey");
    assertNull(blog.getProperty("aNewPropertyKey"));
  }

  /**
   * Tests that the correct calendar (with timezone) is created.
   */
  public void testCalendar() {
    Calendar cal = blog.getCalendar();
    assertEquals(blog.getTimeZone(), cal.getTimeZone());
  }

  /**
   * Tests that we can get a specific Year instance.
   */
   public void testGetBlogForYear() {
   Calendar cal = blog.getCalendar();
   Year year = blog.getBlogForYear(cal.get(Calendar.YEAR));
   assertNotNull(year);
   assertEquals(cal.get(Calendar.YEAR), year.getYear());
   }

   /**
    * Tests that we can get a previous Year instance.
    */
   public void testGetBlogForPreviousYear() {
   Calendar cal = blog.getCalendar();
   Year year = blog.getBlogForYear(cal.get(Calendar.YEAR));
   year = blog.getBlogForPreviousYear(year);
   assertNotNull(year);
   assertEquals(cal.get(Calendar.YEAR)-1, year.getYear());
   }

   /**
    * Tests that we can get a next Year instance.
    */
   public void testGetBlogForNextYear() {
   Calendar cal = blog.getCalendar();
   Year year = blog.getBlogForYear(cal.get(Calendar.YEAR));
   year = blog.getBlogForNextYear(year);
   assertNotNull(year);
   assertEquals(cal.get(Calendar.YEAR)+1, year.getYear());
   }

  /**
   * Tests that we can get the first Month instance.
   */
  public void testGetBlogForFirstMonth() {
    Month month = blog.getBlogForFirstMonth();
    assertNotNull(month);
//    assertEquals(blog.getBlogForFirstYear(), month.getYear());
    Calendar cal = blog.getCalendar();
    assertEquals(cal.get(Calendar.MONTH)+1, month.getMonth());
  }

  /**
   * Tests that we can get a Month instance.
   */
  public void testGetBlogForMonth() {
    Month month = blog.getBlogForMonth(2003, 4);
    assertNotNull(month);
    assertEquals(2003, month.getYear().getYear());
    assertEquals(4, month.getMonth());
  }

  /**
   * Tests that we can get the Month instance for this month.
   */
  public void testGetBlogForThisMonth() {
    Calendar cal = blog.getCalendar();
    Month month = blog.getBlogForThisMonth();
    assertNotNull(month);
    assertEquals(cal.get(Calendar.YEAR), month.getYear().getYear());
    assertEquals(cal.get(Calendar.MONTH) + 1, month.getMonth());
  }

  /**
   * Tests that we can get a Day instance.
   */
  public void testGetBlogForDay() {
    Day day = blog.getBlogForDay(2003, 7, 14);
    assertNotNull(day);
    assertEquals(2003, day.getMonth().getYear().getYear());
    assertEquals(7, day.getMonth().getMonth());
    assertEquals(14, day.getDay());
  }

  /**
   * Tests that we can get a Day instance.
   */
  public void testGetBlogForDate() {
    Calendar cal = blog.getCalendar();
    cal.set(Calendar.YEAR, 2003);
    cal.set(Calendar.MONTH, 6);
    cal.set(Calendar.DAY_OF_MONTH, 14);
    Day day = blog.getBlogForDay(cal.getTime());
    assertNotNull(day);
    assertEquals(2003, day.getMonth().getYear().getYear());
    assertEquals(7, day.getMonth().getMonth());
    assertEquals(14, day.getDay());
  }

  /**
   * Tests that we can get the Day instance for today.
   */
  public void testGetBlogForToday() {
    Calendar cal = blog.getCalendar();
    Day day = blog.getBlogForToday();
    assertNotNull(day);
    assertEquals(cal.get(Calendar.YEAR), day.getMonth().getYear().getYear());
    assertEquals(cal.get(Calendar.MONTH) + 1, day.getMonth().getMonth());
    assertEquals(cal.get(Calendar.DAY_OF_MONTH), day.getDay());
  }

  /**
   * Tests that blog owners can be assigned.
   */
  public void testAssignBlogOwners() {
    blog.setProperty(Blog.BLOG_OWNERS_KEY, "user1");
    assertEquals("user1", blog.getProperty(Blog.BLOG_OWNERS_KEY));
    assertEquals("user1", blog.getBlogOwnersAsString());

    Collection users = blog.getUsersInRole(Constants.BLOG_OWNER_ROLE);
    assertEquals(1, users.size());
    assertTrue(users.contains("user1"));

    blog.setProperty(Blog.BLOG_OWNERS_KEY, "user1,user2");
    assertEquals("user1,user2", blog.getProperty(Blog.BLOG_OWNERS_KEY));
    assertEquals("user1,user2", blog.getBlogOwnersAsString());

    users = blog.getUsersInRole(Constants.BLOG_OWNER_ROLE);
    assertEquals(2, users.size());
    assertTrue(users.contains("user1"));
    assertTrue(users.contains("user2"));
  }

  /**
   * Tests that blog owners can be assigned.
   */
  public void testNullBlogOwners() {
    blog.removeProperty(Blog.BLOG_OWNERS_KEY);
    assertEquals(null, blog.getBlogOwnersAsString());

    Collection users = blog.getUsersInRole(Constants.BLOG_OWNER_ROLE);
    assertEquals(0, users.size());
  }

  /**
   * Tests that it can be determined that a user is a blog owner.
   */
  public void testUserIsBlogOwner() {
    blog.setProperty(Blog.BLOG_OWNERS_KEY, "user1");
    assertTrue(blog.isUserInRole(Constants.BLOG_OWNER_ROLE, "user1"));
    assertFalse(blog.isUserInRole(Constants.BLOG_OWNER_ROLE, "user2"));
  }

  /**
   * Tests that when no blog contributors are specified, then everybody takes
   * on that role.
   */
  public void testUserIsBlogOwnerByDefault() {
    blog.removeProperty(Blog.BLOG_OWNERS_KEY);
    assertTrue(blog.isUserInRole(Constants.BLOG_OWNER_ROLE, "user1"));
    assertTrue(blog.isUserInRole(Constants.BLOG_OWNER_ROLE, "usern"));
  }

  /**
   * Tests that blog contributors can be assigned.
   */
  public void testAssignBlogContributors() {
    blog.setProperty(Blog.BLOG_CONTRIBUTORS_KEY, "user1");
    assertEquals("user1", blog.getProperty(Blog.BLOG_CONTRIBUTORS_KEY));
    assertEquals("user1", blog.getBlogContributorsAsString());

    Collection users = blog.getUsersInRole(Constants.BLOG_CONTRIBUTOR_ROLE);
    assertEquals(1, users.size());
    assertTrue(users.contains("user1"));

    blog.setProperty(Blog.BLOG_CONTRIBUTORS_KEY, "user1,user2");
    assertEquals("user1,user2", blog.getProperty(Blog.BLOG_CONTRIBUTORS_KEY));
    assertEquals("user1,user2", blog.getBlogContributorsAsString());

    users = blog.getUsersInRole(Constants.BLOG_CONTRIBUTOR_ROLE);
    assertEquals(2, users.size());
    assertTrue(users.contains("user1"));
    assertTrue(users.contains("user2"));

    blog.setProperty(Blog.BLOG_CONTRIBUTORS_KEY, "user1, user2");
    assertEquals("user1, user2", blog.getProperty(Blog.BLOG_CONTRIBUTORS_KEY));
    assertEquals("user1, user2", blog.getBlogContributorsAsString());

    users = blog.getUsersInRole(Constants.BLOG_CONTRIBUTOR_ROLE);
    assertEquals(2, users.size());
    assertTrue(users.contains("user1"));
    assertTrue(users.contains("user2"));
  }

  /**
   * Tests that blog contributors can be assigned.
   */
  public void testNullBlogContributors() {
    blog.removeProperty(Blog.BLOG_CONTRIBUTORS_KEY);
    assertEquals(null, blog.getBlogContributorsAsString());

    Collection users = blog.getUsersInRole(Constants.BLOG_CONTRIBUTOR_ROLE);
    assertEquals(0, users.size());
  }

  /**
   * Tests that it can be determined that a user is a blog contributor.
   */
  public void testUserIsBlogContributor() {
    blog.setProperty(Blog.BLOG_CONTRIBUTORS_KEY, "user1");
    assertTrue(blog.isUserInRole(Constants.BLOG_CONTRIBUTOR_ROLE, "user1"));
    assertFalse(blog.isUserInRole(Constants.BLOG_CONTRIBUTOR_ROLE, "user2"));
  }

  /**
   * Tests that when no blog contributors are specified, then everybody takes
   * on that role.
   */
  public void testUserIsBlogContributorByDefault() {
    blog.removeProperty(Blog.BLOG_CONTRIBUTORS_KEY);
    assertTrue(blog.isUserInRole(Constants.BLOG_CONTRIBUTOR_ROLE, "user1"));
    assertTrue(blog.isUserInRole(Constants.BLOG_CONTRIBUTOR_ROLE, "usern"));
  }

  public void testInvalidDayOfMonthAfterTimeZoneChanges() {
    blog.getRecentBlogEntries();
    blog.setProperty(Blog.TIMEZONE_KEY, "America/New_York");

    // this should not cause an exception to be thrown
    blog.getRecentBlogEntries();
  }

  public void testGetRecentBlogEntriesFromEmptyBlog() {
    assertTrue(blog.getRecentBlogEntries(3).isEmpty());
  }

  public void testGetRecentBlogEntries() throws BlogServiceException {
    BlogService service = new BlogService();

    BlogEntry entry1 = new BlogEntry(blog);
    entry1.setTitle("title1");
    entry1.setBody("body1");
    service.putBlogEntry(entry1);

    BlogEntry entry2 = new BlogEntry(blog);
    entry2.setTitle("title2");
    entry2.setBody("body2");
    service.putBlogEntry(entry2);

    BlogEntry entry3 = new BlogEntry(blog);
    entry3.setTitle("title3");
    entry3.setBody("body3");
    service.putBlogEntry(entry3);

    BlogEntry entry4 = new BlogEntry(blog);
    entry4.setTitle("title4");
    entry4.setBody("body4");
    service.putBlogEntry(entry4);

    List entries = blog.getRecentBlogEntries(3);

    assertEquals(3, entries.size());
    assertEquals(entry4, entries.get(0));
  }

  /**
   * Tests the images directory is correct and that it exists.
   */
  public void testImagesDirectoryAccessible() {
    File file = new File(blog.getRoot(), "images");
    assertEquals(file, new File(blog.getImagesDirectory()));
    assertTrue(file.exists());
  }

  /**
   * Tests the files directory is correct and that it exists.
   */
  public void testFilesDirectoryAccessible() {
    File file = new File(blog.getRoot(), "files");
    assertEquals(file, new File(blog.getFilesDirectory()));
    assertTrue(file.exists());
  }

  /**
   * Tests the theme directory is correct and that it doesn't exist by default
   *  - starting up Pebble creates a theme based on the template if the theme
   *  - directory doesn't exist.
   */
  public void testThemeDirectoryAccessible() {
    File file = new File(blog.getRoot(), "theme");
    assertEquals(file, new File(blog.getThemeDirectory()));
    assertTrue(file.exists());
  }

  /**
   * Tests setting a single e-mail address.
   */
  public void testSingleEmailAddress() {
    blog.setProperty(Blog.EMAIL_KEY, "me@mydomain.com");
    assertEquals("me@mydomain.com", blog.getEmail());
    assertEquals(1, blog.getEmailAddresses().size());
    assertEquals("me@mydomain.com", blog.getEmailAddresses().iterator().next());
  }

  /**
   * Tests setting multiple e-mail address.
   */
  public void testMultipleEmailAddresses() {
    blog.setProperty(Blog.EMAIL_KEY, "me@mydomain.com,you@yourdomain.com");
    assertEquals("me@mydomain.com,you@yourdomain.com", blog.getEmail());
    assertEquals(2, blog.getEmailAddresses().size());
    Iterator it = blog.getEmailAddresses().iterator();
    assertEquals("me@mydomain.com", it.next());
    assertEquals("you@yourdomain.com", it.next());
  }

  /**
   * Tests getting the first of multiple e-mail addresses.
   */
  public void testFirstEmailAddress() {
    blog.setProperty(Blog.EMAIL_KEY, "");
    assertEquals("", blog.getFirstEmailAddress());
    blog.setProperty(Blog.EMAIL_KEY, "me@mydomain.com");
    assertEquals("me@mydomain.com", blog.getFirstEmailAddress());
    blog.setProperty(Blog.EMAIL_KEY, "me@mydomain.com,you@yourdomain.com");
    assertEquals("me@mydomain.com", blog.getFirstEmailAddress());
  }

  /**
   * Tests the domain.
   */
  public void testDomain() {
    assertEquals("www.yourdomain.com", blog.getDomainName());

    PebbleContext.getInstance().getConfiguration().setUrl("http://www.yourdomain.com:8080/blog");
    assertEquals("www.yourdomain.com", blog.getDomainName());
  }

  /**
   * Tests the protocol.
   */
  public void testProtocol() {
    assertEquals("http://", blog.getProtocol());
  }

  /**
   * Tests the context.
   */
  public void testContext() {
    assertEquals("/blog/", blog.getContext());

    PebbleContext.getInstance().getConfiguration().setUrl("http://www.yourdomain.com:8080");
    assertEquals("/", blog.getContext());

    PebbleContext.getInstance().getConfiguration().setUrl("http://www.yourdomain.com:8080/");
    assertEquals("/", blog.getContext());
  }

  /**
   * Tests the logger can be accessed and is of the correct type.
   */
  public void testLogger() {
    assertNotNull(blog.getLogger());
    assertTrue(blog.getLogger() instanceof CombinedLogFormatLogger);
  }

  /**
   * Tests that listeners are fired when the blog is started.
   */
  public void testListenersFiredWhenBlogStarted() {
    final StringBuffer buf = new StringBuffer("123");
    BlogListener listener = new BlogListener() {
      public void blogStarted(BlogEvent event) {
        assertEquals(blog, event.getSource());
        buf.reverse();
      }

      public void blogStopped(BlogEvent event) {
        fail();
      }
    };
    blog.getEventListenerList().addBlogListener(listener);
    blog.start();
    assertEquals("321", buf.toString());
    blog.getEventListenerList().removeBlogListener(listener);
    blog.stop();
  }

  /**
   * Tests that listeners are fired when the blog is stopped.
   */
  public void testListenersFiredWhenBlogStopped() {
    final StringBuffer buf = new StringBuffer("123");
    BlogListener listener = new BlogListener() {
      public void blogStarted(BlogEvent event) {
        fail();
      }

      public void blogStopped(BlogEvent event) {
        assertEquals(blog, event.getSource());
        buf.reverse();
      }
    };
    blog.getEventListenerList().addBlogListener(listener);
    blog.stop();
    assertEquals("321", buf.toString());
  }

  public void testApprovedCommentsForUnpublishedBlogEntriesDontShowUp() throws BlogServiceException {
    BlogService service = new BlogService();

    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setTitle("title1");
    blogEntry.setBody("body1");
    blogEntry.setPublished(false);
    service.putBlogEntry(blogEntry);

    Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    blogEntry.addComment(comment);
    service.putBlogEntry(blogEntry);

    assertFalse(blog.getRecentApprovedResponses().contains(comment));
  }

}