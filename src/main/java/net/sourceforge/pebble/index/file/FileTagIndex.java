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
package net.sourceforge.pebble.index.file;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Tag;
import net.sourceforge.pebble.index.IndexedTag;
import net.sourceforge.pebble.index.TagIndex;
import net.sourceforge.pebble.util.TagRanker;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

/**
 * Represents the tag index for a blog.
 *
 * @author    Simon Brown
 */
public class FileTagIndex implements TagIndex {

  private static final Log log = LogFactory.getLog(FileTagIndex.class);

  private Blog blog;

  /** the map containing the tags */
  private Map<String, IndexedTag> tags = new HashMap<String, IndexedTag>();

  /** a view onto the map, ordered by tag name */
  private Collection<Tag> orderedTags = new ArrayList<Tag>();

  public FileTagIndex(Blog blog) {
    this.blog = blog;

    readIndex();
    recalculateTagRankings();
  }

  /**
   * Clears the index.
   */
  public void clear() {
    tags = new HashMap<String,IndexedTag>();
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
        for (Tag tag : blogEntry.getAllTags()) {
          IndexedTag t = getTag(tag.getName());
          t.addBlogEntry(blogEntry.getId());
        }
      }
    }

    writeIndex();
    recalculateTagRankings();
  }

  /**
   * Indexes a single blog entry.
   *
   * @param blogEntry   a BlogEntry instance
   */
  public synchronized void index(BlogEntry blogEntry) {
    if (blogEntry.isPublished()) {
      for (Tag tag : blogEntry.getAllTags()) {
        IndexedTag t = getTag(tag.getName());
        t.addBlogEntry(blogEntry.getId());
      }

      writeIndex();
      recalculateTagRankings();
    }
  }

  /**
   * Unindexes a single blog entry.
   *
   * @param blogEntry   a BlogEntry instance
   */
  public synchronized void unindex(BlogEntry blogEntry) {
    for (Tag tag : tags.values()) {
      IndexedTag t = getTag(tag.getName());
      t.removeBlogEntry(blogEntry.getId());
    }

    writeIndex();
    recalculateTagRankings();
  }

  /**
   * Helper method to load the index.
   */
  private void readIndex() {
    File indexFile = new File(blog.getIndexesDirectory(), "tags.index");
    if (indexFile.exists()) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(indexFile));
        String indexEntry = reader.readLine();
        while (indexEntry != null) {
          String[] tuple = indexEntry.split("=");
          IndexedTag tag = getTag(tuple[0]);

          if (tuple.length > 1 && tuple[1] != null) {
            String[] blogEntries = tuple[1].split(",");
            for (String blogEntry : blogEntries) {
              tag.addBlogEntry(blogEntry);
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
      File indexFile = new File(blog.getIndexesDirectory(), "tags.index");
      BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile));

      for (IndexedTag tag : tags.values()) {
        writer.write(tag.getName());
        writer.write("=");
        for (String blogEntry : tag.getBlogEntries()) {
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
   * Gets a tag from the index, creating it if necessary.
   *
   * @param name    the tag as a String
   * @return    a Tag instance
   */
  synchronized IndexedTag getTag(String name) {
    String encodedName = Tag.encode(name);
    IndexedTag tag = tags.get(encodedName);
    if (tag == null) {
      tag = new IndexedTag(name, blog);
      tags.put(encodedName, tag);
    }
    return tag;
  }

  private synchronized void recalculateTagRankings() {
    orderedTags = TagRanker.calculateTagRankings(tags.values());
  }

  /**
   * Gets the list of tags associated with this blog.
   */
  public Collection<Tag> getTags() {
    return orderedTags;
  }

  /**
   * Gets the blog entries for a given tag.
   *
   * @param tag   a tag
   * @return  a List of blog entry IDs
   */
  public List<String> getRecentBlogEntries(Tag tag) {
    return new ArrayList<String>(getTag(tag.getName()).getBlogEntries());
  }

}
