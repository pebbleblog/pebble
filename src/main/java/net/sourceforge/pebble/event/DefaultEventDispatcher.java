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

import net.sourceforge.pebble.api.event.blog.BlogEvent;
import net.sourceforge.pebble.api.event.blog.BlogListener;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryListener;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.comment.CommentListener;
import net.sourceforge.pebble.api.event.EventDispatcher;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackListener;

import java.util.Iterator;

/**
 * Responsible for dispatching events to registered listeners, which are
 * called in the order they were added.
 *
 * @author Simon Brown
 */
public class DefaultEventDispatcher extends EventDispatcher {

  /**
   * Fires a blog event to registered listeners.
   *
   * @param event   the BlogEvent instance
   */
  public void fireBlogEvent(BlogEvent event) {
    Iterator it = getEventListenerList().getBlogListeners().iterator();
    while (it.hasNext()) {
      BlogListener listener = (BlogListener)it.next();
      if (event.getType() == BlogEvent.BLOG_STARTED) {
        listener.blogStarted(event);
      } else if (event.getType() == BlogEvent.BLOG_STOPPED) {
        listener.blogStopped(event);
      }

      // has the event been vetoed?
      if (event.isVetoed()) {
        break;
      }
    }
  }

  /**
   * Fires a blog entry event to registered listeners.
   *
   * @param event   the BlogEntryEvent instance
   */
  public void fireBlogEntryEvent(BlogEntryEvent event) {
    Iterator it = getEventListenerList().getBlogEntryListeners().iterator();
    while (it.hasNext()) {
      BlogEntryListener listener = (BlogEntryListener)it.next();
      if (event.getType() == BlogEntryEvent.BLOG_ENTRY_ADDED) {
        listener.blogEntryAdded(event);
      } else if (event.getType() == BlogEntryEvent.BLOG_ENTRY_REMOVED) {
        listener.blogEntryRemoved(event);
      } else if (event.getType() == BlogEntryEvent.BLOG_ENTRY_CHANGED) {
        listener.blogEntryChanged(event);
      } else if (event.getType() == BlogEntryEvent.BLOG_ENTRY_PUBLISHED) {
        listener.blogEntryPublished(event);
      } else if (event.getType() == BlogEntryEvent.BLOG_ENTRY_UNPUBLISHED) {
        listener.blogEntryUnpublished(event);
      }

      // has the event been vetoed?
      if (event.isVetoed()) {
        break;
      }
    }
  }

  /**
   * Fires a comment event to registered listeners.
   *
   * @param event   the CommentEvent instance
   */
  public void fireCommentEvent(CommentEvent event) {
    Iterator it = getEventListenerList().getCommentListeners().iterator();
    while (it.hasNext()) {
      CommentListener listener = (CommentListener)it.next();
      if (event.getType() == CommentEvent.COMMENT_ADDED) {
        listener.commentAdded(event);
      } else if (event.getType() == CommentEvent.COMMENT_REMOVED) {
        listener.commentRemoved(event);
      } else if (event.getType() == CommentEvent.COMMENT_APPROVED) {
        listener.commentApproved(event);
      } else if (event.getType() == CommentEvent.COMMENT_REJECTED) {
        listener.commentRejected(event);
      }

      // has the event been vetoed?
      if (event.isVetoed()) {
        break;
      }
    }
  }

  /**
   * Fires a TrackBack event to registered listeners.
   *
   * @param event   the TrackBackEvent instance
   */
  public void fireTrackBackEvent(TrackBackEvent event) {
    Iterator it = getEventListenerList().getTrackBackListeners().iterator();
    while (it.hasNext()) {
      TrackBackListener listener = (TrackBackListener)it.next();
      if (event.getType() == TrackBackEvent.TRACKBACK_ADDED) {
        listener.trackBackAdded(event);
      } else if (event.getType() == TrackBackEvent.TRACKBACK_REMOVED) {
        listener.trackBackRemoved(event);
      } else if (event.getType() == TrackBackEvent.TRACKBACK_APPROVED) {
        listener.trackBackApproved(event);
      } else if (event.getType() == TrackBackEvent.TRACKBACK_REJECTED) {
        listener.trackBackRejected(event);
      }

      // has the event been vetoed?
      if (event.isVetoed()) {
        break;
      }
    }
  }

}