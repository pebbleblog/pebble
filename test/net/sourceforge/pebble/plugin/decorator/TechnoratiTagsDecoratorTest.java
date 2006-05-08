/*
 * Copyright (c) 2003-2006, Simon Brown
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
package net.sourceforge.pebble.plugin.decorator;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.SingleBlogTestCase;

/**
 * Tests for the TechnoratiTagsDecorator class.
 *
 * @author    Simon Brown
 */
public class TechnoratiTagsDecoratorTest extends SingleBlogTestCase {

  private BlogEntryDecorator decorator;
  private BlogEntry blogEntry;
  private BlogEntryDecoratorContext context;

  private BlogEntryDecoratorChain chain;

  public void setUp() {
    super.setUp();

    blogEntry = new BlogEntry(blog);
    decorator = new TechnoratiTagsDecorator();
    chain = new BlogEntryDecoratorChain(null);
    context = new BlogEntryDecoratorContext();
    context.setBlogEntry(blogEntry);
  }

  /**
   * Tests that a blog entry with no tags remains unchanged.
   */
  public void testBlogEntryHasNoTags() throws Exception {
    blogEntry.setExcerpt("Excerpt - here is some text");
    blogEntry.setBody("Body - here is some text");
    context.setMedia(BlogEntryDecoratorContext.HTML_PAGE);
    decorator.decorate(chain, context);
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
    context.setMedia(BlogEntryDecoratorContext.HTML_PAGE);
    decorator.decorate(chain, context);

    StringBuffer tags = new StringBuffer();
    tags.append("<div class=\"tags\">Tags : ");
    tags.append("<a href=\"http://technorati.com/tag/automatedunittesting\" rel=\"tag\">automatedunittesting</a> ");
    tags.append("<a href=\"http://technorati.com/tag/junit\" rel=\"tag\">junit</a> ");
    tags.append("<a href=\"http://technorati.com/tag/java\" rel=\"tag\">java</a>");
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
    context.setMedia(BlogEntryDecoratorContext.NEWS_FEED);
    decorator.decorate(chain, context);

    StringBuffer tags = new StringBuffer();
//    tags.append("<!--<div class=\"tags\">Tags : ");
//    tags.append("<a href=\"http://technorati.com/tag/automated+unit+testing\" rel=\"tag\">automated unit testing</a>, ");
//    tags.append("<a href=\"http://technorati.com/tag/junit\" rel=\"tag\">junit</a>, ");
//    tags.append("<a href=\"http://technorati.com/tag/java\" rel=\"tag\">java</a>");
//    tags.append("</div>-->");

    assertEquals("Excerpt - here is some text" + tags, blogEntry.getExcerpt());
    assertEquals("Body - here is some text" + tags, blogEntry.getBody());
  }

}
