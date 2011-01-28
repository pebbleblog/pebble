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
import net.sourceforge.pebble.web.security.RequireSecurityToken;
import net.sourceforge.pebble.web.validation.ValidationContext;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.UserView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;
import java.util.Enumeration;

/**
 * Saves user details (this is the blog admin version, where roles
 * can be changed).
 *
 * @author    Simon Brown
 */
@RequireSecurityToken
public class SaveUserAction extends SecureAction {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(SaveUserAction.class);

  private static final String PREFERENCE = "preference.";

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
      String username = request.getParameter("username");
      String password1 = request.getParameter("password1");
      String password2 = request.getParameter("password2");
      String name = request.getParameter("name");
      String emailAddress = request.getParameter("emailAddress");
      String website = request.getParameter("website");
      String profile = request.getParameter("profile");
      String roles[] = request.getParameterValues("role");
      boolean newUser = request.getParameter("newUser").equalsIgnoreCase("true");
      String detailsUpdateableAsString = request.getParameter("detailsUpdateable");
      boolean detailsUpdateable = detailsUpdateableAsString != null && detailsUpdateableAsString.equalsIgnoreCase("true");
      Map<String,String> preferences = new HashMap<String,String>();
      Enumeration parameterNames = request.getParameterNames();
      while (parameterNames.hasMoreElements()) {
        String parameterName = (String)parameterNames.nextElement();
        if (parameterName.startsWith(PREFERENCE)) {
          preferences.put(parameterName.substring(PREFERENCE.length()), request.getParameter(parameterName));
        }
      }

      SecurityRealm realm = PebbleContext.getInstance().getConfiguration().getSecurityRealm();
      PebbleUserDetails currentUserDetails = realm.getUser(username);
      PebbleUserDetails newUserDetails = new PebbleUserDetails(username, password1, name, emailAddress, website, profile, roles, preferences, detailsUpdateable);

      ValidationContext validationContext = new ValidationContext();

      if (newUser && currentUserDetails != null) {
        validationContext.addError("A user with this username already exists");
      } else if (newUser && (username == null || username.trim().length() == 0)) {
        validationContext.addError("Username can't be empty");
      } else if (password1 != null && password1.length() > 0 && !password1.equals(password2)) {
        validationContext.addError("Passwords must match");
      } else {

        if (newUser) {
          try {
            realm.createUser(newUserDetails);
          } catch (SecurityRealmException sre) {
            validationContext.addError(sre.getMessage());
          }
        } else {
          realm.updateUser(newUserDetails);
          if (password1 != null && password1.length() > 0) {
            realm.changePassword(username, password1);
          }
        }
        return new RedirectView(blog.getUrl() + "viewUsers.secureaction");
      }

      getModel().put("validationContext", validationContext);
      getModel().put("user", newUserDetails);
      getModel().put("newUser", newUser);

      return new UserView();
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
    return new String[]{Constants.BLOG_ADMIN_ROLE};
  }

}
