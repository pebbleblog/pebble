/*
 * Copyright (c) 2003-2011, Simon Brown
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

package net.sourceforge.pebble.index.file;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.sourceforge.pebble.comparator.ReverseBlogEntryIdComparator;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.index.BlogEntryIndex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;

/**
 * Keeps an index of all blog entries, allowing efficient access at runtime.
 *
 * @author Simon Brown
 */
public class FileBlogEntryIndex implements BlogEntryIndex {
  private static final Log log = LogFactory.getLog(FileBlogEntryIndex.class);

  private Map<String, CachedIndex> cache = Maps.newConcurrentMap();

  public void init(Blog blog) {
    cache.put(blog.getRoot(), new CachedIndex(blog));
  }

  public void clear(Blog blog) {
    cache.get(blog.getRoot()).clear();
  }

  public void index(Blog blog, Collection<BlogEntry> blogEntries) {
    cache.get(blog.getRoot()).index(blogEntries);
  }

  public void index(Blog blog, BlogEntry blogEntry) {
    cache.get(blog.getRoot()).index(blogEntry);
  }

  public void unindex(Blog blog, BlogEntry blogEntry) {
    cache.get(blog.getRoot()).unindex(blogEntry);
  }

  public int getNumberOfBlogEntries(Blog blog) {
    return cache.get(blog.getRoot()).getNumberOfBlogEntries();
  }

  public int getNumberOfPublishedBlogEntries(Blog blog) {
    return cache.get(blog.getRoot()).getNumberOfPublishedBlogEntries();
  }

  public int getNumberOfUnpublishedBlogEntries(Blog blog) {
    return cache.get(blog.getRoot()).getNumberOfUnpublishedBlogEntries();
  }

  public List<String> getBlogEntries(Blog blog) {
    return cache.get(blog.getRoot()).getBlogEntries();
  }

  public List<String> getPublishedBlogEntries(Blog blog) {
    return cache.get(blog.getRoot()).getPublishedBlogEntries();
  }

  public List<String> getUnpublishedBlogEntries(Blog blog) {
    return cache.get(blog.getRoot()).getUnpublishedBlogEntries();
  }

  public Year getBlogForYear(Blog blog, int year) {
    return cache.get(blog.getRoot()).getArchive().getYear(year);
  }

  public Archive getArchive(Blog blog) {
    return cache.get(blog.getRoot()).getArchive();
  }

  public String getPreviousBlogEntry(Blog blog, String blogEntryId) {
    return cache.get(blog.getRoot()).getPreviousBlogEntry(blogEntryId);
  }

  public String getNextBlogEntry(Blog blog, String blogEntryId) {
    return cache.get(blog.getRoot()).getNextBlogEntry(blogEntryId);
  }

  public List<String> getBlogEntriesForDay(Blog blog, int year, int month, int day) {
    return cache.get(blog.getRoot()).getEntriesForDay(year, month, day);
  }

  public List<String> getBlogEntriesForMonth(Blog blog, int year, int month) {
    return cache.get(blog.getRoot()).getEntriesForMonth(year, month);
  }

  private class CachedIndex {

    private final Blog blog;

    // These are mutable lists and all access to them must be synchronised on this class. They should only
    // be used by operations that require write access
    private final List<String> indexEntries = new ArrayList<String>();
    private final List<String> publishedIndexEntries = new ArrayList<String>();
    private final List<String> unpublishedIndexEntries = new ArrayList<String>();
    private final List<Year> years = new ArrayList<Year>();
    private final List<YearCache> yearCaches = new ArrayList<YearCache>();

    // These are immutable lists and should be only used by read only operations. When a write operation is
    // done, these should be updated accordingly
    private volatile List<String> readIndexEntries = Collections.emptyList();
    private volatile List<String> readPublishedIndexEntries = Collections.emptyList();
    private volatile List<String> readUnpublishedIndexEntries = Collections.emptyList();
    private volatile Archive archive;
    private volatile List<YearCache> readYearCaches = Collections.emptyList();

    public CachedIndex(Blog blog) {
      this.blog = blog;

      readIndex(true);
      readIndex(false);
    }

    /**
     * Clears the index.
     */
    public synchronized void clear() {
      indexEntries.clear();
      publishedIndexEntries.clear();
      unpublishedIndexEntries.clear();
      years.clear();
      yearCaches.clear();
      updateReadVersions();
      writeIndex(true);
      writeIndex(false);
    }

    /**
     * Perform the given operation on all the blog entries
     *
     * @param date The date of the blog entry
     * @param operation The operation to perform
     */
    public void performOperation(Date date, IndexOperation operation) {
      Calendar cal = getCalendar(date);
      Year year = getYear(cal);
      Month month = year.getMonth(cal.get(Calendar.MONTH) + 1);
      Day day = month.getDay(cal.get(Calendar.DAY_OF_MONTH));
      YearCache yearCache = getYearCache(year);
      MonthCache monthCache = getMonthCache(yearCache, month);
      DayCache dayCache = getDayCache(monthCache, day);

      DayCache.Builder builder = DayCache.builder(dayCache);

      operation.performOperation(builder);

      dayCache = builder.build();
      if (dayCache.getNumberPublishedBlogEntries() != day.getNumberOfBlogEntries()) {
        year = Year.builder(year).putMonth(Month.builder(month).putDay(
            Day.builder(day).setNumberOfBlogEntries(dayCache.getNumberPublishedBlogEntries()).build()
        ).build()).build();
        boolean found = false;
        for (int i = 0; i < years.size(); i++) {
          if (years.get(i).getYear() == year.getYear()) {
            years.set(i, year);
            found = true;
          }
        }
        if (!found) {
          years.add(year);
        }
      }
      yearCache = YearCache.builder(yearCache).putMonth(MonthCache.builder(monthCache).putDay(dayCache).build()).build();
      boolean found = false;
      for (int i = 0; i < yearCaches.size(); i++) {
        if (yearCaches.get(i).getYear().getYear() == yearCache.getYear().getYear()) {
          yearCaches.set(i, yearCache);
          found = true;
        }
      }
      if (!found) {
        yearCaches.add(yearCache);
      }
    }

    /**
     * Indexes one or more blog entries.
     *
     * @param blogEntries a List of BlogEntry instances
     */
    public synchronized void index(Collection<BlogEntry> blogEntries) {
      for (final BlogEntry blogEntry : blogEntries) {
        performOperation(blogEntry.getDate(), new IndexOperation() {
          public void performOperation(DayCache.Builder dayCache) {
            if (blogEntry.isPublished()) {
              publishedIndexEntries.add(blogEntry.getId());
              dayCache.addPublishedBlogEntry(blogEntry.getId());
            } else {
              unpublishedIndexEntries.add(blogEntry.getId());
              dayCache.addUnpublishedBlogEntry(blogEntry.getId());
            }
            indexEntries.add(blogEntry.getId());
          }
        });
      }

      Collections.sort(indexEntries, new ReverseBlogEntryIdComparator());
      Collections.sort(publishedIndexEntries, new ReverseBlogEntryIdComparator());
      Collections.sort(unpublishedIndexEntries, new ReverseBlogEntryIdComparator());
      Collections.sort(yearCaches);
      Collections.sort(years);

      updateReadVersions();

      writeIndex(true);
      writeIndex(false);
    }

    /**
     * Indexes a single blog entry.
     *
     * @param blogEntry a BlogEntry instance
     */
    public synchronized void index(BlogEntry blogEntry) {
      index(Arrays.asList(blogEntry));
    }

    /**
     * Unindexes a single blog entry.
     *
     * @param blogEntry a BlogEntry instance
     */
    public synchronized void unindex(final BlogEntry blogEntry) {
      performOperation(blogEntry.getDate(), new IndexOperation() {
        public void performOperation(DayCache.Builder dayCache) {
          dayCache.removeBlogEntry(blogEntry);
          indexEntries.remove(blogEntry.getId());
          publishedIndexEntries.remove(blogEntry.getId());
          unpublishedIndexEntries.remove(blogEntry.getId());
        }
      });
      updateReadVersions();

      writeIndex(true);
      writeIndex(false);

    }

    private void updateReadVersions() {
      readIndexEntries = ImmutableList.copyOf(indexEntries);
      readPublishedIndexEntries = ImmutableList.copyOf(publishedIndexEntries);
      readUnpublishedIndexEntries = ImmutableList.copyOf(unpublishedIndexEntries);
      readYearCaches = ImmutableList.copyOf(yearCaches);
      archive = Archive.builder(blog).setYears(years).build();
    }

    /**
     * Helper method to load the index.
     *
     * @param published Whether the published or unpublished index should be read
     */
    private void readIndex(final boolean published) {
      File indexFile;
      if (published) {
        indexFile = new File(blog.getIndexesDirectory(), "blogentries-published.index");
      } else {
        indexFile = new File(blog.getIndexesDirectory(), "blogentries-unpublished.index");
      }

      if (indexFile.exists()) {
        try {
          BufferedReader reader = new BufferedReader(new FileReader(indexFile));
          String indexEntry = reader.readLine();
          while (indexEntry != null) {
            indexEntries.add(indexEntry);
            final String entry = indexEntry;

            // and add it to the internal memory structures
            Date date = new Date(Long.parseLong(indexEntry));
            performOperation(date, new IndexOperation() {
              public void performOperation(DayCache.Builder dayCache) {
                if (published) {
                  publishedIndexEntries.add(entry);
                  dayCache.addPublishedBlogEntry(entry);
                } else {
                  unpublishedIndexEntries.add(entry);
                  dayCache.addUnpublishedBlogEntry(entry);
                }
              }
            });
            indexEntry = reader.readLine();
          }

          reader.close();
        } catch (Exception e) {
          log.error("Error while reading index", e);
        }
      }

      Collections.sort(indexEntries, new ReverseBlogEntryIdComparator());
      Collections.sort(publishedIndexEntries, new ReverseBlogEntryIdComparator());
      Collections.sort(unpublishedIndexEntries, new ReverseBlogEntryIdComparator());
      Collections.sort(yearCaches);
      Collections.sort(years);

      updateReadVersions();
    }

    /**
     * Helper method to write out the index to disk.
     *
     * @param published Whether the published or unpublished index should be written
     */
    private void writeIndex(boolean published) {
      try {
        File indexFile;
        if (published) {
          indexFile = new File(blog.getIndexesDirectory(), "blogentries-published.index");
        } else {
          indexFile = new File(blog.getIndexesDirectory(), "blogentries-unpublished.index");
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile));

        if (published) {
          for (String indexEntry : publishedIndexEntries) {
            writer.write(indexEntry);
            writer.newLine();
          }
        } else {
          for (String indexEntry : unpublishedIndexEntries) {
            writer.write(indexEntry);
            writer.newLine();
          }
        }

        writer.flush();
        writer.close();
      } catch (Exception e) {
        log.error("Error while writing index", e);
      }
    }

    /**
     * Gets the number of blog entries for this blog.
     *
     * @return an int
     */
    public int getNumberOfBlogEntries() {
      return readIndexEntries.size();
    }

    /**
     * Gets the number of published blog entries for this blog.
     *
     * @return an int
     */
    public int getNumberOfPublishedBlogEntries() {
      return readPublishedIndexEntries.size();
    }

    /**
     * Gets the number of unpublished blog entries for this blog.
     *
     * @return an int
     */
    public int getNumberOfUnpublishedBlogEntries() {
      return readUnpublishedIndexEntries.size();
    }

    /**
     * Gets the full list of blog entries.
     *
     * @return a List of blog entry IDs
     */
    public List<String> getBlogEntries() {
      return readIndexEntries;
    }

    /**
     * Gets the full list of published blog entries.
     *
     * @return a List of blog entry IDs
     */
    public List<String> getPublishedBlogEntries() {
      return readPublishedIndexEntries;
    }

    /**
     * Gets the full list of unpublished blog entries.
     *
     * @return a List of blog entry IDs
     */
    public List<String> getUnpublishedBlogEntries() {
      return readUnpublishedIndexEntries;
    }

    public String getNextBlogEntry(String entry) {
      // Get a reference to the list first so we are atomic
      List<String> entries = readPublishedIndexEntries;
      int position = Collections.binarySearch(entries, entry, ReverseBlogEntryIdComparator.INSTANCE);
      if (position - 1 >= 0) {
        return entries.get(position - 1);
      }
      return null;
    }

    public String getPreviousBlogEntry(String entry) {
      // Get a reference to the list first so we are atomic
      List<String> entries = readPublishedIndexEntries;
      int position = Collections.binarySearch(entries, entry, ReverseBlogEntryIdComparator.INSTANCE);
      if (position >= 0 && position + 1 < entries.size()) {
        return entries.get(position + 1);
      }
      return null;
    }

    public Archive getArchive() {
      return archive;
    }

    public List<String> getEntriesForDay(int year, int month, int day) {
      for (YearCache yearCache : readYearCaches) {
        if (yearCache.getYear().getYear() == year) {
          return yearCache.getMonthCache(month).getDayCache(day).getBlogEntries();
        }
      }
      return Collections.emptyList();
    }

    public List<String> getEntriesForMonth(int year, int month) {
      for (YearCache yearCache : yearCaches) {
        if (yearCache.getYear().getYear() == year) {
          return yearCache.getMonthCache(month).getBlogEntries();
        }
      }
      return Collections.emptyList();
    }

    private Calendar getCalendar(Date date) {
      Calendar cal = blog.getCalendar();
      cal.setTime(date);
      return cal;
    }

    private Year getYear(Calendar cal) {
      int yearNo = cal.get(Calendar.YEAR);
      for (Year year : years) {
        if (year.getYear() == yearNo) {
          return year;
        }
      }
      return Year.emptyYear(blog, yearNo);
    }

    private YearCache getYearCache(Year year) {
      for (YearCache yearCache : yearCaches) {
        if (yearCache.getYear().equals(year)) {
          return yearCache;
        }
      }

      return YearCache.builder(year).build();
    }

    private MonthCache getMonthCache(YearCache yearCache, Month month) {
      MonthCache monthCache = yearCache.getMonthCache(month.getMonth());
      if (monthCache != null) {
        return monthCache;
      } else {
        return MonthCache.builder(month).build();
      }
    }

    private DayCache getDayCache(MonthCache monthCache, Day day) {
      DayCache dayCache = monthCache.getDayCache(day.getDay());
      if (dayCache != null) {
        return dayCache;
      } else {
        return DayCache.builder(day).build();
      }
    }

  }

  private interface IndexOperation {
    void performOperation(DayCache.Builder dayCache);
  }

}