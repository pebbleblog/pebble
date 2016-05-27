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

package net.sourceforge.pebble.decorator;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndLink;
import com.rometools.rome.feed.synd.SyndLinkImpl;
import net.sourceforge.pebble.api.decorator.FeedDecorator;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Response;
import net.sourceforge.pebble.event.blogentry.PubSubHubBubBlogEntryListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Adds hub links to the feed, implementing the discovery part of pubsubhubbub
 *
 * @author James Roper
 */
public class PubSubHubBubFeedDecorator implements FeedDecorator {
  private static final Log log = LogFactory.getLog(PubSubHubBubFeedDecorator.class);

  public static final String HUBS_PROPERTY = "pubsubhubbub.hubs";
  private volatile Boolean listenerEnabled;

  public void decorate(SyndFeed feed, Blog blog) {
    if (checkListenerEnabled(blog)) {
      List<SyndLink> links = new ArrayList<SyndLink>(feed.getLinks());
      for (String hub : getHubs(blog)) {
        SyndLink link = new SyndLinkImpl();
        link.setRel("hub");
        link.setHref(hub.trim());
        links.add(link);
      }
      feed.setLinks(links);
    } else {
      log.warn("PubSubHubBub Feed Decorator plugin is enabled, however the PubSubHubBub " +
          "blog entry listener does not appeared to be enabled. Not adding hub to feed, " +
          "otherwise aggregators will never get updates.");
    }
  }

  public void decorate(SyndFeed feed, Blog blog, BlogEntry blogEntry) {
    // We don't use hubs for response feeds
  }

  public void decorate(SyndEntry entry, Blog blog, BlogEntry blogEntry) {
  }

  public void decorate(SyndEntry entry, Blog blog, Response response) {
    // We don't use hubs for response feeds
  }

  private boolean checkListenerEnabled(Blog blog) {
    return blog.getBlogEntryListeners().contains(PubSubHubBubBlogEntryListener.class.getName());
  }

  private Collection<String> getHubs(Blog blog) {
    String hubs = blog.getPluginProperties().getProperty(HUBS_PROPERTY);
    if (hubs == null || hubs.length() == 0) {
      return Collections.emptyList();
    }
    return Arrays.asList(hubs.split(","));
  }
}
