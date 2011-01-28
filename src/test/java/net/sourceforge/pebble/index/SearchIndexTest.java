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
import net.sourceforge.pebble.search.SearchResults;

/**
 * Tests for the SearchResults class.
 *
 * @author    Simon Brown
 */
public class SearchIndexTest extends SingleBlogTestCase {

  private SearchIndex index;

  protected void setUp() throws Exception {
    super.setUp();

    index = new SearchIndex(blog);
    index.clear();
  }

  /**
   * Tests that a search can be performed on a blog entry title.
   */
   public void testSearchOnTitle() {
     try {
       SearchResults results = index.search("jsp");
       assertEquals(0, results.getNumberOfHits());

       BlogEntry blogEntry = new BlogEntry(blog);
       blogEntry.setTitle("Some JSP topic");
       blogEntry.setPublished(true);
       index.index(blogEntry);

       results = index.search("jsp");
       assertEquals(1, results.getNumberOfHits());
     } catch (Exception e) {
       e.printStackTrace();
       fail();
     }
   }

   /**
    * Tests that a search can be performed on a blog entry body.
    */
   public void testSearchOnBody() {
     try {
       SearchResults results = index.search("jsp");
       assertEquals(0, results.getNumberOfHits());

       BlogEntry blogEntry = new BlogEntry(blog);
       blogEntry.setBody("Some JSP topic");
       blogEntry.setPublished(true);
       index.index(blogEntry);

       results = index.search("jsp");
       assertEquals(1, results.getNumberOfHits());
     } catch (Exception e) {
       e.printStackTrace();
       fail();
     }
   }

   /**
    * Tests that a search can be performed on a category.
    */
   public void testSearchOnCategory() {
     try {
       blog.addCategory(new Category("/category1", "Category 1"));
       blog.addCategory(new Category("/category2", "Category 2"));

       SearchResults results = index.search("category:/category1");
       assertEquals(0, results.getNumberOfHits());

       BlogEntry blogEntry = new BlogEntry(blog);
       blogEntry.addCategory(blog.getCategory("/category1"));
       blogEntry.setPublished(true);
       index.index(blogEntry);

       results = index.search("category:/category1");
       assertEquals(1, results.getNumberOfHits());

       // and add another category
       blogEntry.addCategory(blog.getCategory("/category2"));
       index.index(blogEntry);

       results = index.search("category:/category1");
       assertEquals(1, results.getNumberOfHits());
       results = index.search("category:/category2");
       assertEquals(1, results.getNumberOfHits());
     } catch (Exception e) {
       e.printStackTrace();
       fail();
     }
   }

   /**
    * Tests that searches work across comments.
    */
   public void testSearchOnComments() {
     try {
       SearchResults results = index.search("swing");
       assertEquals(0, results.getNumberOfHits());

       BlogEntry blogEntry = new BlogEntry(blog);
       blogEntry.addCategory(blog.getCategory("/category1"));
       blogEntry.setPublished(true);
       index.index(blogEntry);

       // should be no hits returned yet
       results = index.search("swing");
       assertEquals(0, results.getNumberOfHits());

       // now add some comments
     Comment comment = blogEntry.createComment("Comment title", "Comment body", "Some author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
       blogEntry.addComment(comment);
       index.index(blogEntry);
       results = index.search("swing");
       assertEquals(0, results.getNumberOfHits());

       comment = blogEntry.createComment("Comment title", "Comment body with Swing in it", "Some author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
       blogEntry.addComment(comment);
       index.index(blogEntry);
       results = index.search("swing");
       assertEquals(1, results.getNumberOfHits());
     } catch (Exception e) {
       e.printStackTrace();
       fail();
     }
   }

   /**
    * Tests that blog entries can be removed from the index.
    */
   public void testSearchOnRemovedBlogEntry() {
     try {
       BlogEntry blogEntry = new BlogEntry(blog);
       blogEntry.setTitle("Some JSP topic");
       blogEntry.setPublished(true);
       index.index(blogEntry);

       SearchResults results = index.search("jsp");
       assertEquals(1, results.getNumberOfHits());

       // and now remove the blog entry, checking that the search no longer
       // returns that hit
       index.unindex(blogEntry);
       results = index.search("jsp");
       assertEquals(0, results.getNumberOfHits());
     } catch (Exception e) {
       e.printStackTrace();
       fail();
     }
   }

}
