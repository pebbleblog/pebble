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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.apache.xmlrpc.XmlRpcException;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Tests for the BloggerAPIHandler class, when using a composite blog.
 *
 * @author    Simon Brown
 */
public class MultiBlogBloggerAPIHandlerTest extends MultiBlogTestCase {

  private BloggerAPIHandler handler = new BloggerAPIHandler();

  protected void setUp() throws Exception {
    super.setUp();

    handler.setAuthenticationManager(new net.sourceforge.pebble.mock.MockAuthenticationManager(true, new GrantedAuthority[] {new GrantedAuthorityImpl(Constants.BLOG_CONTRIBUTOR_ROLE)}));
    blog1.setProperty(Blog.BLOG_CONTRIBUTORS_KEY, "username");
    blog2.setProperty(Blog.BLOG_CONTRIBUTORS_KEY, "username2");
  }

  public void testGetRecentPostsFromEmptyBlog() {
    try {
      Vector posts = handler.getRecentPosts("appkey", "blog1", "username", "password", 3);
      assertTrue(posts.isEmpty());
    } catch (Exception e) {
      fail();
    }
  }

  public void testGetRecentPostsFromNonExistentBlog() {
    String blogid = "someBlog";
    try {
      handler.getRecentPosts("appkey", blogid, "username", "password", 3);
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("Blog with ID of " + blogid + " not found.", xmlrpce.getMessage());
    }
  }

  public void testGetRecentPosts() {
    try {
      BlogService service = new BlogService();

      BlogEntry entry1 = new BlogEntry(blog1);
      entry1.setTitle("title1");
      entry1.setBody("body1");
      service.putBlogEntry(entry1);

      BlogEntry entry2 = new BlogEntry(blog1);
      entry2.setTitle("title2");
      entry2.setBody("body2");
      service.putBlogEntry(entry2);      

      BlogEntry entry3 = new BlogEntry(blog1);
      entry3.setTitle("title3");
      entry3.setBody("body3");
      service.putBlogEntry(entry3);

      BlogEntry entry4 = new BlogEntry(blog1);
      entry4.setTitle("title4");
      entry4.setBody("body4");
      service.putBlogEntry(entry4);

      Vector posts = handler.getRecentPosts("appkey", "blog1", "username", "password", 3);

      assertEquals(3, posts.size());
      Hashtable ht = (Hashtable)posts.get(0);
      assertEquals("blog1/" + entry4.getId(), ht.get(BloggerAPIHandler.POST_ID));
      assertEquals("<title>title4</title><category></category>body4", ht.get(BloggerAPIHandler.CONTENT));
      ht = (Hashtable)posts.get(1);
      assertEquals("blog1/" + entry3.getId(), ht.get(BloggerAPIHandler.POST_ID));
      assertEquals("<title>title3</title><category></category>body3", ht.get(BloggerAPIHandler.CONTENT));
      ht = (Hashtable)posts.get(2);
      assertEquals("blog1/" + entry2.getId(), ht.get(BloggerAPIHandler.POST_ID));
      assertEquals("<title>title2</title><category></category>body2", ht.get(BloggerAPIHandler.CONTENT));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testGetPost() {
    try {
      BlogService service = new BlogService();
      BlogEntry entry = new BlogEntry(blog1);
      entry.setTitle("title");
      entry.setBody("body");
      entry.setAuthor("simon");
      service.putBlogEntry(entry);

      Hashtable post = handler.getPost("appkey", "blog1/" + entry.getId(), "username", "password");
      assertEquals("<title>title</title><category></category>body", post.get(BloggerAPIHandler.CONTENT));
      assertEquals(entry.getAuthor(), post.get(BloggerAPIHandler.USER_ID));
      assertEquals(entry.getDate(), post.get(BloggerAPIHandler.DATE_CREATED));
      assertEquals("blog1/" + entry.getId(), post.get(BloggerAPIHandler.POST_ID));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testGetPostWithCategory() {
    try {
      BlogService service = new BlogService();
      BlogEntry entry = new BlogEntry(blog1);
      entry.setTitle("title");
      entry.setBody("body");
      entry.setAuthor("simon");
      entry.addCategory(new Category("java", "Java"));
      service.putBlogEntry(entry);

      Hashtable post = handler.getPost("appkey", "blog1/" + entry.getId(), "username", "password");
      assertEquals("<title>title</title><category>/java</category>body", post.get(BloggerAPIHandler.CONTENT));
      assertEquals(entry.getAuthor(), post.get(BloggerAPIHandler.USER_ID));
      assertEquals(entry.getDate(), post.get(BloggerAPIHandler.DATE_CREATED));
      assertEquals("blog1/" + entry.getId(), post.get(BloggerAPIHandler.POST_ID));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testGetPostWithIdThatDoesntExist() {
    String postid = "1234567890123";
    try {
      handler.getPost("appkey", "blog1/" + postid, "username", "password");
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("Blog entry with ID of " + postid + " was not found.", xmlrpce.getMessage());
    }
  }

  public void testGetPostWithNullId() {
    String postid = null;
    try {
      handler.getPost("appkey", postid, "username", "password");
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("Blog with ID of " + null + " not found.", xmlrpce.getMessage());
    }
  }

  public void testDeletePost() {
    try {
      BlogService service = new BlogService();
      BlogEntry entry = new BlogEntry(blog1);
      entry.setTitle("title");
      entry.setBody("body");
      entry.setAuthor("simon");
      service.putBlogEntry(entry);

      boolean result = handler.deletePost("appkey", "blog1/" + entry.getId(), "username", "password", true);
      assertTrue("deletePost() returned false instead of true", result);
      assertNull(service.getBlogEntry(blog1, entry.getId()));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testDeletePostWithIdThatDoesntExist() {
    String postid = "1234567890123";
    try {
      handler.deletePost("appkey", "blog1/" + postid, "username", "password", true);
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("Blog entry with ID of " + postid + " was not found.", xmlrpce.getMessage());
    }
  }

  public void testDeletePostWithNullId() {
    String postid = null;
    try {
      handler.deletePost("appkey", postid, "username", "password", true);
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("Blog with ID of " + null + " not found.", xmlrpce.getMessage());
    }
  }

  public void testGetUserInfo() {
    try {
      Hashtable userInfo = handler.getUserInfo("appkey", "username", "password");
      assertEquals("username", userInfo.get("userid"));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testGetUsersBlogs() {
    try {
      Vector blogs = handler.getUsersBlogs("appkey", "username", "password");
      assertEquals(1, blogs.size());

      Hashtable blog = (Hashtable)blogs.get(0);
      assertEquals("http://www.yourdomain.com/blog/blog1/", blog.get("url"));
      assertEquals("blog1", blog.get("blogid"));
      assertEquals("My blog", blog.get("blogName"));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testAddCategory() {
    try {
      BlogService service = new BlogService();
      BlogEntry entry = new BlogEntry(blog1);
      service.putBlogEntry(entry);
      blog1.addCategory(new Category("/aCategory", "A Category"));

      boolean result = handler.addCategory("appkey", "blog1/" + entry.getId(), "username", "password", "/aCategory");
      entry = service.getBlogEntry(blog1, entry.getId());
      assertTrue("Category wasn't added", result);
      assertTrue(entry.inCategory(blog1.getCategory("aCategory")));

      result = handler.addCategory("appkey", "blog1/" + entry.getId(), "username", "password", "/aNonExistentCategory");
      entry = service.getBlogEntry(blog1, entry.getId());
      assertFalse("Category was added", result);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testAddCategoryWithIdThatDoesntExist() {
    String postid = "1234567890123";
    try {
      handler.addCategory("appkey", "blog1/" + postid, "username", "password", "aCategory");
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("Blog entry with ID of " + postid + " was not found.", xmlrpce.getMessage());
    }
  }

  public void testAddCategoryWithNullId() {
    String postid = null;
    try {
      handler.addCategory("appkey", postid, "username", "password", "aCategory");
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("Blog with ID of " + null + " not found.", xmlrpce.getMessage());
    }
  }

}
