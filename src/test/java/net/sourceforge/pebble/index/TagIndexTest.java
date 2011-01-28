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

import net.sourceforge.pebble.domain.*;

/**
 * Tests for the TagIndex class.
 *
 * @author    Simon Brown
 */
public class TagIndexTest extends SingleBlogTestCase {

  private TagIndex index;

  protected void setUp() throws Exception {
    super.setUp();

    this.index = new TagIndex(blog);
  }

  /**
   * Tests that a single blog entry can be indexed.
   */
  public void testIndexBlogEntry() throws Exception {
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setTags("junit");
    blogEntry.setPublished(true);
    index.index(blogEntry);

    assertEquals(1, index.getTag("junit").getNumberOfBlogEntries());
    assertTrue(index.getTags().contains(new Tag("junit", blog)));
    assertEquals(1, index.getTags().size());
  }

  /**
   * Tests that a single blog entry can be unindexed.
   */
  public void testUnindexBlogEntry() throws Exception {
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setTags("junit");
    index.index(blogEntry);

    blogEntry.setTags("");
    index.unindex(blogEntry);

    assertEquals(0, index.getTag("junit").getNumberOfBlogEntries());
    assertEquals(0, index.getTags().size());
  }

//  /**
//   * Tests that category/tag statistics are updated.
//   */
//  public void testTagStatisticsUpdatedWithCategories() throws Exception {
//    CategoryBuilder builder = new CategoryBuilder(blog);
//    Category java = new Category("/java", "Java");
//    builder.addCategory(java);
//    java.setTags("java");
//    Category apple = new Category("/apple", "Apple");
//    builder.addCategory(apple);
//    apple.setTags("apple");
//    Category root = builder.getRootCategory();
//    root.setTags("myblog");
//    blog.setRootCategory(root);
//
//    BlogEntry blogEntry = new BlogEntry(blog);
//    blogEntry.addCategory(java);
//    blogEntry.setTags("junit");
//
//    assertEquals(0, index.getTag("myblog").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("java").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("apple").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("junit").getNumberOfBlogEntries());
//    assertTrue(index.getTags().isEmpty());
//
//    // blog entry added
//    BlogService service = new BlogService();
//    service.putBlogEntry(blogEntry);
//
//    assertEquals(1, index.getTag("myblog").getNumberOfBlogEntries());
//    assertEquals(1, index.getTag("java").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("apple").getNumberOfBlogEntries());
//    assertEquals(1, index.getTag("junit").getNumberOfBlogEntries());
//    assertFalse(index.getTags().contains(index.getTag("apple")));
//
//    // blog entry changed - category added
//    blogEntry.addCategory(apple);
//    service.putBlogEntry(blogEntry);
//    assertEquals(1, index.getTag("myblog").getNumberOfBlogEntries());
//    assertEquals(1, index.getTag("java").getNumberOfBlogEntries());
//    assertEquals(1, index.getTag("apple").getNumberOfBlogEntries());
//    assertEquals(1, index.getTag("junit").getNumberOfBlogEntries());
//
//    // blog entry changed - category changed
//    blogEntry.removeAllCategories();
//    blogEntry.addCategory(apple);
//    service.putBlogEntry(blogEntry);
//    assertEquals(1, index.getTag("myblog").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("java").getNumberOfBlogEntries());
//    assertEquals(1, index.getTag("apple").getNumberOfBlogEntries());
//    assertEquals(1, index.getTag("junit").getNumberOfBlogEntries());
//    assertFalse(index.getTags().contains(index.getTag("java")));
//
//    // blog entry changed - tag removed
//    blogEntry.setTags("");
//    service.putBlogEntry(blogEntry);
//    assertEquals(1, index.getTag("myblog").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("java").getNumberOfBlogEntries());
//    assertEquals(1, index.getTag("apple").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("junit").getNumberOfBlogEntries());
//    assertFalse(index.getTags().contains(index.getTag("java")));
//    assertFalse(index.getTags().contains(index.getTag("junit")));
//
//    // blog entry changed - tag added
//    blogEntry.setTags("junit");
//    service.putBlogEntry(blogEntry);
//    assertEquals(1, index.getTag("myblog").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("java").getNumberOfBlogEntries());
//    assertEquals(1, index.getTag("apple").getNumberOfBlogEntries());
//    assertEquals(1, index.getTag("junit").getNumberOfBlogEntries());
//    assertFalse(index.getTags().contains(index.getTag("java")));
//
//    // blog entry rejected
//    blogEntry.setRejected();
//    service.putBlogEntry(blogEntry);
//    assertEquals(0, index.getTag("myblog").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("java").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("apple").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("junit").getNumberOfBlogEntries());
//    assertTrue(index.getTags().isEmpty());
//
//    // blog entry approved
//    blogEntry.setApproved();
//    service.putBlogEntry(blogEntry);
//    assertEquals(1, index.getTag("myblog").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("java").getNumberOfBlogEntries());
//    assertEquals(1, index.getTag("apple").getNumberOfBlogEntries());
//    assertEquals(1, index.getTag("junit").getNumberOfBlogEntries());
//    assertFalse(index.getTags().contains(index.getTag("java")));
//
//    // blog entry removed
//    service.removeBlogEntry(blogEntry);
//    assertEquals(0, index.getTag("myblog").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("java").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("apple").getNumberOfBlogEntries());
//    assertEquals(0, index.getTag("junit").getNumberOfBlogEntries());
//    assertTrue(index.getTags().isEmpty());
//  }
//
}