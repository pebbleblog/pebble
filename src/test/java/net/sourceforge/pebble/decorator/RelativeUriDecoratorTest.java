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

import net.sourceforge.pebble.domain.Attachment;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.api.decorator.ContentDecorator;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

/**
 * Tests for the RelativeUriDecorator class.
 *
 * @author    Simon Brown
 */
public class RelativeUriDecoratorTest extends SingleBlogTestCase {

  private ContentDecorator decorator;
  private BlogEntry blogEntry;
  private StaticPage staticPage;
  private ContentDecoratorContext context;

  protected void setUp() throws Exception {
    super.setUp();

    blogEntry = new BlogEntry(blog);
    staticPage = new StaticPage(blog);
    decorator = new RelativeUriDecorator();
    context = new ContentDecoratorContext();
    decorator.setBlog(blog);
  }

  /**
   * Tests that a relative URI pointing to an image is translated, in the body.
   */
  public void testImageUriInBody() throws Exception {
    blogEntry.setBody("Body - <img src=\"./images/someimage.jpg\">");
    decorator.decorate(context, blogEntry);
    assertEquals("Body - <img src=\"http://www.yourdomain.com/blog/images/someimage.jpg\">", blogEntry.getBody());
  }

  /**
   * Tests that a relative URI pointing to a file is translated, in the body.
   */
  public void testFileUriInBody() throws Exception {
    blogEntry.setBody("Body - <a href=\"./files/someimage.zip\">");
    decorator.decorate(context, blogEntry);
    assertEquals("Body - <a href=\"http://www.yourdomain.com/blog/files/someimage.zip\">", blogEntry.getBody());
  }

  /**
   * Tests that a relative URI pointing to a blog entry is translated, in the body.
   */
  public void testBlogEntryUriInBody() throws Exception {
    blogEntry.setBody("Body - <a href=\"./2007/01/01/some_title.html\">");
    decorator.decorate(context, blogEntry);
    assertEquals("Body - <a href=\"http://www.yourdomain.com/blog/2007/01/01/some_title.html\">", blogEntry.getBody());
  }

  /**
   * Tests that a relative URI pointing to an image is translated, in the excerpt.
   */
  public void testImageUriInExcerpt() throws Exception {
    blogEntry.setExcerpt("Excerpt - <img src=\"./images/someimage.jpg\">");
    decorator.decorate(context, blogEntry);
    assertEquals("Excerpt - <img src=\"http://www.yourdomain.com/blog/images/someimage.jpg\">", blogEntry.getExcerpt());
  }

  /**
   * Tests that a relative URI pointing to a file is translated, in the excerpt.
   */
  public void testFileUriInExcerpt() throws Exception {
    blogEntry.setExcerpt("Excerpt - <a href=\"./files/someimage.zip\">");
    decorator.decorate(context, blogEntry);
    assertEquals("Excerpt - <a href=\"http://www.yourdomain.com/blog/files/someimage.zip\">", blogEntry.getExcerpt());
  }

  /**
   * Tests that a relative URI in an attachment is translated.
   */
  public void testRelativeUriInAttachment() throws Exception {
    blogEntry.setAttachment(new Attachment("./files/someimage.jpg", 1024, "image/jpeg"));
    decorator.decorate(context, blogEntry);
    assertEquals("http://www.yourdomain.com/blog/files/someimage.jpg", blogEntry.getAttachment().getUrl());

    blogEntry.setAttachment(new Attachment("http://www.domain.com/files/someimage.jpg", 1024, "image/jpeg"));
    decorator.decorate(context, blogEntry);
    assertEquals("http://www.domain.com/files/someimage.jpg", blogEntry.getAttachment().getUrl());
  }

  /**
   * Tests that a relative URI pointing to an image is translated, in the body.
   */
  public void testImageUriInBodyOfStaticPage() throws Exception {
    staticPage.setBody("Body - <img src=\"./images/someimage.jpg\">");
    decorator.decorate(context, staticPage);
    assertEquals("Body - <img src=\"http://www.yourdomain.com/blog/images/someimage.jpg\">", staticPage.getBody());
  }

  /**
   * Tests that a relative URI pointing to a file is translated, in the body.
   */
  public void testFileUriInBodyOfStaticPage() throws Exception {
    staticPage.setBody("Body - <a href=\"./files/someimage.zip\">");
    decorator.decorate(context, staticPage);
    assertEquals("Body - <a href=\"http://www.yourdomain.com/blog/files/someimage.zip\">", staticPage.getBody());
  }

}
