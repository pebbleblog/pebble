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
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.util.CookieUtils;
import net.sourceforge.pebble.util.SecurityUtils;
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

/**
 * Allows the user to reply to a specific blog entry.
 *
 * @author    Simon Brown
 */
public class ReplyToBlogEntryAction extends AbstractCommentAction {

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
      try {
        blogEntry = service.getBlogEntry(blog, entryId);
      } catch (BlogServiceException e) {
        throw new ServletException(e);
      }
    }

    if (blogEntry == null) {
      // the entry cannot be found - it may have been removed or the
      // requesting URL was wrong

      return new NotFoundView();
    } else if (!blogEntry.isPublished() && !(SecurityUtils.isUserAuthorisedForBlog(blog))) {
      // the entry exists, but isn't yet published
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

      ContentDecoratorContext decoratorContext = new ContentDecoratorContext();
      decoratorContext.setView(ContentDecoratorContext.DETAIL_VIEW);
      decoratorContext.setMedia(ContentDecoratorContext.HTML_PAGE);
      Comment comment = createBlankComment(blog, blogEntry, request);
      Comment decoratedComment = (Comment)comment.clone();
      blog.getContentDecoratorChain().decorate(decoratorContext, decoratedComment);
      getModel().put("decoratedComment", decoratedComment);
      getModel().put("undecoratedComment", comment);

      // are we replying to an existing comment?
      String parentCommentId = request.getParameter("comment");
      if (parentCommentId != null && parentCommentId.length() > 0) {
        Comment parentComment = blogEntry.getComment(Long.parseLong(parentCommentId));
        blog.getContentDecoratorChain().decorate(decoratorContext, parentComment);
        getModel().put("parentComment", parentComment);
      }

      return new CommentFormView();
    }
  }

}