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

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.event.response.IpAddressListener;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.Comment;

import java.util.Date;

/**
 * Tests for the PublishBlogEntryAction class.
 *
 * @author    Simon Brown
 */
public class PublishBlogEntryActionTest extends SecureActionTestCase {

  protected void setUp() throws Exception {
    action = new PublishBlogEntryAction();

    super.setUp();
  }

  public void testPublishBlogEntryNow() throws Exception {
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setDate(new Date(100000));
    blogEntry.setPublished(false);
    service.putBlogEntry(blogEntry);

    // now execute the action
    request.setParameter("entry", blogEntry.getId());
    request.setParameter("publishDate", "now");
    request.setParameter("submit", "Publish");
    View view = action.process(request, response);

    blogEntry = (BlogEntry)blog.getRecentBlogEntries(1).get(0);
    assertTrue(blogEntry.isPublished());
    assertEquals(new Date().getTime(), blogEntry.getDate().getTime(), 1000);
    assertTrue(view instanceof RedirectView);
  }

  public void testPublishBlogEntryAsIsAndCheckCommentsStaysIndexed() throws Exception {
    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "127.0.0.1");
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setDate(new Date(100000));
    blogEntry.setPublished(false);
    service.putBlogEntry(blogEntry);

    Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    blogEntry.addComment(comment);
    service.putBlogEntry(blogEntry);

    String commentId = comment.getGuid();
    assertTrue(blog.getResponseIndex().getApprovedResponses().contains(commentId));

    // now execute the action
    request.setParameter("entry", blogEntry.getId());
    request.setParameter("publishDate", "as-is");
    request.setParameter("submit", "Publish");
    View view = action.process(request, response);

    blogEntry = service.getBlogEntry(blog, blogEntry.getId());
    assertTrue(blogEntry.isPublished());
    assertEquals(new Date(100000), blogEntry.getDate());

    // check that the original comment remains intact
    assertTrue(blog.getResponseIndex().getApprovedResponses().contains(commentId));
  }

  public void testPublishBlogEntryNowAndCheckCommentsReindexed() throws Exception {
    blog.getPluginProperties().setProperty(IpAddressListener.WHITELIST_KEY, "127.0.0.1");
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setDate(new Date(100000));
    blogEntry.setPublished(false);
    service.putBlogEntry(blogEntry);

    Comment comment = blogEntry.createComment("title", "body", "author", "email", "website", "avatar", "127.0.0.1");
    blogEntry.addComment(comment);
    service.putBlogEntry(blogEntry);

    String commentId = comment.getGuid();
    assertTrue(blog.getResponseIndex().getApprovedResponses().contains(commentId));

    // now execute the action
    request.setParameter("entry", blogEntry.getId());
    request.setParameter("publishDate", "now");
    request.setParameter("submit", "Publish");
    View view = action.process(request, response);

    blogEntry = (BlogEntry)blog.getRecentBlogEntries(1).get(0);
    assertTrue(blogEntry.isPublished());
    assertEquals(new Date().getTime(), blogEntry.getDate().getTime(), 1000);

    // check that the original comment has been unindexed
    assertFalse(blog.getResponseIndex().getApprovedResponses().contains(commentId));
    assertTrue(blog.getResponseIndex().getApprovedResponses().contains(blogEntry.getComments().get(0).getGuid()));
  }

  public void testUnpublishBlogEntry() throws Exception {
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setPublished(true);
    service.putBlogEntry(blogEntry);

    // now execute the action
    request.setParameter("entry", blogEntry.getId());
    request.setParameter("submit", "Unpublish");
    View view = action.process(request, response);

    blogEntry = service.getBlogEntry(blog, blogEntry.getId());
    assertTrue(blogEntry.isUnpublished());
    assertTrue(view instanceof RedirectView);
  }

  /**
   * Test that only blog owners can approve comments.
   */
  public void testDefaultRoleIsBlogPublisher() {
    String roles[] = action.getRoles(request);
    assertEquals(1, roles.length);
    assertEquals(Constants.BLOG_PUBLISHER_ROLE, roles[0]);
  }

}
