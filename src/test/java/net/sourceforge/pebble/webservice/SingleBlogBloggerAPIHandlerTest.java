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
import org.apache.xmlrpc.XmlRpcException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Tests for the BloggerAPIHandler class, when using a simple blog.
 *
 * @author    Simon Brown
 */
public class SingleBlogBloggerAPIHandlerTest extends SingleBlogTestCase {

  private BloggerAPIHandler handler = new BloggerAPIHandler();

  protected void setUp() throws Exception {
    super.setUp();

    handler.setAuthenticationManager(new net.sourceforge.pebble.mock.MockAuthenticationManager(true, new GrantedAuthority[] {new GrantedAuthorityImpl(Constants.BLOG_CONTRIBUTOR_ROLE)}));
    blog.setProperty(Blog.BLOG_CONTRIBUTORS_KEY, "username");
  }

  public void testAuthenticationFailure() {
    handler.setAuthenticationManager(new MockAuthenticationManager(false));
    try {
      handler.getUserInfo("", "username", "password");
      fail();
    } catch (XmlRpcAuthenticationException xmlrpcae) {
    } catch (XmlRpcException xmlrpce) {
      fail();
    }
    try {
      handler.deletePost("", "default/123", "username", "password", true);
      fail();
    } catch (XmlRpcAuthenticationException xmlrpcae) {
    } catch (XmlRpcException xmlrpce) {
      fail();
    }
    try {
      handler.editPost("", "default/123", "username", "password", "content", true);
      fail();
    } catch (XmlRpcAuthenticationException xmlrpcae) {
    } catch (XmlRpcException xmlrpce) {
      fail();
    }
    try {
      handler.getPost("", "default/123", "username", "password");
      fail();
    } catch (XmlRpcAuthenticationException xmlrpcae) {
    } catch (XmlRpcException xmlrpce) {
      fail();
    }
    try {
      handler.getRecentPosts("", "default", "username", "password", 10);
      fail();
    } catch (XmlRpcAuthenticationException xmlrpcae) {
    } catch (XmlRpcException xmlrpce) {
      fail();
    }
    try {
      handler.newPost("", "default", "username", "password", "content", true);
      fail();
    } catch (XmlRpcAuthenticationException xmlrpcae) {
    } catch (XmlRpcException xmlrpce) {
      fail();
    }
  }

  public void testAuthenticationSuccess() {
    try {
      handler.deletePost("", "123", "username", "password", true);
    } catch (XmlRpcAuthenticationException xmlrpcae) {
      fail();
    } catch (XmlRpcException xmlrpce) {
    }
    try {
      handler.editPost("", "123", "username", "password", "content", true);
    } catch (XmlRpcAuthenticationException xmlrpcae) {
      fail();
    } catch (XmlRpcException xmlrpce) {
    }
    try {
      handler.getPost("", "123", "username", "password");
    } catch (XmlRpcAuthenticationException xmlrpcae) {
      fail();
    } catch (XmlRpcException xmlrpce) {
    }
    try {
      handler.getRecentPosts("", "", "username", "password", 10);
    } catch (XmlRpcAuthenticationException xmlrpcae) {
      fail();
    } catch (XmlRpcException xmlrpce) {
    }
    try {
      handler.getUsersBlogs("", "username", "password");
    } catch (XmlRpcAuthenticationException xmlrpcae) {
      fail();
    } catch (XmlRpcException xmlrpce) {
    }
    try {
      handler.newPost("", "", "username", "password", "<title>title</title>content", true);
    } catch (XmlRpcAuthenticationException xmlrpcae) {
      fail();
    } catch (XmlRpcException xmlrpce) {
    }
  }

  public void testGetRecentPostsFromEmptyBlog() {
    try {
      Vector posts = handler.getRecentPosts("appkey", "default", "username", "password", 3);
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

      Vector posts = handler.getRecentPosts("appkey", "default", "username", "password", 3);

      assertFalse(posts.isEmpty());
      assertEquals(3, posts.size());
      Hashtable ht = (Hashtable)posts.get(0);
      assertEquals("default/" + entry4.getId(), ht.get(BloggerAPIHandler.POST_ID));
      assertEquals("<title>title4</title><category></category>body4", ht.get(BloggerAPIHandler.CONTENT));
      ht = (Hashtable)posts.get(1);
      assertEquals("default/" + entry3.getId(), ht.get(BloggerAPIHandler.POST_ID));
      assertEquals("<title>title3</title><category></category>body3", ht.get(BloggerAPIHandler.CONTENT));
      ht = (Hashtable)posts.get(2);
      assertEquals("default/" + entry2.getId(), ht.get(BloggerAPIHandler.POST_ID));
      assertEquals("<title>title2</title><category></category>body2", ht.get(BloggerAPIHandler.CONTENT));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testGetPost() {
    try {
      BlogService service = new BlogService();
      BlogEntry entry = new BlogEntry(blog);
      entry.setTitle("title");
      entry.setBody("body");
      entry.setAuthor("simon");
      service.putBlogEntry(entry);

      Hashtable post = handler.getPost("appkey", "default/" + entry.getId(), "username", "password");
      assertEquals("<title>title</title><category></category>body", post.get(BloggerAPIHandler.CONTENT));
      assertEquals(entry.getAuthor(), post.get(BloggerAPIHandler.USER_ID));
      assertEquals(entry.getDate(), post.get(BloggerAPIHandler.DATE_CREATED));
      assertEquals("default/" + entry.getId(), post.get(BloggerAPIHandler.POST_ID));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testGetPostWithCategory() {
    try {
      BlogService service = new BlogService();
      BlogEntry entry = new BlogEntry(blog);
      entry.setTitle("title");
      entry.setBody("body");
      entry.setAuthor("simon");
      entry.addCategory(new Category("java", "Java"));
      service.putBlogEntry(entry);

      Hashtable post = handler.getPost("appkey", "default/" + entry.getId(), "username", "password");
      assertEquals("<title>title</title><category>/java</category>body", post.get(BloggerAPIHandler.CONTENT));
      assertEquals(entry.getAuthor(), post.get(BloggerAPIHandler.USER_ID));
      assertEquals(entry.getDate(), post.get(BloggerAPIHandler.DATE_CREATED));
      assertEquals("default/" + entry.getId(), post.get(BloggerAPIHandler.POST_ID));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testGetPostWithIdThatDoesntExist() {
    String postid = "1234567890123";
    try {
      handler.getPost("appkey", "default/" + postid, "username", "password");
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

  public void testNewPostWithTitleAndCategory() {
    try {
      Category category = new Category("/aCategory", "A Category");
      blog.addCategory(category);

      String postid = handler.newPost("appkey", "default", "username", "password", "<title>Title</title><category>/aCategory</category><p>Content</p>", true);

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

  public void testNewPostWithTitleAndCategories() {
    try {
      Category category1 = new Category("/category1", "Category 1");
      blog.addCategory(category1);
      Category category2 = new Category("/category2", "Category 2");
      blog.addCategory(category2);

      String postid = handler.newPost("appkey", "default", "username", "password", "<title>Title</title><category>/category1, /category2</category><p>Content</p>", true);

      BlogService service = new BlogService();
      BlogEntry entry = service.getBlogEntry(blog, postid.substring("default".length()+1));

      assertEquals("default/" + entry.getId(), postid);
      assertEquals("Title", entry.getTitle());
      assertTrue(entry.inCategory(category1));
      assertTrue(entry.inCategory(category2));
      assertEquals("<p>Content</p>", entry.getBody());
      assertEquals("username", entry.getAuthor());

    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testNewPostWithoutTitle() {
    try {
      String postid = handler.newPost("appkey", "default", "username", "password", "<p>Content</p>", true);

      BlogService service = new BlogService();
      BlogEntry entry = service.getBlogEntry(blog, postid.substring("default".length()+1));
      assertEquals("default/" + entry.getId(), postid);
      assertEquals("", entry.getTitle());
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

      boolean result = handler.editPost("appkey", "default/" + entry.getId(), "username", "password", "<title>Title</title><p>Content</p>", true);

      entry = service.getBlogEntry(blog, entry.getId());
      assertTrue(result);
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
      handler.editPost("appkey", postid, "username", "password", "<title>Title</title><p>Content</p>", true);
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("Blog with ID of " + postid + " not found.", xmlrpce.getMessage());
    }
  }

  public void testEditPostWithIdThatDoesntExist() {
    String postid = "1234567890123";
    try {
      handler.editPost("appkey", "default/" + postid, "username", "password", "<title>Title</title><p>Content</p>", true);
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("Blog entry with ID of " + postid + " was not found.", xmlrpce.getMessage());
    }
  }

  public void testDeletePost() {
    try {
      BlogService service = new BlogService();
      BlogEntry entry = new BlogEntry(blog);
      entry.setTitle("title");
      entry.setBody("body");
      entry.setAuthor("simon");
      service.putBlogEntry(entry);

      boolean result = handler.deletePost("appkey", "default/" + entry.getId(), "username", "password", true);
      assertTrue("deletePost() returned false instead of true", result);
      assertNull(service.getBlogEntry(blog, entry.getId()));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testDeletePostWithIdThatDoesntExist() {
    String postid = "1234567890123";
    try {
      handler.deletePost("appkey", "default/" + postid, "username", "password", true);
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
      assertEquals("http://www.yourdomain.com/blog/", blog.get("url"));
      assertEquals("default", blog.get("blogid"));
      assertEquals("My blog", blog.get("blogName"));
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testGetTemplate() {
    try {
      handler.getTemplate("appkey", "blogid", "username", "password", "templateType");
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("getTemplate is not supported by Pebble.", xmlrpce.getMessage());
    }
  }

  public void testSetTemplate() {
    try {
      handler.setTemplate("appkey", "blogid", "username", "password", "template", "templateType");
      fail();
    } catch (XmlRpcException xmlrpce) {
      assertEquals("setTemplate is not supported by Pebble.", xmlrpce.getMessage());
    }
  }

  public void testAddCategory() {
    try {
      BlogService service = new BlogService();
      BlogEntry entry = new BlogEntry(blog);
      service.putBlogEntry(entry);
      blog.addCategory(new Category("/aCategory", "A Category"));

      boolean result = handler.addCategory("appkey", "default/" + entry.getId(), "username", "password", "/aCategory");

      entry = service.getBlogEntry(blog, entry.getId());
      assertTrue("Category wasn't added", result);
      assertTrue(entry.inCategory(blog.getCategory("aCategory")));

      result = handler.addCategory("appkey", "default/" + entry.getId(), "username", "password", "/aNonExistentCategory");
      assertFalse("Category was added", result);

    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  public void testAddCategoryWithIdThatDoesntExist() {
    String postid = "1234567890123";
    try {
      handler.addCategory("appkey", "default/" + postid, "username", "password", "aCategory");
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
