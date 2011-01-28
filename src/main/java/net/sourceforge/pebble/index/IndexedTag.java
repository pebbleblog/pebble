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
import net.sourceforge.pebble.domain.Tag;
import net.sourceforge.pebble.comparator.ReverseBlogEntryIdComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Represents a tag.
 *
 * @author    Simon Brown
 */
public class IndexedTag extends Tag {

  /** the blog entries associated with this tag */
  private List<String> blogEntries = new ArrayList<String>();

  /**
   * Creates a new tag with the specified properties.
   *
   * @param name    the name
   * @param blog    a Blog instance
   */
  public IndexedTag(String name, Blog blog) {
    super(name, blog);
  }

  /**
   * Gets the blog entries associated with this tag.
   *
   * @return  a Collection of BlogEntry instances
   */
  public List<String> getBlogEntries() {
    return new ArrayList<String>(blogEntries);
  }

  /**
   * Adds a blog entry to this tag.
   *
   * @param blogEntryId   a blog entry ID
   */
  public synchronized void addBlogEntry(String blogEntryId) {
    if (blogEntryId != null && !blogEntries.contains(blogEntryId)) {
      blogEntries.add(blogEntryId);
      Collections.sort(blogEntries, new ReverseBlogEntryIdComparator());
    }
  }

  /**
   * Removes a blog entry to this tag.
   *
   * @param blogEntryId   a blog entry ID
   */
  public synchronized void removeBlogEntry(String blogEntryId) {
    if (blogEntryId != null) {
      blogEntries.remove(blogEntryId);
    }
  }

  /**
   * Gets the number of blog entries associated with this tag.
   *
   * @return  an int
   */
  public int getNumberOfBlogEntries() {
    return blogEntries.size();
  }

  /**
   * Gets the rank for this tag.
   *
   * @return  an int between 1 and 10;
   */
  public int getRank() {
    return this.rank;
  }

  /**
   * Sets the rank for this tag.
   *
   */
  void calculateRank(int[] thresholds) {
    for (int i = 0; i < thresholds.length; i++) {
      int numberOfBlogEntries = getNumberOfBlogEntries();
      if (numberOfBlogEntries <= thresholds[i]) {
        this.rank = i+1;
        return;
      }
    }
  }

}
