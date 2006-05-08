package net.sourceforge.pebble.index;

import net.sourceforge.pebble.event.blogentry.BlogEntryListener;
import net.sourceforge.pebble.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.Tag;

import java.util.Collection;
import java.util.Iterator;

public class TagIndexListener implements BlogEntryListener {

  /**
   * Called when a blog entry has been added.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryAdded(BlogEntryEvent event) {
//    if (!event.getBlogEntry().isApproved()) {
//      return;
//    }
//
//    BlogEntry blogEntry = event.getBlogEntry();
//    Blog blog = blogEntry.getBlog();
//
//    addBlogEntryToCategories(blogEntry.getCategories(), blogEntry);
//    addBlogEntryToTags(blogEntry.getTagsAsList(), blogEntry);
//    blog.recalculateTagRankings();
  }

  /**
   * Called when a blog entry has been removed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryRemoved(BlogEntryEvent event) {
//    BlogEntry blogEntry = event.getBlogEntry();
//    Blog blog = blogEntry.getBlog();
//
//    removeBlogEntryFromCategories(blogEntry.getCategories(), blogEntry);
//    removeBlogEntryFromTags(blogEntry.getTagsAsList(), blogEntry);
//    blog.recalculateTagRankings();
  }

  /**
   * Called when a blog entry has been changed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryChanged(BlogEntryEvent event) {
//    BlogEntry blogEntry = event.getBlogEntry();
//    Blog blog = blogEntry.getBlog();
//
//    if (!blogEntry.isApproved()) {
//      return;
//    }
//
//    List propertyChangeEvents = event.getPropertyChangeEvents();
//    Iterator it = propertyChangeEvents.iterator();
//    while (it.hasNext()) {
//      PropertyChangeEvent pce = (PropertyChangeEvent)it.next();
//      String property = pce.getPropertyName();
//      if (property.equals(BlogEntry.CATEGORIES_PROPERTY)) {
//        removeBlogEntryFromCategories((Collection)pce.getOldValue(), blogEntry);
//        addBlogEntryToCategories((Collection)pce.getNewValue(), blogEntry);
//      } else if (property.equals(BlogEntry.TAGS_PROPERTY)) {
//        removeBlogEntryFromTags(Tag.parse(blog, (String)pce.getOldValue()), blogEntry);
//        addBlogEntryToTags(Tag.parse(blog, (String)pce.getNewValue()), blogEntry);
//      }
//    }
//    blog.recalculateTagRankings();
  }

  /**
   * Called when a blog entry has been approved.
   *
   * @param event a BlogEntryEvent inistance
   */
  public void blogEntryApproved(BlogEntryEvent event) {
//    BlogEntry blogEntry = event.getBlogEntry();
//    Blog blog = blogEntry.getBlog();
//
//    addBlogEntryToCategories(blogEntry.getCategories(), blogEntry);
//    addBlogEntryToTags(blogEntry.getTagsAsList(), blogEntry);
//    blog.recalculateTagRankings();
  }

  /**
   * Called when a blog entry has been rejected.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryRejected(BlogEntryEvent event) {
//    BlogEntry blogEntry = event.getBlogEntry();
//    Blog blog = blogEntry.getBlog();
//
//    removeBlogEntryFromCategories(blogEntry.getCategories(), blogEntry);
//    removeBlogEntryFromTags(blogEntry.getTagsAsList(), blogEntry);
//    blog.recalculateTagRankings();
  }

  /**
   * Adds a blog entry to the specified categories.
   *
   * @param categories    a Collection of Category instances
   * @param blogEntry     a BlogEntry instance
   */
  private void addBlogEntryToCategories(Collection categories, BlogEntry blogEntry) {
    Blog blog = blogEntry.getBlog();

    Iterator it = categories.iterator();
    while (it.hasNext()) {
      Category category = (Category)it.next();
      category.addBlogEntry(blogEntry);
    }
    blog.getRootCategory().addBlogEntry(blogEntry);
  }

  /**
   * Removes a blog entry from the specified categories.
   *
   * @param categories    a Collection of Category instances
   * @param blogEntry     a BlogEntry instance
   */
  private void removeBlogEntryFromCategories(Collection categories, BlogEntry blogEntry) {
    Blog blog = blogEntry.getBlog();

    Iterator it = categories.iterator();
    while (it.hasNext()) {
      Category category = (Category)it.next();
      category.removeBlogEntry(blogEntry);
    }
    blog.getRootCategory().removeBlogEntry(blogEntry);
  }

  /**
   * Adds a blog entry to the specified tags.
   *
   * @param tags          a Collection of tags
   * @param blogEntry     a BlogEntry instance
   */
  private void addBlogEntryToTags(Collection tags, BlogEntry blogEntry) {
    Iterator it = tags.iterator();
    while (it.hasNext()) {
      Tag tag = (Tag)it.next();
      tag.addBlogEntry(blogEntry);
    }
  }

  /**
   * Removes a blog entry from the specified tags.
   *
   * @param tags          a Collection of tags
   * @param blogEntry     a BlogEntry instance
   */
  private void removeBlogEntryFromTags(Collection tags, BlogEntry blogEntry) {
    Iterator it = tags.iterator();
    while (it.hasNext()) {
      Tag tag = (Tag)it.next();
      tag.removeBlogEntry(blogEntry);
    }
  }

}
