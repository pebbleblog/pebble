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
package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.comparator.BlogEntryComparator;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.web.view.ForwardView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.AbstractRomeFeedView;
import net.sourceforge.pebble.web.view.impl.FeedView;
import net.sourceforge.pebble.web.view.impl.RdfView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Exports an entire blog as RSS/RDF/Atom.
 *
 * @author    Simon Brown
 */
public class ExportBlogAction extends SecureAction {

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    String flavor = request.getParameter("flavor");

    if (flavor != null && flavor.equalsIgnoreCase("zip")) {
      return new ForwardView("/zipDirectory.secureaction?type=blogData");
    }

    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);

    response.setContentType("application/xml; charset=" + blog.getCharacterEncoding());

    List<BlogEntry> blogEntries = blog.getBlogEntries();
    Collections.sort(blogEntries, new BlogEntryComparator());
    getModel().put(Constants.BLOG_ENTRIES, blogEntries);

    // set the locale of this feed request to be English
    javax.servlet.jsp.jstl.core.Config.set(
        request,
        javax.servlet.jsp.jstl.core.Config.FMT_LOCALE,
        Locale.ENGLISH);

    if (flavor != null && flavor.equalsIgnoreCase("atom")) {
      return new FeedView(AbstractRomeFeedView.FeedType.ATOM);
    } else if (flavor != null && flavor.equalsIgnoreCase("rdf")) {
      return new RdfView();
    } else {
      return new FeedView(AbstractRomeFeedView.FeedType.RSS);
    }
  }

  /**
   * Gets a list of all roles that are allowed to access this action.
   *
   * @return  an array of Strings representing role names
   * @param request The request
   */
  public String[] getRoles(HttpServletRequest request) {
    return new String[]{Constants.BLOG_ADMIN_ROLE, Constants.BLOG_OWNER_ROLE};
  }

}