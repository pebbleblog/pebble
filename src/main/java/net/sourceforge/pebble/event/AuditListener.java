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
package net.sourceforge.pebble.event;

import net.sourceforge.pebble.api.event.blogentry.BlogEntryListener;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.api.event.comment.CommentListener;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackListener;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.audit.AuditTrail;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.TrackBack;

/**
 * @author Simon Brown
 */
public class AuditListener implements BlogEntryListener, CommentListener, TrackBackListener {

  /**
   * Called when a blog entry has been added.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryAdded(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    AuditTrail.log("Blog entry \"" + blogEntry.getTitle() + "\" (" + blogEntry.getGuid() + ") added");
  }

  /**
   * Called when a blog entry has been removed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryRemoved(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    AuditTrail.log("Blog entry \"" + blogEntry.getTitle() + "\" (" + blogEntry.getGuid() + ") removed");
  }

  /**
   * Called when a blog entry has been changed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryChanged(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    AuditTrail.log("Blog entry \"" + blogEntry.getTitle() + "\" (" + blogEntry.getGuid() + ") changed");
  }

  /**
   * Called when a blog entry has been published.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryPublished(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    AuditTrail.log("Blog entry \"" + blogEntry.getTitle() + "\" (" + blogEntry.getGuid() + ") published");
  }

  /**
   * Called when a blog entry has been unpublished.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryUnpublished(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    AuditTrail.log("Blog entry \"" + blogEntry.getTitle() + "\" (" + blogEntry.getGuid() + ") unpublished");
  }

  /**
   * Called when a comment has been added.
   *
   * @param event a CommentEvent instance
   */
  public void commentAdded(CommentEvent event) {
    Comment comment = event.getComment();
    AuditTrail.log("Comment \"" + comment.getTitle() + "\" from " + comment.getAuthor() + " (" + comment.getGuid() + ") added");
  }

  /**
   * Called when a comment has been removed.
   *
   * @param event a CommentEvent instance
   */
  public void commentRemoved(CommentEvent event) {
    Comment comment = event.getComment();
    AuditTrail.log("Comment \"" + comment.getTitle() + "\" from " + comment.getAuthor() + " (" + comment.getGuid() + ") removed");
  }

  /**
   * Called when a comment has been approved.
   *
   * @param event a CommentEvent instance
   */
  public void commentApproved(CommentEvent event) {
    Comment comment = event.getComment();
    AuditTrail.log("Comment \"" + comment.getTitle() + "\" from " + comment.getAuthor() + " (" + comment.getGuid() + ") approved");
  }

  /**
   * Called when a comment has been rejected.
   *
   * @param event a CommentEvent instance
   */
  public void commentRejected(CommentEvent event) {
    Comment comment = event.getComment();
    AuditTrail.log("Comment \"" + comment.getTitle() + "\" from " + comment.getAuthor() + " (" + comment.getGuid() + ") rejected");
  }

  /**
   * Called when a TrackBack has been added.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackAdded(TrackBackEvent event) {
    TrackBack trackback = event.getTrackBack();
    AuditTrail.log("TrackBack \"" + trackback.getTitle() + "\" from " + trackback.getBlogName() + " (" + trackback.getGuid() + ") added");
  }

  /**
   * Called when a TrackBack has been removed.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackRemoved(TrackBackEvent event) {
    TrackBack trackback = event.getTrackBack();
    AuditTrail.log("TrackBack \"" + trackback.getTitle() + "\" from " + trackback.getBlogName() + " (" + trackback.getGuid() + ") removed");
  }

  /**
   * Called when a TrackBack has been approved.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackApproved(TrackBackEvent event) {
    TrackBack trackback = event.getTrackBack();
    AuditTrail.log("TrackBack \"" + trackback.getTitle() + "\" from " + trackback.getBlogName() + " (" + trackback.getGuid() + ") approved");
  }

  /**
   * Called when a TrackBack has been rejected.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackRejected(TrackBackEvent event) {
    TrackBack trackback = event.getTrackBack();
    AuditTrail.log("TrackBack \"" + trackback.getTitle() + "\" from " + trackback.getBlogName() + " (" + trackback.getGuid() + ") rejected");
  }
}
