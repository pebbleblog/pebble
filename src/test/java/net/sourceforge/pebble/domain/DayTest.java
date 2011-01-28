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

import java.beans.Introspector;
import java.util.Calendar;
import java.util.Date;

/**
 * Tests for the Day class.
 *
 * @author    Simon Brown
 */
public class DayTest extends SingleBlogTestCase {

  private Day day;

  protected void setUp() throws Exception {
    super.setUp();
    day = blog.getBlogForToday();
  }

  /**
   * Tests that the root blog is setup correctly.
   */
  public void testGetRootBlog() {
    assertEquals(blog, day.getBlog());
  }

  /**
   * Tests the getter for the day property.
   */
  public void testGetDay() {
    Calendar cal = blog.getCalendar();
    assertEquals(cal.get(Calendar.DAY_OF_MONTH), day.getDay());
  }

  /**
   * Tests the getter for the monthly blog.
   */
  public void testGetMonth() {
    assertEquals(blog.getBlogForThisMonth(), day.getMonth());
  }

  /**
   * Tests the getter for the date property.
   */
  public void testGetDate() {
    day = blog.getBlogForDay(2003, 4, 7);
    Date date = day.getDate();
    Calendar cal = blog.getCalendar();
    cal.setTime(date);
    assertEquals(2003, cal.get(Calendar.YEAR));
    assertEquals(4, cal.get(Calendar.MONTH) + 1);
    assertEquals(7, cal.get(Calendar.DAY_OF_MONTH));
    assertEquals(0, cal.get(Calendar.HOUR));
    assertEquals(0, cal.get(Calendar.MINUTE));
    assertEquals(0, cal.get(Calendar.SECOND));
    assertEquals(0, cal.get(Calendar.MILLISECOND));
  }

  /**
   * Tests the permalink.
   */
  public void testGetPermalink() {
    String permalink = blog.getUrl() + "2003/04/01.html";
    day = blog.getBlogForDay(2003, 4, 1);
    assertEquals(permalink, day.getPermalink());

    permalink = blog.getUrl() + "2003/04/10.html";
    day = blog.getBlogForDay(2003, 4, 10);
    assertEquals(permalink, day.getPermalink());
  }

//  /**
//   * Tests the ability to check for entries.
//   */
//  public void testHasEntries() {
//    assertFalse(day.hasBlogEntries());
//
//    BlogEntry entry = day.createBlogEntry();
//    day.addEntry(entry);
//    assertTrue(day.hasBlogEntries());
//  }

//  /**
//   * Tests the ability to check for entries in a specific category.
//   */
//  public void testHasEntriesInCategory() {
//    Category cat = new Category("testCategory1", "Test Category 1");
//    assertFalse(day.hasBlogEntries(cat));
//
//    BlogEntry entry = day.createBlogEntry();
//    day.addEntry(entry);
//    assertFalse(day.hasBlogEntries(cat));
//
//    entry.addCategory(cat);
//    assertTrue(day.hasBlogEntries(cat));
//  }

//  /**
//   * Tests the ability to get all day entries.
//   */
//  public void testGetEntries() {
//    assertNotNull(day.getEntries());
//    assertTrue(day.getEntries().isEmpty());
//
//    BlogEntry entry = day.createBlogEntry();
//    day.addEntry(entry);
//    assertNotNull(day.getEntries());
//    assertTrue(day.getEntries().size() == 1);
//    assertTrue(day.getEntries().contains(entry));
//  }

//  /**
//   * Tests the ability to create a new blog entry.
//   */
//  public void testCreateBlogEntry() {
//    BlogEntry entry = day.createBlogEntry();
//    assertNotNull(entry);
//
//    // and check it's not yet been added to the Day instance
//    assertFalse(day.hasBlogEntries());
//  }

//  /**
//   * Tests that the previous blog entry can be obtained.
//   */
//  public void testPreviousBlogEntry() {
//    Calendar cal = blog.getCalendar();
//    cal.set(Calendar.HOUR_OF_DAY, 2);
//    BlogEntry entry1 = day.createBlogEntry("Title", "Body", cal.getTime());
//    day.addEntry(entry1);
//    cal.set(Calendar.HOUR_OF_DAY, 3);
//    BlogEntry entry2 = day.createBlogEntry("Title", "Body", cal.getTime());
//    day.addEntry(entry2);
//    cal.set(Calendar.HOUR_OF_DAY, 4);
//    BlogEntry entry3 = day.createBlogEntry("Title", "Body", cal.getTime());
//    day.addEntry(entry3);
//
//    assertNull(day.getPreviousBlogEntry(null));
//    assertNull(day.getPreviousBlogEntry(entry1));
//    assertEquals(entry1, day.getPreviousBlogEntry(entry2));
//    assertEquals(entry2, day.getPreviousBlogEntry(entry3));
//  }

//  /**
//   * Tests that the next blog entry can be obtained.
//   */
//  public void testNextBlogEntry() {
//    Calendar cal = blog.getCalendar();
//    cal.set(Calendar.HOUR_OF_DAY, 2);
//    BlogEntry entry1 = day.createBlogEntry("Title", "Body", cal.getTime());
//    day.addEntry(entry1);
//    cal.set(Calendar.HOUR_OF_DAY, 3);
//    BlogEntry entry2 = day.createBlogEntry("Title", "Body", cal.getTime());
//    day.addEntry(entry2);
//    cal.set(Calendar.HOUR_OF_DAY, 4);
//    BlogEntry entry3 = day.createBlogEntry("Title", "Body", cal.getTime());
//    day.addEntry(entry3);
//
//    assertNull(day.getNextBlogEntry(null));
//    assertNull(day.getNextBlogEntry(entry3));
//    assertEquals(entry2, day.getNextBlogEntry(entry1));
//    assertEquals(entry3, day.getNextBlogEntry(entry2));
//  }

//  /**
//   * Tests that blog entries can't be added with the same ID.
//   */
//  public void testAddingBlogEntriesWithTheSameId() {
//    Day today = blog.getBlogForToday();
//    BlogEntry blogEntry1 = today.createBlogEntry();
//    BlogEntry blogEntry2 = today.createBlogEntry();
//    BlogEntry blogEntry3 = today.createBlogEntry();
//    today.addEntry(blogEntry1);
//    today.addEntry(blogEntry2);
//    today.addEntry(blogEntry3);
//
//    assertFalse("IDs are the same", blogEntry1.getId().equals(blogEntry2.getId()));
//    assertFalse("IDs are the same", blogEntry2.getId().equals(blogEntry3.getId()));
//    assertFalse("IDs are the same", blogEntry1.getId().equals(blogEntry3.getId()));
//  }

//  /**
//   * Tests that listeners are fired when a blog entry is added.
//   */
//  public void testListenersFiredWhenBlogEntryAdded() {
//    final StringBuffer buf = new StringBuffer("123");
//    final BlogEntry blogEntry = day.createBlogEntry();
//
//    BlogEntryListener listener = new BlogEntryListener() {
//      public void blogEntryAdded(BlogEntryEvent event) {
//        assertEquals(blogEntry, event.getSource());
//        buf.reverse();
//      }
//
//      public void blogEntryRemoved(BlogEntryEvent event) {
//        fail();
//      }
//
//      public void blogEntryChanged(BlogEntryEvent event) {
//        fail();
//      }
//
//      public void blogEntryApproved(BlogEntryEvent event) {
//        fail();
//      }
//
//      public void blogEntryRejected(BlogEntryEvent event) {
//        fail();
//      }
//    };
//
//    blog.getEventListenerList().addBlogEntryListener(listener);
//    day.addEntry(blogEntry);
//    assertEquals("321", buf.toString());
//  }

//  /**
//   * Tests that listeners are fired when a blog entry is removed.
//   */
//  public void testListenersFiredWhenBlogEntryRemoved() {
//    final StringBuffer buf = new StringBuffer("123");
//    final BlogEntry blogEntry = day.createBlogEntry();
//    day.addEntry(blogEntry);
//
//    BlogEntryListener listener = new BlogEntryListener() {
//      public void blogEntryAdded(BlogEntryEvent event) {
//        fail();
//      }
//
//      public void blogEntryRemoved(BlogEntryEvent event) {
//        assertEquals(blogEntry, event.getSource());
//        buf.reverse();
//      }
//
//      public void blogEntryChanged(BlogEntryEvent event) {
//        fail();
//      }
//
//      public void blogEntryApproved(BlogEntryEvent event) {
//        fail();
//      }
//
//      public void blogEntryRejected(BlogEntryEvent event) {
//        fail();
//      }
//    };
//
//    blog.getEventListenerList().addBlogEntryListener(listener);
//    day.removeEntry(blogEntry);
//    assertEquals("321", buf.toString());
//  }

//  /**
//   * Tests that event listeners are enabled after an entry has been added.
//   */
//  public void testBlogEntryEventListenersEnabledAfterAddition() {
//    final BlogEntry blogEntry = day.createBlogEntry();
//    assertFalse(blogEntry.areEventsEnabled());
//    day.addEntry(blogEntry);
//    assertTrue(blogEntry.areEventsEnabled());
//  }

  public void testIntrospection() throws Exception {
    Introspector.getBeanInfo(Day.class);
  }

}