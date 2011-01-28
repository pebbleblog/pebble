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

import net.sourceforge.pebble.api.event.blog.BlogListener;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryListener;
import net.sourceforge.pebble.api.event.comment.CommentListener;
import net.sourceforge.pebble.api.event.trackback.TrackBackListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Maintains a list of listeners, allowing them to be added and removed.
 *
 * @author Simon Brown
 */
public class EventListenerList {

  /** the log in use by this class */
  private static final Log log = LogFactory.getLog(EventListenerList.class);

  /** the list of blog listeners */
  private List blogListeners;

  /** the list of blog entry listeners */
  private List blogEntryListeners;

  /** the list of comment listeners */
  private List commentListeners;

  /** the list of TrackBack listeners */
  private List trackBackListeners;

  /**
   * Default, no args constructor.
   */
  public EventListenerList() {
    this.blogListeners = new ArrayList();
    this.blogEntryListeners = new ArrayList();
    this.commentListeners = new ArrayList();
    this.trackBackListeners = new ArrayList();
  }

  /**
   * Gets the list of blog listeners.
   *
   * @return  a List of BlogListener instances
   */
  public List getBlogListeners() {
    return this.blogListeners;
  }

  /**
   * Registers a blog listener.
   *
   * @param listener    a BlogListener instance
   */
  public void addBlogListener(BlogListener listener) {
    if (!blogListeners.contains(listener)) {
      blogListeners.add(listener);
      log.debug(listener.getClass().getName() + " registered");
    }
  }

  /**
   * Unregisters a blog listener.
   *
   * @param listener    a BlogListener instance
   */
  public void removeBlogListener(BlogListener listener) {
    blogListeners.remove(listener);
  }

  /**
   * Gets the list of blog entry listeners.
   *
   * @return  a List of BlogEntryListener instances
   */
  public List getBlogEntryListeners() {
    return this.blogEntryListeners;
  }

  /**
   * Registers a blog entry listener.
   *
   * @param listener    a BlogEntryListener instance
   */
  public void addBlogEntryListener(BlogEntryListener listener) {
    if (!blogEntryListeners.contains(listener)) {
      blogEntryListeners.add(listener);
      log.debug(listener.getClass().getName() + " registered");
    }
  }

  /**
   * Gets the list of comment listeners.
   *
   * @return  a List of CommentListener instances
   */
  public List getCommentListeners() {
    return this.commentListeners;
  }

  /**
   * Registers a comment listener.
   *
   * @param listener    a CommentListener instance
   */
  public void addCommentListener(CommentListener listener) {
    if (!commentListeners.contains(listener)) {
      commentListeners.add(listener);
      log.debug(listener.getClass().getName() + " registered");
    }
  }

  /**
   * Gets the list of TrackBack listeners.
   *
   * @return  a List of TrackBackListener instances
   */
  public List getTrackBackListeners() {
    return this.trackBackListeners;
  }

  /**
   * Registers a TrackBack listener.
   *
   * @param listener    a TrackBackListener instance
   */
  public void addTrackBackListener(TrackBackListener listener) {
    if (!trackBackListeners.contains(listener)) {
      trackBackListeners.add(listener);
      log.debug(listener.getClass().getName() + " registered");
    }
  }


}
