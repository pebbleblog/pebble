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
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.plugin.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.plugin.decorator.ContentDecoratorChain;
import net.sourceforge.pebble.util.CookieUtils;
import net.sourceforge.pebble.util.MailUtils;
import net.sourceforge.pebble.web.view.NotFoundView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.CommentConfirmationView;
import net.sourceforge.pebble.web.view.impl.CommentFormView;
import net.sourceforge.pebble.web.validation.ValidationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ResourceBundle;

/**
 * Adds a comment to an existing blog entry.
 *
 * @author    Simon Brown
 */
public class SaveCommentAction extends Action {

  private static final String REFERER_HEADER = "Referer";

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

    String referer = request.getHeader(REFERER_HEADER);
    log.debug("Referer is " + referer);

    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    BlogEntry blogEntry = null;
    Comment comment = null;

    String entry = request.getParameter("entry");
    long parent = -1;
    try {
      parent = Long.parseLong(request.getParameter("parent"));
    } catch (NumberFormatException nfe) {
    }
    String rememberMe = request.getParameter("rememberMe");
    String submitType = request.getParameter("submit");

    BlogService service = new BlogService();
    blogEntry = service.getBlogEntry(blog, entry);
    if (blogEntry == null) {
      // just send back a 404 - this is probably somebody looking for a way
      // to send comment spam ;-)
      return new NotFoundView();
    } else if (!blogEntry.isCommentsEnabled()) {
      return new CommentConfirmationView();
    } else if (referer == null ||
      (
        referer.indexOf("replyToBlogEntry.action") == -1 &&
        referer.indexOf("saveComment.action") == -1
      )) {
      // somebody is trying to hit this action directly without using the form
      // could be spam
      return new NotFoundView();
    }

    getModel().put(Constants.BLOG_ENTRY_KEY, blogEntry);
    comment = createComment(request, blogEntry);
    ValidationContext context = validateComment(comment);

    getModel().put("rememberMe", rememberMe);

    // are we previewing or adding the comment?
    ResourceBundle bundle = ResourceBundle.getBundle("resources", blog.getLocale());
    String previewButton = bundle.getString("comment.previewButton");

    if (submitType == null || submitType.equalsIgnoreCase(previewButton) || context.hasErrors()) {
      ContentDecoratorContext decoratorContext = new ContentDecoratorContext();
      decoratorContext.setView(ContentDecoratorContext.DETAIL_VIEW);
      decoratorContext.setMedia(ContentDecoratorContext.HTML_PAGE);

//      BlogEntry clonedBlogEntry = (BlogEntry)blogEntry.clone();
//      comment.setParent(clonedBlogEntry.getComment(parent));
//      clonedBlogEntry.addComment(comment);
//      BlogEntry decoratedBlogEntry = ContentDecoratorChain.applyDecorators(clonedBlogEntry, decoratorContext);
      Comment decoratedComment = (Comment)comment.clone();
      blog.getContentDecoratorChain().decorate(decoratorContext, decoratedComment);
      getModel().put("decoratedComment", decoratedComment);
      getModel().put("undecoratedComment", comment);

      return new CommentFormView();
    } else {
      // we are storing the comment
      comment.setParent(blogEntry.getComment(parent));
      blogEntry.addComment(comment);

      try {
        service.putBlogEntry(blogEntry);

        // remember me functionality
        if (rememberMe != null && rememberMe.equals("true")) {
          CookieUtils.addCookie(response, "rememberMe", "true", CookieUtils.ONE_MONTH);
          CookieUtils.addCookie(response, "rememberMe.author", encode(comment.getAuthor(), blog.getCharacterEncoding()), CookieUtils.ONE_MONTH);
          CookieUtils.addCookie(response, "rememberMe.email", encode(comment.getEmail(), blog.getCharacterEncoding()), CookieUtils.ONE_MONTH);
          CookieUtils.addCookie(response, "rememberMe.website", encode(comment.getWebsite(), blog.getCharacterEncoding()), CookieUtils.ONE_MONTH);
        } else {
          CookieUtils.removeCookie(response, "rememberMe");
          CookieUtils.removeCookie(response, "rememberMe.author");
          CookieUtils.removeCookie(response, "rememberMe.email");
          CookieUtils.removeCookie(response, "rememberMe.website");
        }

        getModel().put(Constants.COMMENT_KEY, comment);
        return new CommentConfirmationView();
      } catch (BlogException be) {
        log.error(be.getMessage(), be);
        throw new ServletException(be);
      }
    }
  }

  private Comment createComment(HttpServletRequest request, BlogEntry blogEntry) {
    String author = request.getParameter("author");
    String email = request.getParameter("email");
    String website = request.getParameter("website");
    String ipAddress = request.getRemoteAddr();
    String title = request.getParameter("title");
    String body = request.getParameter("body");

    Comment comment = null;
    comment = blogEntry.createComment(title, body, author, email, website, ipAddress);
    return comment;
  }

  private ValidationContext validateComment(Comment comment) {
    ValidationContext context = new ValidationContext();
    MailUtils.validate(comment.getEmail(), context);
    getModel().put("validationContext", context);
    return context;
  }

  private String encode(String s, String characterEncoding) {
    if (s == null) {
      return "";
    } else {
      try {
        return URLEncoder.encode(s, characterEncoding);
      } catch (UnsupportedEncodingException e) {
        log.error(e);
        return "";
      }
    }
  }

}