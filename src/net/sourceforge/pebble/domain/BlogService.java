/*
 * Copyright (c) 2003-2006, Simon Brown
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

import net.sourceforge.pebble.comparator.BlogEntryByTitleComparator;
import net.sourceforge.pebble.dao.BlogEntryDAO;
import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.event.PebbleEvent;
import net.sourceforge.pebble.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.event.comment.CommentEvent;
import net.sourceforge.pebble.event.trackback.TrackBackEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class BlogService {

  private static final Log log = LogFactory.getLog(BlogService.class);

  /**
   * Gets the blog entry with the specified id.
   *
   * @param blogEntryId   the id of the blog entry
   * @return  a BlogEntry instance, or null if the entry couldn't be found
   */
  public BlogEntry getBlogEntry(Blog blog, String blogEntryId) {
    BlogEntryDAO dao = DAOFactory.getConfiguredFactory().getBlogEntryDAO();
    BlogEntry blogEntry = null;
    try {
      blogEntry = dao.loadBlogEntry(blog, blogEntryId);

      if (blogEntry != null) {
        blogEntry.setType(BlogEntry.PUBLISHED);
        blogEntry.setEventsEnabled(true);
      }
    } catch (PersistenceException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
    }
    return blogEntry;
  }

  public List<BlogEntry> getBlogEntries(Blog blog, int year, int month, int day) {
    DailyBlog dailyBlog = blog.getBlogForDay(year, month, day);
    List<String> blogEntryIds = dailyBlog.getBlogEntries();
    return getBlogEntries(blog, blogEntryIds);
  }

  public List<BlogEntry> getBlogEntries(Blog blog, int year, int month) {
    MonthlyBlog monthlyBlog = blog.getBlogForMonth(year, month);
    List<String> blogEntryIds = monthlyBlog.getBlogEntries();
    return getBlogEntries(blog, blogEntryIds);
  }

  /**
   * Gets all blog entries for the specified blog.
   *
   * @return  a List of BlogEntry objects
   */
  public List<BlogEntry> getBlogEntries(Blog blog) {
    BlogEntryDAO dao = DAOFactory.getConfiguredFactory().getBlogEntryDAO();
    try {
      return dao.loadBlogEntries(blog);
    } catch (PersistenceException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      return new ArrayList<BlogEntry>();
    }
  }

  private List<BlogEntry> getBlogEntries(Blog blog, List<String> blogEntryIds) {
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
  public void putBlogEntry(BlogEntry blogEntry) throws BlogException {
    Blog blog = blogEntry.getBlog();
    blogEntry.setEventsEnabled(false);

    synchronized (blog) {
      try {
        BlogEntry be = getBlogEntry(blog, blogEntry.getId());
        if (blogEntry.getType() == BlogEntry.NEW && be != null) {
          // the blog entry is new but one exists with the same ID already
          // - increment the ID and try again
          blogEntry.setDate(new Date(blogEntry.getDate().getTime() + 1));
          putBlogEntry(blogEntry);
        } else {
          DAOFactory factory = DAOFactory.getConfiguredFactory();
          BlogEntryDAO dao = factory.getBlogEntryDAO();
          dao.storeBlogEntry(blogEntry);
        }

        if (blogEntry.getType() == BlogEntry.NEW) {
          blog.getEventDispatcher().fireBlogEntryEvent(new BlogEntryEvent(blogEntry, BlogEntryEvent.BLOG_ENTRY_ADDED));
          blogEntry.setType(BlogEntry.PUBLISHED);
        } else {
          if (blogEntry.isDirty()) {
            BlogEntryEvent event = new BlogEntryEvent(blogEntry, blogEntry.getPropertyChangeEvents());
            blog.getEventDispatcher().fireBlogEntryEvent(event);
          }
        }

        // and also fire any other events that have been stored up
        for (PebbleEvent event : blogEntry.getEvents()) {
          if (event instanceof BlogEntryEvent) {
            blog.getEventDispatcher().fireBlogEntryEvent((BlogEntryEvent)event);
          } else if (event instanceof CommentEvent) {
            blog.getEventDispatcher().fireCommentEvent((CommentEvent)event);
          } else if (event instanceof TrackBackEvent) {
            blog.getEventDispatcher().fireTrackBackEvent((TrackBackEvent)event);
          }
        }
      } catch (PersistenceException pe) {
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
  public void removeBlogEntry(BlogEntry blogEntry) throws BlogException {
    try {
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      BlogEntryDAO dao = factory.getBlogEntryDAO();
      dao.removeBlogEntry(blogEntry);

      if (blogEntry.getType() == BlogEntry.PUBLISHED) {
        // and finally un-index the published entry
        blogEntry.getBlog().getSearchIndex().unindex(blogEntry);
      }

      blogEntry.getBlog().getEventDispatcher().fireBlogEntryEvent(new BlogEntryEvent(blogEntry, BlogEntryEvent.BLOG_ENTRY_REMOVED));

//      todo
//      if (isStaticPage()) {
//        SearchIndex indexer = new SearchIndex();
//        indexer.unindex(blogEntry);
//        getBlog().getStaticPageIndex().reindex();
//      }
    } catch (PersistenceException pe) {
    }
  }

  /**
   * Gets the blog entry with the specified id.
   *
   * @param responseId    the id of the response
   * @return  a response instance, or null if the entry couldn't be found
   */
  public Response getResponse(Blog blog, String responseId) {
    String blogEntryId = responseId.substring(responseId.indexOf("/")+1, responseId.lastIndexOf("/"));
    BlogEntry blogEntry = getBlogEntry(blog, blogEntryId);
    if (blogEntry != null) {
      return blogEntry.getResponse(responseId);
    } else {
      return null;
    }
  }

  /**
   * Gets the list of static pages for the given blog.
   *
   * @param blog    the Blog
   * @return  a list of BlogEntry instances
   */
  public List<BlogEntry> getStaticPages(Blog blog) {
    List<BlogEntry> blogEntries = new ArrayList<BlogEntry>();

    try {
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      BlogEntryDAO dao = factory.getBlogEntryDAO();
      blogEntries.addAll(dao.loadStaticPages(blog));
    } catch (PersistenceException e) {
      e.printStackTrace();
    }

    Collections.sort(blogEntries, new BlogEntryByTitleComparator());

    return blogEntries;
  }

  /**
   * Gets the static page with the specified id.
   *
   * @param pageId   the id of the blog entry
   * @param blog    the Blog
   * @return  a BlogEntry instance, or null if the entry couldn't be found
   */
  public BlogEntry getStaticPage(Blog blog, String pageId) {
    try {
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      BlogEntryDAO dao = factory.getBlogEntryDAO();
      BlogEntry page = dao.loadStaticPage(blog, pageId);
      if (page != null) {
        page.setType(BlogEntry.STATIC_PAGE);
      }
      return page;
    } catch (PersistenceException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * Puts the static page.
   */
  public void putStaticPage(BlogEntry blogEntry) throws BlogException {
    Blog blog = blogEntry.getBlog();

    synchronized (blog) {
      try {
        BlogEntry be = getStaticPage(blog, blogEntry.getId());
        if (blogEntry.getType() == BlogEntry.NEW && be != null) {
          // the blog entry is new but one exists with the same ID already
          // - increment the ID and try again
          blogEntry.setDate(new Date(blogEntry.getDate().getTime() + 1));
          putStaticPage(blogEntry);
        } else {
          DAOFactory factory = DAOFactory.getConfiguredFactory();
          BlogEntryDAO dao = factory.getBlogEntryDAO();
          dao.storeBlogEntry(blogEntry);

          blogEntry.getBlog().getSearchIndex().index(blogEntry);
        }
      } catch (PersistenceException pe) {
      }
    }
  }

  /**
   * Removes a static page.
   */
  public void removeStaticPage(BlogEntry blogEntry) throws BlogException {
    try {
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      BlogEntryDAO dao = factory.getBlogEntryDAO();
      dao.removeBlogEntry(blogEntry);

      // un-index the published entry
//      todo
//      blogEntry.getBlog().getStaticPageIndex().unindex(blogEntry);
      blogEntry.getBlog().getSearchIndex().unindex(blogEntry);

    } catch (PersistenceException pe) {
    }
  }

}
