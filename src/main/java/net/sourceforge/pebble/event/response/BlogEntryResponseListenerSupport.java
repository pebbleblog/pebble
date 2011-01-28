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
package net.sourceforge.pebble.event.response;

import net.sourceforge.pebble.domain.Response;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.comment.CommentListener;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackListener;

/**
 * Starting point for implementation of classes that are both comment
 * and TrackBack listeners. Override the blogEntryResponseX methods.
 *
 * @author Simon Brown
 */
public abstract class BlogEntryResponseListenerSupport implements CommentListener, TrackBackListener {

  /**
   * Called when a comment has been added.
   *
   * @param event   a CommentEvent instance
   */
  public void commentAdded(CommentEvent event) {
    blogEntryResponseAdded(event.getComment());
  }

  /**
   * Called when a comment has been removed.
   *
   * @param event   a CommentEvent instance
   */
  public void commentRemoved(CommentEvent event) {
    blogEntryResponseRemoved(event.getComment());
  }

  /**
   * Called when a comment has been approved.
   *
   * @param event   a CommentEvent instance
   */
  public void commentApproved(CommentEvent event) {
    blogEntryResponseApproved(event.getComment());
  }

  /**
   * Called when a comment has been rejected.
   *
   * @param event   a CommentEvent instance
   */
  public void commentRejected(CommentEvent event) {
    blogEntryResponseRejected(event.getComment());
  }

  /**
   * Called when a TrackBack has been added.
   *
   * @param event   a TrackBackEvent instance
   */
  public void trackBackAdded(TrackBackEvent event) {
    blogEntryResponseAdded(event.getTrackBack());
  }

  /**
   * Called when a TrackBack has been removed.
   *
   * @param event   a TrackBackEvent instance
   */
  public void trackBackRemoved(TrackBackEvent event) {
    blogEntryResponseRemoved(event.getTrackBack());
  }

  /**
   * Called when a TrackBack has been approved.
   *
   * @param event   a TrackBackEvent instance
   */
  public void trackBackApproved(TrackBackEvent event) {
    blogEntryResponseApproved(event.getTrackBack());
  }

  /**
   * Called when a TrackBack has been rejected.
   *
   * @param event   a TrackBackEvent instance
   */
  public void trackBackRejected(TrackBackEvent event) {
    blogEntryResponseRejected(event.getTrackBack());
  }

  /**
   * Called when a comment or TrackBack has been added.
   *
   * @param response    a Response
   */
  protected void blogEntryResponseAdded(Response response) {
  }

  /**
   * Called when a comment or TrackBack has been removed.
   *
   * @param response    a Response
   */
  protected void blogEntryResponseRemoved(Response response) {
  }

  /**
   * Called when a comment or TrackBack has been approved.
   *
   * @param response    a Response
   */
  protected void blogEntryResponseApproved(Response response) {
  }

  /**
   * Called when a comment or TrackBack has been rejected.
   *
   * @param response    a Response
   */
  protected void blogEntryResponseRejected(Response response) {
  }

}