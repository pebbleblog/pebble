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

import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackListener;


/**
 * Tests for the TrackBack class.
 *
 * @author    Simon Brown
 */
public class TrackBackTest extends SingleBlogTestCase {

  private TrackBack trackback;

  protected void setUp() throws Exception {
    super.setUp();

    trackback = new BlogEntry(blog).createTrackBack("Title", "Excerpt", "http://www.somedomain.com", "Some blog", "127.0.0.1");
    trackback.setEventsEnabled(true);
  }

  /**
   * Test that a TrackBack instance can be created correctly.
   */
  public void testConstructionOfSimpleInstance() {
    assertNotNull(trackback);
    assertEquals("Title", trackback.getTitle());
    assertEquals("Excerpt", trackback.getExcerpt());
    assertEquals("http://www.somedomain.com", trackback.getUrl());
    assertEquals("Some blog", trackback.getBlogName());
    assertEquals("127.0.0.1", trackback.getIpAddress());
    assertNotNull(trackback.getDate());
    assertEquals(trackback.getDate().getTime(), trackback.getId());
    assertNotNull(trackback.getBlogEntry());
    assertEquals(State.APPROVED, trackback.getState());
    assertEquals("t/" + trackback.getBlogEntry().getId() + "/" + trackback.getId(), trackback.getGuid());
  }

  /**
   * Tests that the title is set appropriately.
   */
  public void testTitle() {
    assertEquals("Title", trackback.getTitle());
  }

  /**
   * Tests that the title defaults to the URL is not specified.
   */
  public void testTitleDefaultsToUrlWhenNotSpecified() {
    trackback.setTitle("");
    assertEquals("http://www.somedomain.com", trackback.getTitle());

    trackback.setTitle(null);
    assertEquals("http://www.somedomain.com", trackback.getTitle());
  }

  /**
   * Tests that the excerpt can't be null.
   */
  public void testExcerptNeverNull() {
    trackback.setExcerpt("");
    assertEquals("", trackback.getExcerpt());

    trackback.setExcerpt(null);
    assertEquals("", trackback.getExcerpt());
  }

  /**
   * Tests that the blog name can't be null.
   */
  public void testBlogNameNeverNull() {
    trackback.setBlogName("");
    assertEquals("", trackback.getBlogName());

    trackback.setBlogName(null);
    assertEquals("", trackback.getBlogName());
  }

  /**
   * Tests the permalink for a TrackBack.
   */
  public void testPermalink() {
    assertEquals(trackback.getBlogEntry().getPermalink() + "#trackback" + trackback.getId(), trackback.getPermalink());
  }

  /**
   * Tests that a TrackBack can be cloned.
   */
  public void testClone() {
    TrackBack clonedTrackBack = (TrackBack)trackback.clone();

    assertEquals(trackback.getTitle(), clonedTrackBack.getTitle());
    assertEquals(trackback.getExcerpt(), clonedTrackBack.getExcerpt());
    assertEquals(trackback.getUrl(), clonedTrackBack.getUrl());
    assertEquals(trackback.getBlogName(), clonedTrackBack.getBlogName());
    assertEquals(trackback.getIpAddress(), clonedTrackBack.getIpAddress());
    assertEquals(trackback.getDate(), clonedTrackBack.getDate());
    assertEquals(trackback.getId(), clonedTrackBack.getId());
    assertEquals(trackback.getState(), clonedTrackBack.getState());
    assertEquals(trackback.getBlogEntry(), clonedTrackBack.getBlogEntry());
  }

  /**
   * Tests that listeners are not fired when a TrackBack is marked as pending.
   */
  public void testListenersFiredWhenTrackBackMarkedAsPending() {

    TrackBackListener listener = new TrackBackListener() {
      public void trackBackAdded(TrackBackEvent event) {
        fail();
      }

      public void trackBackRemoved(TrackBackEvent event) {
        fail();
      }

      public void trackBackApproved(TrackBackEvent event) {
        fail();
      }

      public void trackBackRejected(TrackBackEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addTrackBackListener(listener);
    trackback.setPending();
  }

  /**
   * Tests that a TrackBackEvent can be vetoed.
   */
  public void trackBackEventCanBeVetoed() {
    // create 2 listeners, veto the event in the first and
    // fail if the second receives the event

    trackback.setPending();

    TrackBackListener listener1 = new TrackBackListener() {
      public void trackBackAdded(TrackBackEvent event) {
        fail();
      }

      public void trackBackRemoved(TrackBackEvent event) {
        fail();
      }

      public void trackBackApproved(TrackBackEvent event) {
        event.veto();
      }

      public void trackBackRejected(TrackBackEvent event) {
        fail();
      }
    };

    TrackBackListener listener2 = new TrackBackListener() {
      public void trackBackAdded(TrackBackEvent event) {
        fail();
      }

      public void trackBackRemoved(TrackBackEvent event) {
        fail();
      }

      public void trackBackApproved(TrackBackEvent event) {
        fail();
      }

      public void trackBackRejected(TrackBackEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addTrackBackListener(listener1);
    blog.getEventListenerList().addTrackBackListener(listener2);

    trackback.setApproved();
  }

  /**
   * Tests that listeners are not fired when a cloned TrackBack is approved.
   * Why? Because manipulating comments from a blog entry decorator will
   * generate excess events if not disabled.
   */
  public void testListenersNotFiredWhenClonedTrackBackApproved() {
    trackback.setPending();
    trackback = (TrackBack)trackback.clone();

    TrackBackListener listener = new TrackBackListener() {
      public void trackBackAdded(TrackBackEvent event) {
        fail();
      }

      public void trackBackRemoved(TrackBackEvent event) {
        fail();
      }

      public void trackBackApproved(TrackBackEvent event) {
        fail();
      }

      public void trackBackRejected(TrackBackEvent event) {
        fail();
      }
    };

    blog.getEventListenerList().addTrackBackListener(listener);
    trackback.setApproved();
  }

}
