package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryListener;

/**
 * Listens to blog entry events and keeps the blog entry index up to date.
 *
 * @author    Simon Brown
 */
public class BlogEntryIndexListener implements BlogEntryListener {

  /**
   * Called when a blog entry has been added.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryAdded(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    blogEntry.getBlog().getBlogEntryIndex().index(blogEntry);
  }

  /**
   * Called when a blog entry has been removed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryRemoved(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    blogEntry.getBlog().getBlogEntryIndex().unindex(blogEntry);
  }

  /**
   * Called when a blog entry has been changed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryChanged(BlogEntryEvent event) {
  }

  /**
   * Called when a blog entry has been published.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryPublished(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    blogEntry.getBlog().getBlogEntryIndex().unindex(blogEntry);
    blogEntry.getBlog().getBlogEntryIndex().index(blogEntry);
  }

  /**
   * Called when a blog entry has been unpublished.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryUnpublished(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    blogEntry.getBlog().getBlogEntryIndex().unindex(blogEntry);
    blogEntry.getBlog().getBlogEntryIndex().index(blogEntry);
  }

}
