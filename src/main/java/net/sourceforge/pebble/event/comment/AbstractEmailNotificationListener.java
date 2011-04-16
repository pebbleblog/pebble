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
package net.sourceforge.pebble.event.comment;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.api.event.comment.CommentEvent;
import net.sourceforge.pebble.util.MailUtils;
import net.sourceforge.pebble.util.StringUtils;
import net.sourceforge.pebble.web.security.SecurityTokenValidator;
import net.sourceforge.pebble.web.security.SecurityTokenValidatorImpl;

import javax.mail.Session;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Base class for listeners that send an e-mail notification when new
 * comments are added.
 *
 * @author Simon Brown
 */
public abstract class AbstractEmailNotificationListener extends CommentListenerSupport {

  /**
   * a token to be replaced when sending e-mails
   */
  private static final String EMAIL_ADDRESS_TOKEN = "EMAIL_ADDRESS";

  /**
   * Called when a comment has been added.
   *
   * @param event a CommentEvent instance
   */
  public void commentAdded(CommentEvent event) {
    Comment comment = event.getComment();
    if (comment.isApproved()) {
      sendNotification(comment);
    } else if (comment.isPending()) {
      sendApprovalRequest(comment);
    }
  }

  /**
   * Called when a comment has been approved.
   *
   * @param event a CommentEvent instance
   */
  public void commentApproved(CommentEvent event) {
    Comment comment = event.getComment();
    sendNotification(comment);
  }

  private void sendNotification(Comment comment) {
    Blog blog = comment.getBlogEntry().getBlog();

    ContentDecoratorContext context = new ContentDecoratorContext();
    context.setView(ContentDecoratorContext.DETAIL_VIEW);
    context.setMedia(ContentDecoratorContext.EMAIL);

    blog.getContentDecoratorChain().decorate(context, comment);

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
    sdf.setTimeZone(blog.getTimeZone());

    String subject = MailUtils.getCommentPrefix(blog) + " " + comment.getTitle();
    String author = StringUtils.transformHTML(comment.getAuthor());
    if (comment.getWebsite() != null) {
      author = "<a href=\"" + comment.getWebsite() + "\">" + author + "</a>";
    }

    String message = "Comment from " + author + " on " + sdf.format(comment.getDate());
    message += " in response to " + comment.getBlogEntry().getTitle();
    message += "\n\n<br><br>";
    message += comment.getBody();
    message += "\n\n<br><br>";
    message += "<a href=\"" + comment.getPermalink() + "\">Permalink</a>";
    message += " | ";
    message += "<a href=\"" + blog.getUrl() + "removeEmailAddress.action?entry=" + comment.getBlogEntry().getId() + "&email=" + EMAIL_ADDRESS_TOKEN + "\">Opt-out</a>";

    Collection to = getEmailAddresses(comment);
    Iterator it = comment.getBlogEntry().getComments().iterator();
    Comment blogComment;
    while (it.hasNext()) {
      blogComment = (Comment) it.next();
      if (blogComment.getEmail() != null && blogComment.getEmail().length() > 0) {
        to.add(blogComment.getEmail());
      }
    }

    // now send personalized e-mails to the blog owner and everybody
    // that left a comment specifying their e-mail address
    try {
      Session session = MailUtils.createSession();
      Iterator emailAddresses = to.iterator();
      while (emailAddresses.hasNext()) {
        String emailAddress = (String) emailAddresses.next();

        // customize the opt-out link and send the message
        MailUtils.sendMail(session, blog, emailAddress, subject,
                message.replaceAll(EMAIL_ADDRESS_TOKEN, emailAddress));
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void sendApprovalRequest(Comment comment) {
    Blog blog = comment.getBlogEntry().getBlog();

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
    sdf.setTimeZone(blog.getTimeZone());

    String subject = MailUtils.getCommentPrefix(blog, comment.getState()) + " " + comment.getTitle();

    String author = StringUtils.transformHTML(comment.getAuthor());
    if (comment.getWebsite() != null) {
      author = "<a href=\"" + comment.getWebsite() + "\">" + author + "</a>";
    }

    SecurityTokenValidator validator = new SecurityTokenValidatorImpl();

    String message = "Comment from " + author + " on " + sdf.format(comment.getDate());
    message += " in response to " + comment.getBlogEntry().getTitle();
    message += "\n\n<br><br>";
    message += comment.getBody();
    message += "\n\n<br><br>";
    message += "<a href=\"" + comment.getPermalink() + "\">Permalink</a>";
    message += " | ";
    message += "<a href=\"" + blog.getUrl() + validator.generateSignedQueryString("manageResponses.secureaction",
            createMap("response", comment.getGuid(), "submit", "Approve"), blog.getXsrfSigningSalt()) + "\">Approve</a>";
    message += " | ";
    message += "<a href=\"" + blog.getUrl() + validator.generateSignedQueryString("manageResponses.secureaction",
            createMap("response", comment.getGuid(), "submit", "Reject"), blog.getXsrfSigningSalt()) + "\">Reject</a>";
    message += " | ";
    message += "<a href=\"" + blog.getUrl() + validator.generateSignedQueryString("manageResponses.secureaction",
            createMap("response", comment.getGuid(), "submit", "Remove"), blog.getXsrfSigningSalt()) + "\">Remove</a>";

    Collection to = getEmailAddresses(comment);

    try {
      MailUtils.sendMail(MailUtils.createSession(), blog, to, subject, message);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Map<String, String[]> createMap(String... values) {
    Map<String, String[]> map = new HashMap<String, String[]>();
    for (int i = 0; i < values.length - 1; i += 2) {
      map.put(values[i], new String[]{values[i + 1]});
    }
    return map;
  }

  /**
   * Returns the collection of recipients.
   *
   * @param comment the Comment from the event
   * @return a Collection of e-mail addresses (Strings)
   */
  protected abstract Collection getEmailAddresses(Comment comment);

}
