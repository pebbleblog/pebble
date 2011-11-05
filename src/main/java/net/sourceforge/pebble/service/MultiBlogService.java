package net.sourceforge.pebble.service;

import net.sourceforge.pebble.domain.BlogEntry;

import java.util.List;

/**
 * Service for dealing with the multi blog
 */
public interface MultiBlogService {
  /**
   * Get the recent published blog entries
   *
   * @param max The maximum entries to get
   * @return The entries
   */
  List<BlogEntry> getRecentPublishedBlogEntries(int max);

  /**
   * Get the recent published blog entries
   *
   * @return The entries
   */
  List<BlogEntry> getRecentPublishedBlogEntries();
}
