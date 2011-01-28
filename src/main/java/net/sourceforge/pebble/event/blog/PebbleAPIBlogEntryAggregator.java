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
package net.sourceforge.pebble.event.blog;

import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.webservice.PebbleAPIHandler;
import net.sourceforge.pebble.PluginProperties;
import net.sourceforge.pebble.api.event.blog.BlogEvent;
import net.sourceforge.pebble.api.event.blog.BlogListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcClient;

import java.util.*;

/**
 * Blog listener that polls a Pebble blog, using the Pebble XML-RPC API,
 * to pull across and aggregate blog entries. This assumes that the IDs of
 * blog entries in both the local and remote blogs don't clash. 
 *
 * @author Simon Brown
 */
public abstract class PebbleAPIBlogEntryAggregator extends TimerTask implements BlogListener {

  private static final int ONE_HOUR = 1000 * 60 * 60;
  private static final Log log = LogFactory.getLog(PebbleAPIBlogEntryAggregator.class);

  public static final String XMLRPC_URL_KEY = ".xmlrpcUrl";
  public static final String BLOG_KEY = ".blog";
  public static final String USERNAME_KEY = ".username";
  public static final String PASSWORD_KEY = ".password";
  public static final String BLOG_ENTRIES_KEY = ".blogEntries";

  private Blog blog;
  private Timer timer = new Timer();

  /**
   * Called when a blog has been started.
   *
   * @param event   a BlogEvent instance
   */
  public void blogStarted(BlogEvent event) {
    this.blog = event.getBlog();
    timer.schedule(this, 0, ONE_HOUR);
  }

  /**
   * Called when a blog has been stopped.
   *
   * @param event   a BlogEvent instance
   */
  public void blogStopped(BlogEvent event) {
    timer.cancel();
  }

  /**
   *
   */
  public void run() {
    try {
      PluginProperties props = blog.getPluginProperties();
      XmlRpcClient xmlrpc = new XmlRpcClient(props.getProperty(getPropertyPrefix() + XMLRPC_URL_KEY));
      Vector<Object> params = new Vector<Object>();
      params.add(props.getProperty(getPropertyPrefix() + BLOG_KEY));
      params.add(props.getProperty(getPropertyPrefix() + USERNAME_KEY));
      params.add(props.getProperty(getPropertyPrefix() + PASSWORD_KEY));

      int numberOfBlogEntries = 10;
      try {
        numberOfBlogEntries = Integer.parseInt(props.getProperty(getPropertyPrefix() + BLOG_ENTRIES_KEY));
      } catch (NumberFormatException nfe) {
        // do nothing, the value has already been defaulted
      }
      params.add(numberOfBlogEntries);

      // get the remote blog entries
      Vector<Hashtable> remoteBlogEntries = (Vector<Hashtable>)xmlrpc.execute("pebble.getRecentBlogEntries", params);

      // loop through them and add them to the local blog
      for (Hashtable remoteBlogEntry : remoteBlogEntries) {
        String id = (String)remoteBlogEntry.get(PebbleAPIHandler.ID);
        String title = (String)remoteBlogEntry.get(PebbleAPIHandler.TITLE);
        String subtitle = (String)remoteBlogEntry.get(PebbleAPIHandler.SUBTITLE);
        String excerpt = (String)remoteBlogEntry.get(PebbleAPIHandler.EXCERPT);
        String body = (String)remoteBlogEntry.get(PebbleAPIHandler.BODY);
        Date date = (Date)remoteBlogEntry.get(PebbleAPIHandler.DATE);
        String author = (String)remoteBlogEntry.get(PebbleAPIHandler.AUTHOR);
        String permalink = (String)remoteBlogEntry.get(PebbleAPIHandler.PERMALINK);
        Vector<String> categories = (Vector<String>)remoteBlogEntry.get(PebbleAPIHandler.CATEGORIES);
        Vector<String> tags = (Vector<String>)remoteBlogEntry.get(PebbleAPIHandler.TAGS);
        Hashtable attachment = (Hashtable)remoteBlogEntry.get(PebbleAPIHandler.ATTACHMENT);

        if (!preAggregate(remoteBlogEntry)) {
          continue;
        }

        log.info("Aggregating " + title + " [ " + id + " | " + permalink + " ]");

        BlogService service = new BlogService();
        BlogEntry blogEntry = service.getBlogEntry(blog, id);
        if (blogEntry == null) {
          // create a new blog entry if one doesn't exist
          blogEntry = new BlogEntry(blog);
          blogEntry.setDate(new Date(Long.parseLong(id)));
          service.putBlogEntry(blogEntry);
        }

        // now ensure the local blog entry is in sync
        blogEntry.setTitle(title);
        blogEntry.setSubtitle(subtitle);
        blogEntry.setBody(body);
        blogEntry.setExcerpt(excerpt);
        blogEntry.setAuthor(author);
        blogEntry.setOriginalPermalink(permalink);
        blogEntry.setCommentsEnabled(false);
        blogEntry.setTrackBacksEnabled(false);

        if (categories != null) {
          for (String categoryId : categories) {
            blogEntry.addCategory(blog.getCategory(categoryId));
          }
        }

        if (tags != null) {
          StringBuffer buf = new StringBuffer();
          Iterator it = tags.iterator();
          while (it.hasNext()) {
            String tag = (String)it.next();
            buf.append(tag);
            if (it.hasNext()) {
              buf.append(" ");
            }
          }
          blogEntry.setTags(buf.toString());
        }

        if (attachment != null) {
          Attachment a = new Attachment();
          a.setUrl((String)attachment.get(PebbleAPIHandler.ATTACHMENT_URL));
          a.setSize((Long)attachment.get(PebbleAPIHandler.ATTACHMENT_SIZE));
          a.setType((String)attachment.get(PebbleAPIHandler.ATTACHMENT_TYPE));
          blogEntry.setAttachment(a);
        }

        postAggregate(blogEntry);

        service.putBlogEntry(blogEntry);
      }
    } catch (Exception e) {
      log.error("Exception encountered", e);
    }
  }

  /**
   * Called before each blog entry is aggregated. Returning false will
   * stop the entry from being aggregated.
   *
   * @param blogEntry   a Hashtable instance representing a blog entry
   * @return  true if the blog entry should be aggregated, false otherwise
   */
  protected abstract boolean preAggregate(Hashtable blogEntry);

  /**
   * Called after each blog entry is aggregated. Use this to enrich
   * aggregated blog entries.
   *
   * @param blogEntry   a Hashtable instance representing a blog entry
   */
  protected abstract void postAggregate(BlogEntry blogEntry);

  /**
   * Gets the prefix to use when defining properties for this plugin.
   *
   * @return  a String (the class name)
   */
  protected String getPropertyPrefix() {
    return this.getClass().getSimpleName();
  }

}