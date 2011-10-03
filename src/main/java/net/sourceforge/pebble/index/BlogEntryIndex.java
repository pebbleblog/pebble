package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;

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
}
