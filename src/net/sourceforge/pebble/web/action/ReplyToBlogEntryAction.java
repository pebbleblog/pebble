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
package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.util.CookieUtils;
import net.sourceforge.pebble.web.view.NotFoundView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.CommentConfirmationView;
import net.sourceforge.pebble.web.view.impl.CommentFormView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Allows the user to reply to a specific blog entry.
 *
 * @author    Simon Brown
 */
public class ReplyToBlogEntryAction extends Action {

  /** the log used for this action */
  private static final Log log = LogFactory.getLog(ViewBlogEntryAction.class);

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    String entryId = request.getParameter("entry");

    BlogService service = new BlogService();
    BlogEntry blogEntry = null;
    if (entryId != null) {
      blogEntry = service.getBlogEntry(blog, entryId);
    }

    if (blogEntry == null) {
      // the entry cannot be found - it may have been removed or the
      // requesting URL was wrong

      return new NotFoundView();
    } else if (!blogEntry.isCommentsEnabled()) {
      getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
      return new CommentConfirmationView();
    } else {
      getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);

      // is "remember me" set?
      Cookie rememberMe = CookieUtils.getCookie(request.getCookies(), "rememberMe");
      if (rememberMe != null) {
        getModel().put("rememberMe", "true");
      }

      Comment comment = createComment(blog, blogEntry, request);
      getModel().put("undecoratedComment", comment);

      return new CommentFormView();
    }
  }

  private Comment createComment(Blog blog, BlogEntry blogEntry, HttpServletRequest request) {
    Comment comment = blogEntry.createComment("", "", "", "", "", request.getRemoteAddr());

    try {
      // is "remember me" set?
      Cookie rememberMe = CookieUtils.getCookie(request.getCookies(), "rememberMe");
      if (rememberMe != null) {
        // remember me has been checked and we're not already previewing a comment
        // so create a new comment as this will populate the author/email/website
        Cookie author = CookieUtils.getCookie(request.getCookies(), "rememberMe.author");
        if (author != null) {
          comment.setAuthor(URLDecoder.decode(author.getValue(), blog.getCharacterEncoding()));
        }

        Cookie email = CookieUtils.getCookie(request.getCookies(), "rememberMe.email");
        if (email != null) {
          comment.setEmail(URLDecoder.decode(email.getValue(), blog.getCharacterEncoding()));
        }

        Cookie website = CookieUtils.getCookie(request.getCookies(), "rememberMe.website");
        if (website != null) {
          comment.setWebsite(URLDecoder.decode(website.getValue(), blog.getCharacterEncoding()));
        }
      }
    } catch (UnsupportedEncodingException e) {
      log.error(e);
    }

    // are we replying to an existing comment?
    String parentCommentId = request.getParameter("comment");
    if (parentCommentId != null && parentCommentId.length() > 0) {
      long parent = Long.parseLong(parentCommentId);
      Comment parentComment = blogEntry.getComment(parent);
      if (parentComment != null) {
        comment.setParent(parentComment);
        comment.setTitle(parentComment.getTitle());
      }
    }

    return comment;
  }

}