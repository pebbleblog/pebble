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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.BlogServiceException;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.security.PebbleUserDetails;
import net.sourceforge.pebble.util.CookieUtils;
import net.sourceforge.pebble.util.MailUtils;
import net.sourceforge.pebble.util.SecurityUtils;
import net.sourceforge.pebble.util.StringUtils;
import net.sourceforge.pebble.web.validation.ValidationContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Adds a comment to an existing blog entry.
 *
 * @author    Simon Brown
 */
public abstract class AbstractCommentAction extends Action {

  private static final Log log = LogFactory.getLog(AbstractCommentAction.class);

  protected Comment createComment(HttpServletRequest request, BlogEntry blogEntry) {
    String author = StringUtils.transformHTML(request.getParameter("author"));
    String email = request.getParameter("email");
    String website = request.getParameter("website");
    String avatar = request.getParameter("avatar");
    String ipAddress = request.getRemoteAddr();
    String title = StringUtils.transformHTML(request.getParameter("title"));
    String body = request.getParameter("commentBody");

    Comment comment = blogEntry.createComment(title, body, author, email, website, avatar, ipAddress);

    // if the user is authenticated, overwrite the author information
    if (SecurityUtils.isUserAuthenticated()) {
      PebbleUserDetails user = SecurityUtils.getUserDetails();
      if (user != null) {
        comment.setAuthor(user.getName());
        comment.setEmail(user.getEmailAddress());
        if (user.getWebsite() != null && !user.getWebsite().equals("")) {
          comment.setWebsite(user.getWebsite());
        } else {
          comment.setWebsite(blogEntry.getBlog().getUrl() + "authors/" + user.getUsername() + "/");
        }
        comment.setAuthenticated(true);
      }
    }

    // are we replying to an existing comment?
    String parentCommentId = request.getParameter("comment");
    if (parentCommentId != null && parentCommentId.length() > 0) {
      long parent = Long.parseLong(parentCommentId);
      Comment parentComment = blogEntry.getComment(parent);
      if (parentComment != null) {
        comment.setParent(parentComment);
      }
    }

    return comment;
  }

  protected Comment createBlankComment(Blog blog, BlogEntry blogEntry, HttpServletRequest request) {
    Comment comment = blogEntry.createComment("", "", "", "", "", "", request.getRemoteAddr());

    // populate the author, email and website from one of :
    // - the logged in user details
    // - the "remember me" cookie
    if (SecurityUtils.isUserAuthenticated()) {
      PebbleUserDetails user = SecurityUtils.getUserDetails();
      if (user != null) {
        comment.setAuthor(user.getName());
        comment.setEmail(user.getEmailAddress());
        if (user.getWebsite() != null && !user.getWebsite().equals("")) {
          comment.setWebsite(user.getWebsite());
        } else {
          comment.setWebsite(blogEntry.getBlog().getUrl() + "authors/" + user.getUsername() + "/");
        }
        comment.setAuthenticated(true);
      }
    } else {
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
        log.error("Exception encountered", e);
      }
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

  protected ValidationContext validateComment(Comment comment) {
    ValidationContext context = new ValidationContext();
    try {
      MailUtils.validate(comment.getEmail(), context);
    } catch (NoClassDefFoundError e) {
      // most likely: JavaMail is not in classpath
      // ignore, when we can not send email we must not validate address
      // this might lead to problems when mail is activated later without this
      // address being validated... Discussion started on mailing list, Oct-25 2008
    }
    getModel().put("validationContext", context);
    return context;
  }

  protected void saveComment(HttpServletRequest request, HttpServletResponse response, BlogEntry blogEntry, Comment comment) throws BlogServiceException {
    Blog blog = blogEntry.getBlog();
    blogEntry.addComment(comment);

    BlogService service = new BlogService();
    service.putBlogEntry(blogEntry);

    // remember me functionality
    String rememberMe = (String)request.getSession().getAttribute("rememberMe");
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
  }

  private String encode(String s, String characterEncoding) {
    if (s == null) {
      return "";
    } else {
      try {
        return URLEncoder.encode(s, characterEncoding);
      } catch (UnsupportedEncodingException e) {
        log.error("Exception encountered", e);
        return "";
      }
    }
  }

}