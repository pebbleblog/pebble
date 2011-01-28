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
package net.sourceforge.pebble.util;

import net.sourceforge.pebble.Configuration;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.dao.CategoryDAO;
import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.file.*;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;
import net.sourceforge.pebble.event.response.IpAddressListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.util.*;
import java.text.SimpleDateFormat;

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
    log.info("Reindexing blog");
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
//      } catch (BlogServiceException e) {
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
      } catch (BlogServiceException e) {
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
      log.error("Exception encountered", e);
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
        } catch (BlogServiceException e) {
          log.info("Error storing " + blogEntry.getTitle() + " (" + blogEntry.getDate() + ")");
        }
      }
    }
  }

  /**
   * Resets the theme of a blog to "default".
   */
  public static void resetTheme(Blog blog) {
    log.info("Resetting theme to default");
    try {
      blog.removeProperty(Blog.THEME_KEY);
      blog.storeProperties();
    } catch (BlogServiceException e) {
      e.printStackTrace();
    }
  }

  /**
   * Blasts the blog specific theme and overwrites it with the default theme.
   */
  public static void restoreTheme(Blog blog, String themeName) {
    log.info("Restoring theme to " + themeName);
    blog.getEditableTheme().restoreToSpecifiedTheme(themeName);
  }

  /**
   * Resets the plugins back to their defaults.
   */
  public static void resetPlugins(Blog blog) {
    log.info("Resetting plugins to the default configuration");
    try {
      blog.removeProperty(Blog.PERMALINK_PROVIDER_KEY);
      blog.removeProperty(Blog.CONTENT_DECORATORS_KEY);
      blog.removeProperty(Blog.BLOG_LISTENERS_KEY);
      blog.removeProperty(Blog.BLOG_ENTRY_LISTENERS_KEY);
      blog.removeProperty(Blog.COMMENT_LISTENERS_KEY);
      blog.removeProperty(Blog.COMMENT_CONFIRMATION_STRATEGY_KEY);
      blog.removeProperty(Blog.TRACKBACK_LISTENERS_KEY);
      blog.removeProperty(Blog.TRACKBACK_CONFIRMATION_STRATEGY_KEY);
      blog.removeProperty(Blog.LUCENE_ANALYZER_KEY);
      blog.removeProperty(Blog.LOGGER_KEY);
      blog.removeProperty(Blog.PAGE_DECORATORS_KEY);
      blog.removeProperty(Blog.OPEN_ID_COMMENT_AUTHOR_PROVIDERS_KEY);
      blog.storeProperties();
    } catch (BlogServiceException e) {
      e.printStackTrace();
    }
  }

  /**
   * Moves blog entries from one category to another.
   *
   * @param blog    a Blog instance
   */
  public static void restructureBlogToGMT(Blog blog) {
    log.info("Restructuring blog entries into GMT directory hierarchy");
    TimeZone gmt = TimeZone.getTimeZone("GMT");
    FileBlogEntryDAO dao = new FileBlogEntryDAO();
    File root = new File(blog.getRoot());
    File years[] = root.listFiles(new FourDigitFilenameFilter());
    for (File year : years) {
      File months[] = year.listFiles(new TwoDigitFilenameFilter());
      for (File month : months) {
        File days[] = month.listFiles(new TwoDigitFilenameFilter());
        for (File day : days) {
          File blogEntryFiles[] = day.listFiles(new BlogEntryFilenameFilter());
          for (File blogEntryFile : blogEntryFiles) {
            String filename = blogEntryFile.getName();
            String id = filename.substring(0, filename.indexOf('.'));
            File oldFile = blogEntryFile;
            File newDirectory = new File(dao.getPath(blog, id, gmt));
            File newFile = new File(newDirectory, filename);

            if (!oldFile.equals(newFile)) {
              log.info("Moving " + id + " to " + newFile.getAbsolutePath() + " from " + oldFile.getAbsolutePath());
              newDirectory.mkdirs();
              oldFile.renameTo(newFile);
            }
          }
        }
      }
    }
  }

  /**
   * Restructures how static pages are stored on disk.
   *
   * @param blog    a Blog instance
   */
  public static void restructureStaticPages(Blog blog) {
    log.info("Restructuring static pages");
    File root = new File(blog.getRoot(), "pages");
    // Upon first start this directory does not exist yet 
    if(!root.isDirectory()) {
    	root.mkdir();
    }

    File files[] = root.listFiles(new FilenameFilter() {
        public boolean accept(File dir, String name) {
          return name.matches("(\\d+\\.xml)|(\\d+\\.xml\\.bak)");
        }
    });

    for (File file : files) {
      if (file.getName().endsWith(".xml")) {
        File staticPageDirectory = new File(root, file.getName().substring(0, file.getName().indexOf(".xml")));
        if (!staticPageDirectory.exists()) {
          log.info("Creating static page directory at " + staticPageDirectory.getAbsolutePath());
          staticPageDirectory.mkdir();
        }
        File destination = new File(staticPageDirectory, file.getName());
        log.info("Moving " + file.getAbsolutePath() + " to " + destination.getAbsolutePath());
        file.renameTo(destination);
      } else {
        File staticPageDirectory = new File(root, file.getName().substring(0, file.getName().indexOf(".xml")));
        if (!staticPageDirectory.exists()) {
          log.info("Creating static page directory at " + staticPageDirectory.getAbsolutePath());
          staticPageDirectory.mkdir();
        }
        SimpleDateFormat archiveFileExtension = new SimpleDateFormat("yyyyMMdd-HHmmss");
        archiveFileExtension.setTimeZone(blog.getTimeZone());
        File destination = new File(staticPageDirectory, file.getName().substring(0, file.getName().length()-3) + archiveFileExtension.format(new Date(file.lastModified())));
        log.info("Moving " + file.getAbsolutePath() + " to " + destination.getAbsolutePath());
        file.renameTo(destination);
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
  /**
   * this is a very simple way to honor https transfer. As
   * the frontend depends upon setting a base url in the html/head block, it
   * should at least contain the https scheme if the current page has been
   * requested through https. 
   * If there is no https involved, pebbles previous behaviour 
   * (as of version 2.3.1) does not change. This change implies also changing 
   * several frontend jsps WEB-INF/tags/page.tag to use the value calculated
   * here instead of ${blog.url}. In the context of the patch that introduced
   * this method, the values have been named ${blogUrl} and ${multiBlogUrl}

   * @param currentScheme the scheme the current request has been sent through (e.g. request.getScheme())
   * @param blogUrl the ordinary blog url that might be changed to the secure one.
   * @return value to be used as base url for the blog named in blogUrl
   */
  public static String calcBaseUrl(String currentScheme, String blogUrl) {
  	Configuration configuration = PebbleContext.getInstance().getConfiguration();
  	if ("https".equals(currentScheme) && configuration.getSecureUrl().startsWith("https")) {
		return blogUrl.replace(configuration.getUrl(), configuration.getSecureUrl());
	} 
  	return blogUrl;
  }

}