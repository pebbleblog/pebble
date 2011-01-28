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

import net.sourceforge.pebble.logging.Referer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Tests for the RefererFilterManager class.
 *
 * @author    Simon Brown
 */
public class RefererFilterManagerTest extends SingleBlogTestCase {

  private RefererFilterManager refererFilterManager;

  protected void setUp() throws Exception {
    super.setUp();
    refererFilterManager = new RefererFilterManager(blog);
  }

  /**
   * Tests that a collection of all filters can be obtained.
   */
  public void testGetFilters() {
    Collection filters = refererFilterManager.getFilters();
    assertEquals(0, filters.size());
  }

  /**
   * Tests that a filter can be added.
   */
  public void testAddFilter() {
    RefererFilter filter = new RefererFilter("A new filter");
    refererFilterManager.addFilter(filter);
    assertTrue(refererFilterManager.getFilters().contains(filter));
    assertEquals(1, refererFilterManager.getFilters().size());
  }

  /**
   * Tests that a duplicate filter can be added.
   */
  public void testAddDuplicateFilter() {
    RefererFilter filter1 = new RefererFilter("A new filter");
    RefererFilter filter2 = new RefererFilter("A new filter");
    refererFilterManager.addFilter(filter1);
    assertTrue(refererFilterManager.getFilters().contains(filter1));
    assertEquals(1, refererFilterManager.getFilters().size());

    // now add the duplicate - there should still be one filter
    refererFilterManager.addFilter(filter2);
    assertTrue(refererFilterManager.getFilters().contains(filter1));
    assertEquals(1, refererFilterManager.getFilters().size());
  }

  /**
   * Tests that a list of referers is filtered correctly.
   */
  public void testFilter() {
    List referers = new ArrayList();
    Referer url1 = new Referer("http://www.google.com");
    referers.add(url1);
    Referer url2 = new Referer("http://www.test.com");
    referers.add(url2);
    Referer url3 = new Referer("http://www.yahoo.com");
    referers.add(url3);
    assertEquals(3, referers.size());
  }

  /** todo
   * Tests that a filter can be removed.
  public void testRemoveFilterThatExists() {
    RefererFilter filter = new RefererFilter("A new filter");
    refererFilterManager.addFilter(filter);
    assertEquals(1, refererFilterManager.getFilters().size());

    assertTrue(refererFilterManager.removeFilter(filter.getId()));
    assertEquals(0, refererFilterManager.getFilters().size());
  }
   */

  /** todo
   * Tests that a filter can be removed.
  public void testRemoveFilterThatDoesntExists() {
    RefererFilter filter = new RefererFilter("A new filter");
    refererFilterManager.addFilter(filter);
    assertEquals(1, refererFilterManager.getFilters().size());

    assertFalse(refererFilterManager.removeFilter(2));
    assertEquals(1, refererFilterManager.getFilters().size());
  }
   */

}
