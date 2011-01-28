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
package net.sourceforge.pebble.util;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the Pageable class.
 *
 * @author    Simon Brown
 */
public class PageableTest extends TestCase {

  private Pageable pageable;
  private List list;

  protected void setUp() throws Exception {
    list = new ArrayList();
    for (int i = 0; i < 30; i++) {
      list.add(new Integer(i));
    }
  }

  public void testConstruction() {
    pageable = new Pageable(list);
    pageable.setPage(1);
    assertEquals(list, pageable.getList());
    assertEquals(Pageable.DEFAULT_PAGE_SIZE, pageable.getPageSize());
    assertEquals(1, pageable.getPage());
  }

  public void testOneFullPage() {
    pageable = new Pageable(list);
    pageable.setPageSize(30);
    pageable.setPage(1);

    assertEquals(1, pageable.getPage());
    assertEquals(1, pageable.getMaxPages());
    assertEquals(0, pageable.getPreviousPage());
    assertEquals(0, pageable.getNextPage());
    assertEquals(list, pageable.getListForPage());
  }

  public void testOnePartialPage() {
    pageable = new Pageable(list);
    pageable.setPageSize(50);
    pageable.setPage(1);

    assertEquals(1, pageable.getPage());
    assertEquals(1, pageable.getMaxPages());
    assertEquals(0, pageable.getPreviousPage());
    assertEquals(0, pageable.getNextPage());
    assertEquals(list, pageable.getListForPage());
  }

  public void testTwoFullPages() {
    pageable = new Pageable(list);
    pageable.setPageSize(15);

    pageable.setPage(1);
    assertEquals(1, pageable.getPage());
    assertEquals(2, pageable.getMaxPages());
    assertEquals(0, pageable.getPreviousPage());
    assertEquals(2, pageable.getNextPage());
    assertEquals(list.subList(0, 15), pageable.getListForPage());

    pageable.setPage(2);
    assertEquals(2, pageable.getPage());
    assertEquals(2, pageable.getMaxPages());
    assertEquals(1, pageable.getPreviousPage());
    assertEquals(0, pageable.getNextPage());
    assertEquals(list.subList(15, 30), pageable.getListForPage());
  }

  public void testTwoPartialPages() {
    pageable = new Pageable(list);
    pageable.setPageSize(20);

    pageable.setPage(1);
    assertEquals(1, pageable.getPage());
    assertEquals(2, pageable.getMaxPages());
    assertEquals(0, pageable.getPreviousPage());
    assertEquals(2, pageable.getNextPage());
    assertEquals(list.subList(0, 20), pageable.getListForPage());

    pageable.setPage(2);
    assertEquals(2, pageable.getPage());
    assertEquals(2, pageable.getMaxPages());
    assertEquals(1, pageable.getPreviousPage());
    assertEquals(0, pageable.getNextPage());
    assertEquals(list.subList(20, 30), pageable.getListForPage());
  }

  public void testSetPageOutOfBounds() {
    pageable = new Pageable(list);
    pageable.setPageSize(15);

    pageable.setPage(-1);
    assertEquals(1, pageable.getPage());
    assertEquals(2, pageable.getMaxPages());
    assertEquals(0, pageable.getPreviousPage());
    assertEquals(2, pageable.getNextPage());
    assertEquals(list.subList(0, 15), pageable.getListForPage());

    pageable.setPage(0);
    assertEquals(1, pageable.getPage());
    assertEquals(2, pageable.getMaxPages());
    assertEquals(0, pageable.getPreviousPage());
    assertEquals(2, pageable.getNextPage());
    assertEquals(list.subList(0, 15), pageable.getListForPage());

    pageable.setPage(3);
    assertEquals(2, pageable.getPage());
    assertEquals(2, pageable.getMaxPages());
    assertEquals(1, pageable.getPreviousPage());
    assertEquals(0, pageable.getNextPage());
    assertEquals(list.subList(15, 30), pageable.getListForPage());
  }

  public void testGetMinPageRange() {
    pageable = new Pageable(list);
    pageable.setPageSize(2);

    pageable.setPage(1);
    assertEquals(1, pageable.getMinPageRange());
    pageable.setPage(2);
    assertEquals(1, pageable.getMinPageRange());
    pageable.setPage(9);
    assertEquals(1, pageable.getMinPageRange());
    pageable.setPage(10);
    assertEquals(1, pageable.getMinPageRange());
    pageable.setPage(11);
    assertEquals(1, pageable.getMinPageRange());
    pageable.setPage(12);
    assertEquals(2, pageable.getMinPageRange());
    pageable.setPage(13);
    assertEquals(3, pageable.getMinPageRange());
  }

  public void testGetMaxPageRange() {
    pageable = new Pageable(list);
    pageable.setPageSize(2);

    pageable.setPage(15);
    assertEquals(15, pageable.getMaxPageRange());
    pageable.setPage(14);
    assertEquals(15, pageable.getMaxPageRange());
    pageable.setPage(6);
    assertEquals(15, pageable.getMaxPageRange());
    pageable.setPage(5);
    assertEquals(15, pageable.getMaxPageRange());
    pageable.setPage(4);
    assertEquals(14, pageable.getMaxPageRange());
    pageable.setPage(3);
    assertEquals(13, pageable.getMaxPageRange());
  }

}
