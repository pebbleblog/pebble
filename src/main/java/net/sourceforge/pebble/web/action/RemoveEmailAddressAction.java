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
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.web.security.RequireSecurityToken;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.NotFoundView;
import net.sourceforge.pebble.web.view.impl.RemoveEmailAddressConfirmationView;
import net.sourceforge.pebble.web.view.impl.RemoveEmailAddressView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Iterator;

/**
 * Removes an email address subscription to blog comments
 *
 * @author    Simon Brown
 */
public class RemoveEmailAddressAction extends Action {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(RemoveEmailAddressAction.class);

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    BlogEntry blogEntry = null;
    Comment comment = null;

    String entry = request.getParameter("entry");
    String email = request.getParameter("email");

    BlogService service = new BlogService();
    try {
      blogEntry = service.getBlogEntry(blog, entry);
    } catch (BlogServiceException e) {
      throw new ServletException(e);
    }
    if (blogEntry == null) {
      return new NotFoundView();
    }

    getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
    
    if (email != null && email.length() > 0) {

      Iterator it = blogEntry.getComments().iterator();
      while (it.hasNext()) {
        comment = (Comment)it.next();

        if (comment.getEmail() != null && comment.getEmail().equals(email)) {
          comment.setEmail(null);
        }
      }

      try {
        service.putBlogEntry(blogEntry);
      } catch (BlogServiceException be) {
        log.error(be.getMessage(), be);
        throw new ServletException(be);
      }

      return new RemoveEmailAddressConfirmationView();
    } else {
      return new RemoveEmailAddressView();
    }
  }
}