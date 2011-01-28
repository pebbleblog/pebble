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

import net.sourceforge.pebble.comparator.ReverseBlogEntryIdComparator;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

/**
 * Keeps an index of all blog entries from a specific author, allowing efficient access at runtime.
 *
 * @author    Simon Brown
 */
public class AuthorIndex {

  private static final Log log = LogFactory.getLog(AuthorIndex.class);

  private Blog blog;

  /** the map containing the tags */
  private Map<String,List<String>> authors = new HashMap<String,List<String>>();

  public AuthorIndex(Blog blog) {
    this.blog = blog;

    readIndex();
  }

  /**
   * Clears the index.
   */
  public void clear() {
    authors = new HashMap<String,List<String>>();
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
        List<String> blogEntryIds = getBlogEntries(blogEntry.getAuthor());
        blogEntryIds.add(blogEntry.getId());
        Collections.sort(blogEntryIds, new ReverseBlogEntryIdComparator());
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
      List<String> blogEntryIds = getBlogEntries(blogEntry.getAuthor());
      blogEntryIds.add(blogEntry.getId());
      Collections.sort(blogEntryIds, new ReverseBlogEntryIdComparator());

      writeIndex();
    }
  }

  /**
   * Unindexes a single blog entry.
   *
   * @param blogEntry   a BlogEntry instance
   */
  public synchronized void unindex(BlogEntry blogEntry) {
    List<String> blogEntries = authors.get(blogEntry.getAuthor());
    if (blogEntries != null) {
      blogEntries.remove(blogEntry.getId());

      if (blogEntries.isEmpty()) {
        authors.remove(blogEntry.getAuthor());
      }
    }

    writeIndex();
  }

  /**
   * Helper method to load the index.
   */
  private void readIndex() {
    File indexFile = new File(blog.getIndexesDirectory(), "authors.index");
    if (indexFile.exists()) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(indexFile));
        String indexEntry = reader.readLine();
        while (indexEntry != null) {
          String[] tuple = indexEntry.split("=");
          String author = tuple[0];
          List<String> blogEntries = getBlogEntries(author);

          if (tuple.length > 1 && tuple[1] != null) {
            String[] blogEntryIds = tuple[1].split(",");
            for (String blogEntry : blogEntryIds) {
              blogEntries.add(blogEntry);
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
      File indexFile = new File(blog.getIndexesDirectory(), "authors.index");
      BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile));

      for (String author : authors.keySet()) {
        writer.write(author);
        writer.write("=");
        List<String> blogEntries = authors.get(author);
        if (blogEntries != null) {
          for (String blogEntry : blogEntries) {
            writer.write(blogEntry);
            writer.write(",");
          }
        }
        writer.newLine();
      }

      writer.flush();
      writer.close();
    } catch (Exception e) {
      log.error("Error while writing index", e);
    }
  }

  private synchronized List<String> getBlogEntries(String author) {
    List<String> blogEntries = authors.get(author);
    if (blogEntries == null) {
      blogEntries = new LinkedList<String>();
      authors.put(author, blogEntries);
    }

    return blogEntries;
  }

  /**
   * Gets the list of authors associated with this blog.
   */
  public List<String> getAuthors() {
    return new LinkedList<String>(authors.keySet());
  }

  /**
   * Gets the blog entries for a given author.
   *
   * @param username    a username (String)
   * @return  a List of blog entry IDs
   */
  public List<String> getRecentBlogEntries(String username) {
    List<String> blogEntries = authors.get(username);
    if (blogEntries == null) {
      return new LinkedList<String>();
    } else {
      return new LinkedList<String>(blogEntries);
    }
  }

}