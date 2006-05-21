package net.sourceforge.pebble.domain;

import net.sourceforge.pebble.web.validation.ValidationContext;
import net.sourceforge.pebble.event.comment.CommentListener;
import net.sourceforge.pebble.event.comment.CommentEvent;
import net.sourceforge.pebble.event.trackback.TrackBackListener;
import net.sourceforge.pebble.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.event.blogentry.BlogEntryListener;
import net.sourceforge.pebble.event.blogentry.BlogEntryEvent;

import java.util.*;
import java.text.DecimalFormat;
import java.beans.PropertyChangeEvent;

/**
 * Tests for the BlogService class.
 *
 * @author    Simon Brown
 */
public class BlogServiceTest extends SingleBlogTestCase {

  private BlogService service;
  private BlogEntry blogEntry;

  public void setUp() {
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
    final Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "127.0.0.1");

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
    final Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "127.0.0.1");
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
    final Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "127.0.0.1");
    blogEntry.addComment(comment);
    comment.setState(State.PENDING);
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
    comment.setState(State.APPROVED);
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that listeners are fired when a comment is rejected.
   */
  public void testListenersFiredWhenCommentRejected() throws Exception {
    final StringBuffer buf = new StringBuffer("123");
    final Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "127.0.0.1");
    blogEntry.addComment(comment);
    comment.setState(State.PENDING);
    service.putBlogEntry(blogEntry);

    CommentListener listener = new CommentListener() {
      public void commentAdded(CommentEvent event) {
        fail();
      }

      public void commentRemoved(CommentEvent event) {
        fail();
      }

      public void commentApproved(CommentEvent event) {
        fail();
      }

      public void commentRejected(CommentEvent event) {
        assertEquals(comment, event.getSource());
        buf.reverse();
      }
    };

    blog.getEventListenerList().addCommentListener(listener);
    comment.setState(State.REJECTED);
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
    trackBack.setState(State.PENDING);
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
    trackBack.setState(State.APPROVED);
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
    trackBack.setState(State.PENDING);
    service.putBlogEntry(blogEntry);

    TrackBackListener listener = new TrackBackListener() {
      public void trackBackAdded(TrackBackEvent event) {
        fail();
      }

      public void trackBackRemoved(TrackBackEvent event) {
        fail();
      }

      public void trackBackApproved(TrackBackEvent event) {
        fail();
      }

      public void trackBackRejected(TrackBackEvent event) {
        assertEquals(trackBack, event.getSource());
        buf.reverse();
      }
    };

    blog.getEventListenerList().addTrackBackListener(listener);
    trackBack.setState(State.REJECTED);
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that listeners are fired when a blog entry is approved.
   */
  public void testListenersFiredWhenBlogEntryApproved() throws Exception {
    final StringBuffer buf = new StringBuffer("123");
    service.putBlogEntry(blogEntry);
    blogEntry.setState(State.PENDING);

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

      public void blogEntryApproved(BlogEntryEvent event) {
        assertEquals(blogEntry, event.getSource());
        buf.reverse();
      }

      public void blogEntryRejected(BlogEntryEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addBlogEntryListener(listener);
    blogEntry.setState(State.APPROVED);
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

  /**
   * Tests that listeners are fired when a blog entry is rejected.
   */
  public void testListenersFiredWhenBlogEntryRejected() throws Exception {
    final StringBuffer buf = new StringBuffer("123");
    service.putBlogEntry(blogEntry);
    blogEntry.setState(State.PENDING);

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

      public void blogEntryApproved(BlogEntryEvent event) {
        fail();
      }

      public void blogEntryRejected(BlogEntryEvent event) {
        assertEquals(blogEntry, event.getSource());
        buf.reverse();
      }
    };

    blog.getEventListenerList().addBlogEntryListener(listener);
    blogEntry.setState(State.REJECTED);
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

      public void blogEntryApproved(BlogEntryEvent event) {
        fail();
      }

      public void blogEntryRejected(BlogEntryEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addBlogEntryListener(listener);
    blogEntry.setTitle("A new title");
    service.putBlogEntry(blogEntry);
    assertEquals("321", buf.toString());
  }

}
