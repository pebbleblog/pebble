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

package net.sourceforge.pebble.dao.mock;

import net.sourceforge.pebble.dao.BlogEntryDAO;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;

import java.util.*;

/**
 * This is a mock implementation of BlogEntryDAO that is used when performing
 * unit tests.
 *
 * @author    Simon brown
 */
public class MockBlogEntryDAO implements BlogEntryDAO {

  private Map blogEntries = new HashMap();

  /**
   * Loads a specific blog entry.
   *
   * @param blogEntryId   the blog entry ID
   * @return a BlogEntry instance
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if the specified blog entry cannot be loaded
   */
  public BlogEntry loadBlogEntry(Blog blog, String blogEntryId) throws PersistenceException {
    return (BlogEntry)blogEntries.get(blogEntryId);
  }

  /**
   * Loads all blog entries.
   *
   * @param blog the Blog to load all entries for
   * @return a List of BlogEntry objects
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if the blog entries cannot be loaded
   */
  public Collection<BlogEntry> loadBlogEntries(Blog blog) throws PersistenceException {
    List<BlogEntry> list = new ArrayList<BlogEntry>();
    for (Object o : blogEntries.values()) {
      list.add((BlogEntry)o);
    }
    return list;
  }

  /**
   * Stores the specified blog entry.
   *
   * @param blogEntry   the blog entry to store
   * @throws PersistenceException   if something goes wrong storing the entry
   */
  public void storeBlogEntry(BlogEntry blogEntry) throws PersistenceException {
    blogEntries.put(blogEntry.getId(), blogEntry);
  }

  /**
   * Removes the specified blog entry.
   *
   * @param blogEntry   the blog entry to remove
   * @throws PersistenceException   if something goes wrong removing the entry
   */
  public void removeBlogEntry(BlogEntry blogEntry) throws PersistenceException {
    blogEntries.remove(blogEntry.getId());
  }

}
