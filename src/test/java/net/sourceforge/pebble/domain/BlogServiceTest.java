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

import net.sourceforge.pebble.api.event.comment.CommentListener;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackListener;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryListener;

import java.util.*;

/**
 * Tests for the BlogService class.
 *
 * @author    Simon Brown
 */
public class BlogServiceTest extends SingleBlogTestCase {

  private BlogService service;
  private BlogEntry blogEntry;

  protected void setUp() throws Exception {
    super.setUp();

    service = new BlogService();
    blogEntry = new BlogEntry(blog);
    blogEntry.setTitle("A title");
    blogEntry.setBody("Some body");
    blogEntry.setExcerpt("Some excerpt");
    blogEntry.setAuthor("An author");
    blogEntry.setDate(new Date());
  }

  /**
   * Tests that listeners are fired when a comment is added.
   */
  public void testListenersFiredWhenCommentAdded() throws Exception {
    final StringBuffer buf = new StringBuffer("123");
    final Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");

    CommentListener listener = new CommentListener() {
      public void commentAdded(CommentEvent event) {
        assertEquals(comment, event.getSource());
        buf.reverse();
      }

      public void commentRemoved(CommentEvent event) {
        fail();
      }

      public void commentApproved(CommentEvent event) {
        fail();
      }

      public void commentRejected(CommentEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addCommentListener(listener);
    service.putBlogEntry(blogEntry);
    blogEntry.addComment(comment);
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that listeners are fired when a comment is removed.
   */
  public void testListenersFiredWhenCommentRemoved() throws Exception {
    final StringBuffer buf = new StringBuffer("123");
    final Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    blogEntry.addComment(comment);
    service.putBlogEntry(blogEntry);

    CommentListener listener = new CommentListener() {
      public void commentAdded(CommentEvent event) {
        fail();
      }

      public void commentRemoved(CommentEvent event) {
        assertEquals(comment, event.getSource());
        buf.reverse();
      }

      public void commentApproved(CommentEvent event) {
        fail();
      }

      public void commentRejected(CommentEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addCommentListener(listener);
    blogEntry.removeComment(comment.getId());
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that listeners are fired when a comment is approved.
   */
  public void testListenersFiredWhenCommentApproved() throws Exception {
    final StringBuffer buf = new StringBuffer("123");
    final Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");

    blogEntry.addComment(comment);
    comment.setPending();
    service.putBlogEntry(blogEntry);

    CommentListener listener = new CommentListener() {
      public void commentAdded(CommentEvent event) {
        fail();
      }

      public void commentRemoved(CommentEvent event) {
        fail();
      }

      public void commentApproved(CommentEvent event) {
        assertEquals(comment, event.getSource());
        buf.reverse();
      }

      public void commentRejected(CommentEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addCommentListener(listener);
    comment.setApproved();
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that listeners are fired when a comment is rejected.
   */
  public void testListenersFiredWhenCommentRejected() throws Exception {
    final StringBuffer buf = new StringBuffer("123");
    final Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    blogEntry.addComment(comment);
    comment.setPending();
    service.putBlogEntry(blogEntry);

    CommentListener listener = new CommentListener() {
      public void commentAdded(CommentEvent event) {
      }

      public void commentRemoved(CommentEvent event) {
      }

      public void commentApproved(CommentEvent event) {
      }

      public void commentRejected(CommentEvent event) {
        assertEquals(comment, event.getSource());
        buf.reverse();
      }
    };

    blog.getEventListenerList().addCommentListener(listener);
    comment.setRejected();
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that listeners are fired when a TrackBack is added.
   */
  public void testListenersFiredWhenTrackBackAdded() throws Exception {
    final StringBuffer buf = new StringBuffer("123");
    final TrackBack trackBack = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");

    TrackBackListener listener = new TrackBackListener() {
      public void trackBackAdded(TrackBackEvent event) {
        assertEquals(trackBack, event.getSource());
        buf.reverse();
      }

      public void trackBackRemoved(TrackBackEvent event) {
        fail();
      }

      public void trackBackApproved(TrackBackEvent event) {
        fail();
      }

      public void trackBackRejected(TrackBackEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addTrackBackListener(listener);
    service.putBlogEntry(blogEntry);
    blogEntry.addTrackBack(trackBack);
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that listeners are fired when a TrackBack is removed.
   */
  public void testListenersFiredWhenTrackBackRemoved() throws Exception {
    final StringBuffer buf = new StringBuffer("123");
    final TrackBack trackBack = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");
    blogEntry.addTrackBack(trackBack);
    service.putBlogEntry(blogEntry);

    TrackBackListener listener = new TrackBackListener() {
      public void trackBackAdded(TrackBackEvent event) {
        fail();
      }

      public void trackBackRemoved(TrackBackEvent event) {
        assertEquals(trackBack, event.getSource());
        buf.reverse();
      }

      public void trackBackApproved(TrackBackEvent event) {
        fail();
      }

      public void trackBackRejected(TrackBackEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addTrackBackListener(listener);
    blogEntry.removeTrackBack(trackBack.getId());
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that listeners are fired when a TrackBack is approved.
   */
  public void testListenersFiredWhenTrackBackApproved() throws Exception {
    final StringBuffer buf = new StringBuffer("123");
    final TrackBack trackBack = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");
    blogEntry.addTrackBack(trackBack);
    trackBack.setPending();
    service.putBlogEntry(blogEntry);

    TrackBackListener listener = new TrackBackListener() {
      public void trackBackAdded(TrackBackEvent event) {
        fail();
      }

      public void trackBackRemoved(TrackBackEvent event) {
        fail();
      }

      public void trackBackApproved(TrackBackEvent event) {
        assertEquals(trackBack, event.getSource());
        buf.reverse();
      }

      public void trackBackRejected(TrackBackEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addTrackBackListener(listener);
    trackBack.setApproved();
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that listeners are fired when a TrackBack is rejected.
   */
  public void testListenersFiredWhenTrackBackRejected() throws Exception {
    final StringBuffer buf = new StringBuffer("123");
    final TrackBack trackBack = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");
    blogEntry.addTrackBack(trackBack);
    trackBack.setPending();
    service.putBlogEntry(blogEntry);

    TrackBackListener listener = new TrackBackListener() {
      public void trackBackAdded(TrackBackEvent event) {
      }

      public void trackBackRemoved(TrackBackEvent event) {
      }

      public void trackBackApproved(TrackBackEvent event) {
      }

      public void trackBackRejected(TrackBackEvent event) {
        assertEquals(trackBack, event.getSource());
        buf.reverse();
      }
    };

    blog.getEventListenerList().addTrackBackListener(listener);
    trackBack.setRejected();
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that listeners are fired when a blog entry is published.
   */
  public void testListenersFiredWhenBlogEntryPublished() throws Exception {
    final StringBuffer buf = new StringBuffer("123");
    blogEntry.setPublished(false);
    service.putBlogEntry(blogEntry);

    BlogEntryListener listener = new BlogEntryListener() {
      public void blogEntryAdded(BlogEntryEvent event) {
        fail();
      }

      public void blogEntryRemoved(BlogEntryEvent event) {
        fail();
      }

      public void blogEntryChanged(BlogEntryEvent event) {
        fail();
      }

      public void blogEntryPublished(BlogEntryEvent event) {
        assertEquals(blogEntry, event.getSource());
        buf.reverse();
      }

      public void blogEntryUnpublished(BlogEntryEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addBlogEntryListener(listener);
    blogEntry.setPublished(true);
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that listeners are fired when a blog entry is unpublished.
   */
  public void testListenersFiredWhenBlogEntryUnpublished() throws Exception {
    final StringBuffer buf = new StringBuffer("123");
    blogEntry.setPublished(true);
    service.putBlogEntry(blogEntry);

    BlogEntryListener listener = new BlogEntryListener() {
      public void blogEntryAdded(BlogEntryEvent event) {
        fail();
      }

      public void blogEntryRemoved(BlogEntryEvent event) {
        fail();
      }

      public void blogEntryChanged(BlogEntryEvent event) {
        fail();
      }

      public void blogEntryPublished(BlogEntryEvent event) {
        fail();
      }

      public void blogEntryUnpublished(BlogEntryEvent event) {
        assertEquals(blogEntry, event.getSource());
        buf.reverse();
      }
    };

    blog.getEventListenerList().addBlogEntryListener(listener);
    blogEntry.setPublished(false);
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that listeners are fired when a blog entry is changed.
   */
  public void testListenersFiredWhenBlogEntryChanged() throws Exception {
    BlogService service = new BlogService();
    service.putBlogEntry(blogEntry);

    final StringBuffer buf = new StringBuffer("123");

    BlogEntryListener listener = new BlogEntryListener() {
      public void blogEntryAdded(BlogEntryEvent event) {
        fail();
      }

      public void blogEntryRemoved(BlogEntryEvent event) {
        fail();
      }

      public void blogEntryChanged(BlogEntryEvent event) {
        assertEquals(blogEntry, event.getSource());
        assertNotNull(event.getPropertyChangeEvents());
        buf.reverse();
      }

      public void blogEntryPublished(BlogEntryEvent event) {
        fail();
      }

      public void blogEntryUnpublished(BlogEntryEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addBlogEntryListener(listener);
    blogEntry.setTitle("A new title");
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that comment listeners are fired when a blog entry is removed.
   */
  public void testListenersFiredForCommentsWhenBlogEntryRemoved() throws Exception {
    final Comment comment1 = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    final Comment comment2 = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    final Comment comment3 = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");

    blogEntry.addComment(comment1);
    blogEntry.addComment(comment2);
    service.putBlogEntry(blogEntry);

    comment3.setParent(comment2);
    blogEntry.addComment(comment3);
    service.putBlogEntry(blogEntry);

    final List comments = new ArrayList();

    CommentListener listener = new CommentListener() {
      public void commentAdded(CommentEvent event) {
        fail();
      }

      public void commentRemoved(CommentEvent event) {
        comments.add(event.getSource());
      }

      public void commentApproved(CommentEvent event) {
        fail();
      }

      public void commentRejected(CommentEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addCommentListener(listener);
    service.removeBlogEntry(blogEntry);

    assertEquals(comment1, comments.get(0));
    assertEquals(comment2, comments.get(1));
    assertEquals(comment3, comments.get(2));
  }

  /**
   * Tests that TrackBack listeners are fired when a blog entry is removed.
   */
  public void testListenersFiredForTrackBacksWhenBlogEntryRemoved() throws Exception {
    final TrackBack trackBack1 = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");
    final TrackBack trackBack2 = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");
    final TrackBack trackBack3 = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");

    blogEntry.addTrackBack(trackBack1);
    blogEntry.addTrackBack(trackBack2);
    blogEntry.addTrackBack(trackBack3);
    service.putBlogEntry(blogEntry);

    final List trackBacks = new ArrayList();

    TrackBackListener listener = new TrackBackListener() {
      public void trackBackAdded(TrackBackEvent event) {
        fail();
      }

      public void trackBackRemoved(TrackBackEvent event) {
        trackBacks.add(event.getSource());
      }

      public void trackBackApproved(TrackBackEvent event) {
        fail();
      }

      public void trackBackRejected(TrackBackEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addTrackBackListener(listener);
    service.removeBlogEntry(blogEntry);

    assertEquals(trackBack1, trackBacks.get(0));
    assertEquals(trackBack2, trackBacks.get(1));
    assertEquals(trackBack3, trackBacks.get(2));
  }
}
