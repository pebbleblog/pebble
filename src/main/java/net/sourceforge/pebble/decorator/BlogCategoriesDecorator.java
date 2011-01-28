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

package net.sourceforge.pebble.decorator;

import java.util.Iterator;

import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.util.I18n;

/**
 * Generates category links for inclusion in the body of blog entries,
 * when rendered as HTML.
 * 
 * @author Simon Brown
 */
public class BlogCategoriesDecorator extends ContentDecoratorSupport {

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
  if (context.getMedia() == ContentDecoratorContext.HTML_PAGE) {
      Iterator categories = blogEntry.getCategories().iterator();

      if (categories.hasNext()) {
        StringBuffer buf = new StringBuffer();

        buf.append("<div class=\"categories\"><span>");
        buf.append(I18n.getMessage(blogEntry.getBlog(), "category.categories"));
        buf.append(" : </span>");
        while (categories.hasNext()) {
          Category category = (Category)categories.next();
          buf.append("<a href=\"");
          buf.append(category.getPermalink());
          buf.append("\">");
          buf.append(category.getName());
          buf.append("</a>");

          if (categories.hasNext()) {
            buf.append(", ");
          }
        }
        buf.append("</div>");

        // now add the HTML to the body and excerpt, if they aren't empty
        String body = blogEntry.getBody();
        if (body != null && body.trim().length() > 0) {
          blogEntry.setBody(body + buf.toString());
        }

        String excerpt = blogEntry.getExcerpt();
        if (excerpt != null && excerpt.trim().length() > 0) {
          blogEntry.setExcerpt(excerpt + buf.toString());
        }
      }
    }
  }

}
