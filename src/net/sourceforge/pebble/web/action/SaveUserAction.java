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
package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.security.PebbleUserDetails;
import net.sourceforge.pebble.security.SecurityRealm;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.impl.UserView;
import net.sourceforge.pebble.web.validation.ValidationContext;
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
public class SaveUserAction extends SecureAction {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(SaveUserAction.class);

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    AbstractBlog blog = (AbstractBlog)getModel().get(Constants.BLOG_KEY);
    String username = request.getParameter("username");
    String password = request.getParameter("password1");
    String confirm = request.getParameter("password2");
    String name = request.getParameter("name");
    String emailAddress = request.getParameter("emailAddress");
    String website = request.getParameter("website");
    String roles[] = request.getParameterValues("role");
    String newUser = request.getParameter("newUser");

    SecurityRealm realm = PebbleContext.getInstance().getConfiguration().getSecurityRealm();
    PebbleUserDetails currentUserDetails = realm.getUser(username);
    PebbleUserDetails newUserDetails = new PebbleUserDetails(username, password, name, emailAddress, website, roles);

    ValidationContext validationContext = new ValidationContext();

    if (newUser != null && newUser.equals("true") && currentUserDetails != null) {
      validationContext.addError("A user with this username already exists");          
    } else if (password == null || password.trim().length() == 0) {
      validationContext.addError("Password can't be empty");
    } else if (!password.equals(confirm)) {
      validationContext.addError("Password and confirmation password must be the same");
    } else {
      newUserDetails = new PebbleUserDetails(username, password, name, emailAddress, website, roles);
      realm.putUser(newUserDetails);
      return new RedirectView(blog.getUrl() + "viewUsers.secureaction");
    }

    getModel().put("validationContext", validationContext);
    getModel().put("user", newUserDetails);
    getModel().put("newUser", newUser);

    return new UserView();
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
