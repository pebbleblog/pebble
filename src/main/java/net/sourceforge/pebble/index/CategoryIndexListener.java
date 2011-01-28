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

package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryListener;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;

public class CategoryIndexListener implements BlogEntryListener {

  /**
   * Called when a blog entry has been added.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryAdded(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    if (blogEntry.isPublished()) {
      blogEntry.getBlog().getCategoryIndex().index(blogEntry);
    }
  }

  /**
   * Called when a blog entry has been removed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryRemoved(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    if (blogEntry.isPublished()) {
      blogEntry.getBlog().getCategoryIndex().unindex(blogEntry);
    }
  }

  /**
   * Called when a blog entry has been changed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryChanged(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();

    if (blogEntry.isPublished()) {
      List propertyChangeEvents = event.getPropertyChangeEvents();
      Iterator it = propertyChangeEvents.iterator();
      while (it.hasNext()) {
        PropertyChangeEvent pce = (PropertyChangeEvent)it.next();
        String property = pce.getPropertyName();

        // only if the tags or categories change do we need to reindex the tags
        if (property.equals(BlogEntry.CATEGORIES_PROPERTY)) {
          blogEntry.getBlog().getCategoryIndex().unindex(blogEntry);
          blogEntry.getBlog().getCategoryIndex().index(blogEntry);
        }
      }
    }
  }

  /**
   * Called when a blog entry has been published.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryPublished(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    blogEntry.getBlog().getCategoryIndex().index(blogEntry);
  }

  /**
   * Called when a blog entry has been unpublished.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryUnpublished(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    blogEntry.getBlog().getCategoryIndex().unindex(blogEntry);
  }

}
