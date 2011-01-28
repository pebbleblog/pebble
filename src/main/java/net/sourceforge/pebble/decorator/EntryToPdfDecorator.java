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

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

import java.util.ResourceBundle;

/**
 * Allow to export current blog entry as PDF document
 * 
 * @author Alexander Zagniotov
 */
public class EntryToPdfDecorator extends ContentDecoratorSupport {

	private static final String PDF_IMG = "<img src=\"common/images/pdf_logo.gif\" alt=\"Export this post as PDF document\" align=\"bottom\" border=\"0\" />";

	/**
	 * Decorates the specified blog entry.
	 * 
	 * @param context
	 *            the context in which the decoration is running
	 * @param blogEntry
	 *            the blog entry to be decorated
	 */
	public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {

		String body = blogEntry.getBody();
		if (body != null && body.trim().length() > 0) {

			String html = generateDecorationHtml(blogEntry);
			blogEntry.setBody(body + html);
		}

//		String excerpt = blogEntry.getExcerpt();
//
//		if (excerpt != null && excerpt.trim().length() > 0) {
//			String html = generateDecorationHtml(blogEntry);
//			blogEntry.setExcerpt(excerpt + html);
//		}
	}

	private String generateDecorationHtml(BlogEntry blogEntry) {

			StringBuffer buf = new StringBuffer();
			String title = blogEntry.getTitle();
			String subtitle = blogEntry.getSubtitle();

			buf.append("<p>");
			buf.append("<a href=\"" + blogEntry.getBlog().getUrl() + "entryToPDF.action?entry=" + blogEntry.getId() + "\" title=\"Export " + title + " - " + subtitle + " as PDF document\">");
			buf.append(PDF_IMG);
			buf.append("</a>&nbsp;&nbsp;Export this post to PDF document</p>");

			return buf.toString();
	}

}