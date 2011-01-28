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
package net.sourceforge.pebble.api.permalink;

import net.sourceforge.pebble.domain.*;

/**
 * An interface implemented by any class that generates permalinks.
 *
 * @author Simon Brown
 */
public interface PermalinkProvider {

  /**
   * Gets the blog associated with this provider instance.
   *
   * @return  a Blog instance
   */
  public Blog getBlog();

  /**
   * Sets the blog associated with this provider instance.
   *
   * @param blog    a Blog instance
   */
  public void setBlog(Blog blog);

  /**
   * Gets the permalink for a blog entry.
   *
   * @param   blogEntry   a BlogEntry instance
   * @return  a URI as a String
   */
  public String getPermalink(BlogEntry blogEntry);

  /**
   * Determines whether the specified URI is a blog entry permalink.
   *
   * @param uri   a relative URI
   * @return      true if the URI represents a permalink to a blog entry,
   *              false otherwise
   */
  public boolean isBlogEntryPermalink(String uri);

  /**
   * Gets the blog entry referred to by the specified URI.
   *
   * @param uri   a relative URI
   * @return  a BlogEntry instance, or null if one can't be found
   */
  public BlogEntry getBlogEntry(String uri);

  /**
   * Gets the permalink for a monthly blog.
   *
   * @param month   a Month instance
   * @return  a URI as a String
   */
  public String getPermalink(Month month);

  /**
   * Determines whether the specified URI is a monthly blog permalink.
   *
   * @param uri   a relative URI
   * @return      true if the URI represents a permalink to a monthly blog,
   *              false otherwise
   */
  public boolean isMonthPermalink(String uri);

  /**
   * Gets the monthly blog referred to by the specified URI.
   *
   * @param uri   a relative URI
   * @return  a Month instance, or null if one can't be found
   */
  public Month getMonth(String uri);

  /**
   * Gets the permalink for a day.
   *
   * @param day   a Day instance
   * @return  a URI as a String
   */
  public String getPermalink(Day day);

  /**
   * Determines whether the specified URI is a day permalink.
   *
   * @param uri   a relative URI
   * @return      true if the URI represents a permalink to a day,
   *              false otherwise
   */
  public boolean isDayPermalink(String uri);

  /**
   * Gets the day referred to by the specified URI.
   *
   * @param uri   a relative URI
   * @return  a Day instance, or null if one can't be found
   */
  public Day getDay(String uri);

}
