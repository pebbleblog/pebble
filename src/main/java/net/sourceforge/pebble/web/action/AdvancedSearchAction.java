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
import net.sourceforge.pebble.domain.Tag;
import net.sourceforge.pebble.web.view.ForwardView;
import net.sourceforge.pebble.web.view.View;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Performs a search on the current blog.
 *
 * @author    Simon Brown
 */
public class AdvancedSearchAction extends Action {

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)request.getAttribute(Constants.BLOG_KEY);
    StringBuffer query = new StringBuffer();

    addTerm(query, "title", request.getParameter("title"));
    addTerm(query, "body", request.getParameter("body"));
    addTerms(query, "category", request.getParameterValues("category"));
    addTerm(query, "author", request.getParameter("author"));

    String tags = request.getParameter("tags");
    if (tags != null) {
      String s[] = tags.split(",");
      for (int i = 0; i < s.length; i++) {
        s[i] = Tag.encode(s[i].trim());
      }
      addTerms(query, "tag", s);
    }

    try {
      String encodedQuery = URLEncoder.encode(query.toString(), blog.getCharacterEncoding());
      return new ForwardView("/search.action?query=" + encodedQuery);
    } catch (UnsupportedEncodingException uee) {
      throw new ServletException(uee);
    }
  }

  private void addTerm(StringBuffer query, String key, String value) {
    if (value != null && value.trim().length() > 0) {
      if (query.length() > 0) {
        query.append(" AND ");
      }
      query.append(key + ":" + value.trim());
    }
  }

  private void addTerms(StringBuffer query, String key, String terms[]) {
    if (terms != null) {
      for (int i = 0; i < terms.length; i++) {
        addTerm(query, key, terms[i]);
      }
    }
  }

}