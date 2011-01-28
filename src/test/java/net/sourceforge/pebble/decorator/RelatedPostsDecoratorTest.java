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
 * Tests for the SocialBookmarksDecorator class.
 * 
 * @author Alexander Zagniotov
 */
public class RelatedPostsDecoratorTest extends SingleBlogTestCase {

  private ContentDecorator decorator;
  private BlogEntry blogEntryOne;
  private BlogEntry blogEntryTwo;
  private BlogEntry blogEntryThree;
  private BlogEntry blogEntryFour;
  private BlogEntry blogEntryFive;
  private BlogEntry blogEntrySix;
  private BlogEntry blogEntrySeven;
  private BlogEntry blogEntryEight;
  private BlogService service;
  private ContentDecoratorContext context;

  protected void setUp() throws Exception {
    super.setUp();

    // default for RelatedPostsDecorator.MAX_POSTS is 5
    blog.getPluginProperties().setProperty(RelatedPostsDecorator.MAX_POSTS, "6");

    blogEntryOne = new BlogEntry(blog);
    blogEntryTwo = new BlogEntry(blog);
    blogEntryThree = new BlogEntry(blog);
    blogEntryFour = new BlogEntry(blog);
    blogEntryFive = new BlogEntry(blog);
    blogEntrySix = new BlogEntry(blog);
    blogEntrySeven = new BlogEntry(blog);
    blogEntryEight = new BlogEntry(blog);

    service = new BlogService();

    decorator = new RelatedPostsDecorator();
    context = new ContentDecoratorContext();
  }

  /**
   * Tests that a blog entry has no related posts when output to a HTML page.
   */
  public void testBlogEntryNoRelatedPostsAndMediaIsHtml() throws Exception {

    context.setMedia(ContentDecoratorContext.HTML_PAGE);

    blogEntryOne.setTitle("Title - title one");
    blogEntryOne.setExcerpt("Excerpt - except one");
    blogEntryOne.setBody("Body - body one");
    blogEntryOne.setTags("one two");
    service.putBlogEntry(blogEntryOne);

    blogEntryTwo.setTitle("Title - title two");
    blogEntryTwo.setExcerpt("Excerpt - except two");
    blogEntryTwo.setBody("Body - body two");
    blogEntryTwo.setTags("one two");
    service.putBlogEntry(blogEntryTwo);

    blogEntryThree.setTitle("Title - title three");
    blogEntryThree.setExcerpt("Excerpt - except three");
    blogEntryThree.setBody("Body - body three");
    blogEntryThree.setTags("three");
    service.putBlogEntry(blogEntryThree);

    decorator.decorate(context, blogEntryThree);

    StringBuffer relatedPosts = new StringBuffer();
    relatedPosts.append("<p><b>Related Posts</b><br />");
    relatedPosts.append("<i>There are no related posts for this blog entry</i>");
    relatedPosts.append("</p><br />");

    assertEquals("Body - body three" + relatedPosts, blogEntryThree.getBody());
  }

  /**
   * Tests that a blog entry has several related posts when output to a HTML
   * page.
   */
  public void testBlogEntryWithRelatedPostsAndMediaIsHtml() throws Exception {

    context.setMedia(ContentDecoratorContext.HTML_PAGE);

    Date date = new Date();
    long now = 1;
    date.setTime(now);

    blogEntryOne.setDate(date);
    blogEntryOne.setTitle("Title - title one");
    blogEntryOne.setExcerpt("Excerpt - except one");
    blogEntryOne.setBody("Body - body one");
    blogEntryOne.setTags("one, two");
    service.putBlogEntry(blogEntryOne);

    blogEntryTwo.setDate(date);
    blogEntryTwo.setTitle("Title - title two");
    blogEntryTwo.setExcerpt("Excerpt - except two");
    blogEntryTwo.setBody("Body - body two");
    blogEntryTwo.setTags("one, two");
    service.putBlogEntry(blogEntryTwo);

    blogEntryThree.setDate(date);
    blogEntryThree.setTitle("Title - title three");
    blogEntryThree.setExcerpt("Excerpt - except three");
    blogEntryThree.setBody("Body - body three");
    blogEntryThree.setTags("two, three");
    service.putBlogEntry(blogEntryThree);

    decorator.decorate(context, blogEntryThree);

    StringBuffer relatedPosts = new StringBuffer();
    relatedPosts.append("<p><b>Related Posts</b><br />");
    relatedPosts
        .append("<a href=\"http://www.yourdomain.com/blog/1970/01/01/2.html\" rel=\"bookmark\" title=\"Title - title two\">Title - title two</a><br />");
    relatedPosts
        .append("<a href=\"http://www.yourdomain.com/blog/1970/01/01/1.html\" rel=\"bookmark\" title=\"Title - title one\">Title - title one</a><br />");
    relatedPosts.append("</p><br />");

    assertEquals("Body - body three" + relatedPosts, blogEntryThree.getBody());

    service.removeBlogEntry(blogEntryOne);
    service.removeBlogEntry(blogEntryTwo);
    service.removeBlogEntry(blogEntryThree);

  }

  /**
   * Tests that a blog entry has maximum of six (6) related posts when output to
   * a HTML page.
   */
  public void testBlogEntryMaximumSixRelatedPostsAndMediaIsHtml() throws Exception {

    context.setMedia(ContentDecoratorContext.HTML_PAGE);

    Date date = new Date();
    long now = 1;
    date.setTime(now);

    blogEntryOne.setDate(date);
    blogEntryOne.setTitle("Title - title one");
    blogEntryOne.setExcerpt("Excerpt - except one");
    blogEntryOne.setBody("Body - body one");
    blogEntryOne.setTags("one");
    service.putBlogEntry(blogEntryOne);

    blogEntryTwo.setDate(date);
    blogEntryTwo.setTitle("Title - title two");
    blogEntryTwo.setExcerpt("Excerpt - except two");
    blogEntryTwo.setBody("Body - body two");
    blogEntryTwo.setTags("one, two");
    service.putBlogEntry(blogEntryTwo);

    blogEntryThree.setDate(date);
    blogEntryThree.setTitle("Title - title three");
    blogEntryThree.setExcerpt("Excerpt - except three");
    blogEntryThree.setBody("Body - body three");
    blogEntryThree.setTags("one, two, three");
    service.putBlogEntry(blogEntryThree);

    blogEntryFour.setDate(date);
    blogEntryFour.setTitle("Title - title four");
    blogEntryFour.setExcerpt("Excerpt - except four");
    blogEntryFour.setBody("Body - body four");
    blogEntryFour.setTags("one, two, three, four");
    service.putBlogEntry(blogEntryFour);

    blogEntryFive.setDate(date);
    blogEntryFive.setTitle("Title - title five");
    blogEntryFive.setExcerpt("Excerpt - except five");
    blogEntryFive.setBody("Body - body five");
    blogEntryFive.setTags("one, two, three, four, five");
    service.putBlogEntry(blogEntryFive);

    blogEntrySix.setDate(date);
    blogEntrySix.setTitle("Title - title six");
    blogEntrySix.setExcerpt("Excerpt - except six");
    blogEntrySix.setBody("Body - body six");
    blogEntrySix.setTags("one, two, three, four, five, six");
    service.putBlogEntry(blogEntrySix);

    blogEntrySeven.setDate(date);
    blogEntrySeven.setTitle("Title - title seven");
    blogEntrySeven.setExcerpt("Excerpt - except seven");
    blogEntrySeven.setBody("Body - body seven");
    blogEntrySeven.setTags("one, two, three, four, five, six, seven");
    service.putBlogEntry(blogEntrySeven);

    blogEntryEight.setDate(date);
    blogEntryEight.setTitle("Title - title eight");
    blogEntryEight.setExcerpt("Excerpt - except eight");
    blogEntryEight.setBody("Body - body eight");
    blogEntryEight.setTags("one, two, three, four, five, six, seven, eight");
    service.putBlogEntry(blogEntryEight);

    decorator.decorate(context, blogEntryOne);

    StringBuffer relatedPosts = new StringBuffer();
    relatedPosts.append("<p><b>Related Posts</b><br />");
    relatedPosts
        .append("<a href=\"http://www.yourdomain.com/blog/1970/01/01/8.html\" rel=\"bookmark\" title=\"Title - title eight\">Title - title eight</a><br />");
    relatedPosts
        .append("<a href=\"http://www.yourdomain.com/blog/1970/01/01/7.html\" rel=\"bookmark\" title=\"Title - title seven\">Title - title seven</a><br />");
    relatedPosts
        .append("<a href=\"http://www.yourdomain.com/blog/1970/01/01/6.html\" rel=\"bookmark\" title=\"Title - title six\">Title - title six</a><br />");
    relatedPosts
        .append("<a href=\"http://www.yourdomain.com/blog/1970/01/01/5.html\" rel=\"bookmark\" title=\"Title - title five\">Title - title five</a><br />");
    relatedPosts
        .append("<a href=\"http://www.yourdomain.com/blog/1970/01/01/4.html\" rel=\"bookmark\" title=\"Title - title four\">Title - title four</a><br />");
    relatedPosts
        .append("<a href=\"http://www.yourdomain.com/blog/1970/01/01/3.html\" rel=\"bookmark\" title=\"Title - title three\">Title - title three</a><br />");
    relatedPosts.append("</p><br />");

    assertEquals("Body - body one" + relatedPosts, blogEntryOne.getBody());
  }

}
