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

import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.dao.StaticPageDAO;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.StaticPage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a mock implementation of StaticPageDAO that is used when performing
 * unit tests.
 *
 * @author    Simon Brown
 */
public class MockStaticPageDAO implements StaticPageDAO {

  private Map<String,StaticPage> pages = new HashMap<String,StaticPage>();

  /**
   * Loads the static pages for a given blog.
   *
   * @param blog    the owning Blog instance
   * @return  a Collection of StaticPage instances
   * @throws  PersistenceException    if static pages cannot be loaded
   */
  public Collection<StaticPage> loadStaticPages(Blog blog) throws PersistenceException {
    return pages.values();
  }

  /**
   * Loads a specific static page.
   *
   * @param blog    the owning Blog
   * @param pageId   the page ID
   * @return a StaticPage instance
   * @throws PersistenceException   if the static page cannot be loaded
   */
  public StaticPage loadStaticPage(Blog blog, String pageId) throws PersistenceException {
    return pages.get(pageId);
  }

  /**
   * Stores the specified static page.
   *
   * @param staticPage the static page to store
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if something goes wrong storing the static page
   */
  public void storeStaticPage(StaticPage staticPage) throws PersistenceException {
    pages.put(staticPage.getId(), staticPage);
  }

  /**
   * Removes the specified static page.
   *
   * @param staticPage the static page to remove
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if something goes wrong removing the page
   */
  public void removeStaticPage(StaticPage staticPage) throws PersistenceException {
    pages.remove(staticPage.getId());
  }

  /**
   * Locks the specified static page.
   *
   * @param staticPage the static page to lock
   * @return true if the page could be locked, false otherwise
   */
  public boolean lock(StaticPage staticPage) {
    return true;
  }

  /**
   * Unlocks the specified static page.
   *
   * @param staticPage the static page to unlock
   * @return true if the page could be unlocked, false otherwise
   */
  public boolean unlock(StaticPage staticPage) {
    return true;
  }
}
