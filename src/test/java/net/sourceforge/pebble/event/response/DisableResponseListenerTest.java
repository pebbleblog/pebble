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

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.domain.TrackBack;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;

/**
 * Tests for the DisableResponseListener class.
 *
 * @author Simon Brown
 */
public class DisableResponseListenerTest extends SingleBlogTestCase {

  private DisableResponseListener listener;
  private BlogEntry blogEntry;
  private Comment comment;
  private CommentEvent commentEvent;
  private TrackBack trackBack;
  private TrackBackEvent trackBackEvent;

  /**
   * Common setup code.
   */
  protected void setUp() throws Exception {
    super.setUp();

    listener = new DisableResponseListener();
    blogEntry = new BlogEntry(blog);
    comment = blogEntry.createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
    commentEvent = new CommentEvent(comment, CommentEvent.COMMENT_ADDED);
    trackBack = blogEntry.createTrackBack("Title", "Excerpt", "url", "blogName", "127.0.0.1");
    trackBackEvent = new TrackBackEvent(trackBack, TrackBackEvent.TRACKBACK_ADDED);
  }

  /**
   * Tests a comment.
   */
  public void testCommentIsDeleted() {
    blogEntry.addComment(comment);
    assertEquals(1, blogEntry.getComments().size());
    listener.commentAdded(commentEvent);
    assertEquals(0, blogEntry.getComments().size());
  }

  /**
   * Tests a TrackBack.
   */
  public void testTrackBackIsDeleted() {
    blogEntry.addTrackBack(trackBack);
    assertEquals(1, blogEntry.getTrackBacks().size());
    listener.trackBackAdded(trackBackEvent);
    assertEquals(0, blogEntry.getTrackBacks().size());
  }

}
