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
package net.sourceforge.pebble.web.view.impl;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.web.view.HtmlView;


/**
 * Represents a page containing a single static page.
 *
 * @author    Simon Brown
 */
public class StaticPageView extends HtmlView {

  public void prepare() {
    ContentDecoratorContext context = new ContentDecoratorContext();
    context.setView(ContentDecoratorContext.DETAIL_VIEW);
    context.setMedia(ContentDecoratorContext.HTML_PAGE);

    StaticPage staticPage = (StaticPage)getModel().get(Constants.STATIC_PAGE_KEY);
    staticPage.getBlog().getContentDecoratorChain().decorate(context, staticPage);
  }

  /**
   * Gets the title of this view.
   *
   * @return the title as a String
   */
  public String getTitle() {
    StaticPage staticPage = (StaticPage)getModel().get(Constants.STATIC_PAGE_KEY);
    return staticPage.getTitle();
  }

  /**
   * Gets the URI that this view represents.
   *
   * @return the URI as a String
   */
  public String getUri() {
    return "/WEB-INF/jsp/staticPage.jsp";
  }

}