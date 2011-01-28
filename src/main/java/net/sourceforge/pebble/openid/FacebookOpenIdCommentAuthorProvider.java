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

package net.sourceforge.pebble.openid;

import net.sourceforge.pebble.api.decorator.PageDecoratorContext;
import net.sourceforge.pebble.api.openid.OpenIdCommentAuthorProvider;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Provides OpenID integration from Facebook for comments
 *
 * @author James Roper
 */
public class FacebookOpenIdCommentAuthorProvider implements OpenIdCommentAuthorProvider {
  private static final String HTML_FILE = "facebookOpenIdCommentAuthor.html";

  public void renderProvider(JspWriter jspWriter, PageDecoratorContext pageDecoratorContext) throws IOException {
    // Load the resource
    Reader reader = null;
    try {
      reader = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(HTML_FILE));
      // 8kb buffer, for single byte characters this will be 4kb reads
      char[] buffer = new char[4096];
      int count = reader.read(buffer);
      while (count >= 0) {
        jspWriter.write(buffer, 0, count);
        count = reader.read(buffer);
      }
    } finally {
      if (reader != null) {
        reader.close();
      }
    }
    pageDecoratorContext.getRequest().setAttribute(FacebookOpenIdSupportDecorator.RENDER_FACEBOOK_SUPPORT_KEY, true);
  }
}
