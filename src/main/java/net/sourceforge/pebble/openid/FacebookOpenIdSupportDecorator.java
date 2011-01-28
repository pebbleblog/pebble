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

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.decorator.PageDecorator;
import net.sourceforge.pebble.api.decorator.PageDecoratorContext;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.domain.Blog;

import javax.servlet.jsp.JspWriter;
import java.io.IOException;

/**
 * Provides support for Facebook OpenID operations
 *
 * @author James Roper
 */
public class FacebookOpenIdSupportDecorator implements PageDecorator {

  public static final String RENDER_FACEBOOK_SUPPORT_KEY = "renderFacebookSupport";
  private static final String FACEBOOK_APP_ID_KEY = "facebook.app.id";

  public void decorateHead(JspWriter out, PageDecoratorContext pageDecoratorContext) throws IOException {
  }

  public void decorateHeader(JspWriter out, PageDecoratorContext pageDecoratorContext) throws IOException {
  }

  public void decorateFooter(JspWriter out, PageDecoratorContext pageDecoratorContext) throws IOException {
    Boolean renderFacebookSupport = (Boolean) pageDecoratorContext.getRequest().getAttribute(RENDER_FACEBOOK_SUPPORT_KEY);
    if (renderFacebookSupport != null && renderFacebookSupport) {
      // Find out the app ID
      AbstractBlog abstractBlog = (AbstractBlog) pageDecoratorContext.getRequest().getAttribute(Constants.BLOG_KEY);
      if (abstractBlog instanceof Blog) {
        String appId = ((Blog) abstractBlog).getPluginProperties().getProperty(FACEBOOK_APP_ID_KEY);
        if (appId != null) {
          out.append("<div id=\"fb-root\"></div>\n" +
                  "<script src=\"http://connect.facebook.net/en_US/all.js\"></script>\n" +
                  "<script>\n" +
                  "FB.init({appId: '").append(appId).append("', status: false, cookie: true, xfbml: true, channelUrl: '")
                  .append(abstractBlog.getUrl()).append("facebook_channel.html'});\n" +
                  "</script>");
        } else {
          // Probably should warn that you need an APP id....
        }
      }
    }
  }
}
