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
package net.sourceforge.pebble.web.filter;

import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.permalink.DefaultPermalinkProvider;
import net.sourceforge.pebble.api.permalink.PermalinkProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Responsible for converting an incoming URI to a real URI used by Pebble.
 *
 * @author    Simon Brown
 */
public class UriTransformer {

  /** literal used at the start of category URIs */
  private static final String CATEGORIES = "/categories";

  /** literal used at the start of category URIs, in regex form */
  private static final String CATEGORIES_REGEX = "\\/categories\\/";

  /** literal used at the start of tag URIs */
  private static final String TAGS = "/tags/";

  /** literal used at the start of tag URIs, in regex form */
  private static final String TAGS_REGEX = "\\/tags\\/";

  /** literal used at the start of author URIs */
  private static final String AUTHORS = "/authors/";

  /** literal used at the start of tag URIs, in regex form */
  private static final String AUTHORS_REGEX = "\\/authors\\/";

  /** the log used by this class */
  private static Log log = LogFactory.getLog(UriTransformer.class);

  /**
   * Checks for URI patterns and converts them to the appropriate action.
   *
   * @param uri     the initial URI
   * @param blog    the current Blog instance
   *
   * @return    the URI to used to service the original request (could be
   *            the same)
   */
  public String getUri(String uri, Blog blog) {
    PermalinkProvider permalinkProvider = blog.getPermalinkProvider();
    DefaultPermalinkProvider defaultPermalinkProvider = new DefaultPermalinkProvider();
    defaultPermalinkProvider.setBlog(permalinkProvider.getBlog());

    log.trace("URI before transformation : " + uri);

    if (uri == null || uri.trim().equals("")) {
      uri = "/";
    }

    // try to transform the URI with the permalink provider in use
    String result = getUri(uri, permalinkProvider);
    if (result == null) {
      // for backwards compatibility, try the default permalink provider
      result = getUri(uri, defaultPermalinkProvider);
    }

    // if the result is still null, try the other URL patterns to transform the URI
    if (result == null) {
      if (uri.equals("/categories") || uri.equals("/categories/")) {
        // URI of the form /categories/
        result = "/viewCategories.action";
      } else if (uri.matches(CATEGORIES_REGEX + ".*\\/.*xml")) {
          // URI of the form /category[/subcategories]/[rss|rdf|atom].xml
          int indexOfLastSlash = uri.lastIndexOf("/");
          String categoryId = uri.substring(CATEGORIES.length(), indexOfLastSlash);

          if (uri.endsWith("rdf.xml")) {
            result = "/feed.action?category=" + categoryId + "&flavor=rdf";
          } else if (uri.endsWith("atom.xml")) {
            result = "/feed.action?category=" + categoryId + "&flavor=atom";
          } else {
            result = "/feed.action?category=" + categoryId + "&flavor=rss20";
          }
      } else if (uri.startsWith(CATEGORIES)) {
        // URI of the form /categories/category/
        String category = uri.substring(CATEGORIES.length(), uri.length());
        if (category.endsWith("/")) {
          category = category.substring(0, category.length()-1);
        }
        result = "/viewCategory.action?category=" + category;
      } else if (uri.equals("/tags") || uri.equals("/tags/")) {
          // URI of the form /tags/
          result = "/viewTags.action";
      } else if (uri.matches(TAGS_REGEX + ".*\\/.*xml")) {
        // URI of the form /tags/tag/[rss|rdf|atom].xml
        int indexOfLastSlash = uri.lastIndexOf("/");
        String tag = uri.substring(TAGS.length(), indexOfLastSlash);

        if (uri.endsWith("rdf.xml")) {
          result = "/feed.action?tag=" + tag + "&flavor=rdf";
        } else if (uri.endsWith("atom.xml")) {
          result = "/feed.action?tag=" + tag + "&flavor=atom";
        } else {
          result = "/feed.action?tag=" + tag + "&flavor=rss20";
        }
      } else if (uri.matches(AUTHORS_REGEX + ".*\\/.*xml")) {
        // URI of the form /authors/username/[rss|rdf|atom].xml
        int indexOfLastSlash = uri.lastIndexOf("/");
        String author = uri.substring(AUTHORS.length(), indexOfLastSlash);

        if (uri.endsWith("rdf.xml")) {
          result = "/feed.action?author=" + author + "&flavor=rdf";
        } else if (uri.endsWith("atom.xml")) {
          result = "/feed.action?author=" + author + "&flavor=atom";
        } else {
          result = "/feed.action?author=" + author + "&flavor=rss20";
        }
      } else if (uri.startsWith(TAGS)) {
        // URI of the form /tags/tag/
        String tag = uri.substring(TAGS.length(), uri.length());
        if (tag.endsWith("/")) {
          tag = tag.substring(0, tag.length()-1);
        }
        result = "/viewTag.action?tag=" + Tag.encode(tag);
      } else if (uri.startsWith(AUTHORS)) {
        // URI of the form /authors/usename/
        String author = uri.substring(AUTHORS.length(), uri.length());
        if (author.endsWith("/")) {
          author = author.substring(0, author.length()-1);
        }
        result = "/aboutAuthor.action?user=" + author;
      } else if (uri.equals("/pages/") || uri.equals("/pages")) {
        result = "/viewStaticPage.action?name=index";
      } else if (uri.startsWith("/pages/")) {
        // url matches /pages/xyz.html
        String name = uri.substring(7, uri.length()-5);

        result = "/viewStaticPage.action?name=";
        result += name;
      } else if (uri.startsWith("/images/")) {
        // url matches /images/xyz.xyz
        String name = uri.substring(7, uri.length());

        result = "/file.action?type=" + FileMetaData.BLOG_IMAGE + "&name=";
        result += name;
      } else if (uri.startsWith("/files/")) {
        // url matches /files/xyz.xyz
        String name = uri.substring(6, uri.length());

        result = "/file.action?type=" + FileMetaData.BLOG_FILE + "&name=";
        result += name;
      } else if (uri.startsWith("/theme/")) {
        // url matches /files/xyz.xyz
        String name = uri.substring(6, uri.length());

        result = "/file.action?type=" + FileMetaData.THEME_FILE + "&name=";
        result += name;
      } else if (uri.matches("\\/help\\/\\w*\\.html")) {
        // url matches /help/xyz.html
        String name = uri.substring(6, uri.length());

        result = "/viewHelp.secureaction?name=";
        result += name.substring(0, name.length()-5);
      } else if (uri.equals("/help") || uri.equals("/help/")) {
        // url matches /help/
        result = "/viewHelp.secureaction?name=index";
      } else if (uri.equals("/responses/rss.xml")) {
        // url is for a response feed
        result = "/responseFeed.action?flavor=rss20";
      } else if (uri.startsWith("/responses/rss.xml?entry=")) {
        // url is for a response feed
        result = "/responseFeed.action?flavor=rss20&" + uri.substring("/responses/rss.xml?".length());
      } else if (uri.startsWith("/rss.xml")) {
        // url matches rss.xml
        result = "/feed.action?flavor=rss20";
      } else if (uri.startsWith("/feed.xml")) {
        // url matches feed.xml
        result = "/feed.action?flavor=rss20";
      } else if (uri.startsWith("/rdf.xml")) {
        // url matches rdf.xml
        result = "/feed.action?flavor=rdf";
      } else if (uri.startsWith("/responses/atom.xml")) {
        // url is for a response feed
        result = "/responseFeed.action?flavor=atom";
      } else if (uri.startsWith("/atom.xml")) {
        // url matches atom.xml
        result = "/feed.action?flavor=atom";
      } else if (uri.equals("/today.html")) {
        // URI of the form /today.html
        result = "/viewDay.action";
      } else if (uri.equals("/about.html")) {
        // URI of the form /about.html
        result = "/about.action";
      } else if (uri.startsWith("/blogentries/")) {
        // view blog entries by page /blogentries/1.html
        String page = uri.substring(13, uri.length()-5);
        result = "/viewBlogEntriesByPage.action?page=" + page;
      } else if (uri.equals("/") || uri.equals("/index.jsp") || uri.equals("/index.html")) {
          result = "/viewHomePage.action";
      } else {
        result = uri;
      }
    }

    log.trace("URI after transformation : " + result);

    return result;
  }

  /**
   * Checks for URI patterns and converts them to the appropriate action.
   *
   * @param uri     the initial URI
   * @param blog    the current Blog instance
   *
   * @return    the URI to used to service the original request (could be
   *            the same)
   */
  public String getUri(String uri, MultiBlog blog) {
    String result;

    log.trace("URI before transformation : " + uri);

    if (uri == null || uri.trim().equals("")) {
      uri = "/";
    }

    if (uri.startsWith("/rss.xml")) {
      // url matches rss.xml
      result = "/feed.action?flavor=rss20";
    } else if (uri.startsWith("/feed.xml")) {
      // url matches feed.xml
      result = "/feed.action?flavor=rss20";
    } else if (uri.startsWith("/rdf.xml")) {
      // url matches rdf.xml
      result = "/feed.action?flavor=rdf";
    } else if (uri.startsWith("/atom.xml")) {
      // url matches atom.xml
      result = "/feed.action?flavor=atom";
    } else if (uri.equals("/") || uri.equals("/index.jsp") || uri.equals("/index.html")) {
        result = "/viewHomePage.action";
    } else if (uri.matches("\\/help\\/\\w*\\.html")) {
      // url matches /help/xyz.html
      String name = uri.substring(6, uri.length());

      result = "/viewHelp.secureaction?name=";
      result += name.substring(0, name.length()-5);
    } else {
      result = uri;
    }

    log.trace("URI after transformation : " + result);

    return result;
  }

  /**
   * Checks for URI patterns and converts them to the appropriate action, using
   * the specified permalink provider.
   *
   * @param uri   the initial URI
   * @param permalinkProvider   a PermalinkProvider instance to try and
   *                            transform the URI with
   *
   * @return    the URI to used to service the original request (could be
   *            the same)
   */
  private String getUri(String uri, PermalinkProvider permalinkProvider) {
    String result = null;

    if (permalinkProvider.isBlogEntryPermalink(uri)) {
      BlogEntry blogEntry = permalinkProvider.getBlogEntry(uri);
      if (blogEntry != null) {
        result = "/viewBlogEntry.action?entry=" + blogEntry.getId();
      }
    } else if (permalinkProvider.isDayPermalink(uri)) {
      Day day = permalinkProvider.getDay(uri);
      if (day != null) {
        result = "/viewDay.action";
        result += "?year=" + day.getMonth().getYear().getYear();
        result += "&month=" + day.getMonth().getMonth();
        result += "&day=" + day.getDay();
      }
    } else if (permalinkProvider.isMonthPermalink(uri)) {
      Month month = permalinkProvider.getMonth(uri);
      if (month != null) {
        result = "/viewMonth.action";
        result += "?year=" + month.getYear().getYear();
        result += "&month=" + month.getMonth();
      }
    }

    return result;
  }

}