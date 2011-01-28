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

import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.comment.CommentListener;

import java.util.Calendar;
import java.util.Date;

/**
 * Tests for the Comment class.
 *
 * @author    Simon Brown
 */
public class CommentTest extends SingleBlogTestCase {

  private BlogEntry blogEntry;
  private Comment comment;

  protected void setUp() throws Exception {
    super.setUp();

    blogEntry = new BlogEntry(blog);
    comment = blogEntry.createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
    comment.setEventsEnabled(true);
  }

  /**
   * Test that a Comment instance can be created correctly.
   */
  public void testConstructionOfSimpleInstance() {
    assertNotNull(comment);
    assertEquals("Title", comment.getTitle());
    assertEquals("Body", comment.getBody());
    assertEquals("Author", comment.getAuthor());
    assertEquals("me@somedomain.com", comment.getEmail());
    assertEquals("http://www.google.com", comment.getWebsite());
    assertEquals("http://graph.facebook.com/user/picture", comment.getAvatar());
    assertEquals("127.0.0.1", comment.getIpAddress());
    assertNotNull(comment.getDate());
    assertEquals(comment.getDate().getTime(), comment.getId());
    assertNotNull(comment.getBlogEntry());
    assertEquals(State.APPROVED, comment.getState());
    assertEquals("c/" + comment.getBlogEntry().getId() + "/" + comment.getId(), comment.getGuid());
  }

  /**
   * Tests that the author name is properly escaped and set.
   */
  public void testAuthor() {
    assertEquals("Author", comment.getAuthor());

    // blank or null author name defaults to "Anonymous"
    comment.setAuthor("");
    assertEquals("Anonymous", comment.getAuthor());
    comment.setAuthor(null);
    assertEquals("Anonymous", comment.getAuthor());

    // for security, special HTML characters aren't removed
    // (they are rendered out at runtime)
    comment.setAuthor("<Author>");
    assertEquals("<Author>", comment.getAuthor());
  }

  /**
   * Tests that the e-mail address is properly escaped and set.
   */
  public void testEmailAddress() {
    assertEquals("me@somedomain.com", comment.getEmail());

    // blank or null e-mail defaults to null
    comment.setEmail("");
    assertEquals(null, comment.getEmail());
    comment.setEmail(null);
    assertEquals(null, comment.getEmail());

    // for security, special HTML characters are removed
    comment.setEmail("<me@somedomain.com>");
    assertEquals("&lt;me@somedomain.com&gt;", comment.getEmail());
  }

  /**
   * Tests that the website is properly escaped and set.
   */
  public void testWebsite() {
    assertEquals("http://www.google.com", comment.getWebsite());

    // blank or null website name defaults to null
    comment.setWebsite("");
    assertEquals(null, comment.getWebsite());
    comment.setWebsite(null);
    assertEquals(null, comment.getWebsite());

    // for security, special HTML characters are removed
    comment.setWebsite("<script>http://www.google.com");
    assertEquals("http://www.google.com", comment.getWebsite());

    // anything websites are also checked for known prefixes and "http://"
    // is prepended if missing
    comment.setWebsite("http://www.google.com");
    assertEquals("http://www.google.com", comment.getWebsite());
    comment.setWebsite("https://www.google.com");
    assertEquals("https://www.google.com", comment.getWebsite());
    comment.setWebsite("ftp://www.google.com");
    assertEquals("ftp://www.google.com", comment.getWebsite());
    comment.setWebsite("mailto://www.google.com");
    assertEquals("mailto://www.google.com", comment.getWebsite());
    comment.setWebsite("www.google.com");
    assertEquals("http://www.google.com", comment.getWebsite());
  }


  /**
   * Tests that the avatar is properly escaped and set.
   */
  public void testAvatar() {
    assertEquals("http://graph.facebook.com/user/picture", comment.getAvatar());

    // blank or null avatar name defaults to null
    comment.setAvatar("");
    assertEquals(null, comment.getAvatar());
    comment.setAvatar(null);
    assertEquals(null, comment.getAvatar());

    // for security, special HTML characters are removed
    comment.setAvatar("<script>http://graph.facebook.com/user/picture");
    assertEquals("http://graph.facebook.com/user/picture", comment.getAvatar());

    // anything avatar are also checked for known prefixes and "http://"
    // is prepended if missing
    comment.setAvatar("http://graph.facebook.com/user/picture");
    assertEquals("http://graph.facebook.com/user/picture", comment.getAvatar());
    comment.setAvatar("https://graph.facebook.com/user/picture");
    assertEquals("https://graph.facebook.com/user/picture", comment.getAvatar());
    comment.setAvatar("ftp://graph.facebook.com/user/picture");
    assertEquals("ftp://graph.facebook.com/user/picture", comment.getAvatar());
    comment.setAvatar("mailto://graph.facebook.com/user/picture");
    assertEquals("mailto://graph.facebook.com/user/picture", comment.getAvatar());
    comment.setAvatar("graph.facebook.com/user/picture");
    assertEquals("http://graph.facebook.com/user/picture", comment.getAvatar());
  }

  /**
   * Tests the body.
   */
  public void testBody() {
    comment.setBody("");
    assertEquals(null, comment.getBody());
    comment.setBody(null);
    assertEquals(null, comment.getBody());

    comment.setBody("Here is some text");
    assertEquals("Here is some text", comment.getBody());
  }

  /**
   * Tests that the date can never be null.
   */
  public void testDate() {
    assertNotNull(comment.getDate());

    comment.setDate(new Date());
    assertNotNull(comment.getDate());

    comment.setDate(null);
    assertNotNull(comment.getDate());
  }

  /**
   * Tests that the title is set when an owning blog entry is present.
   */
  public void testTitleTakenFromOwningBlogEntryWhenNotSpecified() {
    BlogEntry entry = new BlogEntry(blog);
    entry.setTitle("My blog entry title");
    comment = entry.createComment(null, "", "", "", "", "", "");
    assertEquals("Re: My blog entry title", comment.getTitle());
    comment = entry.createComment("", "", "", "", "", "", "");
    assertEquals("Re: My blog entry title", comment.getTitle());
  }

  /**
   * Tests the number of parents is 0 by default.
   */
  public void testNumberOfParentsIsZeroByDefault() {
    assertEquals(0, comment.getNumberOfParents());
  }

  /**
   * Tests that the number of parents is correct when comments are nested.
   */
  public void testNumberOfParentsIsCorrectWhenNested() {
    comment.setParent(new BlogEntry(blog).createComment("", "", "", "", "", "", ""));
    assertEquals(1, comment.getNumberOfParents());
  }

  /**
   * Tests that adding a null comment doesn't cause an NPE.
   */
  public void testAddingNullCommentDoesntCauseException() {
    comment.addComment(null);
  }

  /**
   * Tests that removing a null comment doesn't cause an NPE.
   */
  public void testRemovingNullCommentDoesntCauseException() {
    comment.removeComment(null);
  }

  /**
   * Tests for the truncated body.
   */
  public void testTruncatedBody() {
    comment.setBody(null);
    assertEquals("", comment.getTruncatedBody());

    comment.setBody("1234567890");
    assertEquals("1234567890", comment.getTruncatedBody());

    comment.setBody("Here is <b>some</b> <i>HTML</i>.");
    assertEquals("Here is some HTML.", comment.getTruncatedBody());

    comment.setBody("Here is &lt;some&gt; text.");
    assertEquals("Here is some text.", comment.getTruncatedBody());

    comment.setBody("1234567890123456789012345678901234567890123456789012345678901234567890");
    assertEquals("12345678901234567890...", comment.getTruncatedBody());

    comment.setBody("1234567890 123456789012345678901234567890123456789012345678901234567890");
    assertEquals("1234567890 12345678901234567890...", comment.getTruncatedBody());

    comment.setBody("<p>" +
        "You can grab the source for Pebble 1.6 by doing the following:<pre>  cvs -d:pserver:anonymous@cvs.sourceforge.net:/cvsroot/pebble login \n" +
        "  cvs -d:pserver:anonymous@cvs.sourceforge.net:/cvsroot/pebble checkout -r v1_6_0 pebble</pre>\n" +
        "When prompted for a password, just press enter (there is no password).\n" +
        "</p>");
    assertEquals("You can grab the source for Pebble 1.6 by doing the following:  cvs -d:pserver:anonymous...", comment.getTruncatedBody());

    comment.setBody("<p>" +
        "123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 " +
        "123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 " +
        "123456789 123456789 123456789 123456789 12345678W&uuml;nsche");
    assertEquals("123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 123456789 ...", comment.getTruncatedBody());
  }

  /**
   * Tests that a comment can be cloned.
   */
  public void testClone() {
    Comment clonedComment = (Comment)comment.clone();

    assertEquals(comment.getTitle(), clonedComment.getTitle());
    assertEquals(comment.getBody(), clonedComment.getBody());
    assertEquals(comment.getWebsite(), clonedComment.getWebsite());
    assertEquals(comment.getAvatar(), clonedComment.getAvatar());
    assertEquals(comment.getAuthor(), clonedComment.getAuthor());
    assertEquals(comment.getIpAddress(), clonedComment.getIpAddress());
    assertEquals(comment.getDate(), clonedComment.getDate());
    assertEquals(comment.getId(), clonedComment.getId());
    assertEquals(comment.getState(), clonedComment.getState());
    assertEquals(comment.getParent(), clonedComment.getParent());
    assertEquals(comment.getBlogEntry(), clonedComment.getBlogEntry());
  }

  /**
   * Test the equals() method.
   */
  public void testEquals() {
    assertTrue(comment.equals(comment));

    Calendar cal = blog.getCalendar();
    cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)-1);
    Comment comment2 = new BlogEntry(blog).createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1", cal.getTime(), State.APPROVED);
    assertFalse(comment.equals(comment2));

  }

  /**
   * Tests the various states for a comment.
   */
  public void testStates() {
    // the default is approved
    assertEquals(State.APPROVED, comment.getState());
    assertTrue(comment.isApproved());
    assertFalse(comment.isPending());
    assertFalse(comment.isRejected());

    comment.setPending();
    assertEquals(State.PENDING, comment.getState());
    assertFalse(comment.isApproved());
    assertTrue(comment.isPending());
    assertFalse(comment.isRejected());

    comment.setRejected();
    assertEquals(State.REJECTED, comment.getState());
    assertFalse(comment.isApproved());
    assertFalse(comment.isPending());
    assertTrue(comment.isRejected());
  }

  /**
   * Tests that listeners are not fired when a comment is marked as pending.
   */
  public void testListenersFiredWhenCommentMarkedAsPending() {

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
        fail();
      }
    };

    blog.getEventListenerList().addCommentListener(listener);
    comment.setPending();
  }

  /**
   * Tests that a CommentEvent can be vetoed.
   */
  public void commentEventCanBeVetoed() {
    // create 2 listeners, veto the event in the first and
    // fail if the second receives the event

    comment.setPending();

    CommentListener listener1 = new CommentListener() {
      public void commentAdded(CommentEvent event) {
        fail();
      }

      public void commentRemoved(CommentEvent event) {
        fail();
      }

      public void commentApproved(CommentEvent event) {
        event.veto();
      }

      public void commentRejected(CommentEvent event) {
        fail();
      }
    };

    CommentListener listener2 = new CommentListener() {
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
        fail();
      }
    };

    blog.getEventListenerList().addCommentListener(listener1);
    blog.getEventListenerList().addCommentListener(listener2);

    comment.setApproved();
  }

  /**
   * Tests that listeners are not fired when a cloned comment is approved.
   * Why? Because manipulating comments from a blog entry decorator will
   * generate excess events if not disabled.
   */
  public void testListenersNotFiredWhenCommentApprovedOnClone() {
    comment.setPending();
    comment = (Comment)comment.clone();

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
        fail();
      }
    };

    blog.getEventListenerList().addCommentListener(listener);
    comment.setApproved();
  }

  public void testNestedCommentsAreUnindexedWhenParentDeleted() throws Exception {
    BlogService service = new BlogService();
    Comment comment2 = blogEntry.createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
    Comment comment3 = blogEntry.createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");

    service.putBlogEntry(blogEntry);
    blogEntry.addComment(comment);

    comment2.setParent(comment);
    blogEntry.addComment(comment2);
    service.putBlogEntry(blogEntry);

    comment3.setParent(comment);
    blogEntry.addComment(comment3);
    service.putBlogEntry(blogEntry);

    assertTrue(blog.getResponseIndex().getPendingResponses().contains(comment.getGuid()));
    assertTrue(blog.getResponseIndex().getPendingResponses().contains(comment2.getGuid()));
    assertTrue(blog.getResponseIndex().getPendingResponses().contains(comment3.getGuid()));

    blogEntry.removeComment(comment.getId());
    service.putBlogEntry(blogEntry);

    assertFalse(blog.getResponseIndex().getPendingResponses().contains(comment.getGuid()));
    assertFalse(blog.getResponseIndex().getPendingResponses().contains(comment2.getGuid()));
    assertFalse(blog.getResponseIndex().getPendingResponses().contains(comment3.getGuid()));
  }

}
