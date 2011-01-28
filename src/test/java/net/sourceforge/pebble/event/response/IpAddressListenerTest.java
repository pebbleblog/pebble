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
 * Tests for the IpAddressListener class.
 *
 * @author Simon Brown
 */
public class IpAddressListenerTest extends SingleBlogTestCase {

  private IpAddressListener listener;
  private Comment comment;
  private CommentEvent commentEvent;
  private TrackBack trackBack;
  private TrackBackEvent trackBackEvent;

  /**
   * Common setup code.
   */
  protected void setUp() throws Exception {
    super.setUp();

    listener = new IpAddressListener();
    BlogEntry blogEntry = new BlogEntry(blog);
    comment = blogEntry.createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
    commentEvent = new CommentEvent(comment, CommentEvent.COMMENT_ADDED);
    trackBack = blogEntry.createTrackBack("Title", "Excerpt", "url", "blogName", "127.0.0.1");
    trackBackEvent = new TrackBackEvent(trackBack, TrackBackEvent.TRACKBACK_ADDED);
  }

  /**
   * Tests a comment from an IP address that isn't on the whitelist
   * or blacklist.
   */
  public void testCommentIpAddressNotOnWhitelistOrBlacklist() {
    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "");
    blog.getPluginProperties().setProperty(IpAddressListener.BLACKLIST_KEY, "");

    listener.commentAdded(commentEvent);
    assertTrue(comment.isPending());
    assertEquals(0, comment.getSpamScore());
  }

  /**
   * Tests a comment from an IP address that is on the blacklist.
   */
  public void testCommentIpAddressOnBlacklist() {
    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "");
    blog.getPluginProperties().setProperty(IpAddressListener.BLACKLIST_KEY, "127.0.0.1");

    listener.commentAdded(commentEvent);
    assertTrue(comment.isPending());
    assertEquals(1, comment.getSpamScore());
  }

  /**
   * Tests a comment from an IP address that is on the whitelist.
   */
  public void testCommentIpAddressOnWhitelist() {
    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "127.0.0.1");
    blog.getPluginProperties().setProperty(IpAddressListener.BLACKLIST_KEY, "");

    listener.commentAdded(commentEvent);
    assertTrue(comment.isApproved());
    assertEquals(0, comment.getSpamScore());
  }

  /**
   * Tests a TrackBack from an IP address that isn't on the whitelist
   * or blacklist.
   */
  public void testTrackBackIpAddressNotOnWhitelistOrBlacklist() {
    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "");
    blog.getPluginProperties().setProperty(IpAddressListener.BLACKLIST_KEY, "");

    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isPending());
    assertEquals(0, trackBack.getSpamScore());
  }

  /**
   * Tests a TrackBack from an IP address that is on the whitelist.
   */
  public void testTrackBackIpAddressOnWhitelist() {
    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "127.0.0.1");
    blog.getPluginProperties().setProperty(IpAddressListener.BLACKLIST_KEY, "");

    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isApproved());
    assertEquals(0, trackBack.getSpamScore());
  }

  /**
   * Tests a TrackBack from an IP address that is on the blacklist.
   */
  public void testTrackBackIpAddressOnBlacklist() {
    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "");
    blog.getPluginProperties().setProperty(IpAddressListener.BLACKLIST_KEY, "127.0.0.1");

    listener.trackBackAdded(trackBackEvent);
    assertTrue(trackBack.isPending());
    assertEquals(1, trackBack.getSpamScore());
  }

  /**
   * Tests that, when a comment is marked as approved, its IP address is added
   * to the whitelist.
   */
  public void testCommentIpAddressAddedToWhitelistWhenApproved() {
    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "");

    assertEquals("", blog.getPluginProperties().getProperty(IpAddressListener.WHITELIST_KEY));
    listener.blogEntryResponseApproved(comment);
    assertEquals("127.0.0.1", blog.getPluginProperties().getProperty(IpAddressListener.WHITELIST_KEY));
  }

  /**
   * Tests that, when a comment is marked as rejected, its IP address is added
   * to the blacklist.
   */
  public void testCommentIpAddressAddedToBlacklistWhenRejected() {
    blog.getPluginProperties().setProperty(IpAddressListener.BLACKLIST_KEY, "");

    assertEquals("", blog.getPluginProperties().getProperty(IpAddressListener.BLACKLIST_KEY));
    listener.blogEntryResponseRejected(comment);
    assertEquals("127.0.0.1", blog.getPluginProperties().getProperty(IpAddressListener.BLACKLIST_KEY));
  }

  /**
   * Tests that, when a comment is marked as rejected, its IP address is removed
   * from the whitelist.
   */
  public void testCommentIpAddressRemovedFromWhitelistWhenRejected() {
    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "192.168.0.1,127.0.0.1,192.168.0.2");
    listener.blogEntryResponseRejected(comment);
    assertEquals("192.168.0.1,192.168.0.2", blog.getPluginProperties().getProperty(IpAddressListener.WHITELIST_KEY));

    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "127.0.0.1,192.168.0.1");
    listener.blogEntryResponseRejected(comment);
    assertEquals("192.168.0.1", blog.getPluginProperties().getProperty(IpAddressListener.WHITELIST_KEY));

    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "192.168.0.1,127.0.0.1");
    listener.blogEntryResponseRejected(comment);
    assertEquals("192.168.0.1", blog.getPluginProperties().getProperty(IpAddressListener.WHITELIST_KEY));

    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "127.0.0.1");
    listener.blogEntryResponseRejected(comment);
    assertEquals("", blog.getPluginProperties().getProperty(IpAddressListener.WHITELIST_KEY));
  }

  /**
   * Tests that, when a TrackBack is marked as approved, its IP address is added
   * to the whitelist.
   */
  public void testTrackBackIpAddressAddedToWhitelistWhenApproved() {
    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "");

    assertEquals("", blog.getPluginProperties().getProperty(IpAddressListener.WHITELIST_KEY));
    listener.blogEntryResponseApproved(trackBack);
    assertEquals("127.0.0.1", blog.getPluginProperties().getProperty(IpAddressListener.WHITELIST_KEY));
  }

  /**
   * Tests that, when a TrackBack is marked as rejected, its IP address is added
   * to the blacklist.
   */
  public void testTrackBackIpAddressAddedToBlacklistWhenRejected() {
    blog.getPluginProperties().setProperty(IpAddressListener.BLACKLIST_KEY, "");

    assertEquals("", blog.getPluginProperties().getProperty(IpAddressListener.BLACKLIST_KEY));
    listener.blogEntryResponseRejected(trackBack);
    assertEquals("127.0.0.1", blog.getPluginProperties().getProperty(IpAddressListener.BLACKLIST_KEY));
  }


  /**
   * Tests that, when a TrackBack is marked as rejected, its IP address is removed
   * from the whitelist.
   */
  public void testTrackBackIpAddressRemovedFromWhitelistWhenRejected() {
    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "192.168.0.1,127.0.0.1,192.168.0.2");
    listener.blogEntryResponseRejected(trackBack);
    assertEquals("192.168.0.1,192.168.0.2", blog.getPluginProperties().getProperty(IpAddressListener.WHITELIST_KEY));

    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "127.0.0.1,192.168.0.1");
    listener.blogEntryResponseRejected(trackBack);
    assertEquals("192.168.0.1", blog.getPluginProperties().getProperty(IpAddressListener.WHITELIST_KEY));

    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "192.168.0.1,127.0.0.1");
    listener.blogEntryResponseRejected(trackBack);
    assertEquals("192.168.0.1", blog.getPluginProperties().getProperty(IpAddressListener.WHITELIST_KEY));

    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "127.0.0.1");
    listener.blogEntryResponseRejected(trackBack);
    assertEquals("", blog.getPluginProperties().getProperty(IpAddressListener.WHITELIST_KEY));
  }

}
