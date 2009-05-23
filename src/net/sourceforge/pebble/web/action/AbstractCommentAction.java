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
    String ipAddress = request.getRemoteAddr();
    String title = StringUtils.transformHTML(request.getParameter("title"));
    String body = request.getParameter("commentBody");

    Comment comment = blogEntry.createComment(title, body, author, email, website, ipAddress);

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
    Comment comment = blogEntry.createComment("", "", "", "", "", request.getRemoteAddr());

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
        log.error(e);
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
        log.error(e);
        return "";
      }
    }
  }

}