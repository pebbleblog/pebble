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

import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.StaticPage;
import net.sourceforge.pebble.api.decorator.ContentDecorator;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

/**
 * Tests for the PhotoDecorator class.
 *
 * @author    Simon Brown
 */
public class PhotoDecoratorTest extends SingleBlogTestCase {

  private ContentDecorator decorator;
  private ContentDecoratorContext context;
  private BlogEntry blogEntry;
  private StaticPage staticPage;

  protected void setUp() throws Exception {
    super.setUp();

    decorator = new PhotoDecorator();
    blogEntry = new BlogEntry(blog);
    staticPage = new StaticPage(blog);
    context = new ContentDecoratorContext();
  }

  /**
   * Tests that text in the body between <photo> tags is escaped.
   */
  public void testEscapeTextInBody() throws Exception {
    blogEntry.setBody(null);
    decorator.decorate(context, blogEntry);
    assertEquals("", blogEntry.getBody());

    blogEntry.setBody("");
    decorator.decorate(context, blogEntry);
    assertEquals("", blogEntry.getBody());

    blogEntry.setBody("Here is some <b>HTML</b>.");
    decorator.decorate(context, blogEntry);
    assertEquals("Here is some <b>HTML</b>.", blogEntry.getBody());

    blogEntry.setBody("Before\n" +
        "<photos>\n" +
        "./images/photo1.jpg|caption1\n" +
        "./images/photo2.jpg|caption2\n" +
        "\n" +
        "./images/photo3.jpg|caption3\n" +
        "</photos>\n" +
        "After");
    decorator.decorate(context, blogEntry);
    assertEquals("Before\n<div class=\"photos\">\n" +
        "<div>\n" +
        "<img src=\"./images/photo1.jpg\" class=\"photo\" alt=\"caption1\" />\n" +
        "<img src=\"./images/photo2.jpg\" class=\"photo\" alt=\"caption2\" />\n" +
        "</div>\n" +
        "<div>\n" +
        "<img src=\"./images/photo3.jpg\" class=\"photo\" alt=\"caption3\" />\n" +
        "</div>\n" +
        "</div>\n" +
        "After", blogEntry.getBody());

    blogEntry.setBody("Before\n" +
        "<photos>\n" +
        "./images/photo1.jpg|\n" +
        "</photos>\n" +
        "After");
    decorator.decorate(context, blogEntry);
    assertEquals("Before\n<div class=\"photos\">\n" +
        "<div>\n" +
        "<img src=\"./images/photo1.jpg\" class=\"photo\" alt=\"\" />\n" +
        "</div>\n" +
        "</div>\n" +
        "After", blogEntry.getBody());
  }

  /**
   * Tests that text in the excerpt between <photo> tags is escaped.
   */
  public void testEscapeTextInExcerpt() throws Exception {
    blogEntry.setExcerpt(null);
    decorator.decorate(context, blogEntry);
    assertEquals("", blogEntry.getExcerpt());

    blogEntry.setExcerpt("");
    decorator.decorate(context, blogEntry);
    assertEquals("", blogEntry.getExcerpt());

    blogEntry.setExcerpt("Here is some <b>HTML</b>.");
    decorator.decorate(context, blogEntry);
    assertEquals("Here is some <b>HTML</b>.", blogEntry.getExcerpt());

    blogEntry.setExcerpt("Before\n" +
        "<photos>\n" +
        "./images/photo1.jpg|caption1\n" +
        "./images/photo2.jpg|caption2\n" +
        "\n" +
        "./images/photo3.jpg|caption3\n" +
        "</photos>\n" +
        "After");
    decorator.decorate(context, blogEntry);
    assertEquals("Before\n<div class=\"photos\">\n" +
        "<div>\n" +
        "<img src=\"./images/photo1.jpg\" class=\"photo\" alt=\"caption1\" />\n" +
        "<img src=\"./images/photo2.jpg\" class=\"photo\" alt=\"caption2\" />\n" +
        "</div>\n" +
        "<div>\n" +
        "<img src=\"./images/photo3.jpg\" class=\"photo\" alt=\"caption3\" />\n" +
        "</div>\n" +
        "</div>\n" +
        "After", blogEntry.getExcerpt());

    blogEntry.setExcerpt("Before\n" +
        "<photos>\n" +
        "./images/photo1.jpg|\n" +
        "</photos>\n" +
        "After");
    decorator.decorate(context, blogEntry);
    assertEquals("Before\n<div class=\"photos\">\n" +
        "<div>\n" +
        "<img src=\"./images/photo1.jpg\" class=\"photo\" alt=\"\" />\n" +
        "</div>\n" +
        "</div>\n" +
        "After", blogEntry.getExcerpt());
  }

  /**
   * Tests that text in the body of a static page between <photo> tags is escaped.
   */
  public void testEscapeTextInBodyOfStaticPage() throws Exception {
    staticPage.setBody(null);
    decorator.decorate(context, staticPage);
    assertEquals("", staticPage.getBody());

    staticPage.setBody("");
    decorator.decorate(context, staticPage);
    assertEquals("", staticPage.getBody());

    staticPage.setBody("Here is some <b>HTML</b>.");
    decorator.decorate(context, staticPage);
    assertEquals("Here is some <b>HTML</b>.", staticPage.getBody());

    staticPage.setBody("Before\n" +
        "<photos>\n" +
        "./images/photo1.jpg|caption1\n" +
        "./images/photo2.jpg|caption2\n" +
        "\n" +
        "./images/photo3.jpg|caption3\n" +
        "</photos>\n" +
        "After");
    decorator.decorate(context, staticPage);
    assertEquals("Before\n<div class=\"photos\">\n" +
        "<div>\n" +
        "<img src=\"./images/photo1.jpg\" class=\"photo\" alt=\"caption1\" />\n" +
        "<img src=\"./images/photo2.jpg\" class=\"photo\" alt=\"caption2\" />\n" +
        "</div>\n" +
        "<div>\n" +
        "<img src=\"./images/photo3.jpg\" class=\"photo\" alt=\"caption3\" />\n" +
        "</div>\n" +
        "</div>\n" +
        "After", staticPage.getBody());

    staticPage.setBody("Before\n" +
        "<photos>\n" +
        "./images/photo1.jpg|\n" +
        "</photos>\n" +
        "After");
    decorator.decorate(context, staticPage);
    assertEquals("Before\n<div class=\"photos\">\n" +
        "<div>\n" +
        "<img src=\"./images/photo1.jpg\" class=\"photo\" alt=\"\" />\n" +
        "</div>\n" +
        "</div>\n" +
        "After", staticPage.getBody());
  }

}
