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
package net.sourceforge.pebble.logging;

import net.sourceforge.pebble.api.permalink.PermalinkProvider;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Month;
import net.sourceforge.pebble.domain.Day;
import net.sourceforge.pebble.permalink.DefaultPermalinkProvider;

import java.text.SimpleDateFormat;
import java.util.regex.Pattern;


/**
 * Represents a requested URL along with a count of how many times it
 * has been accessed.
 *
 * @author    Simon Brown
 */
public class Request extends CountedUrl {

  private static final Pattern RESPONSES_FEED_REGEX = Pattern.compile("\\/responses\\/.*.xml");
  private static final Pattern CATEGORY_FEED_REGEX = Pattern.compile("\\/categories\\/.*\\/.*.xml");
  private static final Pattern TAG_FEED_REGEX = Pattern.compile("\\/tags\\/.*\\/.*.xml");
  private static final Pattern AUTHOR_FEED_REGEX = Pattern.compile("\\/authors\\/.*\\/.*.xml");

  /**
   * Creates a new instance representing the specified url.
   *
   * @param url   the url as a String
   */
  public Request(String url) {
    super(url);
  }

  /**
   * Creates a new instance representing the specified url.
   *
   * @param url     the url as a String
   * @param blog    the owning Blog
   */
  public Request(String url, Blog blog) {
    super(url, blog);
  }

  protected void setUrl(String url) {
    super.setUrl(url);

    if (url == null || url.length() == 0) {
      setName("None");
    } else if (RESPONSES_FEED_REGEX.matcher(url).matches()) {
      setName("Feed : Responses");
      setNewsFeed(true);
    } else if (CATEGORY_FEED_REGEX.matcher(url).matches()) {
      String categoryName = url.substring("/categories/".length(), url.lastIndexOf("/"));
      setName("Feed : category=" + categoryName);
      setNewsFeed(true);
    } else if (TAG_FEED_REGEX.matcher(url).matches()) {
      String tagName = url.substring("/tags/".length(), url.lastIndexOf("/"));
      setName("Feed : tag=" + tagName);
      setNewsFeed(true);
    } else if (AUTHOR_FEED_REGEX.matcher(url).matches()) {
      String authorName = url.substring("/authors/".length(), url.lastIndexOf("/"));
      setName("Feed : author=" + authorName);
      setNewsFeed(true);
    } else if (url.indexOf("rss.xml") > -1 ||
        url.indexOf("feed.xml") > -1 ||
        url.indexOf("feed.action") > -1 ||
        url.indexOf("rdf.xml") > -1 ||
        url.indexOf("atom.xml") > -1
        ) {
      setName("Feed : Blog Entries");
      setNewsFeed(true);
    } else if (blog != null) {
      if (url.equals("/")) {
        setName("Home");
        setPageView(true);
      } else if (url.equals("/categories/") || url.equals("/categories")) {
        setName("Categories");
        setPageView(true);
      } else if (url.equals("/tags/") || url.equals("/tags")) {
        setName("Tags");
        setPageView(true);
      } else if (url.equals("/files") || (url.startsWith("/files/") && url.endsWith("/"))) {
        setName("Files");
        setPageView(true);
      } else if (url.startsWith("/categories/")) {
        String categoryName = url.substring("/categories/".length());
        if (categoryName.endsWith("/")) {
          categoryName = categoryName.substring(0, categoryName.length()-1);
        }
        setName("Category : " + categoryName);
        setPageView(true);
      } else if (url.startsWith("/tags/")) {
        String tagName = url.substring("/tags/".length());
        if (tagName.endsWith("/")) {
          tagName = tagName.substring(0, tagName.length()-1);
        }
        setName("Tag : " + tagName);
        setPageView(true);
      } else if (url.startsWith("/authors/")) {
        String authorName = url.substring("/authors/".length());
        if (authorName.endsWith("/")) {
          authorName = authorName.substring(0, authorName.length()-1);
        }
        setName("Author : " + authorName);
        setPageView(true);
      } else if (url.startsWith("/files/")) {
        String fileName = url.substring("/files/".length());
        if (fileName.endsWith("/")) {
          fileName = fileName.substring(0, fileName.length()-1);
        }
        setName("File : " + fileName);
        setFileDownload(true);
      } else if (url.startsWith("/pages/")) {
        String pageName = url.substring("/pages/".length());
        setName("Static Page : " + pageName);
        setPageView(true);
      } else if (url.startsWith("/search.action")) {
        setName("Search");
        setPageView(true);
      } else if (url.startsWith("/blogentries/")) {
        String pageNumber = url.substring("/blogentries/".length());
        if (pageNumber.indexOf(".") > -1) {
	        pageNumber = pageNumber.substring(0, pageNumber.indexOf("."));
		} else {
			pageNumber = "1";
		}
        setName("Blog Entries : Page " + pageNumber);
        setPageView(true);
      } else {
        matchOnPermalinkProvider(url, blog.getPermalinkProvider());

        if (getName() == null) {
          // try with the default permalink provider
          DefaultPermalinkProvider defaultPermalinkProvider = new DefaultPermalinkProvider();
          defaultPermalinkProvider.setBlog(blog);
          matchOnPermalinkProvider(url, defaultPermalinkProvider);
        }
      }
    }

    if (getName() == null) {
      setName(url);
      setPageView(true);
    }
  }

  private void matchOnPermalinkProvider(String url,
      PermalinkProvider permalinkProvider) {
    try {
      if (permalinkProvider.isBlogEntryPermalink(url)) {
        BlogEntry blogEntry = permalinkProvider.getBlogEntry(url);
        if (blogEntry != null) {
          setName("Blog Entry : " + blogEntry.getTitle());
          setPageView(true);
        }
      } else if (permalinkProvider.isMonthPermalink(url)) {
        Month month = permalinkProvider.getMonth(url);
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", blog
            .getLocale());
        formatter.setTimeZone(blog.getTimeZone());
        if (month != null) {
          setName("Month : " + formatter.format(month.getDate()));
          setPageView(true);
        }
      } else if (permalinkProvider.isDayPermalink(url)) {
        Day day = null;
        day = permalinkProvider.getDay(url);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy", blog
            .getLocale());
        formatter.setTimeZone(blog.getTimeZone());
        if (day != null) {
          setName("Day : " + formatter.format(day.getDate()));
          setPageView(true);
        }
      }
    } catch (IllegalArgumentException e) {
      setName("Error: " + url);
      setPageView(false);
    }
  }

}