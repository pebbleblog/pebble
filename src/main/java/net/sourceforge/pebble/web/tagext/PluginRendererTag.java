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

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.decorator.PageDecorator;
import net.sourceforge.pebble.api.decorator.PageDecoratorContext;
import net.sourceforge.pebble.api.openid.OpenIdCommentAuthorProvider;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.domain.Blog;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Renders the renderable plugins for a blog
 *
 * @author James Roper
 */
public class PluginRendererTag extends TagSupport {

  private static final String PAGE_DECORATOR_HEAD = "head";
  private static final String PAGE_DECORATOR_HEADER = "header";
  private static final String PAGE_DECORATOR_FOOTER = "footer";
  private static final String OPEN_ID_COMMENT_AUTHOR_PROVIDER = "openidcommentauthorprovider";

  /** The plugin point to render */
  private String plugin;

  @Override
  public int doStartTag() throws JspException {
    HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
    AbstractBlog abstractBlog = (AbstractBlog) request.getAttribute(Constants.BLOG_KEY);

    try {
      if (abstractBlog instanceof Blog) {
        PageDecoratorContext context = new PageDecoratorContext(request);
        Blog blog = (Blog) abstractBlog;
        if (PAGE_DECORATOR_HEAD.equals(plugin)) {
          for (PageDecorator decorator : blog.getPageDecorators()) {
            decorator.decorateHead(pageContext.getOut(), context);
          }
        } else if (PAGE_DECORATOR_HEADER.equals(plugin)) {
          for (PageDecorator decorator : blog.getPageDecorators()) {
            decorator.decorateHeader(pageContext.getOut(), context);
          }
        } else if (PAGE_DECORATOR_FOOTER.equals(plugin)) {
          for (PageDecorator decorator : blog.getPageDecorators()) {
            decorator.decorateFooter(pageContext.getOut(), context);
          }
        }
        if (OPEN_ID_COMMENT_AUTHOR_PROVIDER.equals(plugin)) {
          for (OpenIdCommentAuthorProvider provider : blog.getOpenIdCommentAuthorProviders()) {
            provider.renderProvider(pageContext.getOut(), context);
          }
        }
      }
    } catch (IOException ioe) {
      throw new JspException(ioe);
    }
    return SKIP_BODY;
  }


  public void setPlugin(String plugin) {
    this.plugin = plugin;
  }
}
