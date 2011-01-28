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

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.api.decorator.ContentDecorator;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

/**
 * Tests for the BlogTagsDecorator class.
 *
 * @author    Simon Brown
 */
public class BlogTagsDecoratorTest extends SingleBlogTestCase {

  private ContentDecorator decorator;
  private BlogEntry blogEntry;
  private ContentDecoratorContext context;

  protected void setUp() throws Exception {
    super.setUp();

    blogEntry = new BlogEntry(blog);
    decorator = new BlogTagsDecorator();
    context = new ContentDecoratorContext();
  }

  /**
   * Tests that a blog entry with no tags remains unchanged.
   */
  public void testBlogEntryHasNoTags() throws Exception {
    blogEntry.setExcerpt("Excerpt - here is some text");
    blogEntry.setBody("Body - here is some text");
    context.setMedia(ContentDecoratorContext.HTML_PAGE);
    decorator.decorate(context, blogEntry);
    assertEquals("Excerpt - here is some text", blogEntry.getExcerpt());
    assertEquals("Body - here is some text", blogEntry.getBody());
  }

  /**
   * Tests that a blog entry with tags gets modified, when output to a HTML page.
   */
  public void testBlogEntryHasTagsAndMediaIsHtml() throws Exception {
    Category category = new Category("/java", "Java");
    category.setBlog(blog);
    category.setTags("java");
    blogEntry.addCategory(category);
    blogEntry.setExcerpt("Excerpt - here is some text");
    blogEntry.setBody("Body - here is some text");
    blogEntry.setTags("junit, automated unit testing");
    context.setMedia(ContentDecoratorContext.HTML_PAGE);
    decorator.decorate(context, blogEntry);

    StringBuffer tags = new StringBuffer();
    tags.append("<div class=\"tags\"><span>Tags : </span>");
    tags.append("<a href=\"http://www.yourdomain.com/blog/tags/automatedunittesting\" rel=\"tag\">automatedunittesting</a>, ");
    tags.append("<a href=\"http://www.yourdomain.com/blog/tags/java\" rel=\"tag\">java</a>, ");
    tags.append("<a href=\"http://www.yourdomain.com/blog/tags/junit\" rel=\"tag\">junit</a>");
    tags.append("</div>");

    assertEquals("Excerpt - here is some text" + tags, blogEntry.getExcerpt());
    assertEquals("Body - here is some text" + tags, blogEntry.getBody());
  }

  /**
   * Tests that a blog entry with tags gets modified, when output to a newsfeed.
   */
  public void testBlogEntryHasTagsAndMediaIsNewsfeed() throws Exception {
    Category category = new Category("/java", "Java");
    category.setBlog(blog);
    category.setTags("java");
    blogEntry.addCategory(category);
    blogEntry.setExcerpt("Excerpt - here is some text");
    blogEntry.setBody("Body - here is some text");
    blogEntry.setTags("junit, automated unit testing");
    context.setMedia(ContentDecoratorContext.NEWS_FEED);
    decorator.decorate(context, blogEntry);

    StringBuffer tags = new StringBuffer();
//    tags.append("<!--<div class=\"tags\">Tags : ");
//    tags.append("<a href=\"http://www.yourdomain.com/blog/tags/automated+unit+testing\" rel=\"tag\">automated unit testing</a>, ");
//    tags.append("<a href=\"http://www.yourdomain.com/blog/tags/junit\" rel=\"tag\">junit</a>, ");
//    tags.append("<a href=\"http://www.yourdomain.com/blog/tags/java\" rel=\"tag\">java</a>");
//    tags.append("</div>-->");

    assertEquals("Excerpt - here is some text" + tags, blogEntry.getExcerpt());
    assertEquals("Body - here is some text" + tags, blogEntry.getBody());
  }

}
