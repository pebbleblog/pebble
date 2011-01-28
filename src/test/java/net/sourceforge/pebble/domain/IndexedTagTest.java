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

import net.sourceforge.pebble.index.IndexedTag;


/**
 * Tests for the IndexedTag class.
 *
 * @author    Simon Brown
 */
public class IndexedTagTest extends SingleBlogTestCase {

  private IndexedTag tag;

  protected void setUp() throws Exception {
    super.setUp();

    tag = new IndexedTag("java", blog);
  }

  public void testAddBlogEntry() {
    assertTrue(tag.getBlogEntries().isEmpty());
    assertEquals(0, tag.getNumberOfBlogEntries());
    String blogEntry = "1234567890";

    tag.addBlogEntry(blogEntry);
    assertTrue(tag.getBlogEntries().contains(blogEntry));
    assertEquals(1, tag.getNumberOfBlogEntries());
  }

  public void testRemoveBlogEntry() {
    String blogEntry = "1234567890";
    tag.addBlogEntry(blogEntry);
    assertTrue(tag.getBlogEntries().contains(blogEntry));
    assertEquals(1, tag.getNumberOfBlogEntries());

    tag.removeBlogEntry(blogEntry);
    assertTrue(tag.getBlogEntries().isEmpty());
    assertEquals(0, tag.getNumberOfBlogEntries());
  }

//  public void testCalculateRanking() {
//    tag.calculateRank(0);
//    assertEquals(1, tag.getRank());
//
//    tag.calculateRank(10);
//    assertEquals(1, tag.getRank());
//
//    Day today = blog.getBlogForToday();
//    BlogEntry blogEntry = today.createBlogEntry();
//    today.addEntry(blogEntry);
//    tag.addBlogEntry(blogEntry);
//    tag.calculateRank(10);
//    assertEquals(1, tag.getRank());
//
//    blogEntry = today.createBlogEntry();
//    today.addEntry(blogEntry);
//    tag.addBlogEntry(blogEntry);
//    tag.calculateRank(10);
//    assertEquals(2, tag.getRank());
//    tag.calculateRank(5);
//    assertEquals(4, tag.getRank());
//
//    blogEntry = today.createBlogEntry();
//    today.addEntry(blogEntry);
//    tag.addBlogEntry(blogEntry);
//    tag.calculateRank(10);
//    assertEquals(3, tag.getRank());
//    tag.calculateRank(5);
//    assertEquals(6, tag.getRank());
//
//    blogEntry = today.createBlogEntry();
//    today.addEntry(blogEntry);
//    tag.addBlogEntry(blogEntry);
//    tag.calculateRank(10);
//    assertEquals(4, tag.getRank());
//    tag.calculateRank(5);
//    assertEquals(8, tag.getRank());
//
//    blogEntry = today.createBlogEntry();
//    today.addEntry(blogEntry);
//    tag.addBlogEntry(blogEntry);
//    tag.calculateRank(10);
//    assertEquals(5, tag.getRank());
//    tag.calculateRank(5);
//    assertEquals(10, tag.getRank());
//
//    blogEntry = today.createBlogEntry();
//    today.addEntry(blogEntry);
//    tag.addBlogEntry(blogEntry);
//    tag.calculateRank(10);
//    assertEquals(6, tag.getRank());
//
//    blogEntry = today.createBlogEntry();
//    today.addEntry(blogEntry);
//    tag.addBlogEntry(blogEntry);
//    tag.calculateRank(10);
//    assertEquals(7, tag.getRank());
//
//    blogEntry = today.createBlogEntry();
//    today.addEntry(blogEntry);
//    tag.addBlogEntry(blogEntry);
//    tag.calculateRank(10);
//    assertEquals(8, tag.getRank());
//
//    blogEntry = today.createBlogEntry();
//    today.addEntry(blogEntry);
//    tag.addBlogEntry(blogEntry);
//    tag.calculateRank(10);
//    assertEquals(9, tag.getRank());
//
//    blogEntry = today.createBlogEntry();
//    today.addEntry(blogEntry);
//    tag.addBlogEntry(blogEntry);
//    tag.calculateRank(10);
//    assertEquals(10, tag.getRank());
//
//    blogEntry = today.createBlogEntry();
//    today.addEntry(blogEntry);
//    tag.addBlogEntry(blogEntry);
//    tag.calculateRank(10);
//    assertEquals(10, tag.getRank());
//  }

}