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

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.BlogServiceException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Generates permalinks using the pattern /YYYY/MM/DD/<time-in-millis>.
 *
 * @author Simon Brown
 */
public class DefaultPermalinkProvider extends PermalinkProviderSupport {

  /** the regex used to check for a blog entry permalink : /yyyy/mm/dd/blogentryid.html */
  private static final String BLOG_ENTRY_PERMALINK_REGEX = "/\\d\\d\\d\\d/\\d\\d/\\d\\d/\\d*.html";

  /**
   * Gets the permalink for a blog entry.
   *
   * @return  a URI as a String
   */
  public String getPermalink(BlogEntry blogEntry) {
    Blog blog = blogEntry.getBlog();
    Date date = blogEntry.getDate();
    DateFormat year = new SimpleDateFormat("yyyy");
    year.setTimeZone(blog.getTimeZone());
    DateFormat month = new SimpleDateFormat("MM");
    month.setTimeZone(blog.getTimeZone());
    DateFormat day = new SimpleDateFormat("dd");
    day.setTimeZone(blog.getTimeZone());

    StringBuffer buf = new StringBuffer();
    buf.append("/");
    buf.append(year.format(date));
    buf.append("/");
    buf.append(month.format(date));
    buf.append("/");
    buf.append(day.format(date));
    buf.append("/");
    buf.append(blogEntry.getId());
    buf.append(".html");

    return buf.toString();
  }

  /**
   * Determines whether the specified URI is a blog entry permalink.
   *
   * @param uri   a relative URI
   * @return      true if the URI represents a permalink to a blog entry,
   *              false otherwise
   */
  public boolean isBlogEntryPermalink(String uri) {
    if (uri != null) {
      return uri.matches(BLOG_ENTRY_PERMALINK_REGEX);
    } else {
      return false;
    }
  }

  /**
   * Gets the blog entry referred to by the specified URI.
   *
   * @param uri   a relative URI
   * @return  a BlogEntry instance, or null if one can't be found
   */
  public BlogEntry getBlogEntry(String uri) {
    Blog blog = getBlog();
    BlogService service = new BlogService();
    try {
      return service.getBlogEntry(blog, uri.substring(12, uri.lastIndexOf(".")));
    } catch (BlogServiceException e) {
      return null;
    }
  }

}
