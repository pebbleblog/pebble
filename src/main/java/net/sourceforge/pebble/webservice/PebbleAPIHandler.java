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
package net.sourceforge.pebble.webservice;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.Tag;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcException;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

/**
 * A handler for the Pebble API (accessed via XML-RPC).
 *
 * @author    Simon Brown
 */
public class PebbleAPIHandler extends AbstractAPIHandler {

  public static final String ID = "id";
  public static final String UUID = "uuid";
  public static final String DATE = "date";
  public static final String AUTHOR = "author";
  public static final String TITLE = "title";
  public static final String SUBTITLE = "subtitle";
  public static final String EXCERPT = "excerpt";
  public static final String BODY = "body";
  public static final String PERMALINK = "permalink";
  public static final String CATEGORIES = "categories";
  public static final String TAGS = "tags";
  public static final String ATTACHMENT = "attachment";
  public static final String ATTACHMENT_URL = "attachment.url";
  public static final String ATTACHMENT_SIZE = "attachment.size";
  public static final String ATTACHMENT_TYPE = "attachment.type";

  /** the log used by this class */
  private static Log log = LogFactory.getLog(PebbleAPIHandler.class);

  /**
   * Gets a list of the recent blog entries.
   *
   * @param blogid    the ID of the blog (ignored)
   * @param username  the username used for logging in via XML-RPC
   * @param password  the password used for logging in via XML-RPC
   * @param numberOfPosts   the number of posts to get
   * @return  a Vector of Hashtables (an array of structs) representing blog entries
   * @throws org.apache.xmlrpc.XmlRpcException    if something goes wrong, including an authentication error
   */
  public Vector getRecentBlogEntries(String blogid, String username, String password, int numberOfPosts) throws XmlRpcException {
    log.debug("pebble.getRecentBlogEntries(" +
        blogid + ", " +
        username + ", " +
        "********)");

    Blog blog = getBlogWithBlogId(blogid);
    authenticate(blog, username, password);

    Vector posts = new Vector();
    Collection coll = blog.getRecentPublishedBlogEntries(numberOfPosts);
    Iterator it = coll.iterator();
    BlogEntry entry;
    while (it.hasNext()) {
      entry = (BlogEntry)it.next();
      posts.add(adaptBlogEntry(entry));
    }

    return posts;
  }

  /**
   * Helper method to adapt a blog entry into an XML-RPC compatible struct.
   *
   * @param entry   the BlogEntry to adapt
   * @return  a Hashtable representing the major properties of the entry
   */
  private Hashtable adaptBlogEntry(BlogEntry entry) {
    // first apply decorators - we don't want to go out naked :-)
    ContentDecoratorContext context = new ContentDecoratorContext();
    context.setView(ContentDecoratorContext.DETAIL_VIEW);
    context.setMedia(ContentDecoratorContext.XML_RPC);
    entry.getBlog().getContentDecoratorChain().decorate(context, entry);

    Hashtable post = new Hashtable();
    post.put(TITLE, entry.getTitle());
    post.put(SUBTITLE, entry.getSubtitle());
    post.put(PERMALINK, entry.getPermalink());
    post.put(EXCERPT, entry.getExcerpt());
    post.put(BODY, entry.getBody());
    post.put(DATE, entry.getDate());
    post.put(AUTHOR, entry.getAuthor());
    post.put(ID, entry.getId());
    post.put(UUID, formatPostId(entry.getBlog().getId(), entry.getId()));

    Vector categories = new Vector();
    Iterator it = entry.getCategories().iterator();
    while (it.hasNext()) {
      Category cat = (Category)it.next();
      categories.add(cat.getId());
    }
    post.put(CATEGORIES, categories);

    Vector tags = new Vector();
    it = entry.getTagsAsList().iterator();
    while (it.hasNext()) {
      Tag tag = (Tag)it.next();
      tags.add(tag.getName());
    }
    post.put(TAGS, tags);

    if (entry.getAttachment() != null) {
      Hashtable attachment = new Hashtable();
      attachment.put(ATTACHMENT_URL, entry.getAttachment().getUrl());
      attachment.put(ATTACHMENT_SIZE, entry.getAttachment().getSize());
      attachment.put(ATTACHMENT_TYPE, entry.getAttachment().getType());

      post.put(ATTACHMENT, attachment);
    }

    return post;
  }

}
