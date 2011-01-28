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

import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;

/**
 * Tests for the LinkSpamListener class.
 *
 * @author Simon Brown
 */
public class LinkSpamListenerTest extends SingleBlogTestCase {

  private LinkSpamListener listener;
  private Comment comment;
  private CommentEvent commentEvent;
  private TrackBack trackBack;
  private TrackBackEvent trackBackEvent;

  /**
   * Common setup code.
   */
  protected void setUp() throws Exception {
    super.setUp();

    listener = new LinkSpamListener();
    comment = new BlogEntry(blog).createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
    commentEvent = new CommentEvent(comment, CommentEvent.COMMENT_ADDED);
  }

  /**
   * Tests a comment with no links.
   */
  public void testCommentAddedWithNoLinks() {
    assertTrue(comment.isApproved());
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());
  }

  /**
   * Tests a comment with two links, the default threshold being three.
   */
  public void testCommentAddedWithTwoLinks() {
    StringBuffer buf = new StringBuffer();
    buf.append("Here is some content ...");
    buf.append("<a href=\"http://www.somedomain1.com\">link</a>");
    buf.append("<a href=\"http://www.somedomain2.com\">link</a>");
    comment.setBody(buf.toString());
    assertTrue(comment.isApproved());
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());

    blog.getPluginProperties().setProperty(LinkSpamListener.COMMENT_THRESHOLD_KEY, "1");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isPending());
  }

  /**
   * Tests a comment with three links, the default threshold being three.
   */
  public void testCommentAddedWithThreeLinks() {
    StringBuffer buf = new StringBuffer();
    buf.append("Here is some content ...");
    buf.append("<a href=\"http://www.somedomain1.com\">link</a>");
    buf.append("<a href=\"http://www.somedomain2.com\">link</a>");
    buf.append("<a href=\"http://www.somedomain3.com\">link</a>");
    comment.setBody(buf.toString());
    assertTrue(comment.isApproved());
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());
  }

  /**
   * Tests a comment with four links, the default threshold being three.
   */
  public void testCommentAddedWithFourLinks() {
    StringBuffer buf = new StringBuffer();
    buf.append("Here is some content ...");
    buf.append("<a href=\"http://www.somedomain1.com\">link</a>");
    buf.append("<a href=\"http://www.somedomain2.com\">link</a>");
    buf.append("<A HREF=\"http://www.somedomain3.com\">link</A>");
    buf.append("<a href=\"http://www.somedomain4.com\">link</a>");
    comment.setBody(buf.toString());
    assertTrue(comment.isApproved());
    listener.commentAdded(commentEvent);
    assertTrue(comment.isPending());

    comment.setApproved();
    blog.getPluginProperties().setProperty(LinkSpamListener.COMMENT_THRESHOLD_KEY, "4");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());
  }

  /**
   * Tests that the spam score is increased if the link threshold is exceeded.
   */
  public void testSpamScoreIncremented() {
    StringBuffer buf = new StringBuffer();
    buf.append("Here is some content ...");
    buf.append("<a href=\"http://www.somedomain1.com\">link</a>");
    buf.append("<a href=\"http://www.somedomain2.com\">link</a>");
    buf.append("<A HREF=\"http://www.somedomain3.com\">link</A>");
    buf.append("<a href=\"http://www.somedomain4.com\">link</a>");
    comment.setBody(buf.toString());
    assertTrue(comment.isApproved());
    assertEquals(0, comment.getSpamScore());
    listener.commentAdded(commentEvent);
    assertTrue(comment.isPending());
    assertEquals(1, comment.getSpamScore());
  }

}
