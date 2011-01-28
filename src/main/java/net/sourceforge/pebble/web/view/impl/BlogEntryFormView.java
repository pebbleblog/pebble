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
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.web.view.HtmlView;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

/**
 * Represents the blog entry form.
 *
 * @author    Simon Brown
 */
public class BlogEntryFormView extends HtmlView {

  /**
   * Prepares the view for presentation.
   */
  public void prepare() {
    BlogEntry blogEntry = (BlogEntry)getModel().get(Constants.BLOG_ENTRY_KEY);
    BlogEntry previewBlogEntry = (BlogEntry)blogEntry.clone();

    ContentDecoratorContext context = new ContentDecoratorContext();
    context.setView(ContentDecoratorContext.PREVIEW);
    context.setMedia(ContentDecoratorContext.HTML_PAGE);
    blogEntry.getBlog().getContentDecoratorChain().decorate(context, previewBlogEntry);
    getModel().put("previewBlogEntry", previewBlogEntry);

    List timeZones = Arrays.asList(java.util.TimeZone.getAvailableIDs());
    Collections.sort(timeZones);
    getModel().put("timeZones", timeZones);
  }

  /**
   * Gets the title of this view.
   *
   * @return the title as a String
   */
  public String getTitle() {
    BlogEntry blogEntry = (BlogEntry)getModel().get(Constants.BLOG_ENTRY_KEY);
    return blogEntry.getTitle();
  }

  /**
   * Gets the URI that this view represents.
   *
   * @return the URI as a String
   */
  public String getUri() {
    return "/WEB-INF/jsp/blogEntryForm.jsp";
  }

}