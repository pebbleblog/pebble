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
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Deletes comments and TrackBacks that have been marked as rejected.
 *
 * @author Simon Brown
 */
public class DeleteRejectedListener extends BlogEntryResponseListenerSupport {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(DeleteRejectedListener.class);


  /**
   * Called when a comment has been added.
   *
   * @param event a CommentEvent instance
   */
  public void commentAdded(CommentEvent event) {
    super.commentAdded(event);
    if (event.getComment().isRejected()) {
      event.veto();
    }
  }

  /**
   * Called when a comment has been rejected.
   *
   * @param event a CommentEvent instance
   */
  public void commentRejected(CommentEvent event) {
    super.commentRejected(event);
    event.veto();
  }

  /**
   * Called when a TrackBack has been added.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackAdded(TrackBackEvent event) {
    super.trackBackAdded(event);
    if (event.getTrackBack().isRejected()) {
      event.veto();
    }
  }

  /**
   * Called when a TrackBack has been rejected.
   *
   * @param event a TrackBackEvent instance
   */
  public void trackBackRejected(TrackBackEvent event) {
    super.trackBackRejected(event);
    event.veto();
  }

  /**
   * Called when a comment or TrackBack has been added.
   *
   * @param response a Response
   */
  protected void blogEntryResponseAdded(Response response) {
    if (response.isRejected()) {
      deleteRejectedResponse(response);
    }
  }

  /**
   * Called when a comment or TrackBack has been rejected.
   *
   * @param response a Response
   */
  protected void blogEntryResponseRejected(Response response) {
    deleteRejectedResponse(response);
  }

  /**
   * Helper method to delete rejected response.
   */
  private void deleteRejectedResponse(Response response) {
    log.info("Deleting rejected response for " + response.getBlogEntry().getTitle());
    log.info(response.getTitle());
    log.info(response.getContent());
    log.info(response.getSourceName());
    log.info(response.getSourceLink());
    log.info(response.getIpAddress());
    response.getBlogEntry().removeResponse(response);
  }

}
