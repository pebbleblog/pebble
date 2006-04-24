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

import net.sourceforge.pebble.domain.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.util.Iterator;

/**
 * Wraps up the functionality to index blog entries. This is really just
 * a convenient wrapper around Lucene.
 *
 * @author    Simon Brown
 */
public class BlogIndexer {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(BlogIndexer.class);

  /**
   * Allows an entire root blog to be indexed. In other words, this indexes
   * all of the blog entries within the specified root blog.
   *
   * @param rootBlog    the Blog instance to index
   */
  public void index(Blog rootBlog) {
    synchronized (rootBlog) {
      try {
        log.debug("Creating index for all blog entries in " + rootBlog.getName());
        Analyzer analyzer = getAnalyzer(rootBlog);
        IndexWriter writer = new IndexWriter(rootBlog.getIndexDirectory(), analyzer, true);

        Iterator it = rootBlog.getBlogEntries().iterator();
        while (it.hasNext()) {
          BlogEntry blogEntry = (BlogEntry)it.next();
          index(blogEntry, writer);
        }

        log.debug("Creating index for all static pages in " + rootBlog.getName());
        it = rootBlog.getStaticPages().iterator();
        while (it.hasNext()) {
          BlogEntry blogEntry = (BlogEntry)it.next();
          index(blogEntry, writer);
        }

        writer.close();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  /**
   * Allows a single blog entry to be (re)indexed. If the entry is already
   * indexed, this method deletes the previous index before adding the new
   * one.
   *
   * @param blogEntry   the BlogEntry instance to index
   */
  public void index(BlogEntry blogEntry) {
    try {
      Blog rootBlog = blogEntry.getBlog();
      synchronized (blogEntry) {
        Analyzer analyzer = getAnalyzer(rootBlog);

        // first delete the blog entry from the index (if it was there)
        removeIndex(blogEntry);

        IndexWriter writer = new IndexWriter(rootBlog.getIndexDirectory(), analyzer, false);
        index(blogEntry, writer);
        writer.close();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * Gets the Analyzer implementation to use.
   *
   * @return  an Analyzer instance
   * @throws Exception
   */
  private Analyzer getAnalyzer(Blog blog) throws Exception {
    Class c = Class.forName(blog.getLuceneAnalyzer());
    return (Analyzer)c.newInstance();
  }

  /**
   * Removes the index for a single blog entry to be removed.
   *
   * @param blogEntry   the BlogEntry instance to be removed
   */
  public void removeIndex(BlogEntry blogEntry) {
    try {
      Blog rootBlog = blogEntry.getBlog();
      synchronized (blogEntry) {
        log.debug("Attempting to delete index for " + blogEntry.getTitle());
        IndexReader reader = IndexReader.open(rootBlog.getIndexDirectory());
        Term term = new Term("id", blogEntry.getId());
        log.debug("Deleted " + reader.delete(term) + " document(s) from the index");
        reader.close();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * Helper method to index an individual blog entry. This assumes that the
   * root blog has already been indexed.
   *
   * @param blogEntry   the BlogEntry instance to index
   * @param writer      the IndexWriter to index with
   */
  private void index(BlogEntry blogEntry, IndexWriter writer) {
    if (!blogEntry.isApproved()) {
      return;
    }

    try {
      log.debug("Indexing " + blogEntry.getTitle());
      Document document = new Document();
      document.add(Field.Keyword("id", blogEntry.getId()));
      if (blogEntry.getTitle() != null) {
        document.add(Field.Text("title", blogEntry.getTitle()));
      } else {
        document.add(Field.Text("title", ""));
      }
      document.add(Field.Keyword("permalink", blogEntry.getPermalink()));
      document.add(Field.UnIndexed("date", DateField.dateToString(blogEntry.getDate())));
      if (blogEntry.getBody() != null) {
        document.add(Field.UnStored("body", blogEntry.getBody()));
      } else {
        document.add(Field.UnStored("body", ""));
      }
      if (blogEntry.getTruncatedContent() != null) {
        document.add(Field.Text("truncatedBody", blogEntry.getTruncatedContent()));
      } else {
        document.add(Field.Text("truncatedBody", ""));
      }

      if (blogEntry.getAuthor() != null) {
        document.add(Field.Text("author", blogEntry.getAuthor()));
      }

      // build up one large string with all searchable content
      // i.e. entry title, entry body and all response bodies
      StringBuffer searchableContent = new StringBuffer();
      searchableContent.append(blogEntry.getTitle());
      searchableContent.append(" ");
      searchableContent.append(blogEntry.getBody());

      if (!blogEntry.isStaticPage()) {
        Iterator it = blogEntry.getCategories().iterator();
        Category category;
        while (it.hasNext()) {
          category = (Category)it.next();
          document.add(Field.Text("category", category.getName()));
        }

        Iterator tags = blogEntry.getAllTags().iterator();
        while (tags.hasNext()) {
          document.add(Field.Text("tag", ((Tag)tags.next()).getName()));
        }

        searchableContent.append(" ");
        it = blogEntry.getComments().iterator();
        while (it.hasNext()) {
          Comment comment = (Comment)it.next();
          if (comment.isApproved()) {
            searchableContent.append(comment.getBody());
            searchableContent.append(" ");
          }
        }
        it = blogEntry.getTrackBacks().iterator();
        while (it.hasNext()) {
          TrackBack trackBack = (TrackBack)it.next();
          if (trackBack.isApproved()) {
            searchableContent.append(trackBack.getExcerpt());
            searchableContent.append(" ");
          }
        }
      }

      // join the title and body together to make searching on them both easier
      document.add(Field.UnStored("blogEntry", searchableContent.toString()));

      writer.addDocument(document);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

}

