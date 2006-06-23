package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.util.CookieUtils;
import net.sourceforge.pebble.util.MailUtils;
import net.sourceforge.pebble.web.validation.ValidationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Adds a comment to an existing blog entry.
 *
 * @author    Simon Brown
 */
public abstract class AbstractCommentAction extends Action {

  private static final Log log = LogFactory.getLog(AbstractCommentAction.class);

  protected Comment createComment(HttpServletRequest request, BlogEntry blogEntry) {
    String author = request.getParameter("author");
    String email = request.getParameter("email");
    String website = request.getParameter("website");
    String ipAddress = request.getRemoteAddr();
    String title = request.getParameter("title");
    String body = request.getParameter("body");

    Comment comment = blogEntry.createComment(title, body, author, email, website, ipAddress);

    // are we replying to an existing comment?
    String parentCommentId = request.getParameter("parent");
    if (parentCommentId != null && parentCommentId.length() > 0) {
      long parent = Long.parseLong(parentCommentId);
      Comment parentComment = blogEntry.getComment(parent);
      if (parentComment != null) {
        comment.setParent(parentComment);
      }
    }

    return comment;
  }

  protected void copyCommentToSession(HttpSession session, Comment comment) {
    session.setAttribute("comment.blogEntry", comment.getBlogEntry().getId());
    session.setAttribute("comment.author", comment.getAuthor());
    session.setAttribute("comment.email", comment.getEmail());
    session.setAttribute("comment.website", comment.getWebsite());
    session.setAttribute("comment.ipAddress", comment.getIpAddress());
    session.setAttribute("comment.title", comment.getTitle());
    session.setAttribute("comment.body", comment.getBody());
    if (comment.getParent() != null) {
      session.setAttribute("comment.parent", comment.getParent().getId());
    }
  }

  protected Comment createComment(HttpSession session, BlogEntry blogEntry) {
    String author = (String)session.getAttribute("comment.author");
    String email = (String)session.getAttribute("comment.email");
    String website = (String)session.getAttribute("comment.website");
    String ipAddress = (String)session.getAttribute("comment.ipAddress");
    String title = (String)session.getAttribute("comment.title");
    String body = (String)session.getAttribute("comment.body");

    Comment comment = blogEntry.createComment(title, body, author, email, website, ipAddress);

    // are we replying to an existing comment?
    Long parent = (Long)session.getAttribute("comment.parent");
    if (parent != null) {
      Comment parentComment = blogEntry.getComment(parent);
      if (parentComment != null) {
        comment.setParent(parentComment);
      }
    }

    return comment;
  }

  protected ValidationContext validateComment(Comment comment) {
    ValidationContext context = new ValidationContext();
    MailUtils.validate(comment.getEmail(), context);
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
