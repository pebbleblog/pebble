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
package net.sourceforge.pebble.domain;

import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.dao.RefererFilterDAO;
import net.sourceforge.pebble.logging.CountedUrl;

import java.util.*;
import java.util.regex.Matcher;

/**
 * A class to manage regular expressions used to filter out obscene and spam
 * referers that appear in the logs.
 *
 * @author    Simon Brown
 */
public class RefererFilterManager {

  /** the next filter id - used internally */
  private int nextId = 1;

  /** the owning root blog */
  private Blog rootBlog;

  /** the collection of all filters */
  private Collection filters;

  /**
   * Creates a new instance.
   */
  RefererFilterManager(Blog rootBlog) {
    this.rootBlog = rootBlog;
    init();
  }

  /**
   * Initializes the filters.
   */
  private void init() {
    try {
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      RefererFilterDAO dao = factory.getRefererFilterDAO();
      filters = dao.getRefererFilters(rootBlog);

      Iterator it = filters.iterator();
      RefererFilter filter;
      while (it.hasNext()) {
        filter = (RefererFilter)it.next();
        filter.setId(nextId);
        nextId++;
      }
    } catch (PersistenceException pe) {
      pe.printStackTrace();
    }
  }

  /**
   * Adds a new filter to the existing list.
   *
   * @param newFilter   a RefererFilter instance
   */
  public synchronized void addFilter(RefererFilter newFilter) {
    try {
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      RefererFilterDAO dao = factory.getRefererFilterDAO();

      if (!filters.contains(newFilter)) {
        dao.addRefererFilter(newFilter, rootBlog);
        filters.add(newFilter);
        newFilter.setId(nextId);
        nextId++;
      }
    } catch (PersistenceException pe) {
      pe.printStackTrace();
    }
  }

  /**
   * Removes a filter from the list.
   *
   * @param expression    the expression to be removed
   */
  public synchronized boolean removeFilter(String expression) {
    try {
      DAOFactory factory = DAOFactory.getConfiguredFactory();
      RefererFilterDAO dao = factory.getRefererFilterDAO();

      Iterator it = filters.iterator();
      RefererFilter filter;
      while (it.hasNext()) {
        filter = (RefererFilter)it.next();

        if (filter.getExpression().equals(expression)) {
          // remove it from the persistent store
          dao.deleteRefererFilter(filter, rootBlog);

          // and now remove the in-memory representation
          filters.remove(filter);

          return true;
        }
      }
    } catch (PersistenceException pe) {
      pe.printStackTrace();
    }

    return false;
  }

  /**
   * Gets a collection containing filters.
   *
   * @return  a Collection of RefererFilter instances
   */
  public Collection getFilters() {
    return Collections.unmodifiableCollection(filters);
  }

  /**
   * Filters a collection of referers using the filters
   * managed by this instance. Any urls matching a filter are removed.
   *
   * @param referers    the List of referers (CountedUrls) to be filtered
   * @return  a filtered List containing CountedUrls
   */
  public List filter(List referers) {
    List results = new ArrayList();
    Iterator it = referers.iterator();
    CountedUrl referer;
    while (it.hasNext()) {
      referer = (CountedUrl)it.next();
      if (!filter(referer)) {
        results.add(referer);
      }
    }

    return results;
  }

  /**
   * Helper method to determine whether a single referer should
   * be filtered out.
   *
   * @param referer   a CountedUrl instance
   * @return  true if the referer should be filtered (i.e. matches one of the
   *          regular expressions), false otherwise
   */
  private boolean filter(CountedUrl referer) {

    if (referer.getUrl() == null) {
      return false;
    }

    Iterator it = filters.iterator();
    RefererFilter filter;
    Matcher matcher;
    while (it.hasNext()) {
      filter = (RefererFilter)it.next();
      matcher = filter.getCompiledExpression().matcher(referer.getUrl());
      if (matcher.matches()) {
        return true;
      }
    }

    return false;
  }

}