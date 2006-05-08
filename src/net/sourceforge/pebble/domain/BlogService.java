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

import net.sourceforge.pebble.dao.BlogEntryDAO;
import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.search.BlogIndexer;
import net.sourceforge.pebble.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.index.BlogEntryIndex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
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
    log.info("Loading blog entry with ID " + blogEntryId);
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
    BlogEntryIndex index = new BlogEntryIndex(blog);
    List<String> blogEntryIds = index.getBlogEntries(year, month, day);
    return getBlogEntries(blog, blogEntryIds);
  }

  public List<BlogEntry> getBlogEntries(Blog blog, int year, int month) {
    BlogEntryIndex index = new BlogEntryIndex(blog);
    List<String> blogEntryIds = index.getBlogEntries(year, month);
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
   * Gets the blog entry with the specified id.
   */
  public void putBlogEntry(BlogEntry blogEntry) throws BlogException {
    synchronized (blogEntry.getBlog()) {
      try {
        log.info("Saving blog entry with ID " + blogEntry.getId());

        BlogEntry be = getBlogEntry(blogEntry.getBlog(), blogEntry.getId());
        if (blogEntry.getType() == BlogEntry.NEW && be != null) {
          // the blog entry is new but one exists with the same ID already
          // - increment the ID and try again
          blogEntry.setDate(new Date(blogEntry.getDate().getTime() + 1));
          putBlogEntry(blogEntry);
        } else {
          DAOFactory factory = DAOFactory.getConfiguredFactory();
          BlogEntryDAO dao = factory.getBlogEntryDAO();
          dao.storeBlogEntry(blogEntry);
          blogEntry.setType(BlogEntry.PUBLISHED);
        }


  //      if (areEventsEnabled() && isDirty()) {
  //        BlogEntryEvent event = new BlogEntryEvent(this, getPropertyChangeEvents());
  //        clearPropertyChangeEvents();
  //        getBlog().getEventDispatcher().fireBlogEntryEvent(event);
  //      }

        // now that the entries have been saved, enable events
        // so that listeners get notified when they change
        blogEntry.setEventsEnabled(true);

        // and notify listeners
        blogEntry.getBlog().getEventDispatcher().fireBlogEntryEvent(new BlogEntryEvent(blogEntry, BlogEntryEvent.BLOG_ENTRY_ADDED));
      } catch (PersistenceException pe) {
      }
    }
  }

  /**
   * Removes this blog entry.
   */
  public void removeBlogEntry(BlogEntry blogEntry) throws BlogException {
    try {
      log.info("Removing blog entry with ID " + blogEntry.getId());
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      BlogEntryDAO dao = factory.getBlogEntryDAO();
      dao.removeBlogEntry(blogEntry);

      if (blogEntry.getType() == BlogEntry.PUBLISHED) {
        // and finally un-index the published entry
        BlogIndexer indexer = new BlogIndexer();
        indexer.removeIndex(blogEntry);
      }

      blogEntry.getBlog().getEventDispatcher().fireBlogEntryEvent(new BlogEntryEvent(blogEntry, BlogEntryEvent.BLOG_ENTRY_REMOVED));

//      todo
//      if (isStaticPage()) {
//        BlogIndexer indexer = new BlogIndexer();
//        indexer.removeIndex(blogEntry);
//        getBlog().getStaticPageIndex().reindex();
//      }
    } catch (PersistenceException pe) {
    }
  }

}
