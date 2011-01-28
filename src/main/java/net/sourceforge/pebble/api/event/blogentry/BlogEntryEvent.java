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
package net.sourceforge.pebble.api.event.blogentry;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.api.event.PebbleEvent;

import java.beans.PropertyChangeEvent;
import java.util.List;

/**
 * Event to signify that a blog entry has been added, removed or changed.
 *
 * @author Simon Brown
 */
public class BlogEntryEvent extends PebbleEvent {

  /** constant representing blog entry added type */
  public static final int BLOG_ENTRY_ADDED = 0;

  /** constant representing blog entry removed type */
  public static final int BLOG_ENTRY_REMOVED = 1;

  /** constant representing blog entry changed type */
  public static final int BLOG_ENTRY_CHANGED = 2;

  /** constant representing blog entry published type */
  public static final int BLOG_ENTRY_PUBLISHED = 3;

  /** constant representing blog entry unpublished type */
  public static final int BLOG_ENTRY_UNPUBLISHED = 4;

  /** the list of property change events that caused this event */
  private List<PropertyChangeEvent> propertyChangeEvents;

  /**
   * Creates a new instance with the specified source and type.
   *
   * @param blogEntry   the blog entry that created this event
   * @param type        the type of this event
   */
  public BlogEntryEvent(BlogEntry blogEntry, int type) {
    super(blogEntry, type);
  }

  /**
   * Creates a new instance with the specified source and type.
   *
   * @param blogEntry   the blog entry that created this event
   * @param propertyChangeEvents    the type of this event
   */
  public BlogEntryEvent(BlogEntry blogEntry, List<PropertyChangeEvent> propertyChangeEvents) {
    super(blogEntry, BLOG_ENTRY_CHANGED);
    this.propertyChangeEvents = propertyChangeEvents;
  }

  /**
   * Gets the blog entry that is the source of this event.
   *
   * @return  a BlogEntry instance
   */
  public BlogEntry getBlogEntry() {
    return (BlogEntry)getSource();
  }

  /**
   * Gets the list of property change events that caused this event.
   *
   * @return  a List of PropertyChangeEvent objects, or null if the type of
   *          this event is not BLOG_ENTRY_CHANGED
   */
  public List<PropertyChangeEvent> getPropertyChangeEvents() {
    return this.propertyChangeEvents;
  }

}
