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
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.State;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.PublishBlogEntryView;

/**
 * Tests for the ManageBlogEntryAction class.
 *
 * @author    Simon Brown
 */
public class ManageBlogEntryActionTest extends SecureActionTestCase {

  protected void setUp() throws Exception {
    action = new ManageBlogEntryAction();

    super.setUp();
  }

  public void testPublishBlogEntry() throws Exception {
    BlogService service = new BlogService();
    BlogEntry blogEntry = new BlogEntry(blog);
    blogEntry.setPublished(false);
    service.putBlogEntry(blogEntry);

    // now execute the action
    request.setParameter("entry", blogEntry.getId());
    request.setParameter("submit", "Publish");
    View view = action.process(request, response);

    assertTrue(blogEntry.isUnpublished());
    assertTrue(view instanceof PublishBlogEntryView);
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

    assertTrue(blogEntry.isPublished());
    assertTrue(view instanceof PublishBlogEntryView);
  }

  /**
   * Test that only blog owners can approve comments.
   */
  public void testDefaultRoleIsBlogOwner() {
    String roles[] = action.getRoles(request);
    assertEquals(1, roles.length);
    assertEquals(Constants.BLOG_OWNER_ROLE, roles[0]);
  }

  /**
   * Test that only blog owners can approve comments.
   */
  public void testOnlyBlogOwnersHaveAccessToApprove() {
    request.setParameter("submit", "Approve");
    String roles[] = action.getRoles(request);
    assertEquals(1, roles.length);
    assertEquals(Constants.BLOG_OWNER_ROLE, roles[0]);
  }

}
