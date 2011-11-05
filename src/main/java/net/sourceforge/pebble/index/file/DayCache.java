package net.sourceforge.pebble.index.file;

import net.sourceforge.pebble.comparator.ReverseBlogEntryIdComparator;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Day;
import net.sourceforge.pebble.domain.Month;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A cache of blog entries for the day
 */
public class DayCache {

  private final Day day;
  /**
   * the collection of blog entry keys
   */
  private final List<String> blogEntries;
  private final List<String> publishedBlogEntries;
  private final List<String> unpublishedBlogEntries;

  public DayCache(Day day, List<String> blogEntries, List<String> publishedBlogEntries, List<String> unpublishedBlogEntries) {
    this.day = day;
    this.blogEntries = blogEntries;
    this.publishedBlogEntries = publishedBlogEntries;
    this.unpublishedBlogEntries = unpublishedBlogEntries;
  }

  public Day getDay() {
    return day;
  }

  public int getNumberPublishedBlogEntries() {
    return publishedBlogEntries.size();
  }

  public List<String> getPublishedBlogEntries() {
    return publishedBlogEntries;
  }

  public List<String> getUnpublishedBlogEntries() {
    return unpublishedBlogEntries;
  }

  public List<String> getBlogEntries() {
    return blogEntries;
  }

  /**
   * Gets the blog entry posted previous (before) to the one specified.
   *
   * @param blogEntry a BlogEntry
   * @return the previous BlogEntry, or null if one doesn't exist
   */
  public String getPreviousBlogEntry(String blogEntry) {
    int index = publishedBlogEntries.indexOf(blogEntry);
    if (index >= 0 && index < (publishedBlogEntries.size() - 1)) {
      return publishedBlogEntries.get(index + 1);
    } else {
      return null;
    }
  }

  /**
   * Gets the first entry that was posted on this day.
   *
   * @return a BlogEntry instance, or null is no entries have been posted
   */
  public String getFirstBlogEntry() {
    if (!publishedBlogEntries.isEmpty()) {
      return publishedBlogEntries.get(publishedBlogEntries.size() - 1);
    } else {
      return null;
    }
  }

  /**
   * Gets the last entry that was posted on this day.
   *
   * @return a BlogEntry instance, or null is no entries have been posted
   */
  public String getLastBlogEntry() {
    if (!publishedBlogEntries.isEmpty()) {
      return publishedBlogEntries.get(0);
    } else {
      return null;
    }
  }

  /**
   * Gets the blog entry posted next (afterwards) to the one specified.
   *
   * @param blogEntry a BlogEntry
   * @return the next BlogEntry, or null if one doesn't exist
   */
  public String getNextBlogEntry(String blogEntry) {
    int index = publishedBlogEntries.lastIndexOf(blogEntry);
    if (index > 0 && index <= publishedBlogEntries.size()) {
      return publishedBlogEntries.get(index - 1);
    } else {
      return null;
    }
  }

  public static Builder builder(Day day) {
    return new Builder(day);
  }

  public static Builder builder(DayCache like) {
    return new Builder(like);
  }

  public static class Builder {
    private final Day day;
    private final List<String> blogEntries;
    private final List<String> publishedBlogEntries;
    private final List<String> unpublishedBlogEntries;

    public Builder(Day day) {
      this.day = day;
      blogEntries = new ArrayList<String>();
      publishedBlogEntries = new ArrayList<String>();
      unpublishedBlogEntries = new ArrayList<String>();
    }

    public Builder(DayCache dayCache) {
      this.day = dayCache.day;
      this.blogEntries = new ArrayList<String>(dayCache.blogEntries);
      this.publishedBlogEntries = new ArrayList<String>(dayCache.publishedBlogEntries);
      this.unpublishedBlogEntries = new ArrayList<String>(dayCache.unpublishedBlogEntries);
    }

    public DayCache build() {
      return new DayCache(day, blogEntries, publishedBlogEntries, unpublishedBlogEntries);
    }

    public void addPublishedBlogEntry(String blogEntryId) {
      if (!publishedBlogEntries.contains(blogEntryId)) {
        publishedBlogEntries.add(blogEntryId);
        Collections.sort(publishedBlogEntries, new ReverseBlogEntryIdComparator());
      }
      unpublishedBlogEntries.remove(blogEntryId);

      if (!blogEntries.contains(blogEntryId)) {
        // and add to the aggregated view
        blogEntries.add(blogEntryId);
        Collections.sort(blogEntries, ReverseBlogEntryIdComparator.INSTANCE);
      }
    }

    public void addUnpublishedBlogEntry(String blogEntryId) {
      if (!unpublishedBlogEntries.contains(blogEntryId)) {
        unpublishedBlogEntries.add(blogEntryId);
        Collections.sort(unpublishedBlogEntries, ReverseBlogEntryIdComparator.INSTANCE);
      }
      publishedBlogEntries.remove(blogEntryId);

      if (!blogEntries.contains(blogEntryId)) {
        // and add to the aggregated view
        blogEntries.add(blogEntryId);
        Collections.sort(blogEntries, ReverseBlogEntryIdComparator.INSTANCE);
      }
    }

    public void removeBlogEntry(BlogEntry blogEntry) {
      publishedBlogEntries.remove(blogEntry.getId());
      unpublishedBlogEntries.remove(blogEntry.getId());
      blogEntries.remove(blogEntry.getId());
    }
  }
}
