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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * A class to manage the relationship between blog entries and staticPages.
 *
 * @author    Simon Brown
 */
public class StaticPageIndex {

  /** the owning blog */
  private Blog blog;

  /** the collection of all static pages */
  private Map staticPages;

  /**
   * Creates a new instance.
   */
  StaticPageIndex(Blog blog) {
    this.blog = blog;
    reindex();
  }

  /**
   * Initializes the categories.
   */
  public synchronized void reindex() {
    BlogService service = new BlogService();
    Collection coll = service.getStaticPages(blog);
    staticPages = new HashMap();
    Iterator it = coll.iterator();
    while (it.hasNext()) {
      BlogEntry blogEntry = (BlogEntry)it.next();
      staticPages.put(blogEntry.getStaticName(), blogEntry);
    }
  }

  /**
   * Gets a blog entry for the specified static name.
   *
   * @param name    a String
   * @return  a BlogEntry instance, or null if no static page exists
   *          at the specified name
   */
  public BlogEntry getStaticPage(String name) {
    return (BlogEntry)staticPages.get(name);
  }

  /**
   * Determines whether a static page with the specified permalink exists.
   *
   * @param name   the name as a String
   * @return  true if the page exists, false otherwise
   */
  public boolean contains(String name) {
    return staticPages.containsKey(name);
  }

}