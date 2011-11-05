package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.*;

import java.util.Collection;
import java.util.List;

/**
 * Index for blog entries
 */
public interface BlogEntryIndex {

  /**
   * Init the given blog
   * @param blog The blog to init
   */
  void init(Blog blog);

  /**
   * Clears the index.
   *
   * @param blog The blog to clear the index for
   */
  void clear(Blog blog);

  /**
   * Indexes one or more blog entries.
   *
   * @param blog        The blog to index the entries in
   * @param blogEntries a List of BlogEntry instances
   */
  void index(Blog blog, Collection<BlogEntry> blogEntries);

  /**
   * Indexes a single blog entry.
   *
   * @param blog      the blog to index the entry in
   * @param blogEntry a BlogEntry instance
   */
  void index(Blog blog, BlogEntry blogEntry);

  /**
   * Unindexes a single blog entry.
   *
   * @param blog      the blog to unindex the entry from
   * @param blogEntry a BlogEntry instance
   */
  void unindex(Blog blog, BlogEntry blogEntry);

  /**
   * Gets the number of blog entries for this blog.
   *
   * @param blog The blog to get it for
   * @return an int
   */
  int getNumberOfBlogEntries(Blog blog);

  /**
   * Gets the number of published blog entries for this blog.
   *
   * @param blog The blog to get it for
   * @return an int
   */
  int getNumberOfPublishedBlogEntries(Blog blog);

  /**
   * Gets the number of unpublished blog entries for this blog.
   *
   * @param blog The blog to get it for
   * @return an int
   */
  int getNumberOfUnpublishedBlogEntries(Blog blog);

  /**
   * Gets the full list of blog entries.
   *
   * @param blog The blog to get it for
   * @return a List of blog entry IDs
   */
  List<String> getBlogEntries(Blog blog);

  /**
   * Gets the full list of published blog entries.
   *
   * @param blog The blog to get it for
   * @return a List of blog entry IDs
   */
  List<String> getPublishedBlogEntries(Blog blog);

  /**
   * Gets the full list of unpublished blog entries.
   *
   * @param blog The blog to get it for
   * @return a List of blog entry IDs
   */
  List<String> getUnpublishedBlogEntries(Blog blog);

  /**
   * Get the blog for the given year
   *
   * @param blog The blog to get it for
   * @param year The year to get
   * @return The blog for the given year
   */
  Year getBlogForYear(Blog blog, int year);

  /**
   * Get the blog archive
   *
   * @param blog The blog to get the archive for
   * @return the archive
   */
  Archive getArchive(Blog blog);

  /**
   * Get the previous blog entry
   *
   * @param blog The blog
   * @param blogEntryId The entry to get the previous from
   * @return The previous entry, or null if none found
   */
  String getPreviousBlogEntry(Blog blog, String blogEntryId);

  /**
   * Get the next blog entry
   *
   * @param blog The blog
   * @param blogEntryId The blog entry to get the next from
   * @return The next entry, or null if none found
   */
  String getNextBlogEntry(Blog blog, String blogEntryId);

  /**
   * Get the blog entries for a particular day
   *
   * @param blog The blog
   * @param year The year
   * @param month The month
   * @param day The day
   * @return The entries for that day
   */
  List<String> getBlogEntriesForDay(Blog blog, int year, int month, int day);

  /**
   * Get the blog entries for a particular month
   *
   * @param blog The blog
   * @param year The year
   * @param month The month
   * @return The entries for that day
   */
  List<String> getBlogEntriesForMonth(Blog blog, int year, int month);
}
