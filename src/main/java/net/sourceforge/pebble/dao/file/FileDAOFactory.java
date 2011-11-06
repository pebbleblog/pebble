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
package net.sourceforge.pebble.dao.file;

import net.sourceforge.pebble.dao.*;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.index.AuthorIndex;
import net.sourceforge.pebble.index.BlogEntryIndex;
import net.sourceforge.pebble.index.TagIndex;
import net.sourceforge.pebble.index.file.FileAuthorIndex;
import net.sourceforge.pebble.index.file.FileBlogEntryIndex;
import net.sourceforge.pebble.index.file.FileTagIndex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;

/**
 * Represents a strategy used to load and store blog entries
 * in the filing system using an XML format.
 *
 * @author    Simon Brown
 */
public class FileDAOFactory implements DAOFactory {
  private static final Log log = LogFactory.getLog(FileDAOFactory.class);
  public static final String FILE_STORAGE_TYPE = "file";

  private BlogEntryDAO blogEntryDAO;
  private StaticPageDAO staticPageDAO;
  private CategoryDAO categoryDAO;
  private RefererFilterDAO refererFilterDAO;
  private BlogEntryIndex blogEntryIndex;

  /**
   * Default, no args constructor.
   */
  public FileDAOFactory() {
    this.blogEntryDAO = new FileBlogEntryDAO();
    this.staticPageDAO = new FileStaticPageDAO();
    this.categoryDAO = new FileCategoryDAO();
    this.refererFilterDAO = new FileRefererFilterDAO();
    this.blogEntryIndex = new FileBlogEntryIndex();
  }

  /**
   * Gets a DAO instance responsible for the dao of blog entries.
   *
   * @return  a BlogEntryDAO instance
   */
  public BlogEntryDAO getBlogEntryDAO() {
    return this.blogEntryDAO;
  }

  /**
   * Gets a DAO instance responsible for the dao of static pages.
   *
   * @return a StaticPageDAO instance
   */
  public StaticPageDAO getStaticPageDAO() {
    return this.staticPageDAO;
  }

  /**
   * Gets a DAO instance responsible for the dao of categories.
   *
   * @return  a CategoryDAO instance
   */
  public CategoryDAO getCategoryDAO() {
    return this.categoryDAO;
  }

  /**
   * Gets a DAO instance responsible for the dao of referer filters.
   *
   * @return  a RefererFilterDAO instance
   */
  public RefererFilterDAO getRefererFilterDAO() {
    return this.refererFilterDAO;
  }

  public BlogEntryIndex getBlogEntryIndex() {
    return blogEntryIndex;
  }

  public TagIndex createTagIndex(Blog blog) {
    return new FileTagIndex(blog);
  }

  public AuthorIndex createAuthorIndex(Blog blog) {
    return new FileAuthorIndex(blog);
  }

  public void init(Blog blog) {
    blogEntryIndex.init(blog);
  }

  public void reindex(Blog blog) {
    blogEntryIndex.clear(blog);
    try {
      Collection<BlogEntry> blogEntries = getBlogEntryDAO().loadBlogEntries(blog);
      blog.reindex(blogEntries);
      blogEntryIndex.index(blog, blogEntries);
    } catch (PersistenceException e) {
      blog.error("Error loading all blog entries " + e);
      log.error("Error loading all blog entries", e);
    }
  }

  public void shutdown() {
  }
}
