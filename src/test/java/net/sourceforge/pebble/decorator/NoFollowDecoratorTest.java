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
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.domain.TrackBack;
import net.sourceforge.pebble.api.decorator.ContentDecorator;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

/**
 * Tests for the NoFollowDecorator class.
 *
 * @author    Simon Brown
 */
public class NoFollowDecoratorTest extends SingleBlogTestCase {

  private ContentDecorator decorator;
  private BlogEntry blogEntry;

  protected void setUp() throws Exception {
    super.setUp();

    decorator = new NoFollowDecorator();
    blogEntry = new BlogEntry(blog);
  }

  /**
   * Tests that the rel="nofollow" link is added to comments.
   */
  public void testNoFollowAddedToComments() throws Exception {
    ContentDecoratorContext context = new ContentDecoratorContext();
    context.setView(ContentDecoratorContext.DETAIL_VIEW);

    Comment comment1 = blogEntry.createComment("title1", "body1", "author", "email", "website", "avatar", "127.0.0.1");
    blogEntry.addComment(comment1);
    Comment comment2 = blogEntry.createComment("title2", "body2", "author", "email", "website", "avatar", "127.0.0.1");
    blogEntry.addComment(comment2);

    comment1.setBody("<p>Here is some body.</p>");
    comment2.setBody("<p>Here is some body again.</p>");
    decorator.decorate(context, comment1);
    decorator.decorate(context, comment2);
    assertEquals("<p>Here is some body.</p>", comment1.getBody());
    assertEquals("<p>Here is some body again.</p>", comment2.getBody());

    comment1.setBody("<p>Here is a <a href=\"http://www.google.com\">a link</a>.</p>");
    comment2.setBody("<p>Here is a <a href=http://www.google.com>a link</a> again.</p>");
    decorator.decorate(context, comment1);
    decorator.decorate(context, comment2);
    assertEquals("<p>Here is a <a href=\"http://www.google.com\" rel=\"nofollow\">a link</a>.</p>", comment1.getBody());
    assertEquals("<p>Here is a <a href=http://www.google.com rel=\"nofollow\">a link</a> again.</p>", comment2.getBody());

    comment1.setBody("<p>Here is a <a href=\"ftp://www.google.com\">a link</a>.</p>");
    comment2.setBody("<p>Here is a <a href=\"mailto://www.google.com\">a link</a> again.</p>");
    decorator.decorate(context, comment1);
    decorator.decorate(context, comment2);
    assertEquals("<p>Here is a <a href=\"ftp://www.google.com\" rel=\"nofollow\">a link</a>.</p>", comment1.getBody());
    assertEquals("<p>Here is a <a href=\"mailto://www.google.com\" rel=\"nofollow\">a link</a> again.</p>", comment2.getBody());

    comment1.setBody("<p>Here is a <a href=\"http://www.google.com\">a link</a> and <a href=\"http://www.yahoo.com\">another</a>.</p>");
    comment2.setBody("<p>Here is some body again.</p>");
    decorator.decorate(context, comment1);
    decorator.decorate(context, comment2);
    assertEquals("<p>Here is a <a href=\"http://www.google.com\" rel=\"nofollow\">a link</a> and <a href=\"http://www.yahoo.com\" rel=\"nofollow\">another</a>.</p>", comment1.getBody());
    assertEquals("<p>Here is some body again.</p>", comment2.getBody());

    // test that we don't add an extra nofollow if one is present already
    comment1.setBody("<p>Here is a <a href=\"http://www.google.com\">a link</a> and <a href=\"http://www.yahoo.com\" rel=\"nofollow\">another</a>.</p>");
    decorator.decorate(context, comment1);
    decorator.decorate(context, comment2);
    assertEquals("<p>Here is a <a href=\"http://www.google.com\" rel=\"nofollow\">a link</a> and <a href=\"http://www.yahoo.com\" rel=\"nofollow\">another</a>.</p>", comment1.getBody());
    comment1.setBody("<p>Here is a <a href=\"http://www.google.com\">a link</a> and <a href=\"http://www.yahoo.com\" rel=\"NOFOLLOW\">another</a>.</p>");
    decorator.decorate(context, comment1);
    decorator.decorate(context, comment2);
    assertEquals("<p>Here is a <a href=\"http://www.google.com\" rel=\"nofollow\">a link</a> and <a href=\"http://www.yahoo.com\" rel=\"NOFOLLOW\">another</a>.</p>", comment1.getBody());

    comment1.setBody("<p>Here is a <a target=\"_blank\" href=\"http://www.google.com\">a link</a>.</p>");
    decorator.decorate(context, comment1);
    decorator.decorate(context, comment2);
    assertEquals("<p>Here is a <a target=\"_blank\" href=\"http://www.google.com\" rel=\"nofollow\">a link</a>.</p>", comment1.getBody());

    comment1.setBody("<p>Here is a <A HREF=\"http://www.google.com\">a link</A>.</p>");
    decorator.decorate(context, comment1);
    decorator.decorate(context, comment2);
    assertEquals("<p>Here is a <A HREF=\"http://www.google.com\" rel=\"nofollow\">a link</A>.</p>", comment1.getBody());

    comment1.setBody("<p>Here is a <a href=\"http://www.google.com\" rel=\"bookmark\">a link</a>.</p>");
    decorator.decorate(context, comment1);
    decorator.decorate(context, comment2);
    assertEquals("<p>Here is a <a href=\"http://www.google.com\" rel=\"bookmark nofollow\">a link</a>.</p>", comment1.getBody());

    comment1.setBody("<p>Here is a <a href=\"http://www.google.com\" rel=\"bookmark heading\">a link</a>.</p>");
    decorator.decorate(context, comment1);
    decorator.decorate(context, comment2);
    assertEquals("<p>Here is a <a href=\"http://www.google.com\" rel=\"bookmark heading nofollow\">a link</a>.</p>", comment1.getBody());
  }

  /**
   * Tests that the rel="nofollow" link is added to TrackBacks.
   */
  public void testNoFollowAddedToTrackBacks() throws Exception {
    ContentDecoratorContext context = new ContentDecoratorContext();
    context.setView(ContentDecoratorContext.DETAIL_VIEW);

    TrackBack trackBack1 = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");
    blogEntry.addTrackBack(trackBack1);
    TrackBack trackBack2 = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");
    blogEntry.addTrackBack(trackBack2);

    trackBack1.setExcerpt("<p>Here is an excerpt.</p>");
    trackBack2.setExcerpt("<p>Here is an excerpt again.</p>");
    decorator.decorate(context, trackBack1);
    decorator.decorate(context, trackBack2);
    assertEquals("<p>Here is an excerpt.</p>", trackBack1.getExcerpt());
    assertEquals("<p>Here is an excerpt again.</p>", trackBack2.getExcerpt());

    trackBack1.setExcerpt("<p>Here is a <a href=\"http://www.google.com\">a link</a>.</p>");
    trackBack2.setExcerpt("<p>Here is a <a href=http://www.google.com>a link</a> again.</p>");
    decorator.decorate(context, trackBack1);
    decorator.decorate(context, trackBack2);
    assertEquals("<p>Here is a <a href=\"http://www.google.com\" rel=\"nofollow\">a link</a>.</p>", trackBack1.getExcerpt());
    assertEquals("<p>Here is a <a href=http://www.google.com rel=\"nofollow\">a link</a> again.</p>", trackBack2.getExcerpt());
  }

}
