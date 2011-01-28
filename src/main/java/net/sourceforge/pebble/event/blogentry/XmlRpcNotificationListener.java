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
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.webservice.UpdateNotificationPingsClient;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;

/**
 * Sends XML-RPC pings to websites when a blog entry is added.
 *
 * @author Simon Brown
 */
public class XmlRpcNotificationListener extends BlogEntryListenerSupport {

  /** the name of the URL list property */
  public static final String URL_LIST_KEY = "XmlRpcNotificationListener.urlList";

  /**
   * Called when a blog entry has been added.
   *
   * @param event   a BlogEntryEvent instance
   */
  public void blogEntryAdded(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();

    if (blogEntry.isPublished()) {
      sendNotification(blogEntry);
    }
  }

  /**
   * Called when a blog entry has been published.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryPublished(BlogEntryEvent event) {
    sendNotification(event.getBlogEntry());
  }

  /**
   * Sends the notification.
   *
   * @param blogEntry   the BlogEntry instance
   */
  private void sendNotification(BlogEntry blogEntry) {
    Blog blog = blogEntry.getBlog();
    String urlList = blog.getPluginProperties().getProperty(URL_LIST_KEY);
    String urls[] = new String[0];
    if (urlList != null) {
      urls = urlList.split(",");
    }
    UpdateNotificationPingsClient client = new UpdateNotificationPingsClient();
    client.sendUpdateNotificationPing(blog, urls);
  }
}
