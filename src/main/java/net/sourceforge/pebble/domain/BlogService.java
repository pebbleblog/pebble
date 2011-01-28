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
import net.sourceforge.pebble.dao.BlogEntryDAO;
import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.ContentCache;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Collection;

/**
 * Service that encompasses all functionality related to getting, putting
 * and removing blog entries.
 *
 * @author    Simon Brown
 */
public class BlogService {

  private static final Log log = LogFactory.getLog(BlogService.class);

  /**
   * Gets the blog entry with the specified id.
   *
   * @param blogEntryId   the id of the blog entry
   * @return  a BlogEntry instance, or null if the entry couldn't be found
   */
  public BlogEntry getBlogEntry(Blog blog, String blogEntryId) throws BlogServiceException {
    BlogEntry blogEntry = null;
    ContentCache cache = ContentCache.getInstance();

    // is the blog entry already in the cache?                                                  
    blogEntry = cache.getBlogEntry(blog, blogEntryId);
    if (blogEntry != null) {
      log.debug("Got blog entry " + blogEntryId + " from cache");
    } else {
      log.debug("Loading blog entry " + blogEntryId + " from disk");
      BlogEntryDAO dao = DAOFactory.getConfiguredFactory().getBlogEntryDAO();
      try {
        blogEntry = dao.loadBlogEntry(blog, blogEntryId);

        if (blogEntry != null) {
          // place in the cache for faster lookup next time
          cache.putBlogEntry(blogEntry);
        }
      } catch (PersistenceException pe) {
        throw new BlogServiceException(blog, pe);
      }
    }

    if (blogEntry != null) {
      blogEntry = (BlogEntry)blogEntry.clone();
      blogEntry.setEventsEnabled(true);
      blogEntry.setPersistent(true);
    }

    return blogEntry;
  }

  public List<BlogEntry> getBlogEntries(Blog blog, int year, int month, int day) throws BlogServiceException {
    Day d = blog.getBlogForDay(year, month, day);
    List<String> blogEntryIds = d.getBlogEntries();
    return getBlogEntries(blog, blogEntryIds);
  }

  public List<BlogEntry> getBlogEntries(Blog blog, int year, int month) throws BlogServiceException {
    Month m = blog.getBlogForMonth(year, month);
    List<String> blogEntryIds = m.getBlogEntries();
    return getBlogEntries(blog, blogEntryIds);
  }

  /**
   * Gets all blog entries for the specified blog.
   *
   * @return  a List of BlogEntry objects
   */
  public Collection<BlogEntry> getBlogEntries(Blog blog) throws BlogServiceException {
    BlogEntryDAO dao = DAOFactory.getConfiguredFactory().getBlogEntryDAO();
    Collection<BlogEntry> blogEntries;
    try {
      blogEntries = dao.loadBlogEntries(blog);
      for (BlogEntry blogEntry : blogEntries) {
        blogEntry.setPersistent(true);
      }

    } catch (PersistenceException pe) {
      throw new BlogServiceException(blog, pe);
    }

    return blogEntries;
  }

  private List<BlogEntry> getBlogEntries(Blog blog, List<String> blogEntryIds) throws BlogServiceException {
    List<BlogEntry> blogEntries = new ArrayList<BlogEntry>();

    for (String blogEntryId : blogEntryIds) {
      BlogEntry blogEntry = getBlogEntry(blog, blogEntryId);
      blogEntries.add(blogEntry);
    }

    return blogEntries;
  }

  /**
   * Puts the blog entry with the specified id.
   */
  public void putBlogEntry(BlogEntry blogEntry) throws BlogServiceException {
    DAOFactory factory = DAOFactory.getConfiguredFactory();
    BlogEntryDAO dao = factory.getBlogEntryDAO();
    Blog blog = blogEntry.getBlog();
    ContentCache cache = ContentCache.getInstance();

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
            dao.storeBlogEntry(blogEntry);
            blogEntry.insertEvent(new BlogEntryEvent(blogEntry, BlogEntryEvent.BLOG_ENTRY_ADDED));

            for (Comment comment : blogEntry.getComments()) {
              blogEntry.addEvent(new CommentEvent(comment, CommentEvent.COMMENT_ADDED));
            }
            for (TrackBack trackBack : blogEntry.getTrackBacks()) {
              blogEntry.addEvent(new TrackBackEvent(trackBack, TrackBackEvent.TRACKBACK_ADDED));
            }
          } else {
            dao.storeBlogEntry(blogEntry);
            if (blogEntry.isDirty()) {
              blogEntry.insertEvent(new BlogEntryEvent(blogEntry, blogEntry.getPropertyChangeEvents()));
            }
          }

          blogEntry.getBlog().getEventDispatcher().fireEvents(blogEntry);

          // and store the blog entry now that listeners have been fired
          dao.storeBlogEntry(blogEntry);
          cache.removeBlogEntry(blogEntry);
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

  /**
   * Removes this blog entry.
   */
  public void removeBlogEntry(BlogEntry blogEntry) throws BlogServiceException {
    Blog blog = blogEntry.getBlog();
    ContentCache cache = ContentCache.getInstance();

    try {
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      BlogEntryDAO dao = factory.getBlogEntryDAO();
      dao.removeBlogEntry(blogEntry);
      blogEntry.setPersistent(false);

      // remove from cache
      cache.removeBlogEntry(blogEntry);

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

  /**
   * Gets the blog entry with the specified id.
   *
   * @param responseId    the id of the response
   * @return  a response instance, or null if the entry couldn't be found
   */
  public Response getResponse(Blog blog, String responseId) throws BlogServiceException {
    String blogEntryId = responseId.substring(responseId.indexOf("/")+1, responseId.lastIndexOf("/"));
    BlogEntry blogEntry = getBlogEntry(blog, blogEntryId);
    if (blogEntry != null) {
      return blogEntry.getResponse(responseId);
    } else {
      return null;
    }
  }

}