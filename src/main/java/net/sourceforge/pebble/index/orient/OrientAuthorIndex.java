package net.sourceforge.pebble.index.orient;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.orientechnologies.orient.core.db.object.ODatabaseObjectTx;
import com.orientechnologies.orient.core.record.impl.ODocument;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import net.sourceforge.pebble.dao.orient.OrientDAOFactory;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.index.AuthorIndex;

import java.util.*;

/**
 * Index for authors
 */
public class OrientAuthorIndex implements AuthorIndex {
  private final OrientDAOFactory daoFactory;
  private final Blog blog;

  public OrientAuthorIndex(OrientDAOFactory daoFactory, Blog blog) {
    this.daoFactory = daoFactory;
    this.blog = blog;
  }

  public void clear() {
    // Handled by OrientDB
  }

  public void index(Collection<BlogEntry> blogEntries) {
    // Handled by OrientDB
  }

  public void index(BlogEntry blogEntry) {
    // Handled by OrientDB
  }

  public void unindex(BlogEntry blogEntry) {
    // Handled by OrientDB
  }

  public Collection<String> getAuthors() {
    // This implementation could return authors with only unpublished posts
    ODatabaseObjectTx db = daoFactory.getDb(blog);
    try {
      List<ODocument> authors = db.command(new OCommandSQL("select key from index:OrientBlogEntry.author")).execute();
      return Collections.unmodifiableCollection(Lists.transform(authors, new Function<ODocument, String>() {
        public String apply(ODocument input) {
          return input.field("key");
        }
      }));
    } finally {
      db.close();
    }
  }

  public List<String> getRecentBlogEntries(String username) {
    ODatabaseObjectTx db = daoFactory.getDb(blog);
    try {
      List<ODocument> results = db.query(new OSQLSynchQuery<ODocument>(
          "select id from OrientBlogEntry where author = ? " +
              "and state = 'published' order by date desc"), username);
      return Lists.transform(results, new Function<ODocument, String>() {
        public String apply(ODocument document) {
          return document.field("id");
        }
      });
    } finally {
      db.close();
    }
  }
}
