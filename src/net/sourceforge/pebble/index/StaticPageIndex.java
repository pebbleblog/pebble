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
import net.sourceforge.pebble.domain.StaticPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

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
  private Map<String,String> index = new HashMap<String,String>();

  public StaticPageIndex(Blog blog) {
    this.blog = blog;

    readIndex();
  }

  /**
   * Clears the index.
   */
  public void clear() {
    index = new HashMap<String,String>();
    writeIndex();
  }

  /**
   * Indexes one or more blog entries.
   *
   * @param staticPages   a List of Page instances
   */
  public synchronized void index(List<StaticPage> staticPages) {
    for (StaticPage staticPage : staticPages) {
      index.put(staticPage.getName(), staticPage.getId());
    }

    writeIndex();
  }

  /**
   * Indexes a single page.
   *
   * @param staticPage    a Page instance
   */
  public synchronized void index(StaticPage staticPage) {
    // when static name changes, multiple names will refer to the same page
    // this block removes the page if it's been previously indexed
    Iterator it = index.keySet().iterator();
    while (it.hasNext()) {
      String key = (String)it.next();
      String value = index.get(key);
      if (value.equals(staticPage.getId())) {
        it.remove();
      }
    }

    // and now index the page
    index.put(staticPage.getName(), staticPage.getId());
    writeIndex();
  }

  /**
   * Unindexes a single page.
   *
   * @param staticPage    a Page instance
   */
  public synchronized void unindex(StaticPage staticPage) {
    index.remove(staticPage.getName());
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
          index.put(parts[0], parts[1]);

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
      File indexFile = new File(blog.getIndexesDirectory(), "pages.index");
      BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile));

      for (String name : index.keySet()) {
        writer.write(name + "=" + index.get(name));
        writer.newLine();
      }

      writer.flush();
      writer.close();
    } catch (Exception e) {
      log.error("Error while writing index", e);
    }
  }

  /**
   * Gets the page ID for the specified named page.
   *
   * @param name    a String
   * @return  a String instance, or null if no page exists
   *          with the specified name
   */
  public String getStaticPage(String name) {
    return index.get(name);
  }

  /**
   * Determines whether a page with the specified permalink exists.
   *
   * @param name   the name as a String
   * @return  true if the page exists, false otherwise
   */
  public boolean contains(String name) {
    return index.containsKey(name);
  }

  /**
   * Gets the number of static pages.
   *
   * @return  an int
   */
  public int getNumberOfStaticPages() {
    return index.size();
  }

}