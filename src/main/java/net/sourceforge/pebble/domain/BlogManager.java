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

import com.google.common.collect.Maps;
import net.sourceforge.pebble.ContentCache;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.util.UpgradeUtilities;
import net.sourceforge.pebble.comparator.BlogByLastModifiedDateComparator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

/**
 * A singleton to manage the active blog.
 *
 * @author    Simon Brown
 */
public class BlogManager {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(BlogManager.class);

  private static final String THEMES_PATH = "themes";
  private static final String DEFAULT_BLOG = "default";

  /** the blogs that are currently being managed */
  private final Map<String,Blog> blogs = Maps.newConcurrentMap();

  private boolean multiBlog = false;

  private final BlogService blogService;
  private final DAOFactory daoFactory;
  private final ContentCache contentCache;

  public BlogManager(BlogService blogService, DAOFactory daoFactory, ContentCache contentCache) {
    this.blogService = blogService;
    this.daoFactory = daoFactory;
    this.contentCache = contentCache;
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
    if (id == null) {
      return null;
    }
    return blogs.get(id);
  }

  /**
   * Configures this instance to manage the blog(s) in the specified directory.
   */
  public void startBlogs() {
    File blogsDirectory = getBlogsDirectory();
    File defaultBlog = new File(blogsDirectory, DEFAULT_BLOG);

    if (!blogsDirectory.exists()) {
      log.info("Pebble blog directory does not exist - creating");
      defaultBlog.mkdirs();
    }

    if (isMultiBlog()) {
      // find all directories and set them up as blogs
      File files[] = getBlogsDirectory().listFiles();
      if (files != null) {
        for (File file : files) {
          if (file.isDirectory()) {
            Blog blog = new Blog(this, daoFactory, blogService, file.getAbsolutePath());
            blog.setId(file.getName());
            blogs.put(file.getName(), blog);
          }
        }
      }
    } else {
      // start the default blog only
      Blog blog = new Blog(this, daoFactory, blogService, defaultBlog.getAbsolutePath());
      blog.setId(DEFAULT_BLOG);
      blogs.put(DEFAULT_BLOG, blog);
    }
    contentCache.setNumberOfBlogs(blogs.size());
    for (Blog blog : blogs.values()) {
      startBlog(blog);
    }
  }

  public void stopBlogs() {
    for (Blog blog : blogs.values()) {
      stopBlog(blog);
    }
  }

  private void stopBlog(Blog blog) {
    blog.stop();
  }

  public void reloadBlog(Blog blog) {
    stopBlog(blog);

    File f = new File(getBlogsDirectory(), blog.getId());
    Blog newBlog = new Blog(this, daoFactory, blogService, f.getAbsolutePath());
    newBlog.setId(blog.getId());
    blogs.put(blog.getId(), newBlog);

    startBlog(newBlog);
  }

  /**
   * Loads a blog that is a part of a larger composite blog.
   *
   * @param blog The blog to start
   */
  private void startBlog(Blog blog) {

    File pathToLiveThemes = new File(PebbleContext.getInstance().getWebApplicationRoot(), THEMES_PATH);
    Theme theme = new Theme(blog, "user-" + blog.getId(), pathToLiveThemes.getAbsolutePath());
    blog.setEditableTheme(theme);

    blog.start();

    // which version are we at and do we need to upgrade?
    File versionFile = new File(blog.getRoot(), "pebble.version");
    String blogVersion = null;
    String currentVersion = PebbleContext.getInstance().getBuildVersion();
    try {
      if (versionFile.exists()) {
        BufferedReader reader = new BufferedReader(new FileReader(versionFile));
        blogVersion = reader.readLine();
        reader.close();
      }
    } catch (Exception e) {
      log.error("Exception encountered", e);
    }

    try {
      if (blogVersion == null || !blogVersion.equals(currentVersion)) {
        UpgradeUtilities.upgradeBlog(blog, blogVersion, currentVersion);

        if (currentVersion != null) {
          BufferedWriter writer = new BufferedWriter(new FileWriter(versionFile));
          writer.write(currentVersion);
          writer.close();
        }

        // now that the upgrade is complete, reload the blog
        reloadBlog(blog);
      }
    } catch (Exception e) {
      log.error("Exception encountered", e);
    }
  }

  public void addBlog(String blogId) {
    File file = new File(getBlogsDirectory(), blogId);
    file.mkdirs();
    Blog blog = new Blog(this, daoFactory, blogService, file.getAbsolutePath());
    blog.setId(blogId);
    blogs.put(blogId, blog);
    startBlog(blog);
  }

  /**
   * Determines whether this blog manager supports multiple blogs.
   *
   * @return  true if multiple blogs are supported, false otherwise
   */
  public boolean isMultiBlog() {
    return this.multiBlog;
  }

  public void setMultiBlog(boolean multiBlog) {
    this.multiBlog = multiBlog;
  }

  public void addBlog(Blog blog) {
    blogs.put(blog.getId(), blog);
  }

  public void removeAllBlogs() {
    blogs.clear();
  }

  /**
   * Gets all blogs that are currently being managed.
   *
   * @return  a Collection of Blog instances
   */
  public Collection<Blog> getBlogs() {
    List<Blog> sortedBlogs = new ArrayList<Blog>(blogs.values());
    Collections.sort(sortedBlogs, new BlogByLastModifiedDateComparator());
    return sortedBlogs;
  }

  /**
   * Gets the number of blogs that are currently being managed.
   *
   * @return the number of managed blogs
   */
  public int getNumberOfBlogs() {
	  return blogs.size();
  }
  
  
  /**
   * Gets all blogs that are currently being managed and are
   * to be included in aggregated pages and feeds.
   *
   * @return  a List of Blog instances
   */
  public List<Blog> getPublicBlogs() {
    List<Blog> list = new ArrayList<Blog>();
    for (Blog blog : blogs.values()) {
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
    return new MultiBlog(this, PebbleContext.getInstance().getConfiguration().getDataDirectory());
  }

  private File getBlogsDirectory() {
    return new File(PebbleContext.getInstance().getConfiguration().getDataDirectory(), "blogs");
  }

}