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
package net.sourceforge.pebble.service;

import net.sourceforge.pebble.ContentCache;
import net.sourceforge.pebble.comparator.StaticPageByNameComparator;
import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.dao.StaticPageDAO;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.StaticPage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Service that encompasses all functionality related to getting, putting
 * and removing static pages.
 *
 * @author    Simon Brown
 */
public class StaticPageService {

  private static final Log log = LogFactory.getLog(StaticPageService.class);

  /**
   * Gets the list of static pages for the given blog.
   *
   * @param blog    the Blog
   * @return  a list of BlogEntry instances
   * @throws  StaticPageServiceException if something goes wrong
   */
  public List<StaticPage> getStaticPages(Blog blog) throws StaticPageServiceException {
    List<StaticPage> staticPages = new ArrayList<StaticPage>();
    try {
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      StaticPageDAO dao = factory.getStaticPageDAO();
      staticPages.addAll(dao.loadStaticPages(blog));
    } catch (PersistenceException pe) {
      throw new StaticPageServiceException(blog, pe);
    }

    Collections.sort(staticPages, new StaticPageByNameComparator());

    return staticPages;
  }

  /**
   * Gets the page with the specified id.
   *
   * @param pageId   the id of the static page
   * @param blog    the Blog
   * @return  a Page instance, or null if the page couldn't be found
   * @throws  StaticPageServiceException if something goes wrong
   */
  public StaticPage getStaticPageById(Blog blog, String pageId) throws StaticPageServiceException {
    StaticPage staticPage;
    ContentCache cache = ContentCache.getInstance();

    try {
      staticPage = cache.getStaticPage(blog, pageId);
      if (staticPage != null) {
        log.debug("Got static page " + pageId+ " from cache");
      } else {
        log.debug("Loading static page " + pageId+ " from disk");

        DAOFactory factory = DAOFactory.getConfiguredFactory();
        StaticPageDAO dao = factory.getStaticPageDAO();
        staticPage = dao.loadStaticPage(blog, pageId);
        if (staticPage != null) {
          staticPage.setPersistent(true);
          cache.putStaticPage(staticPage);
        }
      }
    } catch (PersistenceException pe) {
      throw new StaticPageServiceException(blog, pe);
    }

    if (staticPage != null) {
      staticPage = (StaticPage)staticPage.clone();
    }

    return staticPage;
  }

  /**
   * Gets the static page with the specified name.
   *
   * @param name    the name of the static page
   * @param blog    the Blog
   * @return  a StaticPage instance, or null if the page couldn't be found
   * @throws  StaticPageServiceException if something goes wrong
   */
  public StaticPage getStaticPageByName(Blog blog, String name) throws StaticPageServiceException {
    String id = blog.getStaticPageIndex().getStaticPage(name);
    return getStaticPageById(blog, id);
  }

  /**
   * Puts the static page.
   *
   * @param   staticPage    the StaticPage instance to store
   * @throws  StaticPageServiceException if something goes wrong
   */
  public void putStaticPage(StaticPage staticPage) throws StaticPageServiceException {
    ContentCache cache = ContentCache.getInstance();
    DAOFactory factory = DAOFactory.getConfiguredFactory();
    StaticPageDAO dao = factory.getStaticPageDAO();
    Blog blog = staticPage.getBlog();

    synchronized (blog) {
      try {
        StaticPage sp = getStaticPageById(blog, staticPage.getId());

        if (!staticPage.isPersistent() && sp != null) {
          // the static page is new but one exists with the same ID already
          // - increment the date/ID and try again
          staticPage.setDate(new Date(staticPage.getDate().getTime() + 1));
          putStaticPage(staticPage);
        } else {
          dao.storeStaticPage(staticPage);
          staticPage.setPersistent(true);
          cache.removeStaticPage(staticPage);
        }

        staticPage.getBlog().getSearchIndex().index(staticPage);
        staticPage.getBlog().getStaticPageIndex().index(staticPage);
      } catch (PersistenceException pe) {
        throw new StaticPageServiceException(blog, pe);
      }
    }
  }

  /**
   * Removes a static page.
   *
   * @param staticPage    the StaticPage instance to remove
   * @throws  StaticPageServiceException if something goes wrong
   */
  public void removeStaticPage(StaticPage staticPage) throws StaticPageServiceException {
    ContentCache cache = ContentCache.getInstance();
    DAOFactory factory = DAOFactory.getConfiguredFactory();
    StaticPageDAO dao = factory.getStaticPageDAO();
    Blog blog = staticPage.getBlog();

    try {
      dao.removeStaticPage(staticPage);
      cache.removeStaticPage(staticPage);

      staticPage.getBlog().getSearchIndex().unindex(staticPage);
      staticPage.getBlog().getStaticPageIndex().unindex(staticPage);
    } catch (PersistenceException pe) {
      // remove from the cache so that it's picked up from storage when accessed next
      cache.removeStaticPage(staticPage);

      throw new StaticPageServiceException(staticPage.getBlog(), pe);
    }
  }

  /**
   * Locks a given static page.
   *
   * @param staticPage    the static page to lock
   * @return  true if the page could be locked, false otherwise
   */
  public boolean lock(StaticPage staticPage) {
    if (staticPage.isPersistent()) {
      boolean success = DAOFactory.getConfiguredFactory().getStaticPageDAO().lock(staticPage);
      ContentCache.getInstance().removeStaticPage(staticPage);

      return success;
    } else {
      return true;
    }
  }

  /**
   * Unlocks a given static page.
   *
   * @param staticPage    the static page to unlock
   * @return  true if the page could be unlocked, false otherwise
   */
  public boolean unlock(StaticPage staticPage) {
    if (staticPage.isPersistent()) {
      boolean success = DAOFactory.getConfiguredFactory().getStaticPageDAO().unlock(staticPage);
      ContentCache.getInstance().removeStaticPage(staticPage);

      return success;
    } else {
      return true;
    }
  }

}