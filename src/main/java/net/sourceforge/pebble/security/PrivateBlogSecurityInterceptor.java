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
package net.sourceforge.pebble.security;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.*;
import java.io.IOException;

/**
 * Specialised FilterSecurityInterceptor that returns its own type of
 * ObjectDefinitionSource. This is acopy-paste job from Acegi's
 * FilterSecurityInterceptor. :-(
 *
 * @author Simon Brown
 */
public class PrivateBlogSecurityInterceptor extends AbstractSecurityInterceptor implements Filter {

  private static final String FILTER_APPLIED = "__spring_security_privateBlogSecurityInterceptor_filterApplied";

  //~ Instance fields ================================================================================================

  private boolean observeOncePerRequest = true;

  //~ Methods ========================================================================================================

  /**
   * Not used (we rely on IoC container lifecycle services instead)
   */
  public void destroy() {}

  /**
   * Method that is actually called by the filter chain. Simply delegates to the {@link
   * #invoke(FilterInvocation)} method.
   *
   * @param request the servlet request
   * @param response the servlet response
   * @param chain the filter chain
   *
   * @throws IOException if the filter chain fails
   * @throws ServletException if the filter chain fails
   */
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
      FilterInvocation fi = new FilterInvocation(request, response, chain);
      invoke(fi);
  }

  public Class getSecureObjectClass() {
      return FilterInvocation.class;
  }

  /**
   * Not used (we rely on IoC container lifecycle services instead)
   *
   * @param arg0 ignored
   *
   * @throws ServletException never thrown
   */
  public void init(FilterConfig arg0) throws ServletException {}

  public void invoke(FilterInvocation fi) throws IOException, ServletException {
      if ((fi.getRequest() != null) && (fi.getRequest().getAttribute(FILTER_APPLIED) != null)
          && observeOncePerRequest) {
          // filter already applied to this request and user wants us to observce
          // once-per-request handling, so don't re-do security checking
          fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
      } else {
          // first time this request being called, so perform security checking
          if (fi.getRequest() != null) {
              fi.getRequest().setAttribute(FILTER_APPLIED, Boolean.TRUE);
          }

          InterceptorStatusToken token = super.beforeInvocation(fi);

          try {
              fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
          } finally {
              super.afterInvocation(token, null);
          }
      }
  }

  /**
   * Indicates whether once-per-request handling will be observed. By default this is <code>true</code>,
   * meaning the <code>FilterSecurityInterceptor</code> will only execute once-per-request. Sometimes users may wish
   * it to execute more than once per request, such as when JSP forwards are being used and filter security is
   * desired on each included fragment of the HTTP request.
   *
   * @return <code>true</code> (the default) if once-per-request is honoured, otherwise <code>false</code> if
   *         <code>FilterSecurityInterceptor</code> will enforce authorizations for each and every fragment of the
   *         HTTP request.
   */
  public boolean isObserveOncePerRequest() {
      return observeOncePerRequest;
  }

  public void setObserveOncePerRequest(boolean observeOncePerRequest) {
      this.observeOncePerRequest = observeOncePerRequest;
  }

  @Override
  public SecurityMetadataSource obtainSecurityMetadataSource() {
    return new PrivateBlogSecurityMetadataSource();
  }
}