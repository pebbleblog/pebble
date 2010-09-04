package net.sourceforge.pebble.index;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryListener;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;

public class CategoryIndexListener implements BlogEntryListener {

  /**
   * Called when a blog entry has been added.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryAdded(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    if (blogEntry.isPublished()) {
      blogEntry.getBlog().getCategoryIndex().index(blogEntry);
    }
  }

  /**
   * Called when a blog entry has been removed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryRemoved(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    if (blogEntry.isPublished()) {
      blogEntry.getBlog().getCategoryIndex().unindex(blogEntry);
    }
  }

  /**
   * Called when a blog entry has been changed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryChanged(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();

    if (blogEntry.isPublished()) {
      List propertyChangeEvents = event.getPropertyChangeEvents();
      Iterator it = propertyChangeEvents.iterator();
      while (it.hasNext()) {
        PropertyChangeEvent pce = (PropertyChangeEvent)it.next();
        String property = pce.getPropertyName();

        // only if the tags or categories change do we need to reindex the tags
        if (property.equals(BlogEntry.CATEGORIES_PROPERTY)) {
          blogEntry.getBlog().getCategoryIndex().unindex(blogEntry);
          blogEntry.getBlog().getCategoryIndex().index(blogEntry);
        }
      }
    }
  }

  /**
   * Called when a blog entry has been published.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryPublished(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    blogEntry.getBlog().getCategoryIndex().index(blogEntry);
  }

  /**
   * Called when a blog entry has been unpublished.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryUnpublished(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    blogEntry.getBlog().getCategoryIndex().unindex(blogEntry);
  }

}
