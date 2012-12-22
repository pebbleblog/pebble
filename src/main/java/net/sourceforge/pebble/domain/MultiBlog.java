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
package net.sourceforge.pebble.domain;

import net.sourceforge.pebble.comparator.BlogEntryComparator;
import net.sourceforge.pebble.PebbleContext;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * A composite blog is one that is made up of one or more simple blogs. This
 * is effectively a container when Pebble is running in multi-user mode - there
 * being a single MultiBlog and many Blog instances.
 * <br /><br />
 * In addition to managing one or more simple blogs, a composite blog provides
 * some aggegration functionality over the blogs it manages in order to
 * generate an aggregated XML (RSS/RDF/Atom) feed.
 *
 * @author    Simon Brown
 */
public class MultiBlog extends AbstractBlog {

  /**
   * Creates a new Blog instance, based at the specified location.
   *
   * @param root    an absolute path pointing to the root directory of the blog
   */
  public MultiBlog(String root) {
    super(root);

    // probably MultiBlog should be made a final class if init is called from here - 
    // see javadoc comment on AbstractBlog.init() for reasons
    init();
  }

  /**
   * Gets the default properties for a MultiBlog.
   *
   * @return    a Properties instance
   */
  protected Properties getDefaultProperties() {
    Properties defaultProperties = new Properties();
    defaultProperties.setProperty(NAME_KEY, "My blogs");
    defaultProperties.setProperty(DESCRIPTION_KEY, "");
    defaultProperties.setProperty(IMAGE_KEY, "");
    defaultProperties.setProperty(AUTHOR_KEY, "Various");
    defaultProperties.setProperty(TIMEZONE_KEY, "Europe/London");
    defaultProperties.setProperty(RECENT_BLOG_ENTRIES_ON_HOME_PAGE_KEY, "3");
    defaultProperties.setProperty(LANGUAGE_KEY, "en");
    defaultProperties.setProperty(COUNTRY_KEY, "GB");
    defaultProperties.setProperty(CHARACTER_ENCODING_KEY, "UTF-8");
    defaultProperties.setProperty(THEME_KEY, "default");

    return defaultProperties;
  }

  /**
   * Gets the ID of this blog.
   *
   * @return the ID as a String
   */
  public String getId() {
    return "";
  }

  /**
   * Gets the URL where this blog is deployed.
   *
   * @return a URL as a String
   */
  public String getSecureUrl() {
    return PebbleContext.getInstance().getConfiguration().getSecureUrl();
  }
  /**
   * Gets the URL where this blog is deployed.
   *
   * @return a URL as a String
   */
  public String getUrl() {
    return PebbleContext.getInstance().getConfiguration().getUrl();
  }

  /**
   * Gets the relative URL where this blog is deployed.
   *
   * @return a URL as a String
   */
  public String getRelativeUrl() {
    return "/";
  }

  /**
   * Gets the date that this blog was last updated.
   *
   * @return  a Date instance representing the time of the most recent entry
   */
  public Date getLastModified() {
    Date date = new Date(0);

    Iterator it = BlogManager.getInstance().getPublicBlogs().iterator();
    Blog blog;
    while (it.hasNext()) {
      blog = (Blog)it.next();
      if (blog.getLastModified().after(date)) {
        date = blog.getLastModified();
      }
    }

    return date;
  }

  /**
   * Gets the most recent blog entries, the number
   * of which is specified.
   *
   * @param numberOfEntries the number of entries to get
   * @return a List containing the most recent blog entries
   */
  public List<BlogEntry> getRecentBlogEntries(int numberOfEntries) {
    List<BlogEntry> blogEntries = new ArrayList<BlogEntry>();

    for (Blog blog : BlogManager.getInstance().getPublicBlogs()) {
      blogEntries.addAll(blog.getRecentPublishedBlogEntries());
    }

    Collections.sort(blogEntries, new BlogEntryComparator());

    if (blogEntries.size() >= numberOfEntries) {
      return new ArrayList<BlogEntry>(blogEntries).subList(0, numberOfEntries);
    } else {
      return new ArrayList<BlogEntry>(blogEntries);
    }
  }

  /**
   * Logs this request for blog.
   *
   * @param request   the HttpServletRequest instance for this request
   */
  public void log(HttpServletRequest request, int status) {
    // no op
  }

}
