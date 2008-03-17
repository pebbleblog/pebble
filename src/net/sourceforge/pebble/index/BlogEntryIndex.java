package net.sourceforge.pebble.index;

import net.sourceforge.pebble.comparator.ReverseBlogEntryIdComparator;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Day;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

/**
 * Keeps an index of all blog entries, allowing efficient access at runtime.
 *
 * @author    Simon Brown
 */
public class BlogEntryIndex {

  private static final Log log = LogFactory.getLog(BlogEntryIndex.class);

  private Blog blog;

  private List<String> indexEntries = new ArrayList<String>();
  private List<String> publishedIndexEntries = new ArrayList<String>();
  private List<String> unpublishedIndexEntries = new ArrayList<String>();

  public BlogEntryIndex(Blog blog) {
    this.blog = blog;

    readIndex(true);
    readIndex(false);
  }

  /**
   * Clears the index.
   */
  public void clear() {
    indexEntries = new ArrayList<String>();
    publishedIndexEntries = new ArrayList<String>();
    unpublishedIndexEntries = new ArrayList<String>();
    writeIndex(true);
    writeIndex(false);
  }

  /**
   * Indexes one or more blog entries.
   *
   * @param blogEntries   a List of BlogEntry instances
   */
  public synchronized void index(Collection<BlogEntry> blogEntries) {
    for (BlogEntry blogEntry : blogEntries) {
      Day day = blog.getBlogForDay(blogEntry.getDate());
      if (blogEntry.isPublished()) {
        publishedIndexEntries.add(blogEntry.getId());
        day.addPublishedBlogEntry(blogEntry.getId());
      } else {
        unpublishedIndexEntries.add(blogEntry.getId());
        day.addUnpublishedBlogEntry(blogEntry.getId());
      }
      indexEntries.add(blogEntry.getId());
    }

    Collections.sort(indexEntries, new ReverseBlogEntryIdComparator());
    Collections.sort(publishedIndexEntries, new ReverseBlogEntryIdComparator());
    Collections.sort(unpublishedIndexEntries, new ReverseBlogEntryIdComparator());

    writeIndex(true);
    writeIndex(false);
  }

  /**
   * Indexes a single blog entry.
   *
   * @param blogEntry   a BlogEntry instance
   */
  public synchronized void index(BlogEntry blogEntry) {
    Day day = blog.getBlogForDay(blogEntry.getDate());
    if (blogEntry.isPublished()) {
      publishedIndexEntries.add(blogEntry.getId());
      day.addPublishedBlogEntry(blogEntry.getId());
      writeIndex(true);
    } else {
      unpublishedIndexEntries.add(blogEntry.getId());
      day.addUnpublishedBlogEntry(blogEntry.getId());
      writeIndex(false);
    }
    indexEntries.add(blogEntry.getId());

    Collections.sort(indexEntries, new ReverseBlogEntryIdComparator());
    Collections.sort(publishedIndexEntries, new ReverseBlogEntryIdComparator());
    Collections.sort(unpublishedIndexEntries, new ReverseBlogEntryIdComparator());
  }

  /**
   * Unindexes a single blog entry.
   *
   * @param blogEntry   a BlogEntry instance
   */
  public synchronized void unindex(BlogEntry blogEntry) {
    Day day = blog.getBlogForDay(blogEntry.getDate());
    day.removeBlogEntry(blogEntry);

    indexEntries.remove(blogEntry.getId());
    publishedIndexEntries.remove(blogEntry.getId());
    unpublishedIndexEntries.remove(blogEntry.getId());

    writeIndex(true);
    writeIndex(false);
  }

  /**
   * Helper method to load the index.
   */
  private void readIndex(boolean published) {
    File indexFile;
    if (published) {
      indexFile = new File(blog.getIndexesDirectory(), "blogentries-published.index");
    } else {
      indexFile = new File(blog.getIndexesDirectory(), "blogentries-unpublished.index");
    }

    if (indexFile.exists()) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(indexFile));
        String indexEntry = reader.readLine();
        while (indexEntry != null) {
          indexEntries.add(indexEntry);

          // and add it to the internal memory structures
          Date date = new Date(Long.parseLong(indexEntry));
          Day day = blog.getBlogForDay(date);

          if (published) {
            publishedIndexEntries.add(indexEntry);
            day.addPublishedBlogEntry(indexEntry);
          } else {
            unpublishedIndexEntries.add(indexEntry);
            day.addUnpublishedBlogEntry(indexEntry);
          }

          indexEntry = reader.readLine();
        }

        reader.close();
      } catch (Exception e) {
        log.error("Error while reading index", e);
      }
    }

    Collections.sort(indexEntries, new ReverseBlogEntryIdComparator());
    Collections.sort(publishedIndexEntries, new ReverseBlogEntryIdComparator());
    Collections.sort(unpublishedIndexEntries, new ReverseBlogEntryIdComparator());
  }

  /**
   * Helper method to write out the index to disk.
   */
  private void writeIndex(boolean published) {
    try {
      File indexFile;
      if (published) {
        indexFile = new File(blog.getIndexesDirectory(), "blogentries-published.index");
      } else {
        indexFile = new File(blog.getIndexesDirectory(), "blogentries-unpublished.index");
      }
      BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile));

      if (published) {
        for (String indexEntry : publishedIndexEntries) {
          writer.write(indexEntry);
          writer.newLine();
        }
      } else {
        for (String indexEntry : unpublishedIndexEntries) {
          writer.write(indexEntry);
          writer.newLine();
        }
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
   * Gets the number of published blog entries for this blog.
   *
   * @return  an int
   */
  public int getNumberOfPublishedBlogEntries() {
    return publishedIndexEntries.size();
  }

  /**
   * Gets the number of unpublished blog entries for this blog.
   *
   * @return  an int
   */
  public int getNumberOfUnpublishedBlogEntries() {
    return unpublishedIndexEntries.size();
  }

  /**
   * Gets the full list of blog entries.
   *
   * @return  a List of blog entry IDs
   */
  public List<String> getBlogEntries() {
    return new ArrayList<String>(indexEntries);
  }

  /**
   * Gets the full list of published blog entries.
   *
   * @return  a List of blog entry IDs
   */
  public List<String> getPublishedBlogEntries() {
    return new ArrayList<String>(publishedIndexEntries);
  }

  /**
   * Gets the full list of unpublished blog entries.
   *
   * @return  a List of blog entry IDs
   */
  public List<String> getUnpublishedBlogEntries() {
    return new ArrayList<String>(unpublishedIndexEntries);
  }

}