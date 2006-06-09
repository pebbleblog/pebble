/*
 * Copyright (c) 2003-2005, Simon Brown
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
package net.sourceforge.pebble.util;

import net.sourceforge.pebble.dao.CategoryDAO;
import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.file.FileDAOFactory;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.event.response.IpAddressListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

/**
 * Utilities for the current blog, such as those useful for moving
 * between versions of Pebble.
 *
 * @author    Simon Brown
 */
public class Utilities {

  /** the logger used by this action */
  private static final Log log = LogFactory.getLog(Utilities.class);

  /**
   * Builds the indexes for the given blog.
   *
   * @param blog    a Blog instance
   */
  public static void buildIndexes(Blog blog) {
    blog.reindex();
  }

  /**
   * Builds the blacklist and whitelist of IP addresses from all responses
   * for the given blog.
   *
   * @param blog    a Blog instance
   */
  public static void buildIpAddressLists(Blog blog) {
    Iterator blogEntries = blog.getBlogEntries().iterator();
    IpAddressListener ipAddressListener = new IpAddressListener();

    while (blogEntries.hasNext()) {
      BlogEntry blogEntry = (BlogEntry)blogEntries.next();
      log.info("Processing " + blogEntry.getTitle() + " (" + blogEntry.getDate() + ")");
      Iterator comments = blogEntry.getComments().iterator();
      while (comments.hasNext()) {
        Comment comment = (Comment)comments.next();
        if (comment.isApproved()) {
          CommentEvent event = new CommentEvent(comment, CommentEvent.COMMENT_APPROVED);
          ipAddressListener.commentApproved(event);
        } else if (comment.isRejected()) {
          CommentEvent event = new CommentEvent(comment, CommentEvent.COMMENT_REJECTED);
          ipAddressListener.commentRejected(event);
        }
      }

      Iterator trackbacks = blogEntry.getTrackBacks().iterator();
      while (trackbacks.hasNext()) {
        TrackBack trackback = (TrackBack)trackbacks.next();
        if (trackback.isApproved()) {
          TrackBackEvent event = new TrackBackEvent(trackback, TrackBackEvent.TRACKBACK_APPROVED);
          ipAddressListener.trackBackApproved(event);
        } else if (trackback.isRejected()) {
          TrackBackEvent event = new TrackBackEvent(trackback, TrackBackEvent.TRACKBACK_REJECTED);
          ipAddressListener.trackBackRejected(event);
        }
      }
//
//      try {
//        blogEntry.store();
//      } catch (BlogException e) {
//        log.error("Error storing " + blogEntry.getTitle() + " (" + blogEntry.getDate() + ")");
//      }
    }
  }

  /**
   * Fixes HTML escaping of comment and TrackBack content for the given blog.
   *
   * @param blog    a Blog instance
   */
  public static void fixHtmlInResponses(Blog blog) {
    Iterator blogEntries = blog.getBlogEntries().iterator();
    while (blogEntries.hasNext()) {
      BlogEntry blogEntry = (BlogEntry)blogEntries.next();
      log.info("Processing " + blogEntry.getTitle() + " (" + blogEntry.getDate() + ")");
      Iterator comments = blogEntry.getComments().iterator();
      while (comments.hasNext()) {
        Comment comment = (Comment)comments.next();
        if (comment.getBody() != null) {
          comment.setBody(comment.getBody().replaceAll("&amp;", "&"));
          comment.setBody(comment.getBody().replaceAll("&lt;", "<"));
          comment.setBody(comment.getBody().replaceAll("&gt;", ">"));
        }
      }
      Iterator trackbacks = blogEntry.getTrackBacks().iterator();
      while (trackbacks.hasNext()) {
        TrackBack trackback = (TrackBack)trackbacks.next();
        if (trackback.getExcerpt() != null) {
          trackback.setExcerpt(trackback.getExcerpt().replaceAll("&amp;", "&"));
          trackback.setExcerpt(trackback.getExcerpt().replaceAll("&lt;", "<"));
          trackback.setExcerpt(trackback.getExcerpt().replaceAll("&gt;", ">"));
        }
      }
      try {
        BlogService service = new BlogService();
        service.putBlogEntry(blogEntry);
      } catch (BlogException e) {
        log.error("Error storing " + blogEntry.getTitle() + " (" + blogEntry.getDate() + ")");
      }
    }
  }

  /**
   * Converts flat categories to hierarchical categories.
   *
   * @param blog    a Blog instance
   */
  public static void convertCategories(Blog blog) {
    Properties categories = new Properties();
    try {
      FileInputStream in = new FileInputStream(new File(blog.getRoot(), "blog.categories"));
      categories.load(in);
      in.close();

      Iterator it = categories.keySet().iterator();
      while (it.hasNext()) {
        String id = (String)it.next();
        String name = categories.getProperty(id);
        Category category;

        if (!id.startsWith("/")) {
          category = new Category("/" + id, name);
        } else {
          category = new Category(id, name);
        }

        blog.addCategory(category);
        DAOFactory factory = DAOFactory.getConfiguredFactory();
        CategoryDAO dao = factory.getCategoryDAO();
        dao.addCategory(category, blog);
      }
    } catch (Exception e) {
      log.error(e);
    }
  }

  /**
   * Moves blog entries from one category to another.
   *
   * @param blog    a Blog instance
   */
  public static void moveBlogEntriesFromCategory(Blog blog, Category from, Category to) {
    Iterator blogEntries = blog.getBlogEntries().iterator();
    while (blogEntries.hasNext()) {
      BlogEntry blogEntry = (BlogEntry)blogEntries.next();
      log.info("Processing " + blogEntry.getTitle() + " (" + blogEntry.getDate() + ")");

      Collection categories = blogEntry.getCategories();
      if (categories.contains(from)) {
        categories.remove(from);
        categories.add(to);
        blogEntry.setCategories(categories);

        try {
          BlogService service = new BlogService();
          service.putBlogEntry(blogEntry);
        } catch (BlogException e) {
          log.info("Error storing " + blogEntry.getTitle() + " (" + blogEntry.getDate() + ")");
        }
      }
    }
  }
  
  public static void main(String[] args) throws Exception {
    if (args.length != 2) {
      System.out.println("Usage : pebble.util.Utilities %1 %2");
      System.out.println("   %1 : location of Pebble blog");
      System.out.println("   %2 : [ipAddressListener|fixHtmlInResponses|convertCategories]");

      return;
    }

    DAOFactory.setConfiguredFactory(new FileDAOFactory());
    Blog blog = new Blog(args[0]);

    String action = args[1];
    if (action == null) {
      // do nothing
    } else if (action.equalsIgnoreCase("ipAddressListener")) {
      buildIpAddressLists(blog);
    } else if (action.equalsIgnoreCase("fixHtmlInResponses")) {
      fixHtmlInResponses(blog);
    } else if (action.equalsIgnoreCase("buildIndexes")) {
      buildIndexes(blog);
    } else if (action.equalsIgnoreCase("convertCategories")) {
      convertCategories(blog);
    }

  }

}