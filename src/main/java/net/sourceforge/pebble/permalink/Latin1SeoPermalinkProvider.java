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

package net.sourceforge.pebble.permalink;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.BlogServiceException;
import net.sourceforge.pebble.domain.Day;
import net.sourceforge.pebble.domain.Month;

/**
 * Generates permalinks based upon the blog entry title. This implementation
 * retains characters from the latin1 character by converting
 * them to suitable "url-friendly" counterparts.
 * <p/>
 * It also uses dashes instead of underscores for whitespace as this is
 * what Google recommends.
 * <p/>
 * For titles without characters from the latin1 character set
 * the blog entry ID is used for the permalink instead.
 *
 * @author Mattias Reichel
 */
public class Latin1SeoPermalinkProvider extends PermalinkProviderSupport {

  /**
   * the regex used to check for a day request
   */
  private static final String DAY_PERMALINK_REGEX = "/\\d\\d\\d\\d/\\d\\d/\\d\\d";

  /**
   * the regex used to check for a monthly blog request
   */
  private static final String MONTH_PERMALINK_REGEX = "/\\d\\d\\d\\d/\\d\\d";

  /**
   * the regex used to check for a blog entry permalink
   */
  private static final String BLOG_ENTRY_PERMALINK_REGEX = "/[\\w-]*";

  /**
   * the Blog associated with this provider instance
   */
  private Blog blog;

  /**
   * Gets the blog associated with this provider instance.
   *
   * @return a Blog instance
   */
  public Blog getBlog() {
    return this.blog;
  }

  /**
   * Sets the blog associated with this provider instance.
   *
   * @param blog a Blog instance
   */
  public void setBlog(Blog blog) {
    this.blog = blog;
  }

  /**
   * Gets the permalink for a blog entry.
   *
   * @return a URI as a String
   */
  public synchronized String getPermalink(BlogEntry blogEntry) {
    if (blogEntry.getTitle() == null || blogEntry.getTitle().length() == 0) {
      return buildPermalink(blogEntry);
    } else {
      List<BlogEntry> entries = getBlog().getBlogEntries();
      int count = 0;
      for (int i = entries.size() - 1; i >= 0 && !entries.get(i).getId().equals(blogEntry.getId()); i--) {
        if (entries.get(i).getTitle().equals(blogEntry.getTitle())) {
          count++;
        }
      }

      if (count == 0) {
        return buildPermalink(blogEntry);
      } else {
        return buildPermalink(blogEntry) + "_" + blogEntry.getId();
      }
    }
  }

  private String buildPermalink(BlogEntry blogEntry) {
    String title = getCuratedPermalinkTitle(blogEntry, "-");

    return "/" + title;
  }


  public boolean isBlogEntryPermalink(String uri) {
    if (uri != null) {
      return uri.matches(BLOG_ENTRY_PERMALINK_REGEX);
    } else {
      return false;
    }
  }

  public BlogEntry getBlogEntry(String uri) {
    BlogService service = new BlogService();
    Iterator it = getBlog().getBlogEntries().iterator();
    while (it.hasNext()) {
      try {
        BlogEntry blogEntry = service.getBlogEntry(getBlog(), "" + ((BlogEntry) it.next()).getId());
        // use the local permalink, just in case the entry has been aggregated
        // and an original permalink assigned
        if (blogEntry.getLocalPermalink().endsWith(uri)) {
          return blogEntry;
        }
      } catch (BlogServiceException e) {
        // do nothing
      }
    }

    return null;
  }

  /**
   * Gets the permalink for a monthly blog.
   *
   * @param month a Month instance
   * @return a URI as a String
   */
  public String getPermalink(Month month) {
    SimpleDateFormat format = new SimpleDateFormat("'/'yyyy'/'MM");
    format.setTimeZone(blog.getTimeZone());
    return format.format(month.getDate());
  }

  /**
   * Determines whether the specified URI is a monthly blog permalink.
   *
   * @param uri a relative URI
   * @return true if the URI represents a permalink to a monthly blog,
   *         false otherwise
   */
  public boolean isMonthPermalink(String uri) {
    if (uri != null) {
      return uri.matches(MONTH_PERMALINK_REGEX);
    } else {
      return false;
    }
  }

  /**
   * Gets the monthly blog referred to by the specified URI.
   *
   * @param uri a relative URI
   * @return a Month instance, or null if one can't be found
   */
  public Month getMonth(String uri) {
    String year = uri.substring(1, 5);
    String month = uri.substring(6, 8);

    return getBlog().getBlogForMonth(Integer.parseInt(year), Integer.parseInt(month));
  }

  /**
   * Gets the permalink for a day.
   *
   * @param day a Day instance
   * @return a URI as a String
   */
  public String getPermalink(Day day) {
    SimpleDateFormat format = new SimpleDateFormat("'/'yyyy'/'MM'/'dd");
    format.setTimeZone(blog.getTimeZone());
    return format.format(day.getDate());
  }

  /**
   * Determines whether the specified URI is a day permalink.
   *
   * @param uri a relative URI
   * @return true if the URI represents a permalink to a day,
   *         false otherwise
   */
  public boolean isDayPermalink(String uri) {
    if (uri != null) {
      return uri.matches(DAY_PERMALINK_REGEX);
    } else {
      return false;
    }
  }

  /**
   * Gets the day referred to by the specified URI.
   *
   * @param uri a relative URI
   * @return a Day instance, or null if one can't be found
   */
  public Day getDay(String uri) {
    String year = uri.substring(1, 5);
    String month = uri.substring(6, 8);
    String day = uri.substring(9, 11);

    return getBlog().getBlogForDay(Integer.parseInt(year),
            Integer.parseInt(month), Integer.parseInt(day));
  }
}
