package net.sourceforge.pebble.dao.orient;

import com.orientechnologies.orient.core.db.object.ODatabaseObjectTx;
import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import net.sourceforge.pebble.dao.BlogEntryDAO;
import net.sourceforge.pebble.dao.PersistenceException;
import net.sourceforge.pebble.dao.orient.model.OrientBlogEntry;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Blog entry DAO for OrientDB
 */
public class OrientBlogEntryDAO implements BlogEntryDAO {
  private final OrientDAOFactory factory;

  public OrientBlogEntryDAO(OrientDAOFactory factory) {
    this.factory = factory;
  }

  public BlogEntry loadBlogEntry(Blog blog, String blogEntryId) throws PersistenceException {
    ODatabaseObjectTx db = factory.getDb(blog);
    try {
      OrientBlogEntry blogEntry = loadBlogEntry(db, blog, blogEntryId);
      if (blogEntry != null) {
        return blogEntry.toBlogEntry(blog);
      } else {
        return null;
      }
    } finally {
      db.close();
    }
  }

  public Collection<BlogEntry> loadBlogEntries(Blog blog) throws PersistenceException {
    ODatabaseObjectTx db = factory.getDb(blog);
    try {
      Collection<BlogEntry> entries = new ArrayList<BlogEntry>();
      for (OrientBlogEntry entry : db.browseClass(OrientBlogEntry.class)) {
        entries.add(entry.toBlogEntry(blog));
      }
      return entries;
    } finally {
      db.close();
    }

  }

  public void storeBlogEntry(BlogEntry blogEntry) throws PersistenceException {
    ODatabaseObjectTx db = factory.getDb(blogEntry.getBlog());
    try {
      OrientBlogEntry existingBlogEntry = loadBlogEntry(db, blogEntry.getBlog(), blogEntry.getId());
      if (existingBlogEntry != null) {
        existingBlogEntry.populateFrom(blogEntry);
        db.save(existingBlogEntry);
      } else {
        db.save(new OrientBlogEntry(blogEntry));
      }
    } finally {
      db.close();
    }
  }

  public void removeBlogEntry(BlogEntry blogEntry) throws PersistenceException {
    ODatabaseObjectTx db = factory.getDb(blogEntry.getBlog());
    try {
      OrientBlogEntry existingBlogEntry = loadBlogEntry(db, blogEntry.getBlog(), blogEntry.getId());
      if (existingBlogEntry != null) {
        db.delete(existingBlogEntry);
      }
    } finally {
      db.close();
    }
  }

  private OrientBlogEntry loadBlogEntry(ODatabaseObjectTx db, Blog blog, String id) throws PersistenceException {
    List<OrientBlogEntry> results = db.query(new OSQLSynchQuery<OrientBlogEntry>("select * from OrientBlogEntry where id = ?"), id);
    if (results.isEmpty()) {
      return null;
    } else if (results.size() > 1) {
      throw new PersistenceException("Non unique blog entry ID found " + id);
    } else {
      return results.get(0);
    }
  }

}
