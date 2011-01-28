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

import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.api.decorator.ContentDecorator;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

/**
 * Tests for the HideUnapprovedResponsesDecorator class.
 *
 * @author    Simon Brown
 */
public class HideUnapprovedResponsesDecoratorTest extends SingleBlogTestCase {

  private ContentDecorator decorator;

  protected void setUp() throws Exception {
    super.setUp();

    decorator = new HideUnapprovedResponsesDecorator();
  }

  /**
   * Tests that unapproved comments and TrackBacks are removed.
   */
  public void testUnapprovedResponsesRemovedWhenNotLoggedIn() throws Exception {
    BlogEntry blogEntry = new BlogEntry(blog);
    Comment comment1 = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    comment1.setPending();
    blogEntry.addComment(comment1);
    Comment comment2 = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    comment2.setApproved();
    blogEntry.addComment(comment2);

    TrackBack trackBack1 = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");
    trackBack1.setPending();
    blogEntry.addTrackBack(trackBack1);
    TrackBack trackBack2 = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");
    trackBack2.setApproved();
    blogEntry.addTrackBack(trackBack2);

    SecurityUtils.runAsAnonymous();

    ContentDecoratorContext context = new ContentDecoratorContext();
    decorator.decorate(context, blogEntry);
    assertEquals(1, blogEntry.getComments().size());
    assertEquals(1, blogEntry.getTrackBacks().size());

    assertSame(comment2, blogEntry.getComments().get(0));
    assertSame(trackBack2, blogEntry.getTrackBacks().get(0));
  }

  /**
   * Tests that unapproved comments and TrackBacks are removed when logged in,
   * but not a blog contributor.
   */
  public void testUnapprovedResponsesRemovedWhenLoggedIn() throws Exception {
    BlogEntry blogEntry = new BlogEntry(blog);
    Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    comment.setPending();
    blogEntry.addComment(comment);

    TrackBack trackBack = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");
    trackBack.setPending();
    blogEntry.addTrackBack(trackBack);

    SecurityUtils.runAsBlogContributor();
    blog.setProperty(Blog.BLOG_CONTRIBUTORS_KEY, "some_contributor");

    ContentDecoratorContext context = new ContentDecoratorContext();
    decorator.decorate(context, blogEntry);
    assertEquals(0, blogEntry.getComments().size());
    assertEquals(0, blogEntry.getTrackBacks().size());
  }

  /**
   * Tests that unapproved comments and TrackBacks are not removed.
   */
  public void testUnapprovedResponsesNotRemovedWhenLoggedIn() throws Exception {
    BlogEntry blogEntry = new BlogEntry(blog);
    Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    comment.setPending();
    blogEntry.addComment(comment);

    TrackBack trackBack = blogEntry.createTrackBack("title", "excerpt", "url", "blogName", "127.0.0.1");
    trackBack.setPending();
    blogEntry.addTrackBack(trackBack);

    ContentDecoratorContext context = new ContentDecoratorContext();

    SecurityUtils.runAsBlogContributor();

    decorator.decorate(context, blogEntry);
    assertEquals(1, blogEntry.getComments().size());
    assertEquals(1, blogEntry.getTrackBacks().size());
  }

  public void testUnapprovedNestedResponsesRemovedWhenNotLoggedIn() throws Exception {
    BlogEntry blogEntry = new BlogEntry(blog);
    Comment comment1 = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    comment1.setPending();
    blogEntry.addComment(comment1);

    Comment comment2 = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    comment2.setPending();
    comment2.setParent(comment1);
    blogEntry.addComment(comment2);

    SecurityUtils.runAsAnonymous();

    blogEntry = (BlogEntry)blogEntry.clone();

    ContentDecoratorContext context = new ContentDecoratorContext();
    decorator.decorate(context, blogEntry);
    assertEquals(0, blogEntry.getComments().size());
    assertEquals(0, blogEntry.getTrackBacks().size());
  }

}
