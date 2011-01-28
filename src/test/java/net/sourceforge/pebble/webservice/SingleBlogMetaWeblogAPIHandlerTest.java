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

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.mock.MockAuthenticationManager;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.apache.xmlrpc.XmlRpcException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Tests for the MetaWeblogAPIHandler class, when using a simple blog.
 *
 * @author    Simon Brown
 */
public class SingleBlogMetaWeblogAPIHandlerTest extends SingleBlogTestCase {

  private MetaWeblogAPIHandler handler = new MetaWeblogAPIHandler();
  private AuthenticationManager authenticationManager;

  protected void setUp() throws Exception {
    super.setUp();

    authenticationManager = new MockAuthenticationManager(true, new GrantedAuthority[] {new GrantedAuthorityImpl(Constants.BLOG_CONTRIBUTOR_ROLE)});
    handler.setAuthenticationManager(authenticationManager);
    blog.setProperty(Blog.BLOG_CONTRIBUTORS_KEY, "username");
  }

  public void testConfigured() {
    assertSame(authenticationManager, handler.getAuthenticationManager());
  }

  /**
   * Tests that authentication fails properly.
   */
  public void testAuthenticationFailure() {
    handler.setAuthenticationManager(new MockAuthenticationManager(false));
    try {
      handler.getCategories("default", "username", "password");
      fail();
    } catch (XmlRpcAuthenticationException xmlrpcae) {
    } catch (XmlRpcException xmlrpce) {
    }
    try {
      handler.editPost("default/123", "username", "password", new Hashtable(), true);
      fail();
    } catch (XmlRpcAuthenticationException xmlrpcae) {
    } catch (XmlRpcException xmlrpce) {
      xmlrpce.printStackTrace();
      fail();
    }
    try {
      handler.getPost("default/123", "username", "password");
      fail();
    } catch (XmlRpcAuthenticationException xmlrpcae) {
    } catch (XmlRpcException xmlrpce) {
      fail();
    }
    try {
      handler.getRecentPosts("default", "username", "password", 10);
      fail();
    } catch (XmlRpcAuthenticationException xmlrpcae) {
    } catch (XmlRpcException xmlrpce) {
      fail();
    }
    try {
      handler.newPost("default", "username", "password", new Hashtable(), true);
      fail();
    } catch (XmlRpcAuthenticationException xmlrpcae) {
    } catch (XmlRpcException xmlrpce) {
      fail();
    }
  }

  /**
   * Tests that authentication works properly.
   */
  public void testAuthenticationSuccess() {
    try {
      handler.getCategories("123", "username", "password");
    } catch (XmlRpcAuthenticationException xmlrpcae) {
      fail();
    } catch (XmlRpcException xmlrpce) {
    }
    try {
      handler.editPost("123", "username", "password", new Hashtable(), true);
    } catch (XmlRpcAuthenticationException xmlrpcae) {
      fail();
    } catch (XmlRpcException xmlrpce) {
    }
    try {
      handler.getPost("123", "username", "password");
    } catch (XmlRpcAuthenticationException xmlrpcae) {
      fail();
    } catch (XmlRpcException xmlrpce) {
    }
    try {
      handler.getRecentPosts("default", "username", "password", 10);
    } catch (XmlRpcAuthenticationException xmlrpcae) {
      fail();
    } catch (XmlRpcException xmlrpce) {
    }
    try {
      handler.newPost("default", "username", "password", new Hashtable(), true);
    } catch (XmlRpcAuthenticationException xmlrpcae) {
      fail();
    } catch (XmlRpcException xmlrpce) {
    }
  }

  public void testGetRecentPostsFromEmptyBlog() {
    try {
      Vector posts = handler.getRecentPosts("default", "username", "password", 3);
      assertTrue(posts.isEmpty());
    } catch (Exception e) {
      fail();
    }
  }

  public void testGetRecentPosts() {
    try {
      BlogService service = new BlogService();

      BlogEntry entry1 = new BlogEntry(blog);
      entry1.setTitle("title1");
      entry1.setBody("body1");
      service.putBlogEntry(entry1);

      BlogEntry entry2 = new BlogEntry(blog);
      entry2.setTitle("title2");
      entry2.setBody("body2");
      service.putBlogEntry(entry2);

      BlogEntry entry3 = new BlogEntry(blog);
      entry3.setTitle("title3");
      entry3.setBody("body3");
      service.putBlogEntry(entry3);

      BlogEntry entry4 = new BlogEntry(blog);
      entry4.setTitle("title4");
      entry4.setBody("body4");
      service.putBlogEntry(entry4);

      Vector posts = handler.getRecentPosts("default", "username", "password", 3);

      assertFalse(posts.isEmpty());
      assertEquals(3, posts.size());
      Hashtable ht = (Hashtable)posts.get(0);
      assertEquals("default/" + entry4.getId(), ht.get(MetaWeblogAPIHandler.POST_ID));
      assertEquals("body4", ht.get(MetaWeblogAPIHandler.DESCRIPTION));
      assertEquals("title4", ht.get(MetaWeblogAPIHandler.TITLE));
      ht = (Hashtable)posts.get(1);
      assertEquals("default/" + entry3.getId(), ht.get(MetaWeblogAPIHandler.POST_ID));
      assertEquals("body3", ht.get(MetaWeblogAPIHandler.DESCRIPTION));
      assertEquals("title3", ht.get(MetaWeblogAPIHandler.TITLE));
      ht = (Hashtable)posts.get(2);
      assertEquals("default/" + entry2.getId(), ht.get(MetaWeblogAPIHandler.POST_ID));
      assertEquals("body2", ht.get(MetaWeblogAPIHandler.DESCRIPTION));
      assertEquals("title2", ht.get(MetaWeblogAPIHandler.TITLE));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testGetPost() {
    try {
      Category category = new Category("/acategory", "A category");
      blog.addCategory(category);

      BlogService service = new BlogService();
      BlogEntry entry = new BlogEntry(blog);
      entry.setTitle("title");
      entry.setBody("body");
      entry.setAuthor("simon");
      entry.addCategory(category);
      service.putBlogEntry(entry);

      Hashtable post = handler.getPost("default/" + entry.getId(), "username", "password");
      assertEquals("title", post.get(MetaWeblogAPIHandler.TITLE));
      assertEquals("body", post.get(MetaWeblogAPIHandler.DESCRIPTION));
      Vector categories = (Vector)post.get(MetaWeblogAPIHandler.CATEGORIES);
      assertEquals(1, categories.size());
      assertEquals("/acategory", categories.get(0));
      assertEquals(entry.getAuthor(), post.get(MetaWeblogAPIHandler.USER_ID));
      assertEquals(entry.getDate(), post.get(MetaWeblogAPIHandler.DATE_CREATED));
      assertEquals("default/" + entry.getId(), post.get(MetaWeblogAPIHandler.POST_ID));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testGetPostWithIdThatDoesntExist() {
    String postid = "1234567890123";
    try {
      handler.getPost("default/" + postid, "username", "password");
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("Blog entry with ID of " + postid + " was not found.", xmlrpce.getMessage());
    }
  }

  public void testGetPostWithNullId() {
    String postid = null;
    try {
      handler.getPost(postid, "username", "password");
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("Blog with ID of " + null + " not found.", xmlrpce.getMessage());
    }
  }

  public void testNewPost() {
    try {
      Category category = new Category("/acategory", "A category");
      blog.addCategory(category);
      Hashtable struct = new Hashtable();
      struct.put(MetaWeblogAPIHandler.TITLE, "Title");
      struct.put(MetaWeblogAPIHandler.DESCRIPTION, "<p>Content</p>");
      Vector categories = new Vector();
      categories.add(category.getId());
      struct.put(MetaWeblogAPIHandler.CATEGORIES, categories);

      String postid = handler.newPost("default", "username", "password", struct, true);

      BlogService service = new BlogService();
      BlogEntry entry = service.getBlogEntry(blog, postid.substring("default".length()+1));

      assertEquals("default/" + entry.getId(), postid);
      assertEquals("Title", entry.getTitle());
      assertTrue(entry.inCategory(category));
      assertEquals("<p>Content</p>", entry.getBody());
      assertEquals("username", entry.getAuthor());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  /**
   * Tests that non-existent categories are just ignored and no error
   * is produced.
   */
  public void testNewPostWithCategoryThatDoesntExist() {
    try {
      Hashtable struct = new Hashtable();
      struct.put(MetaWeblogAPIHandler.TITLE, "Title");
      struct.put(MetaWeblogAPIHandler.DESCRIPTION, "<p>Content</p>");
      Vector categories = new Vector();
      categories.add("/someUnknownCategory");
      struct.put(MetaWeblogAPIHandler.CATEGORIES, categories);

      String postid = handler.newPost("default", "username", "password", struct, true);

      BlogService service = new BlogService();
      BlogEntry entry = service.getBlogEntry(blog, postid.substring("default".length()+1));

      assertEquals("default/" + entry.getId(), postid);
      assertEquals("Title", entry.getTitle());
      assertEquals(0, entry.getCategories().size());
      assertEquals("<p>Content</p>", entry.getBody());
      assertEquals("username", entry.getAuthor());
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testEditPost() {
    try {
      BlogService service = new BlogService();
      BlogEntry entry = new BlogEntry(blog);
      entry.setTitle("title");
      entry.setBody("body");
      service.putBlogEntry(entry);

      Hashtable struct = new Hashtable();
      struct.put(MetaWeblogAPIHandler.TITLE, "Title");
      struct.put(MetaWeblogAPIHandler.DESCRIPTION, "<p>Content</p>");
      boolean result = handler.editPost("default/" + entry.getId(), "username", "password", struct, true);

      assertTrue(result);
      entry = service.getBlogEntry(blog, entry.getId());
      assertEquals("Title", entry.getTitle());
      assertEquals("<p>Content</p>", entry.getBody());
      assertEquals("username", entry.getAuthor());

    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testEditPostWithNullId() {
    String postid = null;
    try {
      handler.editPost(postid, "username", "password", new Hashtable(), true);
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("Blog with ID of " + postid + " not found.", xmlrpce.getMessage());
    }
  }

  public void testEditPostWithIdThatDoesntExist() {
    String postid = "1234567890123";
    try {
      handler.editPost("default/" + postid, "username", "password", new Hashtable(), true);
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("Blog entry with ID of " + postid + " was not found.", xmlrpce.getMessage());
    }
  }

  public void testGetCategories() throws Exception {
    Hashtable categories = handler.getCategories("default", "username", "password");
    assertEquals(0, categories.size());

    blog.addCategory(new Category("/category1", "Category 1"));
    blog.addCategory(new Category("/category2", "Category 2"));
    categories = handler.getCategories("default", "username", "password");
    assertTrue(categories.size() == 2);
    Hashtable struct = (Hashtable)categories.get("/category1");
    assertEquals("/category1", struct.get(MetaWeblogAPIHandler.DESCRIPTION));
    assertEquals(blog.getUrl() + "categories/category1/", struct.get(MetaWeblogAPIHandler.HTML_URL));
    assertEquals(blog.getUrl() + "rss.xml?category=/category1", struct.get(MetaWeblogAPIHandler.RSS_URL));
    struct = (Hashtable)categories.get("/category2");
    assertEquals("/category2", struct.get(MetaWeblogAPIHandler.DESCRIPTION));
    assertEquals(blog.getUrl() + "categories/category2/", struct.get(MetaWeblogAPIHandler.HTML_URL));
    assertEquals(blog.getUrl() + "rss.xml?category=/category2", struct.get(MetaWeblogAPIHandler.RSS_URL));
  }

  public void testNewPostWithAPubDate() {
    Calendar cal = blog.getCalendar();
    cal.set(Calendar.DAY_OF_MONTH, 14);
    cal.set(Calendar.MONTH, 6);
    cal.set(Calendar.YEAR, 2004);
    try {
      Category category = new Category("/acategory", "A category");
      blog.addCategory(category);
      Hashtable struct = new Hashtable();
      struct.put(MetaWeblogAPIHandler.TITLE, "Title");
      struct.put(MetaWeblogAPIHandler.DESCRIPTION, "<p>Content</p>");
      struct.put(MetaWeblogAPIHandler.PUB_DATE, cal.getTime());
      Vector categories = new Vector();
      categories.add(category.getId());
      struct.put(MetaWeblogAPIHandler.CATEGORIES, categories);

      String postid = handler.newPost("default", "username", "password", struct, true);

      BlogService service = new BlogService();
      BlogEntry entry = service.getBlogEntry(blog, postid.substring("default".length()+1));

      DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
      format.setTimeZone(blog.getTimeZone());
      assertEquals("14/07/2004", format.format(entry.getDate()));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

}
