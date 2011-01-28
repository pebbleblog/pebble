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

import net.sourceforge.pebble.domain.BlogManager;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.util.SecurityUtils;
import org.apache.xmlrpc.XmlRpcException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * A handler for the XML-RPC blogging APIs.
 *
 * @author    Simon Brown
 */
public abstract class AbstractAPIHandler {

  /** character used to separate blog and post IDs in multi-user mode */
  static final char BLOG_ID_SEPARATOR = '/';

  private AuthenticationManager authenticationManager;

  public AuthenticationManager getAuthenticationManager() {
    return authenticationManager;
  }

  public void setAuthenticationManager(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  /**
   * A helper method to authenticate a username/password pair against the
   * properties for the specified Blog instance.
   *
   * @param blog      the Blog instance to test against
   * @param username  the username used for logging in via XML-RPC
   * @param password  the password used for logging in via XML-RPC
   */
  protected void authenticate(Blog blog, String username, String password) throws XmlRpcAuthenticationException {
    try {
      Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      SecurityContextHolder.getContext().setAuthentication(auth);

      if (blog != null && !SecurityUtils.isUserAuthorisedForBlogAsBlogContributor(blog)) {
        throw new XmlRpcAuthenticationException("Not authorised for this blog.");
      }
    } catch (AuthenticationException ae) {
      throw new XmlRpcAuthenticationException("Username and password did not pass authentication.");
    }
  }

  /**
   * Gets the blog from a given String.
   * <br /><br />
   * In single-user mode, blog IDs are irrelevant since there is only one blog.
   * In multi-user mode, the post ID is composed of "blog ID/post ID"
   * (this is Pebble's way of uniquely identifying a blog entry across all
   * users' blogs).
   *
   * @param s   the String containing the post ID
   * @return  the post ID (blog entry ID)
   */
  protected Blog getBlogWithPostId(String s) throws XmlRpcException {
    if (s == null) {
      throw new XmlRpcException(0, "Blog with ID of " + null + " not found.");
    }

    String blogId = null;
    Blog blog;

    int index = s.lastIndexOf(BLOG_ID_SEPARATOR);
    if (index > -1) {
      blogId = s.substring(0, index);
    }

    blog = BlogManager.getInstance().getBlog(blogId);
    if (blog == null) {
      throw new XmlRpcException(0, "Blog with ID of " + blogId + " not found.");
    } else {
      return blog;
    }
  }

  /**
   * Gets the blog from a given String.
   * <br /><br />
   * In single-user mode, blog IDs are irrelevant since there is only one blog.
   * In multi-user mode, the post ID is composed of "blog ID/post ID"
   * (this is Pebble's way of uniquely identifying a blog entry across all
   * users' blogs).
   *
   * @param blogId   the String containing the post ID
   * @return  the blog ID
   */
  protected Blog getBlogWithBlogId(String blogId) throws XmlRpcException {
    Blog blog = BlogManager.getInstance().getBlog(blogId);
    if (blog == null) {
      throw new XmlRpcException(0, "Blog with ID of " + blogId + " not found.");
    } else {
      return blog;
    }
  }

  /**
   * Gets the post ID (blog entry ID) from a given String.
   * <br /><br />
   * In single-user mode, post IDs
   * are specified as just the blog ID. In multi-user mode, the post ID
   * is composed of "blog ID/post ID" (this is Pebble's way of uniquely
   * identifying a blog entry across all users' blogs).
   *
   * @param s   the String containing the post ID
   * @return  the post ID (blog entry ID)
   */
  protected String getPostId(String s) {
    if (s == null) {
      return null;
    }

    int index = s.lastIndexOf(BLOG_ID_SEPARATOR);
    if (index > -1) {
      return s.substring(index+1);
    } else {
      return null;
    }
  }

  /**
   * Formats a post ID for the blogger client.
   *
   * @param blogid    the blog ID
   * @param postid    the post ID
   * @return  if running in multi-user mode, returns "blogid/postid",
   *          otherwise just returns "postid"
   */
  protected String formatPostId(String blogid, String postid) {
    return blogid + BLOG_ID_SEPARATOR + postid;
  }

}