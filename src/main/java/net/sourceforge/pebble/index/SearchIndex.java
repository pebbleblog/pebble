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
import net.sourceforge.pebble.search.SearchException;
import net.sourceforge.pebble.search.SearchHit;
import net.sourceforge.pebble.search.SearchResults;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Collection;

/**
 * Wraps up the functionality to index blog entries. This is really just
 * a convenient wrapper around Lucene.
 *
 * @author    Simon Brown
 */
public class SearchIndex {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(SearchIndex.class);

  private final Blog blog;

  public SearchIndex(Blog blog) {
    this.blog = blog;
  }

  /**
   * Clears the index.
   */
  public void clear() {
    File searchDirectory = new File(blog.getSearchIndexDirectory());
    if (!searchDirectory.exists()) {
      searchDirectory.mkdirs();
    }

    synchronized (blog) {
      try {
        Analyzer analyzer = getAnalyzer();
        IndexWriter writer = new IndexWriter(searchDirectory, analyzer, true);
        writer.close();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  /**
   * Allows a collection of blog entries to be indexed.
   */
  public void indexBlogEntries(Collection<BlogEntry> blogEntries) {
    synchronized (blog) {
      try {
        Analyzer analyzer = getAnalyzer();
        IndexWriter writer = new IndexWriter(blog.getSearchIndexDirectory(), analyzer, false);

        for (BlogEntry blogEntry : blogEntries) {
          index(blogEntry, writer);
        }

        writer.close();
      } catch (Exception e) {
        log.error(e.getMessage(), e);
      }
    }
  }

  /**
   * Allows a collection of static pages to be indexed.
   */
  public void indexStaticPages(Collection<StaticPage> staticPages) {
    synchronized (blog) {
      try {
        Analyzer analyzer = getAnalyzer();
        IndexWriter writer = new IndexWriter(blog.getSearchIndexDirectory(), analyzer, false);

        for (StaticPage staticPage : staticPages) {
          index(staticPage, writer);
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
      synchronized (blog) {
        // first delete the blog entry from the index (if it was there)
        unindex(blogEntry);

        Analyzer analyzer = getAnalyzer();
        IndexWriter writer = new IndexWriter(blog.getSearchIndexDirectory(), analyzer, false);
        index(blogEntry, writer);
        writer.close();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * Allows a single static page to be (re)indexed. If the page is already
   * indexed, this method deletes the previous index before adding the new
   * one.
   *
   * @param staticPage    the StaticPage instance to index
   */
  public void index(StaticPage staticPage) {
    try {
      synchronized (blog) {
        // first delete the static page from the index (if it was there)
        unindex(staticPage);

        Analyzer analyzer = getAnalyzer();
        IndexWriter writer = new IndexWriter(blog.getSearchIndexDirectory(), analyzer, false);
        index(staticPage, writer);
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
  private Analyzer getAnalyzer() throws Exception {
    Class c = Class.forName(blog.getLuceneAnalyzer());
    return (Analyzer)c.newInstance();
  }

  /**
   * Removes the index for a single blog entry to be removed.
   *
   * @param blogEntry   the BlogEntry instance to be removed
   */
  public void unindex(BlogEntry blogEntry) {
    try {
      synchronized (blog) {
        log.debug("Attempting to delete index for " + blogEntry.getTitle());
        IndexReader reader = IndexReader.open(blog.getSearchIndexDirectory());
        Term term = new Term("id", blogEntry.getId());
        log.debug("Deleted " + reader.delete(term) + " document(s) from the index");
        reader.close();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * Removes the index for a single blog entry to be removed.
   *
   * @param staticPage    the StaticPage instance to be removed
   */
  public void unindex(StaticPage staticPage) {
    try {
      synchronized (blog) {
        log.debug("Attempting to delete index for " + staticPage.getTitle());
        IndexReader reader = IndexReader.open(blog.getSearchIndexDirectory());
        Term term = new Term("id", staticPage.getId());
        log.debug("Deleted " + reader.delete(term) + " document(s) from the index");
        reader.close();
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  /**
   * Helper method to index an individual blog entry.
   *
   * @param blogEntry   the BlogEntry instance to index
   * @param writer      the IndexWriter to index with
   */
  private void index(BlogEntry blogEntry, IndexWriter writer) {
    if (!blogEntry.isPublished()) {
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
      if (blogEntry.getSubtitle() != null) {
        document.add(Field.Text("subtitle", blogEntry.getSubtitle()));
      } else {
        document.add(Field.Text("subtitle", ""));
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

      for (Category category : blogEntry.getCategories()) {
        document.add(Field.Text("category", category.getId()));
      }

      for (Tag tag : blogEntry.getAllTags()) {
        document.add(Field.Text("tag", tag.getName()));
      }

      searchableContent.append(" ");
      Iterator it = blogEntry.getComments().iterator();
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

      // join the title and body together to make searching on them both easier
      document.add(Field.UnStored("blogEntry", searchableContent.toString()));

      writer.addDocument(document);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }
  /**
   * Helper method to index an individual blog entry.
   *
   * @param staticPage    the Page instance instance to index
   * @param writer      the IndexWriter to index with
   */
  private void index(StaticPage staticPage, IndexWriter writer) {
    try {
      log.debug("Indexing " + staticPage.getTitle());
      Document document = new Document();
      document.add(Field.Keyword("id", staticPage.getId()));
      if (staticPage.getTitle() != null) {
        document.add(Field.Text("title", staticPage.getTitle()));
      } else {
        document.add(Field.Text("title", ""));
      }
      document.add(Field.Keyword("permalink", staticPage.getPermalink()));
      document.add(Field.UnIndexed("date", DateField.dateToString(staticPage.getDate())));
      if (staticPage.getBody() != null) {
        document.add(Field.UnStored("body", staticPage.getBody()));
      } else {
        document.add(Field.UnStored("body", ""));
      }
      if (staticPage.getTruncatedContent() != null) {
        document.add(Field.Text("truncatedBody", staticPage.getTruncatedContent()));
      } else {
        document.add(Field.Text("truncatedBody", ""));
      }

      if (staticPage.getAuthor() != null) {
        document.add(Field.Text("author", staticPage.getAuthor()));
      }

      // build up one large string with all searchable content
      // i.e. entry title, entry body and all response bodies
      StringBuffer searchableContent = new StringBuffer();
      searchableContent.append(staticPage.getTitle());
      searchableContent.append(" ");
      searchableContent.append(staticPage.getBody());

      // join the title and body together to make searching on them both easier
      document.add(Field.UnStored("blogEntry", searchableContent.toString()));

      writer.addDocument(document);
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
  }

  public SearchResults search(String queryString) throws SearchException {

    log.debug("Performing search : " + queryString);

    SearchResults searchResults = new SearchResults();
    searchResults.setQuery(queryString);

    if (queryString != null && queryString.length() > 0) {
      Searcher searcher = null;

      try {
        searcher = new IndexSearcher(blog.getSearchIndexDirectory());
        Query query = QueryParser.parse(queryString, "blogEntry", getAnalyzer());
        Hits hits = searcher.search(query);

        for (int i = 0; i < hits.length(); i++) {
          Document doc = hits.doc(i);
          SearchHit result = new SearchHit(
              blog,
              doc.get("id"),
              doc.get("permalink"),
              doc.get("title"),
              doc.get("subtitle"),
              doc.get("truncatedBody"),
              DateField.stringToDate(doc.get("date")),
              hits.score(i));
          searchResults.add(result);
        }
      } catch (ParseException pe) {
        pe.printStackTrace();
        searchResults.setMessage("Sorry, but there was an error. Please try another search");
      } catch (Exception e) {
        e.printStackTrace();
        throw new SearchException(e.getMessage());
      } finally {
        if (searcher != null) {
          try {
            searcher.close();
          } catch (IOException e) {
            // can't do much now! ;-)
          }
        }
      }
    }

    return searchResults;
  }

}

