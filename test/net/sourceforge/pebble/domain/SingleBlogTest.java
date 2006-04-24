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

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.event.DefaultEventDispatcher;
import net.sourceforge.pebble.event.blog.BlogEvent;
import net.sourceforge.pebble.event.blog.BlogListener;
import net.sourceforge.pebble.logging.CombinedLogFormatLogger;
import net.sourceforge.pebble.plugin.permalink.DefaultPermalinkProvider;

import java.io.File;
import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Tests for the Blog class.
 *
 * @author    Simon Brown
 */
public class SingleBlogTest extends SingleBlogTestCase {

  private static final Log log = LogFactory.getLog(SingleBlogTest.class);

  public void testConstructionOfDefaultInstance() {
    assertEquals(TEST_BLOG_LOCATION.getAbsolutePath(), blog.getRoot());
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
    assertEquals(3, blog.getRecentCommentsOnHomePage());
    assertTrue(blog.getUpdateNotificationPingsAsCollection().isEmpty());
    assertTrue(blog.isPublic());
    assertFalse(blog.isPrivate());
    assertEquals("net.sourceforge.pebble.plugin.decorator.HideUnapprovedBlogEntriesDecorator\r\nnet.sourceforge.pebble.plugin.decorator.HideUnapprovedResponsesDecorator\r\nnet.sourceforge.pebble.plugin.decorator.HtmlDecorator\r\nnet.sourceforge.pebble.plugin.decorator.EscapeMarkupDecorator\r\nnet.sourceforge.pebble.plugin.decorator.RelativeUriDecorator\r\nnet.sourceforge.pebble.plugin.decorator.BlogTagsDecorator", blog.getBlogEntryDecorators());
    assertEquals("net.sourceforge.pebble.plugin.permalink.DefaultPermalinkProvider", blog.getPermalinkProviderName());
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
   } catch (BlogException e) {
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
   * Tests that we can get a specific YearlyBlog instance.
   public void testGetBlogForYear() {
   Calendar cal = blog.getCalendar();
   YearlyBlog yearlyBlog = blog.getBlogForYear(cal.get(Calendar.YEAR));
   assertNotNull(yearlyBlog);
   assertEquals(cal.get(Calendar.YEAR), yearlyBlog.getYear());
   }

   /**
   * Tests that we can get a previous YearlyBlog instance.
   public void testGetBlogForPreviousYear() {
   Calendar cal = blog.getCalendar();
   YearlyBlog yearlyBlog = blog.getBlogForYear(cal.get(Calendar.YEAR));
   yearlyBlog = blog.getBlogForPreviousYear(yearlyBlog);
   assertNotNull(yearlyBlog);
   assertEquals(cal.get(Calendar.YEAR)-1, yearlyBlog.getYear());
   }

   /**
   * Tests that we can get a next YearlyBlog instance.
   public void testGetBlogForNextYear() {
   Calendar cal = blog.getCalendar();
   YearlyBlog yearlyBlog = blog.getBlogForYear(cal.get(Calendar.YEAR));
   yearlyBlog = blog.getBlogForNextYear(yearlyBlog);
   assertNotNull(yearlyBlog);
   assertEquals(cal.get(Calendar.YEAR)+1, yearlyBlog.getYear());
   }
   */

  /**
   * Tests that we can get the first MonthlyBlog instance.
   */
  public void testGetBlogForFirstMonth() {
    MonthlyBlog monthlyBlog = blog.getBlogForFirstMonth();
    assertNotNull(monthlyBlog);
//    assertEquals(blog.getBlogForFirstYear(), monthlyBlog.getYearlyBlog());
    assertEquals(1, monthlyBlog.getMonth());
  }

  /**
   * Tests that we can get a MonthlyBlog instance.
   */
  public void testGetBlogForMonth() {
    MonthlyBlog monthlyBlog = blog.getBlogForMonth(2003, 4);
    assertNotNull(monthlyBlog);
    assertEquals(2003, monthlyBlog.getYearlyBlog().getYear());
    assertEquals(4, monthlyBlog.getMonth());
  }

  /**
   * Tests that we can get the MonthlyBlog instance for this month.
   */
  public void testGetBlogForThisMonth() {
    Calendar cal = blog.getCalendar();
    MonthlyBlog monthlyBlog = blog.getBlogForThisMonth();
    assertNotNull(monthlyBlog);
    assertEquals(cal.get(Calendar.YEAR), monthlyBlog.getYearlyBlog().getYear());
    assertEquals(cal.get(Calendar.MONTH) + 1, monthlyBlog.getMonth());
  }

  /**
   * Tests that we can get a DailyBlog instance.
   */
  public void testGetBlogForDay() {
    DailyBlog dailyBlog = blog.getBlogForDay(2003, 7, 14);
    assertNotNull(dailyBlog);
    assertEquals(2003, dailyBlog.getMonthlyBlog().getYearlyBlog().getYear());
    assertEquals(7, dailyBlog.getMonthlyBlog().getMonth());
    assertEquals(14, dailyBlog.getDay());
  }

  /**
   * Tests that we can get a DailyBlog instance.
   */
  public void testGetBlogForDate() {
    Calendar cal = blog.getCalendar();
    cal.set(Calendar.YEAR, 2003);
    cal.set(Calendar.MONTH, 6);
    cal.set(Calendar.DAY_OF_MONTH, 14);
    DailyBlog dailyBlog = blog.getBlogForDay(cal.getTime());
    assertNotNull(dailyBlog);
    assertEquals(2003, dailyBlog.getMonthlyBlog().getYearlyBlog().getYear());
    assertEquals(7, dailyBlog.getMonthlyBlog().getMonth());
    assertEquals(14, dailyBlog.getDay());
  }

  /**
   * Tests that we can get the DailyBlog instance for today.
   */
  public void testGetBlogForToday() {
    Calendar cal = blog.getCalendar();
    DailyBlog dailyBlog = blog.getBlogForToday();
    assertNotNull(dailyBlog);
    assertEquals(cal.get(Calendar.YEAR), dailyBlog.getMonthlyBlog().getYearlyBlog().getYear());
    assertEquals(cal.get(Calendar.MONTH) + 1, dailyBlog.getMonthlyBlog().getMonth());
    assertEquals(cal.get(Calendar.DAY_OF_MONTH), dailyBlog.getDay());
  }

  /**
   * Tests that we can get a BlogEntry instance.
   */
  public void testGetBlogEntry() {
    DailyBlog dailyBlog = blog.getBlogForToday();
    BlogEntry blogEntry = dailyBlog.createBlogEntry();
    dailyBlog.addEntry(blogEntry);
    String id = blogEntry.getId();
    assertNotNull(blogEntry);
    blogEntry = blog.getBlogEntry(id);
    assertNotNull(blogEntry);
    assertEquals(id, blogEntry.getId());
  }

  /**
   * Tests that blog owners can be assigned.
   */
  public void testAssignBlogOwners() {
    blog.setProperty(Blog.BLOG_OWNERS_KEY, "user1");
    assertEquals("user1", blog.getProperty(Blog.BLOG_OWNERS_KEY));
    assertEquals("user1", blog.getBlogOwners());

    Collection users = blog.getUsersInRole(Constants.BLOG_OWNER_ROLE);
    assertEquals(1, users.size());
    assertTrue(users.contains("user1"));

    blog.setProperty(Blog.BLOG_OWNERS_KEY, "user1,user2");
    assertEquals("user1,user2", blog.getProperty(Blog.BLOG_OWNERS_KEY));
    assertEquals("user1,user2", blog.getBlogOwners());

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
    assertEquals(null, blog.getBlogOwners());

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
    assertEquals("user1", blog.getBlogContributors());

    Collection users = blog.getUsersInRole(Constants.BLOG_CONTRIBUTOR_ROLE);
    assertEquals(1, users.size());
    assertTrue(users.contains("user1"));

    blog.setProperty(Blog.BLOG_CONTRIBUTORS_KEY, "user1,user2");
    assertEquals("user1,user2", blog.getProperty(Blog.BLOG_CONTRIBUTORS_KEY));
    assertEquals("user1,user2", blog.getBlogContributors());

    users = blog.getUsersInRole(Constants.BLOG_CONTRIBUTOR_ROLE);
    assertEquals(2, users.size());
    assertTrue(users.contains("user1"));
    assertTrue(users.contains("user2"));

    blog.setProperty(Blog.BLOG_CONTRIBUTORS_KEY, "user1, user2");
    assertEquals("user1, user2", blog.getProperty(Blog.BLOG_CONTRIBUTORS_KEY));
    assertEquals("user1, user2", blog.getBlogContributors());

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
    assertEquals(null, blog.getBlogContributors());

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

  public void testUpdateNotificationPingWebSites() {
    blog.setProperty(Blog.UPDATE_NOTIFICATION_PINGS_KEY, null);
    assertTrue(blog.getUpdateNotificationPingsAsCollection().isEmpty());

    blog.setProperty(Blog.UPDATE_NOTIFICATION_PINGS_KEY, "");
    assertTrue(blog.getUpdateNotificationPingsAsCollection().isEmpty());

    blog.setProperty(Blog.UPDATE_NOTIFICATION_PINGS_KEY, "http://www.abc.com");
    assertEquals(1, blog.getUpdateNotificationPingsAsCollection().size());
    assertEquals("http://www.abc.com", blog.getUpdateNotificationPingsAsCollection().iterator().next());

    blog.setProperty(Blog.UPDATE_NOTIFICATION_PINGS_KEY, "http://www.abc.com\r\nhttp://www.def.com");
    assertEquals(2, blog.getUpdateNotificationPingsAsCollection().size());
    Iterator it = blog.getUpdateNotificationPingsAsCollection().iterator();
    assertEquals("http://www.abc.com", it.next());
    assertEquals("http://www.def.com", it.next());
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

  public void testGetRecentBlogEntries() {
    Calendar cal1 = blog.getCalendar();
    cal1.set(Calendar.HOUR_OF_DAY, 1);
    Calendar cal2 = blog.getCalendar();
    cal2.set(Calendar.HOUR_OF_DAY, 2);
    Calendar cal3 = blog.getCalendar();
    cal3.set(Calendar.HOUR_OF_DAY, 3);
    Calendar cal4 = blog.getCalendar();
    cal4.set(Calendar.HOUR_OF_DAY, 4);

    DailyBlog today = blog.getBlogForToday();
    BlogEntry entry1 = today.createBlogEntry("title1", "body1", cal1.getTime());
    today.addEntry(entry1);
    BlogEntry entry2 = today.createBlogEntry("title2", "body2", cal2.getTime());
    today.addEntry(entry2);
    BlogEntry entry3 = today.createBlogEntry("title3", "body3", cal3.getTime());
    today.addEntry(entry3);
    BlogEntry entry4 = today.createBlogEntry("title4", "body4", cal4.getTime());
    today.addEntry(entry4);
    List entries = blog.getRecentBlogEntries(3);

    assertFalse(entries.isEmpty());
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
    assertFalse(file.exists());
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
   * Tests that a draft blog entry can be accessed.
   */
  public void testDraftBlogEntryAccessible() throws Exception {
    assertEquals(0, blog.getDraftBlogEntries().size());
    BlogEntry draft = blog.getBlogForToday().createBlogEntry();
    draft.setType(BlogEntry.DRAFT);
    draft.store();
    assertEquals(1, blog.getDraftBlogEntries().size());
    assertEquals(draft, blog.getDraftBlogEntry(draft.getId()));
  }

  /**
   * Tests that a blog entry template can be accessed.
   */
  public void testBlogEntryTemplateAccessible() throws Exception {
    assertEquals(0, blog.getBlogEntryTemplates().size());
    BlogEntry draft = blog.getBlogForToday().createBlogEntry();
    draft.setType(BlogEntry.TEMPLATE);
    draft.store();
    assertEquals(1, blog.getBlogEntryTemplates().size());
    assertEquals(draft, blog.getBlogEntryTemplate(draft.getId()));
  }

  /**
   * Tests that a static page can be accessed.
   */
  public void testStaticPageAccessible() throws Exception {
    assertEquals(0, blog.getStaticPages().size());
    BlogEntry draft = blog.getBlogForToday().createBlogEntry();
    draft.setType(BlogEntry.STATIC_PAGE);
    draft.store();
    assertEquals(1, blog.getStaticPages().size());
    assertEquals(draft, blog.getStaticPage(draft.getId()));
  }

  /**
   * Tests the domain.
   */
  public void testDomain() {
    assertEquals("www.yourdomain.com", blog.getDomainName());

    BlogManager.getInstance().setBaseUrl("http://www.yourdomain.com:8080/blog");
    assertEquals("www.yourdomain.com", blog.getDomainName());
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
    blog.start();
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

  /**
   * Tests that category/tag statistics are updated.
   */
  public void testCategoryAndTagStatisticsUpdated() throws Exception {
    CategoryBuilder builder = new CategoryBuilder(blog);
    Category java = new Category("/java", "Java");
    builder.addCategory(java);
    java.setTags("java");
    Category apple = new Category("/apple", "Apple");
    builder.addCategory(apple);
    apple.setTags("apple");
    Category root = builder.getRootCategory();
    root.setTags("myblog");
    blog.setRootCategory(root);
    DailyBlog today = blog.getBlogForToday();
    BlogEntry blogEntry = today.createBlogEntry();
    blogEntry.addCategory(java);
    blogEntry.setTags("junit");

    assertEquals(0, root.getNumberOfBlogEntries());
    assertEquals(0, java.getNumberOfBlogEntries());
    assertEquals(0, apple.getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("myblog").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("java").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("apple").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("junit").getNumberOfBlogEntries());
    assertTrue(blog.getTags().isEmpty());

    // blog entry added
    today.addEntry(blogEntry);
    assertEquals(1, root.getNumberOfBlogEntries());
    assertEquals(1, java.getNumberOfBlogEntries());
    assertEquals(0, apple.getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("myblog").getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("java").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("apple").getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("junit").getNumberOfBlogEntries());
    assertFalse(blog.getTags().contains(blog.getTag("apple")));

    // blog entry changed - category added
    blogEntry.addCategory(apple);
    blogEntry.store();
    assertEquals(1, root.getNumberOfBlogEntries());
    assertEquals(1, java.getNumberOfBlogEntries());
    assertEquals(1, apple.getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("myblog").getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("java").getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("apple").getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("junit").getNumberOfBlogEntries());

    // blog entry changed - category changed
    blogEntry.removeAllCategories();
    blogEntry.addCategory(apple);
    blogEntry.store();
    assertEquals(1, root.getNumberOfBlogEntries());
    assertEquals(0, java.getNumberOfBlogEntries());
    assertEquals(1, apple.getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("myblog").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("java").getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("apple").getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("junit").getNumberOfBlogEntries());
    assertFalse(blog.getTags().contains(blog.getTag("java")));

    // blog entry changed - tag removed
    blogEntry.setTags("");
    blogEntry.store();
    assertEquals(1, root.getNumberOfBlogEntries());
    assertEquals(0, java.getNumberOfBlogEntries());
    assertEquals(1, apple.getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("myblog").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("java").getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("apple").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("junit").getNumberOfBlogEntries());
    assertFalse(blog.getTags().contains(blog.getTag("java")));
    assertFalse(blog.getTags().contains(blog.getTag("junit")));

    // blog entry changed - tag added
    blogEntry.setTags("junit");
    blogEntry.store();
    assertEquals(1, root.getNumberOfBlogEntries());
    assertEquals(0, java.getNumberOfBlogEntries());
    assertEquals(1, apple.getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("myblog").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("java").getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("apple").getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("junit").getNumberOfBlogEntries());
    assertFalse(blog.getTags().contains(blog.getTag("java")));

    // blog entry rejected
    blogEntry.setState(State.REJECTED);
    blogEntry.store();
    assertEquals(0, root.getNumberOfBlogEntries());
    assertEquals(0, java.getNumberOfBlogEntries());
    assertEquals(0, apple.getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("myblog").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("java").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("apple").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("junit").getNumberOfBlogEntries());
    assertTrue(blog.getTags().isEmpty());

    // blog entry approved
    blogEntry.setState(State.APPROVED);
    blogEntry.store();
    assertEquals(1, root.getNumberOfBlogEntries());
    assertEquals(0, java.getNumberOfBlogEntries());
    assertEquals(1, apple.getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("myblog").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("java").getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("apple").getNumberOfBlogEntries());
    assertEquals(1, blog.getTag("junit").getNumberOfBlogEntries());
    assertFalse(blog.getTags().contains(blog.getTag("java")));

    // blog entry removed
    today.removeEntry(blogEntry);
    blogEntry.remove();
    assertEquals(0, root.getNumberOfBlogEntries());
    assertEquals(0, java.getNumberOfBlogEntries());
    assertEquals(0, apple.getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("myblog").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("java").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("apple").getNumberOfBlogEntries());
    assertEquals(0, blog.getTag("junit").getNumberOfBlogEntries());
    assertTrue(blog.getTags().isEmpty());
  }

}