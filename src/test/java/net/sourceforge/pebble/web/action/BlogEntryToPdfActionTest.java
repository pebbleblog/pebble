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
package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.PdfView;
import net.sourceforge.pebble.web.view.BinaryView;
import net.sourceforge.pebble.web.view.NotFoundView;


/**
 * Tests for the BlogEntryToPdfAction class.
 *
 * @author    Alexander Zagniotov
 */
public class BlogEntryToPdfActionTest extends SingleBlogActionTestCase {

	private BlogEntry unpublishedEntry;
	private BlogEntry publishedEntry;
	private BlogService service;

  protected void setUp() throws Exception {

    action = new BlogEntryToPdfAction();

    super.setUp();

	unpublishedEntry = new BlogEntry(blog);
	unpublishedEntry.setTitle("This is some unpublished entry title");
	
	publishedEntry = new BlogEntry(blog);
	publishedEntry.setTitle("This is some published entry title that is very important");
	publishedEntry.setPublished(true);

	service = new BlogService();
	service.putBlogEntry(publishedEntry);
	service.putBlogEntry(unpublishedEntry);
  }

  public void testPdfViewForNonExistingBlogEntryId() throws Exception {
	request.setParameter("entry", "88888888888");
    View view = action.process(request, response);
    assertTrue(view instanceof NotFoundView);
  }

   public void testPdfViewForPublishedBlogEntryInvalidParameterName() throws Exception {
	request.setParameter("momo", publishedEntry.getId());
    View view = action.process(request, response);
    assertTrue(view instanceof NotFoundView);
  }

  public void testPdfViewForNullBlogEntryId() throws Exception {
	request.setParameter("entry", "");
    View view = action.process(request, response);
    assertTrue(view instanceof NotFoundView);
  }

   public void testPdfViewForUnpublishedBlogEntry() throws Exception {
	request.setParameter("entry", unpublishedEntry.getId());
    View view = action.process(request, response);
    assertTrue(view instanceof PdfView);
	
	PdfView pdfView = (PdfView) view;
	assertEquals("application/pdf", pdfView.getContentType());
  }

  public void testPdfViewForPublishedBlogEntry() throws Exception {
	request.setParameter("entry", publishedEntry.getId());
    View view = action.process(request, response);
    assertTrue(view instanceof PdfView);
	
	PdfView pdfView = (PdfView) view;
	assertEquals("application/pdf", pdfView.getContentType());

  }
}