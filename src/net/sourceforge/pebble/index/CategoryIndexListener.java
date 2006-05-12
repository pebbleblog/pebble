package net.sourceforge.pebble.index;

import net.sourceforge.pebble.event.blogentry.BlogEntryListener;
import net.sourceforge.pebble.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.Tag;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.beans.PropertyChangeEvent;

public class CategoryIndexListener implements BlogEntryListener {

  /**
   * Called when a blog entry has been added.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryAdded(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    if (blogEntry.isApproved()) {
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
    if (blogEntry.isApproved()) {
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

    if (blogEntry.isApproved()) {
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
   * Called when a blog entry has been approved.
   *
   * @param event a BlogEntryEvent inistance
   */
  public void blogEntryApproved(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    blogEntry.getBlog().getCategoryIndex().index(blogEntry);
  }

  /**
   * Called when a blog entry has been rejected.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryRejected(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    blogEntry.getBlog().getCategoryIndex().unindex(blogEntry);
  }

}
