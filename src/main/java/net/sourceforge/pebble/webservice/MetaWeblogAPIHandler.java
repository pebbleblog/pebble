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

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.BlogService;
import net.sourceforge.pebble.domain.BlogServiceException;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.FileManager;
import net.sourceforge.pebble.domain.FileMetaData;
import net.sourceforge.pebble.domain.IllegalFileAccessException;
import net.sourceforge.pebble.domain.Tag;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlrpc.XmlRpcException;

/**
 * A handler for the MetaWeblog API (accessed via XML-RPC).
 *
 * @author    Simon Brown
 */
@SuppressWarnings("unchecked")
public class MetaWeblogAPIHandler extends AbstractAPIHandler {

  static final String BODY = "body";
  static final String AUTHOR = "author";
  static final String EMAIL = "email";
  static final String DATE = "date";
  static final String WEBSITE = "website";
  static final String IP_ADDRESS = "ipAddress";
  static final String URL = "url";
  static final String BLOG_ID = "blogid";
  static final String BLOG_NAME = "blogName";
  static final String DATE_CREATED = "dateCreated";
  static final String USER_ID = "userId";
  static final String POST_ID = "postid";
  static final String TITLE = "title";
  static final String DESCRIPTION = "description";
  static final String PERMALINK = "permaLink";
  static final String PUB_DATE = "pubDate";
  static final String CATEGORIES = "categories";
  static final String TAGS = "tags";
  static final String NAME = "name";
  static final String TYPE = "type";
  static final String BITS = "bits";
  static final String HTML_URL = "htmlUrl";
  static final String RSS_URL = "rssUrl";
  static final String COMMENTS = "comments";

  /** the log used by this class */
  private static Log log = LogFactory.getLog(MetaWeblogAPIHandler.class);

  /**
   * Creates a new media object on the server.
   *
   * @param blogid    the ID of the blog
   * @param username  the username used for logging in via XML-RPC
   * @param password  the password used for logging in via XML-RPC
   * @return  a Hashtable (structs) containing information about the object
   * @throws org.apache.xmlrpc.XmlRpcException    if something goes wrong, including an authentication error
   */
  public Hashtable newMediaObject(String blogid, String username, String password, Hashtable struct) throws XmlRpcException {
    log.debug("metaWeblog.newMediaObject(" +
        blogid + ", " +
        username + ", " +
        "********)");
    log.debug(" name = " + struct.get(NAME));
    log.debug(" type = " + struct.get(TYPE));

    Blog blog = getBlogWithBlogId(blogid);
    authenticate(blog, username, password);

    Hashtable ht = new Hashtable();

    String name = (String)struct.get(NAME);
    FileManager manager;
    if (name.startsWith("files/")) {
      manager = new FileManager(blog, FileMetaData.BLOG_FILE);
      name = name.substring(name.indexOf("/"));
    } else if (name.startsWith("images/")) {
      manager = new FileManager(blog, FileMetaData.BLOG_IMAGE);
      name = name.substring(name.indexOf("/"));
    } else {
      manager = new FileManager(blog, FileMetaData.BLOG_IMAGE);
      // name is as specified
    }

    log.debug("Saving to " + name);
    try {
      byte bytes[] = (byte[])struct.get(BITS);
      long itemSize = bytes.length/1024; // number of bytes / 1024
      if (FileManager.hasEnoughSpace(blog, itemSize)) {
        FileMetaData file = manager.saveFile(name, bytes);
        ht.put(URL, file.getUrl());
      } else {
        throw new XmlRpcException(0, "You do not have enough free space - please free some space by removing unused files or asking your system administrator to increase your quota from " + PebbleContext.getInstance().getConfiguration().getFileUploadQuota() + " KB.");
      }
    } catch (IOException e) {
      e.printStackTrace();
      throw new XmlRpcException(0, "IOException");
    } catch (IllegalFileAccessException e) {
      e.printStackTrace();
      throw new XmlRpcException(0, "Access forbidden");
    }

    return ht;
  }

  /**
   * Gets a list of categories.
   *
   * @param blogid    the ID of the blog (ignored)
   * @param username  the username used for logging in via XML-RPC
   * @param password  the password used for logging in via XML-RPC
   * @return  a Hashtable of Hashtables (a struct of structs) representing categories
   * @throws org.apache.xmlrpc.XmlRpcException    if something goes wrong, including an authentication error
   */
  public Hashtable getCategories(String blogid, String username, String password) throws XmlRpcException {
    log.debug("metaWeblog.getCategories(" +
        blogid + ", " +
        username + ", " +
        "********)");

    Blog blog = getBlogWithBlogId(blogid);
    authenticate(blog, username, password);

    Hashtable categories = new Hashtable();
    Iterator it = blog.getCategories().iterator();
    Category category;
    while (it.hasNext()) {
      category = (Category)it.next();
      if (!category.isRootCategory()) {
        Hashtable struct = new Hashtable();
        struct.put(DESCRIPTION, category.getId());
        struct.put(HTML_URL, category.getPermalink());
        struct.put(RSS_URL, blog.getUrl() + "rss.xml?category=" + category.getId());
        categories.put(category.getId(), struct);
      }
    }

    return categories;
  }

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
  public Vector getRecentPosts(String blogid, String username, String password, int numberOfPosts) throws XmlRpcException {
    log.debug("metaWeblog.getRecentPosts(" +
        blogid + ", " +
        username + ", " +
        "********)");

    Blog blog = getBlogWithBlogId(blogid);
    authenticate(blog, username, password);

    Vector posts = new Vector();
    Collection coll = blog.getRecentBlogEntries(numberOfPosts);

    Iterator it = coll.iterator();
    BlogEntry entry;
    while (it.hasNext()) {
      entry = (BlogEntry)it.next();
      posts.add(adaptBlogEntry(entry));
    }

    return posts;
  }

  /**
   * Gets an individual blog entry.
   *
   * @param postid    the ID of the blog (ignored)
   * @param username  the username used for logging in via XML-RPC
   * @param password  the password used for logging in via XML-RPC
   * @return  a Hashtable representing a blog entry
   * @throws org.apache.xmlrpc.XmlRpcException    if something goes wrong, including an authentication error
   */
  public Hashtable getPost(String postid, String username, String password) throws XmlRpcException {
    log.debug("metaWeblog.getPost(" +
        postid + ", " +
        username + ", " +
        "********)");

    Blog blog = getBlogWithPostId(postid);
    postid = getPostId(postid);
    authenticate(blog, username, password);
    BlogService service = new BlogService();
    BlogEntry entry = null;
    try {
      entry = service.getBlogEntry(blog, postid);
    } catch (BlogServiceException e) {
      throw new XmlRpcException(0, "Blog entry with ID of " + postid + " could not be loaded");
    }

    if (entry != null) {
      return adaptBlogEntry(entry);
    } else {
      throw new XmlRpcException(0, "Blog entry with ID of " + postid + " was not found.");
    }
  }

  /**
   * Creates a new blog entry.
   *
   * @param blogid    the ID of the blog (ignored)
   * @param username  the username used for logging in via XML-RPC
   * @param password  the password used for logging in via XML-RPC
   * @param struct    the struct containing the new blog entry
   * @param publish   a flag to indicate whether the entry should be published
   * @return  a String representing the ID of the new blog entry
   * @throws org.apache.xmlrpc.XmlRpcException    if something goes wrong, including an authentication error
   */
  public String newPost(String blogid, String username, String password, Hashtable struct, boolean publish) throws XmlRpcException {
    log.debug("metaWeblog.newPost(" +
        blogid + ", " +
        username + ", " +
        "********, " +
        struct + ", " +
        publish + ")");

    try {
      Blog blog = getBlogWithBlogId(blogid);
      authenticate(blog, username, password);

      BlogEntry entry = new BlogEntry(blog);

      if (struct.containsKey(PUB_DATE)) {
        Date date = (Date)struct.get(PUB_DATE);
        entry.setDate(date);
      }

      populateEntry(entry, struct, username);
      entry.setPublished(publish);

      BlogService service = new BlogService();
      service.putBlogEntry(entry);

      return formatPostId(blogid, entry.getId());
    } catch (BlogServiceException be) {
      throw new XmlRpcException(0, be.getMessage());
    }
  }

  /**
   * Edits an existing blog entry.
   *
   * @param postid    the ID of the blog entry to be edited
   * @param username  the username used for logging in via XML-RPC
   * @param password  the password used for logging in via XML-RPC
   * @param struct    the new content of the new blog entry
   * @param publish   a flag to indicate whether the entry should be published
   * @return  a boolean true value to signal success
   * @throws org.apache.xmlrpc.XmlRpcException    if something goes wrong, including an authentication error
   */
  public boolean editPost(String postid, String username, String password, Hashtable struct, boolean publish) throws XmlRpcException {
    log.debug("BloggerAPI.editPost(" +
        postid + ", " +
        username + ", " +
        "********, " +
        struct + ", " +
        publish + ")");

    try {
      Blog blog = getBlogWithPostId(postid);
      postid = getPostId(postid);
      authenticate(blog, username, password);
      BlogService service = new BlogService();
      BlogEntry entry = service.getBlogEntry(blog, postid);

      if (entry != null) {
        populateEntry(entry, struct, username);
        entry.setPublished(publish);

        service.putBlogEntry(entry);
      } else {
        throw new XmlRpcException(0, "Blog entry with ID of " + postid + " was not found.");
      }

      return true;
    } catch (BlogServiceException be) {
      throw new XmlRpcException(0, be.getMessage());
    }
  }

  /**
   * Helper method to adapt a blog entry into an XML-RPC compatible struct.
   *
   * @param entry   the BlogEntry to adapt
   * @return  a Hashtable representing the major properties of the entry
   */
  private Hashtable adaptBlogEntry(BlogEntry entry) {
    Hashtable post = new Hashtable();
    post.put(TITLE, entry.getTitle());
    post.put(PERMALINK, entry.getPermalink());
    post.put(TITLE, entry.getTitle());
    post.put(DESCRIPTION, entry.getBody());
    post.put(DATE_CREATED, entry.getDate());
    post.put(PUB_DATE, entry.getDate());
    post.put(USER_ID, entry.getAuthor());
    post.put(POST_ID, formatPostId(entry.getBlog().getId(), entry.getId()));

    Vector categories = new Vector();
    Iterator it = entry.getCategories().iterator();
    while (it.hasNext()) {
      Category cat = (Category)it.next();
      categories.add(cat.getId());
    }
    post.put(CATEGORIES, categories);

    //Get Tags
    Vector tags = new Vector();
    for (Tag tag : entry.getAllTags()) {
        tags.add(tag.getName());
    }
    post.put(TAGS, tags);

    //Get comments
    Vector comments = new Vector();
    for (Comment comment : entry.getComments()) {
        comments.add(adaptBlogEntryComment(comment));
    }
    post.put(COMMENTS, comments);

    return post;
  }

  /** 
   * help method to adapt a blog entry comments into an XML-RPC compatible struct.
   * 
   * @param entry   the BlogEntry to adapt
   * @return  a Hashtable representing the major properties of the entry
   */
  private Hashtable adaptBlogEntryComment(Comment comment) {
      Hashtable cmnt = new Hashtable();
        
      cmnt.put(BODY, comment.getBody());
      cmnt.put(AUTHOR, comment.getAuthor());
      String email = comment.getEmail();
      if (email != null) cmnt.put(EMAIL, email);
      cmnt.put(DATE, comment.getDate());
      cmnt.put(PERMALINK, comment.getPermalink());
      String website = comment.getWebsite();
      if (website != null) cmnt.put(WEBSITE, website);
      cmnt.put(IP_ADDRESS, comment.getIpAddress());

      return cmnt;
  }

  /**
   * Populates a given BlogEntry.
   *
   * @param entry     the BlogEntry to populate
   * @param struct    a Hashtable containing the blog entry details
   * @param username  the author
   */
  private void populateEntry(BlogEntry entry, Hashtable struct, String username) {
      assert entry != null;
      entry.setTitle((String)struct.get(TITLE));
      entry.setBody((String)struct.get(DESCRIPTION));
      entry.setAuthor(username);

      Vector categories = (Vector)struct.get(CATEGORIES);
      if (categories != null) {
          for (int i = 0; i < categories.size(); i++) {
              Category c = entry.getBlog().getCategory((String)categories.get(i));
              if (c != null)
                  entry.addCategory(c);
          }
      }

      String taglist = (String)struct.get(TAGS);
      entry.setTags( taglist );
  }

}
