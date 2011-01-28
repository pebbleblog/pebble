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
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.search.SearchException;
import net.sourceforge.pebble.search.SearchHit;
import net.sourceforge.pebble.search.SearchResults;
import net.sourceforge.pebble.util.Pageable;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.AdvancedSearchView;
import net.sourceforge.pebble.web.view.impl.SearchResultsView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

/**
 * Performs a search on the current blog.
 *
 * @author    Simon Brown
 */
public class SearchAction extends Action {

  /** the log used for this action */
  private static final Log log = LogFactory.getLog(SearchAction.class);

  /** the number of results to show per page */
  static final int PAGE_SIZE = 20;


  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {

    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    String query = request.getParameter("query");

    if (query == null || query.trim().length() == 0) {
      if (blog instanceof Blog) {
        return new AdvancedSearchView();
      }
    }

    String pageAsString = request.getParameter("page");
    int page = 1;
    if (pageAsString == null || pageAsString.length() == 0) {
      page = 1;
    } else {
      try {
        page = Integer.parseInt(pageAsString);
      } catch (NumberFormatException nfe) {
      }
    }

    try {
      SearchResults results = blog.getSearchIndex().search(query);

      if (results.getNumberOfHits() == 1) {
        // if there is only one hit, redirect the user to it without the
        // search results page
        SearchHit hit = (SearchHit)results.getHits().get(0);
        return new RedirectView(hit.getPermalink());
      } else {
        // show all results on the search results page
        String sort = request.getParameter("sort");
        if (sort != null && sort.equalsIgnoreCase("date")) {
          results.sortByDateDescending();
        } else {
          results.sortByScoreDescending();
        }

        Pageable pageable = new Pageable(results.getHits());
        pageable.setPageSize(PAGE_SIZE);
        pageable.setPage(page);

        try {
          getModel().put("searchResults", results);
          getModel().put("pageable", pageable);
          getModel().put("query", java.net.URLEncoder.encode(query, blog.getCharacterEncoding()));
        } catch (UnsupportedEncodingException uee) {
          log.error(uee);
        }

        return new SearchResultsView();
      }
    } catch (SearchException se) {
      throw new ServletException(se);
    }
  }

}