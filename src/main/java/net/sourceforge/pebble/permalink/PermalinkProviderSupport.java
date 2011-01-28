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

import net.sourceforge.pebble.domain.Day;
import net.sourceforge.pebble.domain.Month;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.api.permalink.PermalinkProvider;

import java.text.SimpleDateFormat;

/**
 * Support class that can be used as a basis for PermalinkProvider
 * implementations.
 *
 * @author Simon Brown
 */
public abstract class PermalinkProviderSupport implements PermalinkProvider {

  /** the regex used to check for a day request */
  private static final String DAY_PERMALINK_REGEX = "/\\d\\d\\d\\d/\\d\\d/\\d\\d.html";

  /** the regex used to check for a monthly blog request */
  private static final String MONTH_PERMALINK_REGEX = "/\\d\\d\\d\\d/\\d\\d.html";

  /** the Blog associated with this provider instance */
  private Blog blog;

  /**
   * Gets the blog associated with this provider instance.
   *
   * @return  a Blog instance
   */
  public Blog getBlog() {
    return this.blog;
  }

  /**
   * Sets the blog associated with this provider instance.
   *
   * @param blog    a Blog instance
   */
  public void setBlog(Blog blog) {
    this.blog = blog;
  }

  /**
   * Gets the permalink for a monthly blog.
   *
   * @param month a Month instance
   * @return a URI as a String
   */
  public String getPermalink(Month month) {
    SimpleDateFormat format = new SimpleDateFormat("'/'yyyy'/'MM'.html'");
    format.setTimeZone(blog.getTimeZone());
    return format.format(month.getDate());
  }

  /**
   * Determines whether the specified URI is a monthly blog permalink.
   *
   * @param uri   a relative URI
   * @return      true if the URI represents a permalink to a monthly blog,
   *              false otherwise
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
   * @param uri   a relative URI
   * @return  a Month instance, or null if one can't be found
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
    SimpleDateFormat format = new SimpleDateFormat("'/'yyyy'/'MM'/'dd'.html'");
    format.setTimeZone(blog.getTimeZone());
    return format.format(day.getDate());
  }

  /**
   * Determines whether the specified URI is a day permalink.
   *
   * @param uri   a relative URI
   * @return      true if the URI represents a permalink to a day,
   *              false otherwise
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
   * @param uri   a relative URI
   * @return  a Day instance, or null if one can't be found
   */
  public Day getDay(String uri) {
    String year = uri.substring(1, 5);
    String month = uri.substring(6, 8);
    String day = uri.substring(9, 11);

    return getBlog().getBlogForDay(Integer.parseInt(year),
       Integer.parseInt(month), Integer.parseInt(day));
  }

}
