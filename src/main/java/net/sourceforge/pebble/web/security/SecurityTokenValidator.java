/*
 * Copyright (c) 2003-2005, Simon Brown
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
package net.sourceforge.pebble.web.security;

import net.sourceforge.pebble.web.action.Action;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.SecureRandom;

/**
 * Checks requests for a security token
 *
 * @author James Roper
 */
public class SecurityTokenValidator {

  /**
   * the security token name
   */
  public static final String PEBBLE_SECURITY_TOKEN_PARAMETER = "pebbleSecurityToken";

  /**
   * the header for bypassing security token checks
   */
  private static final String PEBBLE_SECURITY_TOKEN_HEADER = "X-Pebble-Token";

  /**
   * the value the header should be for not checking
   */
  private static final String PEBBLE_SECURITY_TOKEN_HEADER_NOCHECK = "nocheck";

  /**
   * For generating secure tokens
   */
  private static final SecureRandom random = new SecureRandom();

  /**
   * Validate the security token for this request, if necessary, setting up the security token cookie if it doesn't
   * exist
   *
   * @param request The request to validate
   * @param response The response
   * @param action The action to validate
   * @return true if the request can proceed, false if not
   */
  public boolean validateSecurityToken(HttpServletRequest request, HttpServletResponse response, Action action) {
    // First, ensure that there is a security token, for future requests
    String token = ensureSecurityTokenExists(request, response);
    if (shouldValidate(action, request)) {
      // Check for the header is there... XSRF attacks can't set custom headers, so if this header is there,
      // it must be safe
      if (PEBBLE_SECURITY_TOKEN_HEADER_NOCHECK.equals(request.getHeader(PEBBLE_SECURITY_TOKEN_HEADER))) {
        return true;
      }
      // We must validate the token
      String requestToken = request.getParameter(PEBBLE_SECURITY_TOKEN_PARAMETER);
      // Compare token to cookie
      return token.equals(requestToken);
    } else {
      return true;
    }
  }

  private boolean shouldValidate(Action action, HttpServletRequest request)
  {
    RequireSecurityToken annotation = action.getClass().getAnnotation(RequireSecurityToken.class);
    if (annotation != null) {
      // Check for a condition
      Class<? extends SecurityTokenValidatorCondition> condition = annotation.value();
      if (condition != null && condition != NullSecurityTokenValidatorCondition.class) {
        // Instantiate condition
        try {
          return condition.newInstance().shouldValidate(request);
        } catch (IllegalAccessException iae) {
          throw new RuntimeException("Could not instantiate " + condition);
        } catch (InstantiationException ie) {
          throw new RuntimeException("Could not instantiate " + condition);
        }
      }
      // Otherwise, with no condition we should return validate
      return true;
    } else {
      // We have no annotation, don't validate
      return false;
    }
  }

  private String ensureSecurityTokenExists(HttpServletRequest request, HttpServletResponse response) {
    String token = (String) request.getAttribute(PEBBLE_SECURITY_TOKEN_PARAMETER);
    if (token != null) {
      // We've already configured it for this request
      return token;
    }
    String contextPath = request.getContextPath();
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (PEBBLE_SECURITY_TOKEN_PARAMETER.equals(cookie.getName())) {
          if (contextPath != null && contextPath.length() > 0) {
            if (contextPath.equals(cookie.getPath())) {
              token = cookie.getValue();
              break;
            }
          } else {
            token = cookie.getValue();
            break;
          }
        }
      }
    }
    // No cookie, generate a token at least 12 characters long
    if (token == null) {
      token = "";
      while (token.length() < 12) {
        token += Long.toHexString(random.nextLong());
      }
      // Set the cookie
      Cookie cookie = new Cookie(PEBBLE_SECURITY_TOKEN_PARAMETER, token);
      // Non persistent
      cookie.setMaxAge(-1);
      cookie.setPath(contextPath);
      response.addCookie(cookie);
    }
    // Set it as a request attribute so the security token tag can find it
    request.setAttribute(PEBBLE_SECURITY_TOKEN_PARAMETER, token);
    return token;
  }
  
}
