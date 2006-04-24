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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.*;

import net.sourceforge.pebble.comparator.BlogByLastModifiedDateComparator;

/**
 * A singleton to manage the active blog.
 *
 * @author    Simon Brown
 */
public class BlogManager {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(BlogManager.class);

  /** the singleton instance of this class */
  private static BlogManager instance = new BlogManager();

  private static final String THEMES_PATH = "themes";
  private static final String DEFAULT_BLOG = "blog";

  /** a flag to indicate whether Pebble is running in multi-user mode */
  private boolean multiUser = false;

  /** the directory containing the blog(s) */
  private String blogDir;

  /** the base URL of the Pebble installation */
  private String baseUrl = "";

  /** the directory where themes are located */
  private String webappRoot;

  /** the blogs that are currently being managed */
  private Map blogs = new HashMap();

  private long fileUploadSize = 2048;
  private long fileUploadQuota = -1;

  /**
   * Creates a new instance - private constructor for the singleton pattern.
   */
  private BlogManager() {
  }

  /**
   * Gets the singleton instance of this class.
   *
   * @return    the singleton BlogManager instance
   */
  public static BlogManager getInstance() {
    return instance;
  }

  /**
   * Gets the default blog.
   *
   * @return    the active Blog instance
   */
  public Blog getBlog() {
    return getBlog(DEFAULT_BLOG);
  }

  /**
   * Gets a named blog. If running in single user mode then this method
   * returns the currently active blog. If running in multi-user mode,
   * this method returns the named blog from the overall composite
   * blog.
   *
   * @param id    the blog ID
   * @return  a Blog instance
   */
  public Blog getBlog(String id) {
    if (multiUser) {
      return (Blog)blogs.get(id);
    } else {
      return (Blog)blogs.get(DEFAULT_BLOG);
    }
  }

  /**
   * Configures this instance to manage the blog(s) in the specified directory.
   */
  public void startBlogs() {
    if (isMultiUser()) {
      // find all directories and set them up as blogs
      File files[] = new File(blogDir).listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.isDirectory()) {
            startBlog(file.getAbsolutePath(), file.getName());
          }
        }
      }
    } else {
      startBlog(blogDir, DEFAULT_BLOG);
    }
  }

  public void stopBlogs() {
    Blog blog;
    Iterator it = blogs.values().iterator();
    while (it.hasNext()) {
      blog = (Blog)it.next();
      stopBlog(blog);
    }
  }

  private void stopBlog(Blog blog) {
    blog.stop();
  }

  public void reloadBlog(Blog blog) {
    stopBlog(blog);

    if (isMultiUser()) {
      File f = new File(blogDir, blog.getId());
      startBlog(f.getAbsolutePath(), blog.getId());
    } else {
      startBlog(blogDir, DEFAULT_BLOG);
    }
  }

  /**
   * Loads a blog that is a part of a larger composite blog.
   *
   * @param blogDir   the blog.dir for the blog
   * @param blogId    the ID for the blog
   */
  private void startBlog(String blogDir, String blogId) {
    Blog blog = new Blog(blogDir);
    blog.setId(blogId);

    File pathToLiveThemes = new File(webappRoot, THEMES_PATH);
    Theme theme = new Theme(blog, blogId, pathToLiveThemes.getAbsolutePath());
    blog.setEditableTheme(theme);

    blog.start();
    blogs.put(blog.getId(), blog);
  }

  public void addBlog(String blogId) {
    if (isMultiUser()) {
      File file = new File(blogDir, blogId);
      file.mkdirs();
      startBlog(file.getAbsolutePath(), blogId);
    }
  }

  /**
   * Gets the directory containing the blog(s).
   *
   * @return  an absolute path as a String
   */
  public String getBlogDir() {
    return this.blogDir;
  }

  /**
   * Sets the directory containing the blog(s).
   *
   * @param s   an absolute path on the filing system
   */
  public void setBlogDir(String s) {
    this.blogDir = evaluateBlogDir(s);

    File file = new File(blogDir);
    if (!file.exists()) {
      log.info("blog.dir does not exist - creating");
      file.mkdirs();
    }
  }

  /**
   * Replaces ${some.property} at the start of the string with the value
   * from System.getProperty(some.property).
   *
   * @param s   the String to transform
   * @return  a new String, or the same String if it doesn't start with a
   *          property name delimited by ${...}
   */
  private String evaluateBlogDir(String s) {
    log.debug("Raw blog.dir is " + s);
    if (s.startsWith("${")) {
      int index = s.indexOf("}");
      String propertyName = s.substring(2, index);
      String propertyValue = System.getProperty(propertyName);
      log.debug(propertyName + " = " + propertyValue);
      return propertyValue + s.substring(index+1);
    } else {
      return s;
    }
  }

  /**
   * Sets the base URL from which Pebble is running.
   *
   * @param url   a URL as a String
   */
  public void setBaseUrl(String url) {
    this.baseUrl = url;

    if (baseUrl != null && !(baseUrl.length() == 0) && !baseUrl.endsWith("/")) {
      baseUrl += "/";
    }
  }

  /**
   * Gets the base URL from which Pebble is running.
   *
   * @return    a URL as a String
   */
  public String getBaseUrl() {
    return this.baseUrl;
  }

  /**
   * Sets whether this blog manager supports multiple blogs.
   *
   * @param b   true if multiple blogs should be supported, false otherwise
   */
  public void setMultiUser(boolean b) {
    this.multiUser = b;
  }

  /**
   * Determines whether this blog manager supports multiple blogs.
   *
   * @return  true if multiple blogs are supported, false otherwise
   */
  public boolean isMultiUser() {
    return this.multiUser;
  }

  /**
   * Sets the directory where themes are located.
   *
   * @param webappRoot    the absolute path to the webapp root on disk
   */
  public void setWebappRoot(String webappRoot) {
    this.webappRoot = webappRoot;
  }

  public void addBlog(Blog blog) {
    blogs.put(blog.getId(), blog);
  }

  public void removeAllBlogs() {
    blogs = new HashMap();
  }

  /**
   * Gets all blogs that are currently being managed.
   *
   * @return  a Collection of Blog instances
   */
  public Collection getBlogs() {
    List sortedBlogs = new ArrayList(blogs.values());
    Collections.sort(sortedBlogs, new BlogByLastModifiedDateComparator());
    return sortedBlogs;
  }

  /**
   * Gets all blogs that are currently being managed and are
   * to be included in aggregated pages and feeds.
   *
   * @return  a Collection of Blog instances
   */
  public Collection getPublicBlogs() {
    Collection coll = blogs.values();
    List list = new ArrayList();
    Iterator it = coll.iterator();
    while (it.hasNext()) {
      Blog blog = (Blog)it.next();
      if (blog.isPublic()) {
        list.add(blog);
      }
    }

    return list;
  }

  /**
   * Determines whether there is a blog with the specified ID.
   *
   * @param id    the blog ID
   * @return  true if a blog with the specified ID exists, false otherwise
   */
  public boolean hasBlog(String id) {
    return blogs.containsKey(id);
  }

  public MultiBlog getMultiBlog() {
    return new MultiBlog(blogDir);
  }

  public long getFileUploadSize() {
    return fileUploadSize;
  }

  public void setFileUploadSize(long fileUploadSize) {
    this.fileUploadSize = fileUploadSize;
  }

  public long getFileUploadQuota() {
    return fileUploadQuota;
  }

  public void setFileUploadQuota(long fileUploadQuota) {
    this.fileUploadQuota = fileUploadQuota;
  }
  
}