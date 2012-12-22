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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.jsp.jstl.core.Config;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Collections;
import java.util.Locale;

import net.sourceforge.pebble.Configuration;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.comparator.BlogEntryComparator;
import net.sourceforge.pebble.decorator.ContentDecoratorChain;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.*;

/**
 * A filter respsonsible for setting up the blog object.
 *
 * @author    Simon Brown
 */
public class BlogLookupFilter implements Filter {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(BlogLookupFilter.class);

  /** the config of this filter */
  private FilterConfig filterConfig;

  /**
   * Initialises this instance.
   *
   * @param config    a FilterConfig instance
   */
  public void init(FilterConfig config) {
    this.filterConfig = config;
  }

  /**
   * Called when this filter is taken out of service.
   */
  public void destroy() {
  }

  /**
   * Contains the processing associated with this filter.
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    HttpServletRequest httpRequest = (HttpServletRequest)request;
    PebbleContext pebbleContext = PebbleContext.getInstance();
    AbstractBlog blog;

    String url = pebbleContext.getConfiguration().getUrl();
    if (pebbleContext != null && (url == null || url.length() == 0)) {
      String scheme = httpRequest.getScheme();
      url = scheme + "://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + httpRequest.getContextPath();
      log.info("Setting Pebble URL to " + url);
      PebbleContext.getInstance().getConfiguration().setUrl(url);
    }

    // get URI and strip off the context (e.g. /blog)
    String uri = httpRequest.getRequestURI();
    uri = uri.substring(httpRequest.getContextPath().length(), uri.length());

    // now we're left with a URI
    if (BlogManager.getInstance().isMultiBlog()) {
      if (uri.length() == 0) {
        uri = "/";
      }
      int index = uri.indexOf("/", 1);
      if (index == -1) {
        index = uri.length();
      }

      String blogName = null;
      Configuration config = pebbleContext.getConfiguration();
      if (config.isVirtualHostingEnabled()) {
        String serverName = httpRequest.getServerName();
        if (config.isVirtualHostingSubdomain()) {
          int index2 = serverName.indexOf(".");
          if (index2 < 0) index2 = serverName.length();
          blogName = serverName.substring(0, index2);
        } else {
          blogName = serverName;
        }
      } else {
        blogName = uri.substring(1, index);
      }

      blogName = URLDecoder.decode(blogName, "UTF-8");
      if (BlogManager.getInstance().hasBlog(blogName)) {
        blog = BlogManager.getInstance().getBlog(blogName);
        uri = uri.substring(index, uri.length());
      } else {
        blog = BlogManager.getInstance().getMultiBlog();
      }
    } else {
      blog = BlogManager.getInstance().getBlog();
    }

    httpRequest.setAttribute(Constants.BLOG_KEY, blog);
    httpRequest.setAttribute(Constants.BLOG_MANAGER, BlogManager.getInstance());
    httpRequest.setAttribute(Constants.PEBBLE_CONTEXT, pebbleContext);

    if (blog instanceof Blog) {
      httpRequest.setAttribute(Constants.BLOG_TYPE, "singleblog");
    } else {
      httpRequest.setAttribute(Constants.BLOG_TYPE, "multiblog");
    }

    chain.doFilter(request, response);
  }
}
