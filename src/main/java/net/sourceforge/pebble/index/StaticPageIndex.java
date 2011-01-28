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
import net.sourceforge.pebble.domain.StaticPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

/**
 * Maintains an index of all static pages
 *
 * @author    Simon Brown
 */
public class StaticPageIndex {

  private static final Log log = LogFactory.getLog(StaticPageIndex.class);

  private static final String PAGES_INDEX_DIRECTORY_NAME = "pages";
  private static final String NAME_TO_ID_INDEX_FILE_NAME = "name.index";
  private static final String LOCK_FILE_NAME = "pages.lock";
  private static final int MAXIMUM_LOCK_ATTEMPTS = 3;

  /** the owning blog */
  private Blog blog;

  /** the collection of all static pages */
  private Map<String,String> index = new HashMap<String,String>();
  private int lockAttempts = 0;

  public StaticPageIndex(Blog blog) {
    this.blog = blog;

    // create the directory structure if it doesn't exist
    File indexDirectory = new File(blog.getIndexesDirectory(), PAGES_INDEX_DIRECTORY_NAME);
    if (!indexDirectory.exists()) {
      indexDirectory.mkdirs();
    }

    readIndex();
  }

  /**
   * Indexes one or more blog entries.
   *
   * @param staticPages   a List of Page instances
   */
  public synchronized void reindex(Collection<StaticPage> staticPages) {
    if (lock()) {
      // clear the index and add all static pages
      index = new HashMap<String,String>();
      for (StaticPage staticPage : staticPages) {
        index.put(staticPage.getName(), staticPage.getId());
      }

      // and finally, write the index
      writeIndex();
      unlock();
    }
  }

  /**
   * Indexes a single page.
   *
   * @param staticPage    a Page instance
   */
  public synchronized void index(StaticPage staticPage) {
    if (lock()) {
      readIndex();

      // remove the old entry for this static page
      Iterator it = index.keySet().iterator();
      while (it.hasNext()) {
        String key = (String)it.next();
        String value = index.get(key);
        if (value.equals(staticPage.getId())) {
          it.remove();
        }
      }

      // and now add the new entry for this page
      index.put(staticPage.getName(), staticPage.getId());
      writeIndex();
      unlock();
    } else {
      if (lockAttempts <= MAXIMUM_LOCK_ATTEMPTS) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException ie) {
          // ignore
        }
        index(staticPage);
      } else {
        blog.error("Could not index static page - try <a href=\"utilities.secureaction?action=buildIndexes\">rebuilding the indexes</a>.");
      }
    }
  }

  /**
   * Unindexes a single page.
   *
   * @param staticPage    a Page instance
   */
  public synchronized void unindex(StaticPage staticPage) {
    if (lock()) {
      readIndex();
      index.remove(staticPage.getName());
      writeIndex();
      unlock();
    } else {
      if (lockAttempts <= MAXIMUM_LOCK_ATTEMPTS) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException ie) {
          // ignore
        }
        unindex(staticPage);
      } else {
        blog.reindexStaticPages();
      }
    }
  }

  /**
   * Helper method to load the index.
   */
  private void readIndex() {
    log.info("Reading index from disk");
    File indexFile = getIndexFile();
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
      File indexFile = getIndexFile();
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
   * Gets the list of static page IDs.
   *
   * @return    a List<String>
   */
  public List<String> getStaticPages() {
    return new LinkedList<String>(index.values());
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

  private File getIndexFile() {
    File indexDirectory = new File(blog.getIndexesDirectory(), PAGES_INDEX_DIRECTORY_NAME);
    return new File(indexDirectory, NAME_TO_ID_INDEX_FILE_NAME);
  }

  private boolean lock() {
    File lockFile = new File(blog.getIndexesDirectory(), LOCK_FILE_NAME);
    boolean success = false;
    try {
      success = lockFile.createNewFile();
      if (!success) {
        lockAttempts++;
      }
    } catch (IOException ioe) {
      log.warn("Error while creating lock file", ioe);
    }

    return success;
  }

  private void unlock() {
    File lockFile = new File(blog.getIndexesDirectory(), LOCK_FILE_NAME);
    lockFile.delete();
    lockAttempts = 0;
  }

}