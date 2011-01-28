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
import net.sourceforge.pebble.util.StringUtils;
import net.sourceforge.pebble.web.validation.ValidationContext;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.UserPreferencesView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.openid.OpenIDAuthenticationStatus;
import org.springframework.security.openid.OpenIDAuthenticationToken;
import org.springframework.security.openid.OpenIDConsumer;
import org.springframework.security.openid.OpenIDConsumerException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author James Roper
 */
public class AddOpenIdAction extends SecureAction {
  private static final Log log = LogFactory.getLog(AddOpenIdAction.class);

  @Inject
  private OpenIDConsumer openIDConsumer;
  @Inject
  private SecurityRealm securityRealm;

  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    PebbleUserDetails userDetails = SecurityUtils.getUserDetails();
    ValidationContext validationContext = new ValidationContext();
    AbstractBlog blog = (AbstractBlog)getModel().get(Constants.BLOG_KEY);

    String identity = request.getParameter("openid.identity");

    // No identity, assume this is an add request
    if (identity == null || identity.length() == 0) {
      String claimedIdentity = request.getParameter("openid_identifier");
      try {
        String returnToUrl = request.getRequestURL().toString();
        String realm = PebbleContext.getInstance().getConfiguration().getUrl();
        String openIdUrl = openIDConsumer.beginConsumption(request, claimedIdentity, returnToUrl, realm);
        return new RedirectView(openIdUrl);
      } catch (OpenIDConsumerException oice) {
        log.error("Error adding OpenID", oice);
        validationContext.addError("Error adding OpenID " + oice.getMessage());
      }

    } else {

      try {
        OpenIDAuthenticationToken token = openIDConsumer.endConsumption(request);
        if (token.getStatus() == OpenIDAuthenticationStatus.SUCCESS) {
          // Check that the OpenID isn't already mapped
          String openId = token.getIdentityUrl();
          if (securityRealm.getUserForOpenId(openId) != null) {
            validationContext.addError("The OpenID supplied is already mapped to a user.");
          } else {
            // Add it
            securityRealm.addOpenIdToUser(userDetails, openId);
            return new RedirectView(blog.getUrl() + "/editUserPreferences.secureaction");
          }
        } else {
          validationContext.addError(StringUtils.transformHTML(token.getMessage()));
        }

      } catch (OpenIDConsumerException oice) {
        log.error("Error in consumer", oice);
        validationContext.addError("Error adding OpenID " + oice.getMessage());
      } catch (SecurityRealmException sre) {
        log.error("Error looking up user by security realm", sre);
      }
    }

    getModel().put("user", userDetails);
    getModel().put("validationContext", validationContext);
    return new UserPreferencesView();
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