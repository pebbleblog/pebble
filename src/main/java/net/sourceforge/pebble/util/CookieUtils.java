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
package net.sourceforge.pebble.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * Utilities for finding and manipulating cookies.
 *
 * @author    Simon Brown
 */
public class CookieUtils {

  /** represents 4 weeks in seconds */
  public static final int ONE_MONTH = 60 * 60 * 24 * 28;

  /**
   * Gets a reference to a named cookie from an array of cookies.
   *
   * @param cookies   an array of cookies
   * @param name      the name of the cookie to find
   * @return    a reference to a Cookie, or null if the cookie couldn't be found
   */
  public static Cookie getCookie(Cookie cookies[], String name) {

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals(name)) {
          return cookie;
        }
      }
    }

    // we've got this far so the specified cookie wasn't found
    return null;
  }

  /**
   * Adds a cookie with the specified name, value and expiry.
   *
   * @param response    the HttpServletResponse to add the cookie to
   * @param name        the name of the cookie
   * @param value       the value of the cookie
   * @param maxAge      the maxAge of the cookie (in seconds)
   */
  public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value);
    cookie.setMaxAge(maxAge);
    response.addCookie(cookie);
  }

  /**
   * Removes a cookie with the specified name.
   *
   * @param response    the HttpServletResponse to remove the cookie from
   * @param name        the name of the cookie
   */
  public static void removeCookie(HttpServletResponse response, String name) {
    addCookie(response, name, "", 0);
  }

}
