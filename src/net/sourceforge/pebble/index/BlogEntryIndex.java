package net.sourceforge.pebble.index;

import net.sourceforge.pebble.comparator.ReverseBlogEntryIdComparator;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.DailyBlog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Keeps an index of all blog entries, allowing efficient access at runtime.
 *
 * @author    Simon Brown
 */
public class BlogEntryIndex {

  private static final Log log = LogFactory.getLog(BlogEntryIndex.class);

  private Blog blog;

  private List<String> indexEntries = new ArrayList<String>();

  public BlogEntryIndex(Blog blog) {
    this.blog = blog;

//    File indexes = new File(blog.getIndexesDirectory());
//    if (!indexes.exists()) {
//      indexes.mkdir();
//    }
    readIndex();
  }

  /**
   * Clears the index.
   */
  public void clear() {
    indexEntries = new ArrayList<String>();
    writeIndex();
  }

  /**
   * Indexes one or more blog entries.
   *
   * @param blogEntries   a List of BlogEntry instances
   */
  public synchronized void index(List<BlogEntry> blogEntries) {
    for (BlogEntry blogEntry : blogEntries) {
      indexEntries.add(blogEntry.getId());
    }

    Collections.sort(indexEntries, new ReverseBlogEntryIdComparator());
    writeIndex();
  }

  /**
   * Indexes a single blog entry.
   *
   * @param blogEntry   a BlogEntry instance
   */
  public synchronized void index(BlogEntry blogEntry) {
    indexEntries.add(blogEntry.getId());

    DailyBlog dailyBlog = blog.getBlogForDay(blogEntry.getDate());
    dailyBlog.addBlogEntry(blogEntry.getId());

    Collections.sort(indexEntries, new ReverseBlogEntryIdComparator());
    writeIndex();
  }

  /**
   * Unindexes a single blog entry.
   *
   * @param blogEntry   a BlogEntry instance
   */
  public synchronized void unindex(BlogEntry blogEntry) {
    DailyBlog dailyBlog = blog.getBlogForDay(blogEntry.getDate());
    dailyBlog.removeBlogEntry(blogEntry.getId());

    indexEntries.remove(blogEntry.getId());

    writeIndex();
  }

  /**
   * Helper method to load the index.
   */
  private void readIndex() {
    File indexFile = new File(blog.getIndexesDirectory(), "blogentries.index");
    if (indexFile.exists()) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(indexFile));
        String indexEntry = reader.readLine();
        while (indexEntry != null) {
          indexEntries.add(indexEntry);


          // and add it to the internal memory structures
          Date date = new Date(Long.parseLong(indexEntry));
          DailyBlog dailyBlog = blog.getBlogForDay(date);
          dailyBlog.addBlogEntry(indexEntry);

          indexEntry = reader.readLine();
        }

        reader.close();
      } catch (Exception e) {
        log.error("Error while reading index", e);
      }
    }

    Collections.sort(indexEntries, new ReverseBlogEntryIdComparator());
  }

  /**
   * Helper method to write out the index to disk.
   */
  private void writeIndex() {
    try {
      File indexFile = new File(blog.getIndexesDirectory(), "blogentries.index");
      BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile));

      for (String indexEntry : indexEntries) {
        writer.write(indexEntry);
        writer.newLine();
      }

      writer.flush();
      writer.close();
    } catch (Exception e) {
      log.error("Error while writing index", e);
    }
  }

  /**
   * Gets the number of blog entries for this blog.
   *
   * @return  an int
   */
  public int getNumberOfBlogEntries() {
    return indexEntries.size();
  }

  /**
   * Gets the full list of blog entries.
   *
   * @return  a List of blog entry IDs
   */
  public List<String> getBlogEntries() {
    return new ArrayList<String>(indexEntries);
  }

}