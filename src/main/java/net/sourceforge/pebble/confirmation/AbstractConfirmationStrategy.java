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

package net.sourceforge.pebble.confirmation;

import net.sourceforge.pebble.PluginProperties;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.api.event.comment.CommentListener;
import net.sourceforge.pebble.api.confirmation.CommentConfirmationStrategy;
import net.sourceforge.pebble.api.confirmation.TrackBackConfirmationStrategy;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.event.response.ContentSpamListener;
import net.sourceforge.pebble.event.response.IpAddressListener;
import net.sourceforge.pebble.event.response.LinkSpamListener;
import net.sourceforge.pebble.event.response.SpamScoreListener;
import net.sourceforge.pebble.util.SecurityUtils;

/**
 * Starting point for ConfirmationStrategy implementations.
 *
 * @author    Simon Brown
 */
public abstract class AbstractConfirmationStrategy implements CommentConfirmationStrategy, TrackBackConfirmationStrategy {

  /** the name of the required override property */
  public static final String REQUIRED_KEY = "CommentConfirmationStrategy.required";

  /**
   * Called to determine whether confirmation is required. This default
   * implementation returns false if the user is authenticated. Otherwise,
   * it runs the default set of comment listeners to determine
   * whether the comment is spam. If so, true is returned.
   *
   * @param comment the Comment being confirmed
   * @return true if the comment should be confirmed, false otherwise
   */
  public boolean confirmationRequired(Comment comment) {
    PluginProperties props = comment.getBlogEntry().getBlog().getPluginProperties();
    String required = props.getProperty(REQUIRED_KEY);

    Blog blog = comment.getBlogEntry().getBlog();
    if (SecurityUtils.isUserAuthorisedForBlog(blog)) {
      return false;
    } else {
      // run a subset of the default comment listeners to figure out whether
      // the comment is spam
      CommentListener listener1 = new IpAddressListener();
      CommentListener listener2 = new LinkSpamListener();
      CommentListener listener3 = new ContentSpamListener();
      CommentListener listener4 = new SpamScoreListener();
      CommentEvent event = new CommentEvent(comment, CommentEvent.COMMENT_ADDED);

      listener1.commentAdded(event);
      listener2.commentAdded(event);
      listener3.commentAdded(event);
      listener4.commentAdded(event);

      return (required != null && required.equalsIgnoreCase("true")) || !comment.isApproved();
    }
  }

  /**
   * Called to determine whether confirmation is required.
   *
   * @param blog    the owning Blog
   * @return true if the confirmation is required, false otherwise
   */
  public boolean confirmationRequired(Blog blog) {
    return !SecurityUtils.isUserAuthorisedForBlog(blog);
  }

}