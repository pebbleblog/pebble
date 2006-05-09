package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.DailyBlog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * Keeps an index of all blog entries, allowing efficient access at runtime.
 *
 * @author    Simon Brown
 */
public class BlogEntryIndex {

  private static final Log log = LogFactory.getLog(BlogEntryIndex.class);

  private static final String INDEX_FORMAT = "yyyy/MM/dd";

  private Blog blog;

  private List<String> indexEntries = new ArrayList<String>();

  public BlogEntryIndex(Blog blog) {
    this.blog = blog;
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
    DateFormat format = new SimpleDateFormat(INDEX_FORMAT);
    for (BlogEntry blogEntry : blogEntries) {
      String indexEntry = format.format(blogEntry.getDate()) + "/" + blogEntry.getId();
      indexEntries.add(indexEntry);
    }

    writeIndex();
  }

  /**
   * Indexes a single blog entry.
   *
   * @param blogEntry   a BlogEntry instance
   */
  public synchronized void index(BlogEntry blogEntry) {
    DateFormat format = new SimpleDateFormat(INDEX_FORMAT);
    String indexEntry = format.format(blogEntry.getDate()) + "/" + blogEntry.getId();
    indexEntries.add(indexEntry);

    DailyBlog dailyBlog = blog.getBlogForDay(blogEntry.getDate());
    dailyBlog.addBlogEntry(blogEntry.getId());

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

    DateFormat format = new SimpleDateFormat(INDEX_FORMAT);
    String indexEntry = format.format(blogEntry.getDate()) + "/" + blogEntry.getId();
    indexEntries.remove(indexEntry);

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

          // split up the yyyy/MM/dd/blogentryid into it's parts
          String parts[] = indexEntry.split("/");
          int year = Integer.parseInt(parts[0]);
          int month = Integer.parseInt(parts[1]);
          int day = Integer.parseInt(parts[2]);
          String blogEntryId = parts[3];

          // and add it to the internal memory structures
          DailyBlog dailyBlog = blog.getBlogForDay(year, month, day);
          dailyBlog.addBlogEntry(blogEntryId);

          indexEntry = reader.readLine();
        }

        reader.close();
      } catch (Exception e) {
        log.error("Error while reading index", e);
      }
    }

    // sort in alphabetical order, and therefore in date order
    Collections.sort(indexEntries);
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

}