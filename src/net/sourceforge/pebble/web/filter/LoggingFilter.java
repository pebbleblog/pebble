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
import net.sourceforge.pebble.domain.FileMetaData;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.util.SecurityUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * A filter that logs certain incoming requests.
 *
 * @author    Simon Brown
 */
public class LoggingFilter implements Filter {

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
    AbstractBlog abstractBlog = (AbstractBlog)request.getAttribute(Constants.BLOG_KEY);
    String internalUri = (String)request.getAttribute(Constants.INTERNAL_URI);
    String externalUri = (String)request.getAttribute(Constants.EXTERNAL_URI);

    if (abstractBlog instanceof Blog) {
      Blog blog = (Blog)abstractBlog;

      // don't log owner/contributor/admin visits
      if (!SecurityUtils.isBlogOwner() && !SecurityUtils.isBlogContributor() && !SecurityUtils.isPebbleAdmin()) {
        // this is the list of URIs that get logged
        if (internalUri.startsWith("/feed.action") ||
            internalUri.startsWith("/responseFeed.action") ||
            internalUri.startsWith("/viewCategory.action") ||
            internalUri.startsWith("/viewTags.action") ||
            internalUri.startsWith("/viewTag.action") ||
            internalUri.startsWith("/search.action") ||
            internalUri.startsWith("/viewBlogEntry.action") ||
            internalUri.startsWith("/viewStaticPage.action") ||
            internalUri.startsWith("/viewDay.action") ||
            internalUri.startsWith("/viewMonthlyBlog.action") ||
            internalUri.startsWith("/file.action?type=" + FileMetaData.BLOG_FILE) ||
            (internalUri.startsWith("/viewHomePage.action") && blog instanceof Blog)) {

          blog.log(httpRequest);
        }
      }
    }

    chain.doFilter(request, response);
  }

}
