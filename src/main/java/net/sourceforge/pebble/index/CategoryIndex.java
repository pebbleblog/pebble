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
package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Category;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.io.*;

/**
 * Represents the category index for a blog.
 *
 * @author    Simon Brown
 */
public class CategoryIndex {

  private static final Log log = LogFactory.getLog(CategoryIndex.class);

  private Blog blog;

  public CategoryIndex(Blog blog) {
    this.blog = blog;

//    File indexes = new File(blog.getIndexesDirectory());
//    if (!indexes.exists()) {
//      indexes.mkdir();
//    }
    readIndex();
  }

  /**
   * Clears the index.
   */
  public void clear() {
    for (Category category : blog.getCategories()) {
      category.removeAllBlogEntries();
    }

    writeIndex();
  }

  /**
   * Indexes one or more blog entries.
   *
   * @param blogEntries   a List of BlogEntry instances
   */
  public synchronized void index(Collection<BlogEntry> blogEntries) {
    for (BlogEntry blogEntry : blogEntries) {
      if (blogEntry.isPublished()) {
        for (Category category: blogEntry.getCategories()) {
          category.addBlogEntry(blogEntry.getId());
        }
      }
    }

    writeIndex();
  }

  /**
   * Indexes a single blog entry.
   *
   * @param blogEntry   a BlogEntry instance
   */
  public synchronized void index(BlogEntry blogEntry) {
    if (blogEntry.isPublished()) {
      for (Category category : blogEntry.getCategories()) {
        category.addBlogEntry(blogEntry.getId());
      }

      writeIndex();
    }
  }

  /**
   * Unindexes a single blog entry.
   *
   * @param blogEntry   a BlogEntry instance
   */
  public synchronized void unindex(BlogEntry blogEntry) {
    for (Category category : blog.getCategories()) {
      category.removeBlogEntry(blogEntry.getId());
    }

    writeIndex();
  }

  /**
   * Helper method to load the index.
   */
  private void readIndex() {
    File indexFile = new File(blog.getIndexesDirectory(), "categories.index");
    if (indexFile.exists()) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(indexFile));
        String indexEntry = reader.readLine();
        while (indexEntry != null) {
          String[] tuple = indexEntry.split("=");
          Category category = blog.getCategory(tuple[0]);

          if (tuple.length > 1 && tuple[1] != null) {
            String[] blogEntries = tuple[1].split(",");
            for (String blogEntry : blogEntries) {
              category.addBlogEntry(blogEntry);
            }
          }

          indexEntry = reader.readLine();
        }

        reader.close();
      } catch (Exception e) {
        log.error("Error while reading index", e);
      }
    }
  }

  /**
   * Helper method to write out the index to disk.
   */
  private void writeIndex() {
    try {
      File indexFile = new File(blog.getIndexesDirectory(), "categories.index");
      BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile));

      for (Category category : blog.getCategories()) {
        writer.write(category.getId());
        writer.write("=");
        for (String blogEntry : category.getBlogEntries()) {
          writer.write(blogEntry);
          writer.write(",");
        }
        writer.newLine();
      }

      writer.flush();
      writer.close();
    } catch (Exception e) {
      log.error("Error while writing index", e);
    }
  }

  /**
   * Gets the the list of blog entries for a given category.
   *
   * @param category    a category
   * @return  a List of blog entry IDs
   */
  public List<String> getRecentBlogEntries(Category category) {
    return new ArrayList<String>(category.getBlogEntries());
  }

}
