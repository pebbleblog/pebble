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
package net.sourceforge.pebble.event.blogentry;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the MarkUnpublishedListener class.
 *
 * @author Simon Brown
 */
public class MarkUnpublishedListenerTest extends SingleBlogTestCase {

  private MarkUnpublishedListener listener;
  private BlogEntry blogEntry;
  private BlogEntryEvent blogEntryEvent;

  /**
   * Common setup code.
   */
  protected void setUp() throws Exception {
    super.setUp();

    listener = new MarkUnpublishedListener();
    blogEntry = new BlogEntry(blog);
    SecurityUtils.runAsBlogContributor();
  }

  /**
   * Tests the blogEntryAdded() method.
   */
  public void testBlogEntryAdded() {
    SecurityUtils.runAsBlogOwner();

    assertTrue(blogEntry.isUnpublished());
    blogEntryEvent = new BlogEntryEvent(blogEntry, BlogEntryEvent.BLOG_ENTRY_ADDED);
    listener.blogEntryAdded(blogEntryEvent);
    assertTrue(blogEntry.isUnpublished());

    SecurityUtils.runAsBlogContributor();
    blogEntryEvent = new BlogEntryEvent(blogEntry, BlogEntryEvent.BLOG_ENTRY_ADDED);
    listener.blogEntryAdded(blogEntryEvent);
    assertTrue(blogEntry.isUnpublished());
  }

  /**
   * Tests the blogEntryChanged() method, when a blog publisher changes
   * the blog entry.
   */
  public void testBlogEntryChangedByBlogPublisher() {
    blogEntry.setPublished(true);
    SecurityUtils.runAsBlogPublisher();

    List propertyChangeEvents = new ArrayList();
    PropertyChangeEvent pce = new PropertyChangeEvent(blogEntry, BlogEntry.TITLE_PROPERTY, null, null);
    propertyChangeEvents.add(pce);
    blogEntryEvent = new BlogEntryEvent(blogEntry, propertyChangeEvents);
    listener.blogEntryChanged(blogEntryEvent);
    assertTrue(blogEntry.isPublished());
  }

  /**
   * Tests the blogEntryChanged() method, when a blog contributor changes
   * the blog entry.
   */
  public void testBlogEntryChangedByBlogContributor() {
    blogEntry.setPublished(true);
    List propertyChangeEvents = new ArrayList();
    PropertyChangeEvent pce = new PropertyChangeEvent(blogEntry, BlogEntry.TITLE_PROPERTY, null, null);
    propertyChangeEvents.add(pce);
    blogEntryEvent = new BlogEntryEvent(blogEntry, propertyChangeEvents);
    listener.blogEntryChanged(blogEntryEvent);
    assertTrue(blogEntry.isUnpublished());
  }

  /**
   * Tests the blogEntryChanged() method when the title is changed.
   */
  public void testBlogEntryChangedForTitleProperty() {
    blogEntry.setPublished(true);
    List propertyChangeEvents = new ArrayList();
    PropertyChangeEvent pce = new PropertyChangeEvent(blogEntry, BlogEntry.TITLE_PROPERTY, null, null);
    propertyChangeEvents.add(pce);
    blogEntryEvent = new BlogEntryEvent(blogEntry, propertyChangeEvents);
    listener.blogEntryChanged(blogEntryEvent);
    assertTrue(blogEntry.isUnpublished());
  }

  /**
   * Tests the blogEntryChanged() method when the excerpt is changed.
   */
  public void testBlogEntryChangedForExcerptProperty() {
    blogEntry.setPublished(true);
    List propertyChangeEvents = new ArrayList();
    PropertyChangeEvent pce = new PropertyChangeEvent(blogEntry, BlogEntry.EXCERPT_PROPERTY, null, null);
    propertyChangeEvents.add(pce);
    blogEntryEvent = new BlogEntryEvent(blogEntry, propertyChangeEvents);
    listener.blogEntryChanged(blogEntryEvent);
    assertTrue(blogEntry.isUnpublished());
  }

  /**
   * Tests the blogEntryChanged() method when the body is changed.
   */
  public void testBlogEntryChangedForBodyProperty() {
    blogEntry.setPublished(true);
    List propertyChangeEvents = new ArrayList();
    PropertyChangeEvent pce = new PropertyChangeEvent(blogEntry, BlogEntry.BODY_PROPERTY, null, null);
    propertyChangeEvents.add(pce);
    blogEntryEvent = new BlogEntryEvent(blogEntry, propertyChangeEvents);
    listener.blogEntryChanged(blogEntryEvent);
    assertTrue(blogEntry.isUnpublished());
  }

  /**
   * Tests the blogEntryChanged() method when the original permalink is changed.
   */
  public void testBlogEntryChangedForOriginalPermalinkProperty() {
    blogEntry.setPublished(true);
    List propertyChangeEvents = new ArrayList();
    PropertyChangeEvent pce = new PropertyChangeEvent(blogEntry, BlogEntry.ORIGINAL_PERMALINK_PROPERTY, null, null);
    propertyChangeEvents.add(pce);
    blogEntryEvent = new BlogEntryEvent(blogEntry, propertyChangeEvents);
    listener.blogEntryChanged(blogEntryEvent);
    assertTrue(blogEntry.isUnpublished());
  }

  /**
   * Tests the blogEntryChanged() method when the comments enabled is changed.
   */
  public void testBlogEntryChangedForCommentsEnabledProperty() {
    blogEntry.setPublished(true);
    List propertyChangeEvents = new ArrayList();
    PropertyChangeEvent pce = new PropertyChangeEvent(blogEntry, BlogEntry.COMMENTS_ENABLED_PROPERTY, null, null);
    propertyChangeEvents.add(pce);
    blogEntryEvent = new BlogEntryEvent(blogEntry, propertyChangeEvents);
    listener.blogEntryChanged(blogEntryEvent);
    assertTrue(blogEntry.isPublished());
  }

}
