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
package net.sourceforge.pebble.api.event;

import net.sourceforge.pebble.api.event.blog.BlogEvent;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.event.EventListenerList;
import net.sourceforge.pebble.domain.BlogEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Responsible for dispatching events to registered listeners.
 *
 * @author Simon Brown
 */
public abstract class EventDispatcher {

  private static final Log log = LogFactory.getLog(EventDispatcher.class);

  /** the event listener list */
  private EventListenerList eventListenerList;

  /**
   * Gets the event listener list.
   *
   * @return  an EventListenerList object
   */
  public EventListenerList getEventListenerList() {
    return this.eventListenerList;
  }

  /**
   * Sets the event listener list.
   *
   * @param eventListenerList   an EventListenerList object
   */
  public void setEventListenerList(EventListenerList eventListenerList) {
    this.eventListenerList = eventListenerList;
  }

  /**
   * Fires all outstanding events on a given blog entry.
   *
   * @param blogEntry   the blog entry to fire events on
   */
  public void fireEvents(BlogEntry blogEntry) {
    while (blogEntry.hasEvents()) {
      PebbleEvent event = blogEntry.nextEvent();
      if (event instanceof BlogEntryEvent) {
        fireBlogEntryEvent((BlogEntryEvent)event);
      } else if (event instanceof CommentEvent) {
        fireCommentEvent((CommentEvent)event);
      } else if (event instanceof TrackBackEvent) {
        fireTrackBackEvent((TrackBackEvent)event);
      }
    }
  }

  /**
   * Fires a blog event to registered listeners.
   *
   * @param event   the BlogEvent instance
   */
  public abstract void fireBlogEvent(BlogEvent event);

  /**
   * Fires a blog entry event to registered listeners.
   *
   * @param event   the BlogEntryEvent instance
   */
  public abstract void fireBlogEntryEvent(BlogEntryEvent event);

  /**
   * Fires a comment event to registered listeners.
   *
   * @param event   the CommentEvent instance
   */
  public abstract void fireCommentEvent(CommentEvent event);
  /**
   * Fires a TrackBack event to registered listeners.
   *
   * @param event   the TrackBackEvent instance
   */
  public abstract void fireTrackBackEvent(TrackBackEvent event);

}
