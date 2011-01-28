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
import net.sourceforge.pebble.dao.RefererFilterDAO;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.RefererFilter;

import java.util.Collection;
import java.util.HashSet;

/**
 * A mock implementation of the CategoryDAO interface that does nothing. This
 * is used when performing unit tests.
 *
 * @author    Simon Brown
 */
public class MockRefererFilterDAO implements RefererFilterDAO {

  /**
   * Loads the referer filters.
   *
   * @param rootBlog    the owning Blog instance
   * @return  a Collection of RefererFilter instances
   * @throws  PersistenceException    if filters cannot be loaded
   */
  public Collection getRefererFilters(Blog rootBlog) throws PersistenceException {
    return new HashSet();
  }

  /**
   * Adds the specified referer filter.
   *
   * @param filter    the RefererFilter instance to be added
   * @param rootBlog    the owning Blog instance
   * @throws PersistenceException   if something goes wrong storing the filters
   */
  public void addRefererFilter(RefererFilter filter, Blog rootBlog) throws PersistenceException {
  }

  /**
   * Removes the specified referer filter.
   *
   * @param filter   the RefererFilter instance to be removed
   * @param rootBlog the owning Blog instance
   * @throws net.sourceforge.pebble.dao.PersistenceException
   *          if something goes wrong removing the filter
   */
  public void deleteRefererFilter(RefererFilter filter, Blog rootBlog) throws PersistenceException {
  }

}
