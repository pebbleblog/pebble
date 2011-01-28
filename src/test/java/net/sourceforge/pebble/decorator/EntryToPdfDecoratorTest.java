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

import java.util.Date;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.api.decorator.ContentDecorator;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

/**
 * Tests for the EntryToPdfDecorator class.
 *
 * @author    Alexander Zagniotov
 */
public class EntryToPdfDecoratorTest extends SingleBlogTestCase {
  
  private BlogEntry blogEntryOne;
  private ContentDecorator decorator;
  private ContentDecoratorContext context;

  private static final String PDF_IMG = "<img src=\"common/images/pdf_logo.gif\" alt=\"Export this post as PDF document\" align=\"bottom\" border=\"0\" />";

  protected void setUp() throws Exception {
    super.setUp();
    blogEntryOne = new BlogEntry(blog);
    decorator = new EntryToPdfDecorator();
    context = new ContentDecoratorContext();
  }

  /**
   * Tests that a blog entry gets a link offers to export it as PDF
   */
  public void testBlogEntryNoRelatedPostsAndMediaIsHtml() throws Exception {
  
	context.setMedia(ContentDecoratorContext.HTML_PAGE);

	blogEntryOne.setTitle("Some title");
	blogEntryOne.setSubtitle("Some subtitle");
	//blogEntryOne.setExcerpt("This is a excerpt");
    blogEntryOne.setBody("This is a body");
	
    decorator.decorate(context, blogEntryOne);

    StringBuffer pdf = new StringBuffer();
    pdf.append("<p>");
	pdf.append("<a href=\"" + blogEntryOne.getBlog().getUrl() + "entryToPDF.action?entry=" + blogEntryOne.getId() + "\" title=\"Export Some title - Some subtitle as PDF document\">");
	pdf.append(PDF_IMG);
	pdf.append("</a>&nbsp;&nbsp;Export this post to PDF document</p>");
	
    assertEquals("This is a body" + pdf, blogEntryOne.getBody());
	//assertEquals("This is a excerpt" + pdf, blogEntryOne.getExcerpt());
  }
}