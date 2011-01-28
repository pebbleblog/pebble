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
package net.sourceforge.pebble.api.event.comment;

import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.api.event.comment.CommentEvent;

/**
 * Tests for the CommentEvent class.
 *
 * @author    Simon Brown
 */
public class CommentEventTest extends SingleBlogTestCase {

  private CommentEvent event;

  /**
   * Tests that a CommentEvent instance is correctly constructed.
   */
  public void testConstruction() {
    Comment comment = new BlogEntry(blog).createComment("Title", "Body", "Author", "me@somedomain.com", "http://www.google.com", "http://graph.facebook.com/user/picture", "127.0.0.1");
    event = new CommentEvent(comment, CommentEvent.COMMENT_ADDED);

    assertEquals(comment, event.getSource());
    assertEquals(comment, event.getComment());
    assertEquals(CommentEvent.COMMENT_ADDED, event.getType());
  }

}
