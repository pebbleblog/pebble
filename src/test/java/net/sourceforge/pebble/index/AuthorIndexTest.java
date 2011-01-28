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

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.SingleBlogTestCase;

/**
 * Tests for the AuthorIndex class.
 *
 * @author    Simon Brown
 */
public class AuthorIndexTest extends SingleBlogTestCase {

  private AuthorIndex index;

  protected void setUp() throws Exception {
    super.setUp();

    this.index = new AuthorIndex(blog);
  }

  /**
   * Tests that a single published blog entry can be indexed.
   */
  public void testIndexPublishedBlogEntry() throws Exception {
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setAuthor("sbrown");
    blogEntry.setPublished(true);
    index.index(blogEntry);

    assertTrue(index.getAuthors().contains("sbrown"));
    assertEquals(1, index.getRecentBlogEntries("sbrown").size());
    assertEquals(blogEntry.getId(), index.getRecentBlogEntries("sbrown").get(0));
  }

  /**
   * Tests that a single unpublished blog entry isn't indexed.
   */
  public void testIndexUnpublishedBlogEntry() throws Exception {
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setAuthor("sbrown");
    blogEntry.setPublished(false);
    index.index(blogEntry);

    assertFalse(index.getAuthors().contains("sbrown"));
    assertEquals(0, index.getRecentBlogEntries("sbrown").size());
  }

  /**
   * Tests that a single blog entry can be unindexed.
   */
  public void testUnindexBlogEntry() throws Exception {
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setAuthor("sbrown");
    blogEntry.setPublished(true);
    index.index(blogEntry);

    assertTrue(index.getAuthors().contains("sbrown"));
    assertEquals(1, index.getRecentBlogEntries("sbrown").size());
    assertEquals(blogEntry.getId(), index.getRecentBlogEntries("sbrown").get(0));

    index.unindex(blogEntry);

    assertFalse(index.getAuthors().contains("sbrown"));
    assertEquals(0, index.getRecentBlogEntries("sbrown").size());
  }

}
