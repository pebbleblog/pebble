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
import net.sourceforge.pebble.domain.Tag;
import net.sourceforge.pebble.index.TagIndex;
import net.sourceforge.pebble.util.TagRanker;

import java.util.*;

/**
 * Tag index implemented using OrientDB.
 */
public class OrientTagIndex implements TagIndex {
  private final Blog blog;
  private final OrientDAOFactory daoFactory;

  public OrientTagIndex(OrientDAOFactory daoFactory, Blog blog) {
    this.blog = blog;
    this.daoFactory = daoFactory;
  }

  public void clear() {
    // Do nothing, index is maintained by orient
  }

  public void index(Collection<BlogEntry> blogEntries) {
    // Do nothing, index is maintained by orient
  }

  public void index(BlogEntry blogEntry) {
    // Do nothing, index is maintained by orient
  }

  public void unindex(BlogEntry blogEntry) {
    // Do nothing, index is maintained by orient
  }

  public Collection<Tag> getTags() {
    ODatabaseObjectTx db = daoFactory.getDb(blog);
    try {
      // First get the list of tags
      List<ODocument> tagsEntries = db.command(new OCommandSQL("select key from index:OrientBlogEntry.tags")).execute();
      // Convert to a set
      Set<String> tags = new HashSet<String>(Lists.transform(tagsEntries, new Function<ODocument, String>() {
        public String apply(ODocument input) {
          return input.field("key");
        }
      }));

      // Iterate through and find the count of published tags
      List<Tag> results = new ArrayList<Tag>();
      for (String tagName : tags) {
        List<ODocument> count = db.query(new OSQLSynchQuery<ODocument>(
            "select count(*) as size from OrientBlogEntry where tags contains ? and state = 'published'"), tagName);
        if (count != null && !count.isEmpty()) {
          ODocument d = count.get(0);
          Long size = d.field("size");
          if (size > 0) {
            results.add(new Tag(tagName, blog, 0, size.intValue()));
          }
        }
      }
      return TagRanker.calculateTagRankings(results);
    } finally {
      db.close();
    }
  }

  public List<String> getRecentBlogEntries(Tag tag) {
    ODatabaseObjectTx db = daoFactory.getDb(blog);
    try {
      List<ODocument> results = db.query(new OSQLSynchQuery<ODocument>(
          "select id from OrientBlogEntry where tags contains ? " +
              "and state = 'published' order by date desc"), tag.getName());
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
