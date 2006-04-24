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
package net.sourceforge.pebble.util;

import net.sourceforge.pebble.domain.Blog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import net.sourceforge.pebble.web.validation.ValidationContext;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.*;

/**
 * Utilities for e-mail related functions.
 *
 * @author    Simon Brown
 */
public class MailUtils {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(MailUtils.class);

  /**
   * Sends an e-mail.
   *
   * @param blog    the notifying blog
   * @param to     the e-mail addresses of the recipients in the TO field
   * @param subject       the subject of the e-mail
   * @param message       the body of the e-mail
   */
  public static void sendMail(Session session, Blog blog, String to, String subject, String message) {
    Collection set = new HashSet();
    set.add(to);
    sendMail(session, blog, set, new HashSet(), new HashSet(), subject, message);
  }

  /**
   * Sends an e-mail.
   *
   * @param blog    the notifying blog
   * @param to     the e-mail addresses of the recipients in the TO field
   * @param subject       the subject of the e-mail
   * @param message       the body of the e-mail
   */
  public static void sendMail(Session session, Blog blog, Collection to, String subject, String message) {
    sendMail(session, blog, to, new HashSet(), new HashSet(), subject, message);
  }

  /**
   * Sends an e-mail.
   *
   * @param blog    the notifying blog
   * @param to     the e-mail addresses of the recipients in the TO field
   * @param cc     the e-mail addresses of the recipients in the CC field
   * @param subject       the subject of the e-mail
   * @param message       the body of the e-mail
   */
  public static void sendMail(Session session, Blog blog, Collection to, Collection cc, String subject, String message) {
    sendMail(session, blog, to, cc, new HashSet(), subject, message);
  }

  /**
   * Sends an e-mail.
   *
   * @param blog    the notifying blog
   * @param to     the e-mail addresses of the recipients in the TO field
   * @param cc     the e-mail addresses of the recipients in the CC field
   * @param bcc     the e-mail addresses of the recipients in the BCC field
   * @param subject       the subject of the e-mail
   * @param message       the body of the e-mail
   */
  public static void sendMail(Session session, Blog blog, Collection to, Collection cc, Collection bcc, String subject, String message) {
    Thread t = new SendMailThread(session, blog, to, cc, bcc, subject, message);
    t.start();
  }

  /**
   * A thread allowing the e-mail to be sent asynchronously, so the requesting
   * thread (and therefore the user) isn't held up.
   */
  static class SendMailThread extends Thread {

    /** the JavaMail session */
    private Session session;

    /** the notifying blog */
    private Blog blog;

    /** the e-mail addresses of the recipients in the TO field */
    private Collection to;

    /** the e-mail addresses of the recipients in the CC field */
    private Collection cc;

    /** the e-mail addresses of the recipients in the BCC field */
    private Collection bcc;

    /** the subject of the e-mail */
    private String subject;

    /** the body of the e-mail */
    private String message;

    /**
     * Creates a new thread to send a new e-mail.
     *
     * @param session   a JavaMail Session instance
     * @param blog    the notifying blog
     * @param to     the e-mail addresses of the recipients in the TO field
     * @param cc     the e-mail addresses of the recipients in the CC field
     * @param bcc     the e-mail addresses of the recipients in the BCC field
     * @param subject       the subject of the e-mail
     * @param message       the body of the e-mail
     */
    public SendMailThread(Session session, Blog blog, Collection to, Collection cc, Collection bcc, String subject, String message) {
      this.session = session;
      this.blog = blog;
      this.to = to;
      this.cc = cc;
      this.bcc = bcc;
      this.subject = subject;
      this.message = message;
    }

    /**
     * Performs the processing associated with this thread.
     */
    public void run() {
      try {
        // create a message and try to send it
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(blog.getFirstEmailAddress(), blog.getName()));
        Collection internetAddresses = new HashSet();
        Iterator it = to.iterator();
        while (it.hasNext()) {
          internetAddresses.add(new InternetAddress(it.next().toString()));
        }
        msg.addRecipients(Message.RecipientType.TO, (InternetAddress[])internetAddresses.toArray(new InternetAddress[]{}));

        internetAddresses = new HashSet();
        it = cc.iterator();
        while (it.hasNext()) {
          internetAddresses.add(new InternetAddress(it.next().toString()));
        }
        msg.addRecipients(Message.RecipientType.CC, (InternetAddress[])internetAddresses.toArray(new InternetAddress[]{}));

        internetAddresses = new HashSet();
        it = bcc.iterator();
        while (it.hasNext()) {
          internetAddresses.add(new InternetAddress(it.next().toString()));
        }
        msg.addRecipients(Message.RecipientType.BCC, (InternetAddress[])internetAddresses.toArray(new InternetAddress[]{}));

        msg.setSubject(subject);
        msg.setSentDate(new Date());
        msg.setContent(message, "text/html");

        log.debug("From : " + blog.getName() + " (" + blog.getFirstEmailAddress() + ")");
        log.debug("Subject : " + subject);
        log.debug("Message : " + message);

        Transport.send(msg);
      } catch (Exception e) {
        log.error("Notification e-mail could not be sent", e);
      }
    }

  }

  /**
   * Creates a reference to a JavaMail Session.
   *
   * @param blog    the Blog to create a session for
   * @return  a Session instance
   * @throws Exception    if something goes wronf creating a session
   */
  public static Session createSession(Blog blog) throws Exception {
    String ref = blog.getSmtpHost();
    if (ref.startsWith("java:comp/env")) {
      // this is a JNDI based mail session
      Context ctx = new InitialContext();
      return (Session)ctx.lookup(ref);
    } else {
      // this is a simple SMTP hostname based session
      Properties props = new Properties();
      props.put("mail.smtp.host", ref);
      return Session.getDefaultInstance(props, null);
    }
  }

  /**
   * Validates the given comment.
   *
   * @param email   the Comment instance to validate
   * @param context   the context in which to perform validation
   */
  public static void validate(String email, ValidationContext context) {
    if (email != null && !email.matches("[A-Za-z0-9_.-]+@[A-Za-z0-9_.-]+\\.[A-Za-z]{2,}")) {
      context.addError(email + " is not a valid e-mail address");
    }
  }

}
