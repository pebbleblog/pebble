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

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.CommentConfirmationView;
import net.sourceforge.pebble.web.view.impl.ConfirmCommentView;
import net.sourceforge.pebble.util.SecurityUtils;

/**
 * Tests for the SaveCommentAction class.
 *
 * @author    Simon Brown
 */
public class SaveCommentActionTest extends SingleBlogActionTestCase {

  protected void setUp() throws Exception {
    action = new SaveCommentAction();

    super.setUp();
  }

  public void testProcessAsBlogContributorWhenReplyingToBlogEntry() throws Exception {
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    service.putBlogEntry(blogEntry);

    request.setParameter("entry", "" + blogEntry.getId());
    request.setParameter("comment", "");
    request.setParameter("title", "Test Title");
    request.setParameter("commentBody", "Test Body");
    request.setParameter("author", "Test Author");
    request.setParameter("website", "http://www.somedomain.com");
    request.setParameter("avatar", "http://www.somedomain.com/avatar");
    request.setParameter("submit", "Add Comment");

    SecurityUtils.runAsBlogContributor();

    View view = action.process(request, response);
    assertTrue(view instanceof CommentConfirmationView);

    blogEntry = service.getBlogEntry(blog, blogEntry.getId());
    assertEquals(1, blogEntry.getComments().size());

    Comment comment = blogEntry.getComments().get(0);
    assertEquals("Test Title", comment.getTitle());
    assertEquals("Test Body", comment.getBody());
    assertEquals("Test Author", comment.getAuthor());
    assertEquals("http://www.somedomain.com", comment.getWebsite());
    assertEquals("http://www.somedomain.com/avatar", comment.getAvatar());
  }

  public void testProcessAsBlogContributorWhenReplyingToComment() throws Exception {
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    service.putBlogEntry(blogEntry);

    Comment comment1 = blogEntry.createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
    blogEntry.addComment(comment1);
    service.putBlogEntry(blogEntry);

    request.setParameter("entry", "" + blogEntry.getId());
    request.setParameter("comment", "" + comment1.getId());
    request.setParameter("title", "Test Title");
    request.setParameter("commentBody", "Test Body");
    request.setParameter("author", "Test Author");
    request.setParameter("website", "http://www.somedomain.com");
    request.setParameter("avatar", "http://www.somedomain.com/avatar");
    request.setParameter("submit", "Add Comment");

    SecurityUtils.runAsBlogContributor();

    View view = action.process(request, response);
    assertTrue(view instanceof CommentConfirmationView);

    blogEntry = service.getBlogEntry(blog, blogEntry.getId());
    assertEquals(2, blogEntry.getComments().size());

    Comment comment2 = blogEntry.getComments().get(1);
    assertEquals("Test Title", comment2.getTitle());
    assertEquals("Test Body", comment2.getBody());
    assertEquals("Test Author", comment2.getAuthor());
    assertEquals("http://www.somedomain.com", comment2.getWebsite());
    assertEquals("http://www.somedomain.com/avatar", comment2.getAvatar());
    assertEquals(comment1.getId(), comment2.getParent().getId());
  }

  public void testProcessAsBlogContributorWhenReplyingToCommentThatDoesntExist() throws Exception {
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    service.putBlogEntry(blogEntry);

    request.setParameter("entry", "" + blogEntry.getId());
    request.setParameter("comment", "123456789");
    request.setParameter("title", "Test Title");
    request.setParameter("commentBody", "Test Body");
    request.setParameter("author", "Test Author");
    request.setParameter("website", "http://www.somedomain.com");
    request.setParameter("avatar", "http://www.somedomain.com/avatar");
    request.setParameter("submit", "Add Comment");

    SecurityUtils.runAsBlogContributor();

    View view = action.process(request, response);
    assertTrue(view instanceof CommentConfirmationView);

    blogEntry = service.getBlogEntry(blog, blogEntry.getId());
    assertEquals(1, blogEntry.getComments().size());

    Comment comment = blogEntry.getComments().get(0);
    assertEquals("Test Title", comment.getTitle());
    assertEquals("Test Body", comment.getBody());
    assertEquals("Test Author", comment.getAuthor());
    assertEquals("http://www.somedomain.com", comment.getWebsite());
    assertEquals("http://www.somedomain.com/avatar", comment.getAvatar());    
    assertNull(comment.getParent());
  }

  public void testProcessAsAnonymousUser() throws Exception {
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    service.putBlogEntry(blogEntry);

    request.setParameter("entry", "" + blogEntry.getId());
    request.setParameter("comment", "");
    request.setParameter("title", "Test Title");
    request.setParameter("commentBody", "Test Body");
    request.setParameter("author", "Test Author");
    request.setParameter("website", "http://www.somedomain.com");
    request.setParameter("avatar", "http://www.somedomain.com/avatar");
    request.setParameter("submit", "Add Comment");

    SecurityUtils.runAsAnonymous();

    View view = action.process(request, response);
    assertTrue(view instanceof ConfirmCommentView);
    assertEquals(0, blogEntry.getComments().size());
  }

  public void testProcessWhenCommentsDisabled() throws Exception {
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setCommentsEnabled(false);
    service.putBlogEntry(blogEntry);

    request.setParameter("entry", "" + blogEntry.getId());
    request.setParameter("comment", "");
    request.setParameter("title", "Test Title");
    request.setParameter("commentBody", "Test Body");
    request.setParameter("author", "Test Author");
    request.setParameter("website", "Test Website");
    request.setParameter("avatar", "http://www.somedomain.com/avatar");
    request.setParameter("submit", "Add Comment");

    View view = action.process(request, response);
    assertTrue(view instanceof CommentConfirmationView);
    assertEquals(0, blogEntry.getComments().size());
  }

}
