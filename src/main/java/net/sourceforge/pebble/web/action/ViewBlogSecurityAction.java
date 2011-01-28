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
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.security.PebbleUserDetails;
import net.sourceforge.pebble.security.SecurityRealmException;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.BlogSecurityView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Edits the security properties associated with the current Blog.
 *
 * @author    Simon Brown
 */
public class ViewBlogSecurityAction extends SecureAction {

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    try {
      getModel().put("blogOwnerUsers", filterUsersByRole(PebbleContext.getInstance().getConfiguration().getSecurityRealm().getUsers(), Constants.BLOG_OWNER_ROLE));
      getModel().put("blogPublisherUsers", filterUsersByRole(PebbleContext.getInstance().getConfiguration().getSecurityRealm().getUsers(), Constants.BLOG_PUBLISHER_ROLE));
      getModel().put("blogContributorUsers", filterUsersByRole(PebbleContext.getInstance().getConfiguration().getSecurityRealm().getUsers(), Constants.BLOG_CONTRIBUTOR_ROLE));
      getModel().put("allUsers", PebbleContext.getInstance().getConfiguration().getSecurityRealm().getUsers());
    } catch (SecurityRealmException sre) {
      throw new ServletException("Could not get list of users", sre);
    }

    return new BlogSecurityView();
  }

  private List<PebbleUserDetails> filterUsersByRole(Collection<PebbleUserDetails> users, String role) {
    List<PebbleUserDetails> list = new LinkedList<PebbleUserDetails>();
    for (PebbleUserDetails user : users) {
      if (user.isUserInRole(role)) {
        list.add(user);
      }
    }

    return list;
  }

  /**
   * Gets a list of all roles that are allowed to access this action.
   *
   * @return  an array of Strings representing role names
   * @param request   the originating request
   */
  public String[] getRoles(HttpServletRequest request) {
    return new String[]{
        Constants.BLOG_ADMIN_ROLE,
        Constants.BLOG_OWNER_ROLE
    };
  }

}