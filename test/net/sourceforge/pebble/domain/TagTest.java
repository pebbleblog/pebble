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

import java.util.List;


/**
 * Tests for the Tag class.
 *
 * @author    Simon Brown
 */
public class TagTest extends SingleBlogTestCase {

  private Tag tag;

  public void setUp() {
    super.setUp();

    tag = new Tag("java", blog);
  }

  public void testConstruction() {
    assertEquals("java", tag.getName());

    assertEquals("http://www.yourdomain.com/blog/tags/java/", tag.getPermalink());
  }

  public void testNameWithSpaces() {
    tag.setName("automated unit testing");
    assertEquals("automatedunittesting", tag.getName());

    assertEquals("http://www.yourdomain.com/blog/tags/automatedunittesting/", tag.getPermalink());
  }

  public void testNameWithMixedCase() {
    tag.setName("automatedUnitTesting");
    assertEquals("automatedunittesting", tag.getName());

    assertEquals("http://www.yourdomain.com/blog/tags/automatedunittesting/", tag.getPermalink());
  }

  public void testAddBlogEntry() {
    assertTrue(tag.getBlogEntries().isEmpty());
    assertEquals(0, tag.getNumberOfBlogEntries());
    DailyBlog today = blog.getBlogForToday();
    BlogEntry blogEntry = today.createBlogEntry();

    tag.addBlogEntry(blogEntry);
    assertTrue(tag.getBlogEntries().contains(blogEntry));
    assertEquals(1, tag.getNumberOfBlogEntries());
  }

  public void testRemoveBlogEntry() {
    DailyBlog today = blog.getBlogForToday();
    BlogEntry blogEntry = today.createBlogEntry();
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
//    DailyBlog today = blog.getBlogForToday();
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

  public void testParse() {
    assertTrue(Tag.parse(blog, null).isEmpty());

    List tags = Tag.parse(blog, "java junit automatedunittesting java");
    assertEquals(3, tags.size());
    assertTrue(tags.contains(blog.getTag("java")));
    assertTrue(tags.contains(blog.getTag("junit")));
    assertTrue(tags.contains(blog.getTag("automated unit testing")));
  }

  public void testEncode() {
    assertEquals("", Tag.encode(null));
    assertEquals("sometag", Tag.encode("sometag"));
    assertEquals("sometag", Tag.encode(" sometag "));
    assertEquals("someothertag", Tag.encode("SomeOtherTag"));
  }

}