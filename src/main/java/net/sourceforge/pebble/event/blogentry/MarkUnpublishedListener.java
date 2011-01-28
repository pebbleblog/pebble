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
package net.sourceforge.pebble.event.blogentry;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;

/**
 * Marks new and changed blog entries as unpublished.
 *
 * @author Simon Brown
 */
public class MarkUnpublishedListener extends BlogEntryListenerSupport {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(MarkUnpublishedListener.class);

  /**
   * Called when a blog entry has been added.
   *
   * @param event   a BlogEntryEvent instance
   */
  public void blogEntryAdded(BlogEntryEvent event) {
    if (!SecurityUtils.isBlogOwner()) {
      event.getBlogEntry().setPublished(false);
    }
  }

  /**
   * Called when a blog entry has been changed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryChanged(BlogEntryEvent event) {
    if (!SecurityUtils.isBlogPublisher()) {

      // mark the entry as unpublished if one of the following properties
      // has changed
      //  - title
      //  - subtitle
      //  - excerpt
      //  - body
      //  - original permalink
      List propertyChangeEvents = event.getPropertyChangeEvents();
      Iterator it = propertyChangeEvents.iterator();
      while (it.hasNext()) {
        PropertyChangeEvent pce = (PropertyChangeEvent)it.next();
        String property = pce.getPropertyName();
        if (property.equals(BlogEntry.TITLE_PROPERTY) ||
            property.equals(BlogEntry.SUBTITLE_PROPERTY) ||
            property.equals(BlogEntry.EXCERPT_PROPERTY) ||
            property.equals(BlogEntry.BODY_PROPERTY) ||
            property.equals(BlogEntry.ORIGINAL_PERMALINK_PROPERTY)) {
          event.getBlogEntry().setPublished(false);
          return;
        }
      }
    }
  }

}
