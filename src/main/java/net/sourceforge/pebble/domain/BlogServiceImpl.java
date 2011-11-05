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

import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.comparator.BlogEntryComparator;
import net.sourceforge.pebble.dao.BlogEntryDAO;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.ContentCache;
import net.sourceforge.pebble.index.BlogEntryIndex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

/**
 * Service that encompasses all functionality related to getting, putting
 * and removing blog entries.
 *
 * @author Simon Brown
 */
public class BlogServiceImpl implements BlogService {

  private static final Log log = LogFactory.getLog(BlogServiceImpl.class);

  private final BlogEntryDAO blogEntryDAO;
  private final ContentCache contentCache;
  private final BlogEntryIndex blogEntryIndex;

  public BlogServiceImpl(BlogEntryDAO blogEntryDAO, ContentCache contentCache, BlogEntryIndex blogEntryIndex) {
    this.blogEntryDAO = blogEntryDAO;
    this.contentCache = contentCache;
    this.blogEntryIndex = blogEntryIndex;
  }

  public BlogEntry getBlogEntry(Blog blog, String blogEntryId) throws BlogServiceException {
    BlogEntry blogEntry;

    // is the blog entry already in the cache?                                                  
    blogEntry = contentCache.getBlogEntry(blog, blogEntryId);
    if (blogEntry != null) {
      log.debug("Got blog entry " + blogEntryId + " from cache");
    } else {
      log.debug("Loading blog entry " + blogEntryId + " from disk");
      try {
        blogEntry = blogEntryDAO.loadBlogEntry(blog, blogEntryId);

        if (blogEntry != null) {
          // place in the cache for faster lookup next time
          contentCache.putBlogEntry(blogEntry);
        }
      } catch (PersistenceException pe) {
        throw new BlogServiceException(blog, pe);
      }
    }
    return cloneBlogEntry(blogEntry);
  }

  public List<BlogEntry> getBlogEntries(Blog blog, int year, int month, int day) throws BlogServiceException {
    List<String> blogEntryIds = blogEntryIndex.getBlogEntriesForDay(blog, year, month, day);
    return getBlogEntries(blog, blogEntryIds);
  }

  public List<BlogEntry> getBlogEntries(Blog blog, int year, int month) throws BlogServiceException {
    List<String> blogEntryIds = blogEntryIndex.getBlogEntriesForMonth(blog, year, month);
    return getBlogEntries(blog, blogEntryIds);
  }

  public List<BlogEntry> getBlogEntries(Blog blog) throws BlogServiceException {
    List<BlogEntry> blogEntries = new ArrayList<BlogEntry>();
    try {
      for (BlogEntry blogEntry : blogEntryDAO.loadBlogEntries(blog)) {
        blogEntries.add(cloneBlogEntry(blogEntry));
      }
    } catch (PersistenceException pe) {
      throw new BlogServiceException(blog, pe);
    }

    Collections.sort(blogEntries, new BlogEntryComparator());
    return blogEntries;
  }

  public List<BlogEntry> getUnpublishedBlogEntries(Blog blog) throws BlogServiceException {
    return getBlogEntries(blog, blogEntryIndex.getUnpublishedBlogEntries(blog));
  }

  private List<BlogEntry> getBlogEntries(Blog blog, List<String> blogEntryIds) throws BlogServiceException {
    List<BlogEntry> blogEntries = new ArrayList<BlogEntry>();

    for (String blogEntryId : blogEntryIds) {
      BlogEntry blogEntry = getBlogEntry(blog, blogEntryId);
      blogEntries.add(blogEntry);
    }

    return blogEntries;
  }

  private List<BlogEntry> tryGetBlogEntries(Blog blog, List<String> blogEntryIds) {
    List<BlogEntry> blogEntries = new ArrayList<BlogEntry>();

    for (String blogEntryId : blogEntryIds) {
      BlogEntry blogEntry = null;
      try {
        blogEntry = getBlogEntry(blog, blogEntryId);
      } catch (BlogServiceException e) {
        // Ignore
      }
      blogEntries.add(blogEntry);
    }

    return blogEntries;
  }

  public void putBlogEntry(BlogEntry blogEntry) throws BlogServiceException {
    Blog blog = blogEntry.getBlog();

    synchronized (blog) {
      try {
        BlogEntry be = getBlogEntry(blog, blogEntry.getId());

        if (!blogEntry.isPersistent() && be != null) {
          // the blog entry is new but one exists with the same ID already
          // - increment the date/ID and try again
          blogEntry.setDate(new Date(blogEntry.getDate().getTime() + 1));
          putBlogEntry(blogEntry);
        } else {
          if (!blogEntry.isPersistent()) {
            blogEntryDAO.storeBlogEntry(blogEntry);
            blogEntry.insertEvent(new BlogEntryEvent(blogEntry, BlogEntryEvent.BLOG_ENTRY_ADDED));

            for (Comment comment : blogEntry.getComments()) {
              blogEntry.addEvent(new CommentEvent(comment, CommentEvent.COMMENT_ADDED));
            }
            for (TrackBack trackBack : blogEntry.getTrackBacks()) {
              blogEntry.addEvent(new TrackBackEvent(trackBack, TrackBackEvent.TRACKBACK_ADDED));
            }
          } else {
            blogEntryDAO.storeBlogEntry(blogEntry);
            if (blogEntry.isDirty()) {
              blogEntry.insertEvent(new BlogEntryEvent(blogEntry, blogEntry.getPropertyChangeEvents()));
            }
          }

          blogEntry.getBlog().getEventDispatcher().fireEvents(blogEntry);

          // and store the blog entry now that listeners have been fired
          blogEntryDAO.storeBlogEntry(blogEntry);
          contentCache.removeBlogEntry(blogEntry);
        }

        blogEntry.setPersistent(true);
      } catch (PersistenceException pe) {
        throw new BlogServiceException(blog, pe);
      } finally {
        blogEntry.clearPropertyChangeEvents();
        blogEntry.clearEvents();
        blogEntry.setEventsEnabled(true);
      }
    }
  }

  public void removeBlogEntry(BlogEntry blogEntry) throws BlogServiceException {

    try {
      blogEntryDAO.removeBlogEntry(blogEntry);
      blogEntry.setPersistent(false);

      // remove from cache
      contentCache.removeBlogEntry(blogEntry);

      blogEntry.insertEvent(new BlogEntryEvent(blogEntry, BlogEntryEvent.BLOG_ENTRY_REMOVED));

      // and remove all of the responses, so the appropriate events are raised
      // and the responses get unindexed
      for (Comment comment : blogEntry.getComments()) {
        blogEntry.addEvent(new CommentEvent(comment, CommentEvent.COMMENT_REMOVED));
      }
      for (TrackBack trackBack : blogEntry.getTrackBacks()) {
        blogEntry.addEvent(new TrackBackEvent(trackBack, TrackBackEvent.TRACKBACK_REMOVED));
      }

      blogEntry.getBlog().getEventDispatcher().fireEvents(blogEntry);
    } catch (PersistenceException pe) {
      throw new BlogServiceException(blogEntry.getBlog(), pe);
    }
  }

  public Response getResponse(Blog blog, String responseId) throws BlogServiceException {
    String blogEntryId = responseId.substring(responseId.indexOf("/") + 1, responseId.lastIndexOf("/"));
    BlogEntry blogEntry = getBlogEntry(blog, blogEntryId);
    if (blogEntry != null) {
      return blogEntry.getResponse(responseId);
    } else {
      return null;
    }
  }

  private BlogEntry cloneBlogEntry(BlogEntry blogEntry) {
    if (blogEntry != null) {
      blogEntry = (BlogEntry) blogEntry.clone();
      blogEntry.setEventsEnabled(true);
      blogEntry.setPersistent(true);
    }
    return blogEntry;
  }

  public List<BlogEntry> getRecentBlogEntries(Blog blog, int max) {
    List<String> blogEntryIds = blogEntryIndex.getBlogEntries(blog);
    List<BlogEntry> blogEntries = new ArrayList<BlogEntry>();
    for (String blogEntryId : blogEntryIds) {
      try {
        BlogEntry blogEntry = getBlogEntry(blog, blogEntryId);
        blogEntries.add(blogEntry);
      } catch (BlogServiceException e) {
        log.error("Exception encountered", e);
      }

      if (blogEntries.size() == max) {
        break;
      }
    }
    return blogEntries;
  }

  public List<BlogEntry> getRecentBlogEntries(Blog blog) {
    return getRecentBlogEntries(blog, blog.getRecentBlogEntriesOnHomePage());
  }

  public List<BlogEntry> getRecentPublishedBlogEntries(Blog blog, int max) {
    List<BlogEntry> blogEntries = new ArrayList<BlogEntry>();
    List<String> blogEntryIds = blogEntryIndex.getPublishedBlogEntries((Blog) blog);
    for (String blogEntryId : blogEntryIds) {
      if (blogEntries.size() == max) {
        break;
      }
      try {
        BlogEntry blogEntry = getBlogEntry((Blog) blog, blogEntryId);
        if (blogEntry != null) {
          blogEntries.add(blogEntry);
        }
      } catch (BlogServiceException e) {
        log.error("Exception encountered", e);
      }
    }
    return blogEntries;
  }

  public List<BlogEntry> getRecentPublishedBlogEntries(Blog blog) {
    return getRecentPublishedBlogEntries(blog, blog.getRecentBlogEntriesOnHomePage());
  }
}