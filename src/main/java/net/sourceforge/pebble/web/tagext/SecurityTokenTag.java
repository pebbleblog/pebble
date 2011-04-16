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
package net.sourceforge.pebble.web.tagext;

import net.sourceforge.pebble.web.security.SecurityTokenValidatorImpl;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Tag that writes the security token as a hidden input parameter to the request
 * @author James Roper
 */
public class SecurityTokenTag extends TagSupport {

  /** true if we're rendering for a query */
  private boolean query;

  @Override
  public int doStartTag() throws JspException {
    JspWriter out = pageContext.getOut();
    String token = (String) pageContext.getRequest().getAttribute(SecurityTokenValidatorImpl.PEBBLE_SECURITY_TOKEN_PARAMETER);
    if (token != null) {
      try {
        if (query) {
          out.append(SecurityTokenValidatorImpl.PEBBLE_SECURITY_TOKEN_PARAMETER).append("=").append(token);
        } else {
          out.append("<input type=\"hidden\" name=\"").append(SecurityTokenValidatorImpl.PEBBLE_SECURITY_TOKEN_PARAMETER);
          out.append("\" value=\"").append(token).append("\"/>");
        }
      } catch (IOException ioe) {
        throw new JspException(ioe);
      }
    }
    return SKIP_BODY;
  }

  public void setQuery(boolean query) {
    this.query = query;
  }
}
