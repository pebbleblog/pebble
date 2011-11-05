package net.sourceforge.pebble.domain;

import java.util.List;

/**
 * The blog service
 */
public interface BlogService {
  /**
   * Gets the blog entry with the specified id.
   *
   * @param blog        The blog to get the entry for
   * @param blogEntryId the id of the blog entry
   * @return a BlogEntry instance, or null if the entry couldn't be found
   * @throws BlogServiceException If an error occurred
   */
  BlogEntry getBlogEntry(Blog blog, String blogEntryId) throws BlogServiceException;

  /**
   * Get the blog entries for the given day
   *
   * @param blog  The blog to get the entries for
   * @param year  The year
   * @param month The month
   * @param day   The day
   * @return the entries
   * @throws BlogServiceException If an error occurred
   */
  List<BlogEntry> getBlogEntries(Blog blog, int year, int month, int day) throws BlogServiceException;

  /**
   * Get the blog entries for the given month
   *
   * @param blog  The blog to get the entries for
   * @param year  The year
   * @param month The month
   * @return the entries
   * @throws BlogServiceException If an error occurred
   */
  List<BlogEntry> getBlogEntries(Blog blog, int year, int month) throws BlogServiceException;

  /**
   * Gets all blog entries for the specified blog.
   *
   * @param blog The blog to get entries for
   * @return a List of BlogEntry objects
   * @throws BlogServiceException When an error occurs
   */
  List<BlogEntry> getBlogEntries(Blog blog) throws BlogServiceException;

  /**
   * Gets all blog unpublished entries for the specified blog.
   *
   * @param blog The blog to get entries for
   * @return a List of BlogEntry objects
   * @throws BlogServiceException When an error occurs
   */
  List<BlogEntry> getUnpublishedBlogEntries(Blog blog) throws BlogServiceException;

  /**
   * Puts the blog entry with the specified id.
   *
   * @param blogEntry The blog entry to put
   * @throws BlogServiceException If an error occurred
   */
  void putBlogEntry(BlogEntry blogEntry) throws BlogServiceException;

  /**
   * Removes this blog entry.
   *
   * @param blogEntry The blog entry to put
   * @throws BlogServiceException If an error occurred
   */
  void removeBlogEntry(BlogEntry blogEntry) throws BlogServiceException;

  /**
   * Gets the response with the specified id.
   *
   * @param blog The blog to get it from
   * @param responseId the id of the response
   * @return a response instance, or null if the entry couldn't be found
   * @throws BlogServiceException If an error occurred
   */
  Response getResponse(Blog blog, String responseId) throws BlogServiceException;

  /**
   * Get up to max recent blog entries
   *
   * @param blog The blog to get the entries for
   * @param max The maximum blog entries to get
   * @return The recent blog entries
   */
  List<BlogEntry> getRecentBlogEntries(Blog blog, int max);

  /**
   * Get the recent blog entries
   *
   * @param blog The blog to get the entries for
   * @return The recent blog entries
   */
  List<BlogEntry> getRecentBlogEntries(Blog blog);

  /**
   * Get up to max recent published blog entries
   *
   * @param blog The blog to get the entries for
   * @param max The maximum blog entries to get
   * @return The recent published blog entries
   */
  List<BlogEntry> getRecentPublishedBlogEntries(Blog blog, int max);

  /**
   * Get the recent published blog entries
   *
   * @param blog The blog to get the entries for
   * @return The recent published blog entries
   */
  List<BlogEntry> getRecentPublishedBlogEntries(Blog blog);

}
