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

import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.domain.TrackBack;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;

/**
 * Tests for the ContentSpamListener class.
 *
 * @author Simon Brown
 */
public class ContentSpamListenerTest extends SingleBlogTestCase {

  private ContentSpamListener listener;
  private Comment comment;
  private CommentEvent commentEvent;
  private TrackBack trackBack;
  private TrackBackEvent trackBackEvent;

  /**
   * Common setup code.
   */
  protected void setUp() throws Exception {
    super.setUp();

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "test");
    listener = new ContentSpamListener();
    BlogEntry blogEntry = new BlogEntry(blog);
    comment = blogEntry.createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
    commentEvent = new CommentEvent(comment, CommentEvent.COMMENT_ADDED);
    trackBack = blogEntry.createTrackBack("Title", "Excerpt", "url", "blogName", "127.0.0.1");
    trackBackEvent = new TrackBackEvent(trackBack, TrackBackEvent.TRACKBACK_ADDED);
  }

  /**
   * Tests a comment with no content spam.
   */
  public void testCommentWithNoContentSpam() {
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());
  }

  /**
   * Tests a comment with some content spam.
   */
  public void testCommentWithSomeContentSpam() {
    comment.setBody("Here is some junk about poker and online casinos.");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());
    assertEquals(0, comment.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isPending());
    assertEquals(1, comment.getSpamScore());
  }

  /**
   * Tests a comment with some content spam in upper case.
   */
  public void testCommentWithSomeContentSpamInUpperCase() {
    comment.setBody("Here is some junk about POKER and ONLINE CASINOS.");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());
    assertEquals(0, comment.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isPending());
    assertEquals(1, comment.getSpamScore());
  }

  /**
   * Tests a comment with some content spam and a custom threshold.
   */
  public void testCommentWithSomeContentSpamAndThreshold() {
    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "poker");
    blog.getPluginProperties().setProperty(ContentSpamListener.THRESHOLD_KEY, "1");
    comment.setBody("Here is some junk about poker and online casinos.");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());
    assertEquals(0, comment.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isPending());
    assertEquals(1, comment.getSpamScore());
  }

  /**
   * Tests a comment with some content spam in the author field.
   */
  public void testCommentWithSomeContentSpamInAuthorField() {
    comment.setAuthor("online casinos");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());
    assertEquals(0, comment.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isPending());
    assertEquals(1, comment.getSpamScore());
  }

  /**
   * Tests a comment with some content spam in the title field.
   */
  public void testCommentWithSomeContentSpamInTitleField() {
    comment.setTitle("online casinos");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());
    assertEquals(0, comment.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isPending());
    assertEquals(1, comment.getSpamScore());
  }

  /**
   * Tests a comment with some content spam in the title field.
   */
  public void testCommentWithSomeContentSpamInWebsiteField() {
    comment.setWebsite("http://online.casinos.com");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());
    assertEquals(0, comment.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isPending());
    assertEquals(1, comment.getSpamScore());
  }

  /**
   * Tests a comment with some content spam in all fields.
   */
  public void testCommentWithSomeContentSpamInAllFields() {
    comment.setAuthor("online casinos");
    comment.setTitle("online casinos");
    comment.setWebsite("http://online.casinos.com");
    comment.setBody("Here is some junk about poker and online casinos.");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());
    assertEquals(0, comment.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.commentAdded(commentEvent);
    assertTrue(comment.isPending());
    assertEquals(4, comment.getSpamScore());
  }

  /**
   * Tests a TrackBack with no content spam.
   */
  public void testTrackBackWithNoContentSpam() {
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isApproved());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isApproved());
  }

  /**
   * Tests a TrackBack with some content spam.
   */
  public void testTrackBackWithSomeContentSpam() {
    trackBack.setExcerpt("Here is some junk about poker and online casinos.");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isApproved());
    assertEquals(0, trackBack.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isPending());
    assertEquals(1, trackBack.getSpamScore());
  }

  /**
   * Tests a TrackBack with some content spam in upper case.
   */
  public void testTrackBackWithSomeContentSpaminUpperCase() {
    trackBack.setExcerpt("Here is some junk about POKER and ONLINE CASINOS.");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isApproved());
    assertEquals(0, trackBack.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isPending());
    assertEquals(1, trackBack.getSpamScore());
  }

  /**
   * Tests a TrackBack with some content spam and a custom threshold.
   */
  public void testTrackBackWithSomeContentSpamAndThreshold() {
    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "poker");
    blog.getPluginProperties().setProperty(ContentSpamListener.THRESHOLD_KEY, "1");
    trackBack.setExcerpt("Here is some junk about poker and online casinos.");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isApproved());
    assertEquals(0, trackBack.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isPending());
    assertEquals(1, trackBack.getSpamScore());
  }

  /**
   * Tests a TrackBack with some content spam in the blog name field.
   */
  public void testTrackBackWithSomeContentSpamInBlogNameField() {
    trackBack.setBlogName("online casinos");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isApproved());
    assertEquals(0, trackBack.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isPending());
    assertEquals(1, trackBack.getSpamScore());
  }

  /**
   * Tests a TrackBack with some content spam in the title field.
   */
  public void testTrackBackWithSomeContentSpamInTitleField() {
    trackBack.setTitle("online casinos");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isApproved());
    assertEquals(0, trackBack.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isPending());
    assertEquals(1, trackBack.getSpamScore());
  }

  /**
   * Tests a TrackBack with some content spam in the blog URL field.
   */
  public void testTrackBackWithSomeContentSpamInBlogUrlField() {
    trackBack.setUrl("http://online.casinos.com");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isApproved());
    assertEquals(0, trackBack.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isPending());
    assertEquals(1, trackBack.getSpamScore());
  }

  /**
   * Tests a TrackBack with some content spam in all fields.
   */
  public void testTrackBackWithSomeContentSpamInAllField() {
    trackBack.setTitle("online casinos");
    trackBack.setBlogName("online casinos");
    trackBack.setUrl("http://online.casinos.com");
    trackBack.setExcerpt("Here is some junk about poker and online casinos.");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isApproved());
    assertEquals(0, trackBack.getSpamScore());

    blog.getPluginProperties().setProperty(ContentSpamListener.REGEX_LIST_KEY, "casinos, poker, drugs");
    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isPending());
    assertEquals(4, trackBack.getSpamScore());
  }

}
