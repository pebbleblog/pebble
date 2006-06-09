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

import net.sourceforge.pebble.comparator.PageBasedContentByTitleComparator;
import net.sourceforge.pebble.dao.BlogEntryDAO;
import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.dao.StaticPageDAO;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
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
        blogEntry.setEventsEnabled(true);
        blogEntry.setPersistent(true);
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
    List<BlogEntry> blogEntries;
    try {
      blogEntries = dao.loadBlogEntries(blog);
      for (BlogEntry blogEntry : blogEntries) {
        blogEntry.setPersistent(true);
      }

    } catch (PersistenceException e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      blogEntries = new ArrayList<BlogEntry>();
    }

    return blogEntries;
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
    DAOFactory factory = DAOFactory.getConfiguredFactory();
    BlogEntryDAO dao = factory.getBlogEntryDAO();
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
        }

        blogEntry.setPersistent(true);
      } catch (PersistenceException pe) {
        pe.printStackTrace();
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
      blogEntry.setPersistent(false);

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
      pe.printStackTrace();
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
  public List<StaticPage> getStaticPages(Blog blog) {
    List<StaticPage> staticPages = new ArrayList<StaticPage>();
    try {
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      StaticPageDAO dao = factory.getStaticPageDAO();
      staticPages.addAll(dao.loadStaticPages(blog));
    } catch (PersistenceException e) {
      e.printStackTrace();
    }

    Collections.sort(staticPages, new PageBasedContentByTitleComparator());

    return staticPages;
  }

  /**
   * Gets the page with the specified id.
   *
   * @param pageId   the id of the blog entry
   * @param blog    the Blog
   * @return  a Page instance, or null if the page couldn't be found
   */
  public StaticPage getStaticPageById(Blog blog, String pageId) {
    try {
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      StaticPageDAO dao = factory.getStaticPageDAO();

      StaticPage staticPage = dao.loadStaticPage(blog, pageId);
      if (staticPage != null) {
        staticPage.setPersistent(true);
      }

      return staticPage;
    } catch (PersistenceException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Gets the static page with the specified name.
   *
   * @param name    the name of the static page
   * @param blog    the Blog
   * @return  a StaticPage instance, or null if the page couldn't be found
   */
  public StaticPage getStaticPageByName(Blog blog, String name) {
    String id = blog.getStaticPageIndex().getStaticPage(name);
    return getStaticPageById(blog, id);
  }

  /**
   * Puts the static page.
   */
  public void putStaticPage(StaticPage staticPage) throws BlogException {
    DAOFactory factory = DAOFactory.getConfiguredFactory();
    StaticPageDAO dao = factory.getStaticPageDAO();
    Blog blog = staticPage.getBlog();

    synchronized (blog) {
      try {
        StaticPage sp = getStaticPageById(blog, staticPage.getId());

        if (!staticPage.isPersistent() && sp != null) {
          // the blog entry is new but one exists with the same ID already
          // - increment the date/ID and try again
          staticPage.setDate(new Date(staticPage.getDate().getTime() + 1));
          putStaticPage(staticPage);
        } else {
          dao.storeStaticPage(staticPage);
        }

        staticPage.setPersistent(true);

        staticPage.getBlog().getSearchIndex().index(staticPage);
        staticPage.getBlog().getStaticPageIndex().index(staticPage);
      } catch (PersistenceException pe) {
        pe.printStackTrace();
      }
    }
  }

  /**
   * Removes a static page.
   */
  public void removeStaticPage(StaticPage staticPage) throws BlogException {
    try {
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      StaticPageDAO dao = factory.getStaticPageDAO();
      dao.removeStaticPage(staticPage);

      staticPage.getBlog().getSearchIndex().unindex(staticPage);
      staticPage.getBlog().getStaticPageIndex().unindex(staticPage);

    } catch (PersistenceException pe) {
    }
  }

}
