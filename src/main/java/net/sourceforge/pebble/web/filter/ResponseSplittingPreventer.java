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
package net.sourceforge.pebble.web.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;

/**
 * Filter that protects against HTTP response splitting
 *
 * @author James Roper
 */
public class ResponseSplittingPreventer implements Filter {
  public void init(FilterConfig filterConfig) throws ServletException {
  }

  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
    if (servletResponse instanceof HttpServletResponse) {
      servletResponse = new ResponseSplittingPreventingResponse((HttpServletResponse) servletResponse);
    }
    filterChain.doFilter(servletRequest, servletResponse);
  }

  public void destroy() {
  }

  private static class ResponseSplittingPreventingResponse extends HttpServletResponseWrapper {
    private ResponseSplittingPreventingResponse(HttpServletResponse response) {
      super(response);
    }

    @Override
    public void setHeader(String name, String value) {
      super.setHeader(name, check(value));
    }

    @Override
    public void addHeader(String name, String value) {
      super.addHeader(name, check(value));
    }

    @Override
    public void sendRedirect(String location) throws IOException {
      super.sendRedirect(check(location));
    }

    private String check(String value) {
      for (int i = 0; i < value.length(); i++) {
        char c = value.charAt(i);
        if (c == '\n' || c == '\r') {
          throw new IllegalArgumentException("Carriage return and line feed characters are not allowed in HTTP headers");
        }
      }
      return value;
    }
  }
}
