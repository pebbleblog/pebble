package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.TrackBack;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.comment.CommentListener;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackListener;

/**
 * Listens to comment/TrackBack events and keeps the response index up to date.
 *
 * @author    Simon Brown
 */
public class ResponseIndexListener implements CommentListener, TrackBackListener {

  /**
   * Called when a comment has been added.
   *
   * @param event a CommentEvent instance
   */
  public void commentAdded(CommentEvent event) {
    Comment comment = event.getComment();
    comment.getBlogEntry().getBlog().getResponseIndex().index(comment);
  }

  /**
   * Called when a comment has been removed.
   *
   * @param event a CommentEvent instance
   */
  public void commentRemoved(CommentEvent event) {
    Comment comment = event.getComment();
    comment.getBlogEntry().getBlog().getResponseIndex().unindex(comment);
  }

  /**
   * Called when a comment has been approved.
   *
   * @param event a CommentEvent instance
   */
  public void commentApproved(CommentEvent event) {
    Comment comment = event.getComment();
    comment.getBlogEntry().getBlog().getResponseIndex().unindex(comment);
    comment.getBlogEntry().getBlog().getResponseIndex().index(comment);
  }

  /**
   * Called when a comment has been rejected.
   *
   * @param event a CommentEvent instance
   */
  public void commentRejected(CommentEvent event) {
    Comment comment = event.getComment();
    comment.getBlogEntry().getBlog().getResponseIndex().unindex(comment);
    comment.getBlogEntry().getBlog().getResponseIndex().index(comment);
  }

  /**
   * Called when a TrackBack has been added.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackAdded(TrackBackEvent event) {
    TrackBack trackBack = event.getTrackBack();
    trackBack.getBlogEntry().getBlog().getResponseIndex().index(trackBack);
  }

  /**
   * Called when a TrackBack has been removed.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackRemoved(TrackBackEvent event) {
    TrackBack trackBack = event.getTrackBack();
    trackBack.getBlogEntry().getBlog().getResponseIndex().unindex(trackBack);
  }

  /**
   * Called when a TrackBack has been approved.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackApproved(TrackBackEvent event) {
    TrackBack trackBack = event.getTrackBack();
    trackBack.getBlogEntry().getBlog().getResponseIndex().unindex(trackBack);
    trackBack.getBlogEntry().getBlog().getResponseIndex().index(trackBack);
  }

  /**
   * Called when a TrackBack has been rejected.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackRejected(TrackBackEvent event) {
    TrackBack trackBack = event.getTrackBack();
    trackBack.getBlogEntry().getBlog().getResponseIndex().unindex(trackBack);
    trackBack.getBlogEntry().getBlog().getResponseIndex().index(trackBack);
  }

}
