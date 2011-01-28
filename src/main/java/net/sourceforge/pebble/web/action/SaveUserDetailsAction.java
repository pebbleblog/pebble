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
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.security.PebbleUserDetails;
import net.sourceforge.pebble.security.SecurityRealm;
import net.sourceforge.pebble.security.SecurityRealmException;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.web.security.RequireSecurityToken;
import net.sourceforge.pebble.web.validation.ValidationContext;
import net.sourceforge.pebble.web.view.ForwardView;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.FourZeroThreeView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Saves user details.
 *
 * @author    Simon Brown
 */
@RequireSecurityToken
public class SaveUserDetailsAction extends SecureAction {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(SaveUserDetailsAction.class);

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    try {
      AbstractBlog blog = (AbstractBlog)getModel().get(Constants.BLOG_KEY);

      String name = request.getParameter("name");
      String emailAddress = request.getParameter("emailAddress");
      String website = request.getParameter("website");
      String profile = request.getParameter("profile");

      PebbleUserDetails currentUserDetails = SecurityUtils.getUserDetails();

      // can the user change their user details?
      if (!currentUserDetails.isDetailsUpdateable()) {
        return new FourZeroThreeView();
      }

      SecurityRealm realm = PebbleContext.getInstance().getConfiguration().getSecurityRealm();
      PebbleUserDetails newUserDetails;

      ValidationContext validationContext = new ValidationContext();

      if (!validationContext.hasErrors()) {
      newUserDetails = new PebbleUserDetails(
          currentUserDetails.getUsername(),
          name,
          emailAddress,
          website,
          profile,
          currentUserDetails.getRoles(),
          currentUserDetails.getPreferences(),
          currentUserDetails.isDetailsUpdateable());

          realm.updateUser(newUserDetails);

          return new RedirectView(blog.getUrl() + "editUserDetails.secureaction");
      }

      getModel().put("validationContext", validationContext);
      return new ForwardView("/editUserDetails.secureaction");
    } catch (SecurityRealmException e) {
      throw new ServletException(e);
    }
  }

  /**
   * Gets a list of all roles that are allowed to access this action.
   *
   * @return  an array of Strings representing role names
   * @param request
   */
  public String[] getRoles(HttpServletRequest request) {
    return new String[]{Constants.ANY_ROLE};
  }

}
