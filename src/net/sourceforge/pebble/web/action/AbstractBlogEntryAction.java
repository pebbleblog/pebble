package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.security.PebbleUserDetails;
import net.sourceforge.pebble.util.CookieUtils;
import net.sourceforge.pebble.util.SecurityUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Abstract action for blog entry actions.
 *
 * @author    Simon Brown
 */
public abstract class AbstractBlogEntryAction extends Action {

  /** the log used for this action */
  private static final Log log = LogFactory.getLog(ViewBlogEntryAction.class);

  protected Comment createComment(Blog blog, BlogEntry blogEntry, HttpServletRequest request) {
    Comment comment = blogEntry.createComment("", "", "", "", "", request.getRemoteAddr());

    // populate the author, email and website from one of :
    // - the logged in user details
    // - the "remember me" cookie
    if (SecurityUtils.isUserAuthenticated()) {
      PebbleUserDetails user = SecurityUtils.getUserDetails();
      if (user != null) {
        comment.setAuthor(user.getName());
        comment.setEmail(user.getEmailAddress());
        comment.setWebsite(user.getWebsite());
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

}