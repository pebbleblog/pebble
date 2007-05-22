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
package net.sourceforge.pebble.logging;

import net.sourceforge.pebble.api.permalink.PermalinkProvider;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.permalink.DefaultPermalinkProvider;


/**
 * Represents a requested URL along with a count of how many times it
 * has been accessed.
 *
 * @author    Simon Brown
 */
public class Request extends CountedUrl {

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
    } else if (url.indexOf("rss.xml") > -1) {
      setName("Feed : RSS 2.0");
    } else if (url.indexOf("feed.xml") > -1 || url.indexOf("feed.action") > -1) {
      setName("Feed : RSS 2.0");
    } else if (url.indexOf("rssWithCommentsAndTrackBacks.xml") > -1) {
      setName("Feed : RSS 2.0 (with comments and TrackBacks)");
    } else if (url.indexOf("rdf.xml") > -1) {
      setName("Feed : RDF 1.0");
    } else if (url.indexOf("atom.xml") > -1) {
      setName("Feed : Atom 1.0");
    } else if (blog != null) {
      if (url.equals(blog.getContext())) {
        setName("Home");
      } else {
        PermalinkProvider permalinkProvider = blog.getPermalinkProvider();
        DefaultPermalinkProvider defaultPermalinkProvider = new DefaultPermalinkProvider();
        defaultPermalinkProvider.setBlog(permalinkProvider.getBlog());
        String uri = url.substring(blog.getContext().length()-1);

        if (permalinkProvider.isBlogEntryPermalink(uri)) {
          BlogEntry blogEntry = permalinkProvider.getBlogEntry(uri);
          if (blogEntry != null) {
            setName("Blog Entry : " + blogEntry.getTitle());
          }
        } else if (defaultPermalinkProvider.isBlogEntryPermalink(uri)) {
          BlogEntry blogEntry = defaultPermalinkProvider.getBlogEntry(uri);
          if (blogEntry != null) {
            setName("Blog Entry : " + blogEntry.getTitle());
          }
        }
      }
    }

    if (getName() == null) {
      setName(url);
    }
  }

}