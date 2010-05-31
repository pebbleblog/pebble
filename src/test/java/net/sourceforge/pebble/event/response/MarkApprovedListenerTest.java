package net.sourceforge.pebble.event.response;

import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.TrackBack;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;

/**
 * Tests for the MarkApprovedListener class.
 *
 * @author Simon Brown
 */
public class MarkApprovedListenerTest extends SingleBlogTestCase {

  private MarkApprovedListener listener;
  private Comment comment;
  private CommentEvent commentEvent;
  private TrackBack trackBack;
  private TrackBackEvent trackBackEvent;

  /**
   * Common setup code.
   */
  protected void setUp() throws Exception {
    super.setUp();

    listener = new MarkApprovedListener();
    comment = new BlogEntry(blog).createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "127.0.0.1");
    commentEvent = new CommentEvent(comment, CommentEvent.COMMENT_ADDED);
    trackBack = new BlogEntry(blog).createTrackBack("Title", "Excerpt", "url", "blogName", "127.0.0.1");
    trackBackEvent = new TrackBackEvent(trackBack, TrackBackEvent.TRACKBACK_ADDED);
  }

  /**
   * Tests the commentAdded() method.
   */
  public void testCommentAdded() {
    assertTrue(comment.isApproved());
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());
  }

  /**
   * Tests the trackBackAdded() method.
   */
  public void testTrackBackAdded() {
    assertTrue(trackBack.isApproved());
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isApproved());
  }

}