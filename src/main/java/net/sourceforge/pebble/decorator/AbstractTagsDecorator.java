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
import net.sourceforge.pebble.domain.PageBasedContent;
import net.sourceforge.pebble.domain.Tag;
import net.sourceforge.pebble.util.I18n;

/**
 * Generates tag links for inclusion in the body of blog entries,
 * when rendered as HTML.
 * 
 * @author Simon Brown
 */
public abstract class AbstractTagsDecorator extends ContentDecoratorSupport {
  private final String resourceKey;
  private final String target;

  /**
   * Extended Parameters for generating Links to different Tagging sites - like Technorati.
   * @param resourceKey is used to determine the label for the tags from pebbles resource files
   * @param openLinkInNewWindow set to true to generate links with 'target="_blank"' 
   */

  public AbstractTagsDecorator(String resourceKey, boolean openLinkInNewWindow) {
	this.resourceKey = resourceKey;
	target=openLinkInNewWindow ? " target=\"_blank\"":"";
  }
	
  /**
   * Default constructors makes Tags use the standard label (key tag.tags) and open links
   * in the same browser window. 
   */

  public AbstractTagsDecorator() {
	this.resourceKey = "tag.tags";
	target="";
  }
	
  protected String generateDecorationHtml(ContentDecoratorContext context, PageBasedContent content) {
    StringBuffer buf = new StringBuffer();

    if (context.getMedia() == ContentDecoratorContext.HTML_PAGE) {
      Iterator<Tag> tags = content.getAllTags().iterator();

      String baseUrl = getBaseUrl(content);

      if (tags.hasNext()) {
        buf.append("<div class=\"tags\"><span>");
		buf.append(I18n.getMessage(content.getBlog(), resourceKey));
        buf.append(" : </span>");

        while (tags.hasNext()) {

          Tag tag = tags.next();

		  if (tag.getName() != null && !tag.getName().equals("")) {

			  buf.append("<a href=\"");
			  buf.append(baseUrl);
			  buf.append(tag.getName() + "\"");
			  buf.append(target);
			  buf.append(" rel=\"tag\">");
			  buf.append(tag.getName());
			  buf.append("</a>");

			  if (tags.hasNext()) {
				buf.append(", ");
			  }
		  }
        }
        buf.append("</div>");

      }
    }

    return buf.toString();
  }

  /**
   * Gets the base URL for tag links, complete with trailing slash.
   *
   * @param content   the owning content
   * @return  a URL as a String
   */
  public abstract String getBaseUrl(PageBasedContent content);
}
