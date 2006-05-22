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
package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.DailyBlog;
import net.sourceforge.pebble.comparator.ReverseBlogEntryIdComparator;

import java.util.*;
import java.io.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Maintains an index of all static pages
 *
 * @author    Simon Brown
 */
public class StaticPageIndex {

  private static final Log log = LogFactory.getLog(StaticPageIndex.class);

  /** the owning blog */
  private Blog blog;

  /** the collection of all static pages */
  private Map<String,String> pages = new HashMap<String,String>();

  public StaticPageIndex(Blog blog) {
    this.blog = blog;
    readIndex();
  }

  /**
   * Clears the index.
   */
  public void clear() {
    pages = new HashMap<String,String>();
    writeIndex();
  }

  /**
   * Indexes one or more blog entries.
   *
   * @param blogEntries   a List of BlogEntry instances
   */
  public synchronized void index(List<BlogEntry> blogEntries) {
    for (BlogEntry blogEntry : blogEntries) {
      pages.put(blogEntry.getStaticName(), blogEntry.getId());
    }

    writeIndex();
  }

  /**
   * Indexes a single blog entry.
   *
   * @param blogEntry   a BlogEntry instance
   */
  public synchronized void index(BlogEntry blogEntry) {
    // todo - when static name changes, multiple names will refer to the same page
    pages.put(blogEntry.getStaticName(), blogEntry.getId());
    writeIndex();
  }

  /**
   * Unindexes a single blog entry.
   *
   * @param blogEntry   a BlogEntry instance
   */
  public synchronized void unindex(BlogEntry blogEntry) {
    pages.remove(blogEntry.getStaticName());
    writeIndex();
  }

  /**
   * Helper method to load the index.
   */
  private void readIndex() {
    File indexFile = new File(blog.getIndexesDirectory(), "pages.index");
    if (indexFile.exists()) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(indexFile));
        String indexEntry = reader.readLine();
        while (indexEntry != null) {
          String[] parts = indexEntry.split("=");
          pages.put(parts[0], parts[1]);

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
      File indexes = new File(blog.getIndexesDirectory());
      if (!indexes.exists()) {
        indexes.mkdir();
      }
      File indexFile = new File(blog.getIndexesDirectory(), "pages.index");
      BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile));

      for (String name : pages.keySet()) {
        writer.write(name + "=" + pages.get(name));
        writer.newLine();
      }

      writer.flush();
      writer.close();
    } catch (Exception e) {
      log.error("Error while writing index", e);
    }
  }

  /**
   * Gets a blog entry for the specified static name.
   *
   * @param name    a String
   * @return  a BlogEntry instance, or null if no static page exists
   *          at the specified name
   */
  public String getStaticPage(String name) {
    return pages.get(name);
  }

  /**
   * Determines whether a static page with the specified permalink exists.
   *
   * @param name   the name as a String
   * @return  true if the page exists, false otherwise
   */
  public boolean contains(String name) {
    return pages.containsKey(name);
  }

}