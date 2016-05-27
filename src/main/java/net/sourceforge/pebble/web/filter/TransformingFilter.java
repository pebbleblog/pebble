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

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogManager;
import net.sourceforge.pebble.domain.MultiBlog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * A filter that transforms an incoming URI into an action URI.
 *
 * @author    Simon Brown
 */
public class TransformingFilter implements Filter {

  /** the config of this filter */
  private FilterConfig filterConfig;

  /** the log used by this class */
  private static Log log = LogFactory.getLog(TransformingFilter.class);

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
    AbstractBlog blog = (AbstractBlog)request.getAttribute(Constants.BLOG_KEY);

    String originalUri = httpRequest.getRequestURI();

    // get URI and strip off the context (e.g. /blog)
    String uri = originalUri.substring(httpRequest.getContextPath().length(), originalUri.length());


    // now we're left with a URI
    if (BlogManager.getInstance().isMultiBlog() && !PebbleContext.getInstance().getConfiguration().isVirtualHostingEnabled()) {
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
        uri = uri.substring(index, uri.length());
      }
    }

    if (uri == null || uri.trim().equals("")) {
      uri = "/";
    }

    log.trace("uri : " + uri);

    // Only add the query string to external URI, otherwise each parameter in the query string will end up being put in
    // twice to the parameter map.
    String externalUri = uri;
    if (httpRequest.getQueryString() != null) {
      externalUri += "?" + httpRequest.getQueryString();
      originalUri += "?" + httpRequest.getQueryString();
    }
    httpRequest.setAttribute(Constants.ORIGINAL_URI, originalUri);
    httpRequest.setAttribute(Constants.EXTERNAL_URI, externalUri);

    UriTransformer transformer = new UriTransformer();
    String internalUri;

    if (blog instanceof Blog) {
      internalUri = transformer.getUri(uri, (Blog)blog);
    } else {
      internalUri = transformer.getUri(uri, (MultiBlog)blog);
    }

    request.setAttribute(Constants.INTERNAL_URI, internalUri);

    log.trace(uri + " -> " + internalUri);

    chain.doFilter(request, response);
  }

}
