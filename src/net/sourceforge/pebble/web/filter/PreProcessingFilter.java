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
package net.sourceforge.pebble.web.filter;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.domain.BlogManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Locale;
import java.net.URLDecoder;

/**
 * A filter respsonsible for setting up common objects.
 *
 * @author    Simon Brown
 */
public class PreProcessingFilter implements Filter {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(PreProcessingFilter.class);

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
    HttpServletResponse httpResponse = (HttpServletResponse)response;

    PebbleContext pebbleContext = null;
    if (filterConfig.getServletContext() != null) {
      ApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(filterConfig.getServletContext());
      pebbleContext = (PebbleContext)applicationContext.getBean("pebbleContext");
      request.setAttribute("pebbleContext", pebbleContext);
    }

    AbstractBlog blog;

    if (pebbleContext != null && (pebbleContext.getUrl() == null || pebbleContext.getUrl().length() == 0)) {
      String scheme = httpRequest.getScheme();
      String url = scheme + "://" + httpRequest.getServerName() + ":" + httpRequest.getServerPort() + httpRequest.getContextPath();
      log.info("Setting Pebble URL to " + url);
      pebbleContext.setUrl(url);
    }

    // get URI and strip off the context (e.g. /blog)
    String uri = httpRequest.getRequestURI();
    uri = uri.substring(httpRequest.getContextPath().length(), uri.length());

    // now we're left with a URI
    if (BlogManager.getInstance().isMultiUser()) {
      if (uri.length() == 0) {
        uri = "/";
      }
      int index = uri.indexOf("/", 1);
      if (index == -1) {
        index = uri.length();
      }
      String blogName = uri.substring(1, index);
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

    if (uri == null || uri.trim().equals("")) {
      uri = "/";
    }
    log.debug("uri : " + uri);

    httpRequest.setAttribute(Constants.BLOG_KEY, blog);
    httpRequest.setAttribute(Constants.EXTERNAL_URI, uri);
    httpRequest.setAttribute("blogManager", BlogManager.getInstance());

    // change the character encoding so that we can successfully get
    // international characters from the request when HTML forms are submitted
    // ... but only if the browser doesn't send the character encoding back
    if (request.getCharacterEncoding() == null) {
      request.setCharacterEncoding(blog.getCharacterEncoding());
    }

    Config.set(request, Config.FMT_LOCALE, blog.getLocale());
    Config.set(request, Config.FMT_FALLBACK_LOCALE, Locale.ENGLISH);

    httpResponse.setHeader("Cache-Control","no-cache, no-store");
    httpResponse.setDateHeader("Expires", 0);
    httpResponse.setHeader("Pragma","no-cache");

    chain.doFilter(request, response);
  }
}
