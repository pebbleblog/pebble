/*
 * Copyright (c) 2003-2006, Simon Brown
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 *   - Neither the name of Pebble nor the names of its contributors may
 *     be used to endorse or promote products derived from this software
 *     without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.sourceforge.pebble.domain;

import net.sourceforge.pebble.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.event.blogentry.BlogEntryListener;
import net.sourceforge.pebble.search.BlogIndexer;

import java.beans.PropertyChangeEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class IndexBlogEntryListener implements BlogEntryListener {

  /**
   * Called when a blog entry has been added.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryAdded(BlogEntryEvent event) {
    if (!event.getBlogEntry().isApproved()) {
      return;
    }

    BlogEntry blogEntry = event.getBlogEntry();
    Blog blog = blogEntry.getBlog();

    addBlogEntryToCategories(blogEntry.getCategories(), blogEntry);
    addBlogEntryToTags(blogEntry.getTagsAsList(), blogEntry);
    blog.recalculateTagRankings();
    updateSearchIndex(blogEntry);
  }

  /**
   * Called when a blog entry has been removed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryRemoved(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    Blog blog = blogEntry.getBlog();

    removeBlogEntryFromCategories(blogEntry.getCategories(), blogEntry);
    removeBlogEntryFromTags(blogEntry.getTagsAsList(), blogEntry);
    blog.recalculateTagRankings();

    BlogIndexer indexer = new BlogIndexer();
    indexer.removeIndex(blogEntry);
  }

  /**
   * Called when a blog entry has been changed.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryChanged(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    Blog blog = blogEntry.getBlog();

    if (!blogEntry.isApproved()) {
      return;
    }

    List propertyChangeEvents = event.getPropertyChangeEvents();
    Iterator it = propertyChangeEvents.iterator();
    while (it.hasNext()) {
      PropertyChangeEvent pce = (PropertyChangeEvent)it.next();
      String property = pce.getPropertyName();
      if (property.equals(BlogEntry.CATEGORIES_PROPERTY)) {
        removeBlogEntryFromCategories((Collection)pce.getOldValue(), blogEntry);
        addBlogEntryToCategories((Collection)pce.getNewValue(), blogEntry);
      } else if (property.equals(BlogEntry.TAGS_PROPERTY)) {
        removeBlogEntryFromTags(Tag.parse(blog, (String)pce.getOldValue()), blogEntry);
        addBlogEntryToTags(Tag.parse(blog, (String)pce.getNewValue()), blogEntry);
      }
    }
    blog.recalculateTagRankings();
    updateSearchIndex(blogEntry);
  }

  /**
   * Called when a blog entry has been approved.
   *
   * @param event a BlogEntryEvent inistance
   */
  public void blogEntryApproved(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    Blog blog = blogEntry.getBlog();

    addBlogEntryToCategories(blogEntry.getCategories(), blogEntry);
    addBlogEntryToTags(blogEntry.getTagsAsList(), blogEntry);
    blog.recalculateTagRankings();
    updateSearchIndex(blogEntry);
  }

  /**
   * Called when a blog entry has been rejected.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryRejected(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    Blog blog = blogEntry.getBlog();

    removeBlogEntryFromCategories(blogEntry.getCategories(), blogEntry);
    removeBlogEntryFromTags(blogEntry.getTagsAsList(), blogEntry);
    blog.recalculateTagRankings();
    updateSearchIndex(blogEntry);
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

  /**
   * Updates the search index to reflect the new/changed/removed blog entry.
   */
  private void updateSearchIndex(BlogEntry blogEntry) {
    BlogIndexer indexer = new BlogIndexer();
    indexer.index(blogEntry);
  }

}
