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
package net.sourceforge.pebble.event.blogentry;

import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.util.MailUtils;

import javax.mail.Session;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Sends an e-mail notification to e-mail subscribers when new blog entries
 * are added.
 *
 * @author Simon Brown
 */
public class EmailSubscriptionListener extends BlogEntryListenerSupport {

  /** a token to be replaced when sending e-mails */
  private static final String EMAIL_ADDRESS_TOKEN = "EMAIL_ADDRESS";

  /**
   * Called when a blog entry has been published.
   *
   * @param event a BlogEntryEvent instance
   */
  public void blogEntryPublished(BlogEntryEvent event) {
    BlogEntry blogEntry = event.getBlogEntry();
    sendNotification((BlogEntry)blogEntry.clone());
  }

  private void sendNotification(BlogEntry blogEntry) {
    Blog blog = blogEntry.getBlog();

    // first of all decorate the blog entry, as if it was being rendered
    // via a HTML page or XML feed
    ContentDecoratorContext context = new ContentDecoratorContext();
    context.setView(ContentDecoratorContext.DETAIL_VIEW);
    context.setMedia(ContentDecoratorContext.EMAIL);
    blog.getContentDecoratorChain().decorate(context, blogEntry);

    SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm:ss z");
    sdf.setTimeZone(blog.getTimeZone());

    String subject = MailUtils.getBlogEntryPrefix(blog) + " " + blogEntry.getTitle();

    String message = "<a href=\"" + blogEntry.getLocalPermalink() + "\">Blog entry</a> posted by " + (blogEntry.getUser() != null ? blogEntry.getUser().getName() : blogEntry.getAuthor()) + " on " + sdf.format(blogEntry.getDate());
    message += "\n<br>";
    if (blogEntry.getExcerpt() != null && blogEntry.getExcerpt().trim().length() > 0) {
      message += blogEntry.getExcerpt();
    } else {
      message += blogEntry.getBody();
    }
    message += "\n<br>";
    message += "<a href=\"" + blogEntry.getLocalPermalink() + "\">Permalink</a>";

    message += " | ";
    message += "<a href=\"" + blog.getUrl() + "unsubscribe.action?email=" + EMAIL_ADDRESS_TOKEN + "\">Opt-out</a>";

    List<String> to = blog.getEmailSubscriptionList().getEmailAddresses();

    // now send personalized e-mails to the blog owner and everybody
    // that left a comment specifying their e-mail address
    try {
      Session session = MailUtils.createSession();
      for (String emailAddress : to) {
        // customize the opt-out link and send the message
        MailUtils.sendMail(session, blog, emailAddress, subject,
            message.replaceAll(EMAIL_ADDRESS_TOKEN, emailAddress));
      }
    } catch (Exception e) {
        e.printStackTrace();
    } catch (NoClassDefFoundError e) {
        // most likely: JavaMail is not in classpath
        e.printStackTrace();
    }
  }

}