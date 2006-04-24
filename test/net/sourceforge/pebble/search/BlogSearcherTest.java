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
package net.sourceforge.pebble.search;

import net.sourceforge.pebble.domain.SingleBlogTestCase;

/**
 * Tests for the SearchResults class.
 *
 * @author    Simon Brown
 */
public class BlogSearcherTest extends SingleBlogTestCase {

  public void testSomething() {

  }

  /**
   * Tests that a search can be performed on a blog entry title.
   public void testSearchOnTitle() {
   try {
   BlogIndexer indexer = new BlogIndexer();
   indexer.index(blog);
   BlogSearcher searcher = new BlogSearcher();

   // create and store a new entry, which should add it to the index
   // this will be used to test that titles are searchable
   SearchResults results = searcher.search(blog, "jsp");
   assertEquals(0, results.getNumberOfHits());
   blogEntry = dailyBlog.createBlogEntry("Some JSP topic", "Here is some text");
   dailyBlog.addEntry(blogEntry);
   blogEntry.store();
   results = searcher.search(blog, "jsp");
   assertEquals(1, results.getNumberOfHits());
   } catch (Exception e) {
   e.printStackTrace();
   fail();
   }
   }

   /**
   * Tests that a search can be performed on a blog entry body.
   public void testSearchOnBody() {
   try {
   BlogIndexer indexer = new BlogIndexer();
   indexer.index(blog);
   BlogSearcher searcher = new BlogSearcher();

   // create and store a new entry, which should add it to the index
   // this will be used to test that bodies are searchable
   SearchResults results = searcher.search(blog, "intellij");
   assertEquals(0, results.getNumberOfHits());
   blogEntry = dailyBlog.createBlogEntry("Some other topic", "IntelliJ is great...");
   dailyBlog.addEntry(blogEntry);
   blogEntry.store();
   results = searcher.search(blog, "intellij");
   assertEquals(1, results.getNumberOfHits());
   } catch (Exception e) {
   e.printStackTrace();
   fail();
   }
   }

   /**
   * Tests that a search can be performed on a category.
   public void testSearchOnCategory() {
   try {
   BlogIndexer indexer = new BlogIndexer();
   indexer.index(blog);
   BlogSearcher searcher = new BlogSearcher();

   // create and store a new entry, which should add it to the index
   // this will be used to test that bodies are searchable
   SearchResults results = searcher.search(blog, "category:\"Test Category 1\"");
   assertEquals(0, results.getNumberOfHits());
   blogEntry = dailyBlog.createBlogEntry("Some topic", "blah blah blah...");
   blogEntry.addCategory(categoryManager.getCategory("testCategory1"));
   dailyBlog.addEntry(blogEntry);
   blogEntry.store();
   results = searcher.search(blog, "category:\"Test Category 1\"");
   assertEquals(1, results.getNumberOfHits());

   // and add another category
   blogEntry.addCategory(categoryManager.getCategory("testCategory2"));
   blogEntry.store();
   results = searcher.search(blog, "category:\"Test Category 1\"");
   assertEquals(1, results.getNumberOfHits());
   results = searcher.search(blog, "category:\"Test Category 2\"");
   assertEquals(1, results.getNumberOfHits());
   } catch (Exception e) {
   e.printStackTrace();
   fail();
   }
   }

   /**
   * Tests that a search can be performed on a blog entry title.
   public void testSearchOnComments() {
   try {
   BlogIndexer indexer = new BlogIndexer();
   indexer.index(blog);
   BlogSearcher searcher = new BlogSearcher();

   // create and store a new entry, which should add it to the index
   // this will be used to test that bodies are searchable
   SearchResults results = searcher.search(blog, "swing");
   assertEquals(0, results.getNumberOfHits());
   blogEntry = dailyBlog.createBlogEntry("Some other topic", "IntelliJ is great...");
   dailyBlog.addEntry(blogEntry);
   blogEntry.store();

   // should be no hits returned yet
   results = searcher.search(blog, "swing");
   assertEquals(0, results.getNumberOfHits());

   // now add some comments
   blogComment = blogEntry.createComment("Comment body", "Some author", "me@somedomain.com", "http://www.google.com");
   blogEntry.addComment(blogComment);
   blogEntry.store();
   results = searcher.search(blog, "swing");
   assertEquals(0, results.getNumberOfHits());
   blogComment = blogEntry.createComment("And it's written in Swing!", "Some author", "me@somedomain.com", "http://www.google.com");
   blogEntry.addComment(blogComment);
   blogEntry.store();
   results = searcher.search(blog, "swing");
   assertEquals(1, results.getNumberOfHits());
   } catch (Exception e) {
   e.printStackTrace();
   fail();
   }
   }

   /**
   * Tests that a search can be performed on a blog entry title.
   public void testSearchOnRemovedBlogEntry() {
   try {
   BlogIndexer indexer = new BlogIndexer();
   indexer.index(blog);
   BlogSearcher searcher = new BlogSearcher();

   // create and store a new entry, which should add it to the index
   // this will be used to test that titles are searchable
   blogEntry = dailyBlog.createBlogEntry("Some JSP topic", "Here is some text");
   dailyBlog.addEntry(blogEntry);
   blogEntry.store();
   SearchResults results = searcher.search(blog, "jsp");
   assertEquals(1, results.getNumberOfHits());

   // and now remove the blog entry, checking that the search no longer
   // returns that hit
   dailyBlog.removeEntry(blogEntry);
   blogEntry.remove();
   results = searcher.search(blog, "jsp");
   assertEquals(0, results.getNumberOfHits());
   } catch (Exception e) {
   e.printStackTrace();
   fail();
   }
   }

   */

}
