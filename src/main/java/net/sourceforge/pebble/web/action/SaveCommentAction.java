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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.api.confirmation.CommentConfirmationStrategy;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.BlogServiceException;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.util.I18n;
import net.sourceforge.pebble.web.security.RequireSecurityToken;
import net.sourceforge.pebble.web.validation.ValidationContext;
import net.sourceforge.pebble.web.view.NotFoundView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.CommentConfirmationView;
import net.sourceforge.pebble.web.view.impl.CommentFormView;
import net.sourceforge.pebble.web.view.impl.ConfirmCommentView;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Adds a comment to an existing blog entry.
 *
 * @author    Simon Brown
 */
@RequireSecurityToken
public class SaveCommentAction extends AbstractCommentAction {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(SaveCommentAction.class);

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    BlogEntry blogEntry;
    Comment comment;

    String entry = request.getParameter("entry");
    String rememberMe = request.getParameter("rememberMe");
    String submitType = request.getParameter("submit");

    BlogService service = new BlogService();
    try {
      blogEntry = service.getBlogEntry(blog, entry);
    } catch (BlogServiceException e) {
      throw new ServletException(e);
    }
    if (blogEntry == null) {
      // just send back a 404 - this is probably somebody looking for a way
      // to send comment spam ;-)
      log.info("ignoring saveComment with no related blog entry (spam) from " + request.getRemoteAddr());
      return new NotFoundView();
    } else if (!blogEntry.isCommentsEnabled()) {
      return new CommentConfirmationView();
    }

    comment = createComment(request, blogEntry);
    ValidationContext context = validateComment(comment);

    // are we previewing or adding the comment?
    String previewButton = I18n.getMessage(blog, "comment.previewButton");

    ContentDecoratorContext decoratorContext = new ContentDecoratorContext();
    decoratorContext.setView(ContentDecoratorContext.DETAIL_VIEW);
    decoratorContext.setMedia(ContentDecoratorContext.HTML_PAGE);

    Comment decoratedComment = (Comment)comment.clone();
    blog.getContentDecoratorChain().decorate(decoratorContext, decoratedComment);
    getModel().put("decoratedComment", decoratedComment);
    getModel().put("undecoratedComment", comment);
    getModel().put("rememberMe", rememberMe);
    getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
    getModel().put(Constants.COMMENT_KEY, comment);
    request.getSession().setAttribute("rememberMe", request.getParameter("rememberMe"));

    if (submitType == null || submitType.equalsIgnoreCase(previewButton) || context.hasErrors()) {
      return new CommentFormView();
    } else {
      CommentConfirmationStrategy strategy = blog.getCommentConfirmationStrategy();

      Comment clonedComment = (Comment)comment.clone();
      request.getSession().setAttribute(Constants.COMMENT_KEY, comment);

      if (strategy.confirmationRequired(clonedComment)) {
        strategy.setupConfirmation(request);
        return new ConfirmCommentView();
      } else {
        try {
          saveComment(request, response, blogEntry, comment);
          request.getSession().removeAttribute(Constants.COMMENT_KEY);
          return new CommentConfirmationView();
        } catch (BlogServiceException be) {
          log.error(be.getMessage(), be);
          throw new ServletException(be);
        }
      }
    }
  }
}