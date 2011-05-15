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

import net.sourceforge.pebble.Configuration;
import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.security.PebbleUserDetails;
import net.sourceforge.pebble.util.HttpsURLRewriter;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.util.UrlRewriter;
import net.sourceforge.pebble.util.Utilities;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.comparator.BlogEntryComparator;
import net.sourceforge.pebble.comparator.BlogByLastModifiedDateComparator;
import net.sourceforge.pebble.decorator.ContentDecoratorChain;
import net.sourceforge.pebble.domain.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * A filter respsonsible for setting up common objects.
 *
 * @author    Simon Brown
 */
public class PreProcessingFilter implements Filter {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(PreProcessingFilter.class);

  /**
   * Initialises this instance.
   *
   * @param config    a FilterConfig instance
   */
  public void init(FilterConfig config) {
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

//    log.info("Session is " + httpRequest.getSession().getId());
//    HttpServletResponse httpResponse = (HttpServletResponse)response;
//    Cookie cookie = CookieUtils.getCookie(httpRequest.getCookies(), "JSESSIONID");
//    if (cookie != null) {
//      log.info("Domain is " + cookie.getDomain());
//      log.info("Path is " + cookie.getPath());
//      cookie.setDomain(".example.com");
//      cookie.setPath("/");
//      httpResponse.addCookie(cookie);
//    }

    String externalUri = (String)request.getAttribute(Constants.EXTERNAL_URI);
    if (externalUri.startsWith("/common/") ||
        externalUri.startsWith("/dwr/") ||
        externalUri.startsWith("/FCKeditor/") ||
        externalUri.startsWith("/scripts/") ||
        externalUri.startsWith("/themes/") ||
        externalUri.equals("/pebble.css") ||
        externalUri.equals("/jckconfig_pebble.js") ||
        externalUri.equals("/favicon.ico")
        ) {
        // do nothing
    } else {
      AbstractBlog blog = (AbstractBlog)request.getAttribute(Constants.BLOG_KEY);
      if (blog instanceof Blog) {
        Blog b = (Blog)blog;
        ContentDecoratorContext context = new ContentDecoratorContext();
        context.setView(ContentDecoratorContext.SUMMARY_VIEW);
        context.setMedia(ContentDecoratorContext.HTML_PAGE);

        List<BlogEntry> blogEntries = b.getRecentPublishedBlogEntries();
        ContentDecoratorChain.decorate(context, blogEntries);
        Collections.sort(blogEntries, new BlogEntryComparator());
        httpRequest.setAttribute(Constants.RECENT_BLOG_ENTRIES, blogEntries);

        List<Response> recentApprovedResponses = b.getRecentApprovedResponses();
        for (Response r : recentApprovedResponses) {
          if (r instanceof Comment) {
            b.getContentDecoratorChain().decorate(context, (Comment)r);
          } else if (r instanceof TrackBack){
            b.getContentDecoratorChain().decorate(context, (TrackBack)r);
          }
        }
        httpRequest.setAttribute(Constants.RECENT_RESPONSES, recentApprovedResponses);

        httpRequest.setAttribute(Constants.CATEGORIES, b.getCategories());
        httpRequest.setAttribute(Constants.TAGS, b.getTags());
        httpRequest.setAttribute(Constants.PLUGIN_PROPERTIES, b.getPluginProperties());
        httpRequest.setAttribute(Constants.ARCHIVES, b.getArchives());
        httpRequest.setAttribute(Constants.BLOG_TYPE, "singleblog");
      } else {
        httpRequest.setAttribute(Constants.BLOG_TYPE, "multiblog");
      }

      if (PebbleContext.getInstance().getConfiguration().isMultiBlog()) {
        httpRequest.setAttribute(Constants.MULTI_BLOG_KEY, BlogManager.getInstance().getMultiBlog());
        httpRequest.setAttribute(Constants.MULTI_BLOG_URL, Utilities.calcBaseUrl(request.getScheme(), BlogManager.getInstance().getMultiBlog().getUrl()));

        List<Blog> blogs = BlogManager.getInstance().getPublicBlogs();
        Collections.sort(blogs, new BlogByLastModifiedDateComparator());
        httpRequest.setAttribute(Constants.BLOGS, blogs);
      }

      // change the character encoding so that we can successfully get
      // international characters from the request when HTML forms are submitted
      // ... but only if the browser doesn't send the character encoding back
      if (request.getCharacterEncoding() == null) {
        request.setCharacterEncoding(blog.getCharacterEncoding());
      }

      PebbleUserDetails user = SecurityUtils.getUserDetails();
      if (user != null) {
        httpRequest.setAttribute(Constants.AUTHENTICATED_USER, user);
      }

      Config.set(request, Config.FMT_LOCALE, blog.getLocale());
      Config.set(request, Config.FMT_FALLBACK_LOCALE, Locale.ENGLISH);
    }

    try {
	  	Configuration configuration = PebbleContext.getInstance().getConfiguration();
	  	if(configuration.getSecureUrl().startsWith("https")) {
	  		UrlRewriter.useThisRewriter(new HttpsURLRewriter(request.getScheme()));
	  	}
	    
	    chain.doFilter(request, response);
    } finally {
    	UrlRewriter.clear();
    }
  }
}