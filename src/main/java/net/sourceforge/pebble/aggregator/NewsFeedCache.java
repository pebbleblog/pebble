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

package net.sourceforge.pebble.aggregator;

import com.rometools.rome.feed.WireFeed;
import com.rometools.rome.feed.atom.Content;
import com.rometools.rome.feed.atom.Entry;
import com.rometools.rome.feed.atom.Link;
import com.rometools.rome.feed.rss.Channel;
import com.rometools.rome.feed.rss.Item;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedInput;
import com.rometools.rome.io.XmlReader;
import net.sourceforge.pebble.domain.Blog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * A cache of newsfeed subscriptions and their entries.
 *
 * @author    Simon Brown
 */
public class NewsFeedCache {

  private static final int FEED_ENTRY_LIMIT = 20;

  private static final Log log = LogFactory.getLog(NewsFeedCache.class);
  private static final NewsFeedCache instance = new NewsFeedCache();

  private final Map<String,Set<String>> subscriptions = new HashMap<String,Set<String>>();
  private final Map<String, NewsFeed> feeds = new HashMap<String, NewsFeed>();
  private final Map<String,List<NewsFeedEntry>> entries = new HashMap<String,List<NewsFeedEntry>>();
  
  private NewsFeedCache() {
  }

  public static NewsFeedCache getInstance() {
    return instance;
  }

  public void addSubscription(Blog blog, String url) {
    synchronized (feeds) {
      Set<String> urls = getUrls(blog.getId());
      urls.add(url);

      NewsFeed feed = feeds.get(url);
      if (feed == null) {
        feed = updateFeed(url);
        feeds.put(url, feed);
      }
    }
  }

  public void removeAllSubscriptions(Blog blog) {
    synchronized (feeds) {
      Set<String> urls = getUrls(blog.getId());
      urls.clear();
    }
  }

  public void refreshFeeds() {
    for (String url : feeds.keySet()) {
      try {
        NewsFeed updatedFeed = updateFeed(url);
        synchronized (feeds) {
          feeds.put(url, updatedFeed);
        }
      } catch (Exception e) {
        log.warn("Couldn't update feed from " + url, e);
      }
    }

    for (String blogId : subscriptions.keySet()) {
      List<NewsFeedEntry> entriesForBlog = new LinkedList<NewsFeedEntry>();
      for (String url : getUrls(blogId)) {
        entriesForBlog.addAll(feeds.get(url).getEntries());
      }

      Collections.sort(entriesForBlog, new NewsFeedEntryComparator());

      if (entriesForBlog.size() > FEED_ENTRY_LIMIT) {
        entriesForBlog = entriesForBlog.subList(0, FEED_ENTRY_LIMIT);
      }

      entries.put(blogId, entriesForBlog);
    }
  }

  private NewsFeed updateFeed(String url) {
    NewsFeed feed = new NewsFeed(url);

    try {
      log.debug("Refreshing feed from " + url);

//      SyndFeedInput input = new SyndFeedInput(true);
//      SyndFeed sf = input.build(new XmlReader(new URL(url)));
//
//      feed.setTitle(sf.getTitle());
//      feed.setLink(sf.getLink());
//
//        for (SyndEntry se : (List<SyndEntry>)sf.getEntries()) {
//          log.info(se);
//          NewsFeedEntry fe = new NewsFeedEntry(
//              se.getLink(),
//              se.getTitle(),
//              se.getDescription() != null ? se.getDescription().getValue() : "",
//              se.getAuthor(),
//              se.getPublishedDate()
//          );
//          feed.add(fe);
//          log.info(fe);
//        }

      WireFeedInput input = new WireFeedInput(true, Locale.US);
      WireFeed wf = input.build(new XmlReader(new URL(url)));

      if (wf.getFeedType() != null && wf.getFeedType().startsWith("rss")) {
        Channel rssFeed = (Channel)wf;

        feed.setTitle(rssFeed.getTitle());
        feed.setLink(rssFeed.getLink());

        for (Item item : (List<Item>)rssFeed.getItems()) {
          NewsFeedEntry fe = new NewsFeedEntry(
              item.getLink(),
              item.getTitle(),
              item.getDescription() != null ? item.getDescription().getValue() : "",
              item.getAuthor(),
              item.getPubDate()
          );
          feed.add(fe);
        }
      } else if (wf.getFeedType() != null && wf.getFeedType().startsWith("atom")) {
        com.rometools.rome.feed.atom.Feed atomFeed = (com.rometools.rome.feed.atom.Feed)wf;
        feed.setTitle(atomFeed.getTitle());
        for (Link link : (List<Link>)atomFeed.getAlternateLinks()) {
          if ("text/html".equals(link.getType()))
            feed.setLink(link.getHref());
        }

        for (Entry entry : (List<Entry>)atomFeed.getEntries()) {
          String href = "";
          for (Link link : (List<Link>)entry.getAlternateLinks()) {
            if ("text/html".equals(link.getType()))
              href = link.getHref();
          }
          String body = null;
          for (Content content : (List<Content>)entry.getContents()) {
            if ("html".equals(content.getType()))
              body = content.getValue();
          }
          if (body == null) {
            for (Content content : (List<Content>)entry.getSummary()) {
              if ("html".equals(content.getType()))
                body = content.getValue();
            }
          }
          String author = entry.getAuthors() != null && entry.getAuthors().size() > 0 ? entry.getAuthors().get(0).toString() : "";
          NewsFeedEntry fe = new NewsFeedEntry(
              href,
              entry.getTitle(),
              body,
              author,
              entry.getPublished()
          );
          feed.add(fe);
        }
      }

      log.debug("Refreshed feed from " + url);
    } catch (FeedException e) {
      log.warn("Error while updating feed from " + url, e);
    } catch (IOException e) {
      log.warn("Error while updating feed from " + url, e);
    }

    return feed;
  }

  public NewsFeed getFeed(String url) {
    return feeds.get(url);
  }

  public List<NewsFeedEntry> getNewsFeedEntries(Blog blog) {
    List<NewsFeedEntry> list = entries.get(blog.getId());
    if (list == null) {
      list = new LinkedList<NewsFeedEntry>();
    }

    return list;
  }

  private Set<String> getUrls(String blogId) {
    Set<String> urls = subscriptions.get(blogId);
    if (urls == null) {
      urls = new HashSet<String>();
      subscriptions.put(blogId, urls);
    }

    return urls;
  }

}
