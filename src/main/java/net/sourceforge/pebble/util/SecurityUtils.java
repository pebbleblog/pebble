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
package net.sourceforge.pebble.util;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.security.PebbleUserDetails;
import net.sourceforge.pebble.security.SecurityRealm;
import net.sourceforge.pebble.security.SecurityRealmException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

/**
 * A collection of utility methods for security.
 *
 * @author    Simon Brown
 */
public final class SecurityUtils {

  private static final Log log = LogFactory.getLog(SecurityUtils.class);

  public static String getUsername() {
    SecurityContext ctx = SecurityContextHolder.getContext();
    Authentication auth = ctx.getAuthentication();
    return getUsername(auth);
  }

  public static String getUsername(Authentication auth) {
    if (auth != null) {
      return auth.getName();
    } else {
      return null;
    }
  }

  public static PebbleUserDetails getUserDetails() {
    try {
      SecurityRealm realm = PebbleContext.getInstance().getConfiguration().getSecurityRealm();
      return realm.getUser(getUsername());
    } catch (SecurityRealmException e) {
      log.error("Exception encountered", e);
      return null;
    }
  }

  public static boolean isUserInRole(String role) {
    SecurityContext ctx = SecurityContextHolder.getContext();
    Authentication auth = ctx.getAuthentication();
    return isUserInRole(auth, role);
  }

  public static boolean isUserInRole(Authentication auth, String role) {
    if (auth != null) {
      Collection<GrantedAuthority> authorities = auth.getAuthorities();
      if (authorities != null) {
        for (GrantedAuthority authority : authorities) {
          if (authority.getAuthority().equals(role)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Determines whether this user is a Pebble admin user.
   *
   * @return  true if the user is a Pebble admin, false otherwise
   */
  public static boolean isBlogAdmin() {
    return isUserInRole(Constants.BLOG_ADMIN_ROLE);
  }

  /**
   * Determines whether this user is a blog owner.
   *
   * @return  true if the user is a blog owner, false otherwise
   */
  public static boolean isBlogOwner() {
    return isUserInRole(Constants.BLOG_OWNER_ROLE);
  }

  /**
   * Determines whether this user is a blog publisher.
   *
   * @return  true if the user is a blog publisher, false otherwise
   */
  public static boolean isBlogPublisher() {
    return isUserInRole(Constants.BLOG_PUBLISHER_ROLE);
  }

  /**
   * Determines whether this user is a blog contributor.
   *
   * @return  true if the user is a blog contributor, false otherwise
   */
  public static boolean isBlogContributor() {
    return isUserInRole(Constants.BLOG_CONTRIBUTOR_ROLE);
  }

  /**
   * Determines whether this user is a Pebble admin user.
   *
   * @return  true if the user is a Pebble admin, false otherwise
   */
  public static boolean isBlogAdmin(Authentication auth) {
    return isUserInRole(auth, Constants.BLOG_ADMIN_ROLE);
  }

  /**
   * Determines whether this user is a blog owner.
   *
   * @return  true if the user is a blog owner, false otherwise
   */
  public static boolean isBlogOwner(Authentication auth) {
    return isUserInRole(auth, Constants.BLOG_OWNER_ROLE);
  }

  /**
   * Determines whether this user is a blog publisher.
   *
   * @return  true if the user is a blog publisher, false otherwise
   */
  public static boolean isBlogPublisher(Authentication auth) {
    return isUserInRole(auth, Constants.BLOG_PUBLISHER_ROLE);
  }

  /**
   * Determines whether this user is a blog contributor.
   *
   * @return  true if the user is a blog contributor, false otherwise
   */
  public static boolean isBlogContributor(Authentication auth) {
    return isUserInRole(auth, Constants.BLOG_CONTRIBUTOR_ROLE);
  }

  public static void runAsBlogOwner() {
    Authentication auth = new TestingAuthenticationToken("username", "password", new GrantedAuthority[] {new GrantedAuthorityImpl(Constants.BLOG_OWNER_ROLE)});
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public static void runAsBlogPublisher() {
    Authentication auth = new TestingAuthenticationToken("username", "password", new GrantedAuthority[] {new GrantedAuthorityImpl(Constants.BLOG_PUBLISHER_ROLE)});
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public static void runAsBlogContributor() {
    Authentication auth = new TestingAuthenticationToken("username", "password", new GrantedAuthority[] {new GrantedAuthorityImpl(Constants.BLOG_CONTRIBUTOR_ROLE)});
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public static void runAsAnonymous() {
    Authentication auth = new TestingAuthenticationToken("username", "password", new GrantedAuthority[] {});
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public static void runAsUnauthenticated() {
    SecurityContextHolder.getContext().setAuthentication(null);
  }

  public static boolean isUserAuthorisedForBlogAsBlogOwner(Blog blog) {
    String currentUser = SecurityUtils.getUsername();
    return isBlogOwner() && blog.isUserInRole(Constants.BLOG_OWNER_ROLE, currentUser);
  }

  public static boolean isUserAuthorisedForBlogAsBlogPublisher(Blog blog) {
    String currentUser = SecurityUtils.getUsername();
    return isBlogPublisher() && blog.isUserInRole(Constants.BLOG_PUBLISHER_ROLE, currentUser);
  }

  public static boolean isUserAuthorisedForBlogAsBlogContributor(Blog blog) {
    String currentUser = SecurityUtils.getUsername();
    return isBlogContributor() && blog.isUserInRole(Constants.BLOG_CONTRIBUTOR_ROLE, currentUser);
  }

  public static boolean isUserAuthorisedForBlogAsBlogOwner(Authentication auth, Blog blog) {
    String currentUser = SecurityUtils.getUsername(auth);
    return isBlogOwner(auth) && blog.isUserInRole(Constants.BLOG_OWNER_ROLE, currentUser);
  }

  public static boolean isUserAuthorisedForBlogAsBlogPublisher(Authentication auth, Blog blog) {
    String currentUser = SecurityUtils.getUsername(auth);
    return isBlogPublisher(auth) && blog.isUserInRole(Constants.BLOG_PUBLISHER_ROLE, currentUser);
  }

  public static boolean isUserAuthorisedForBlogAsBlogContributor(Authentication auth, Blog blog) {
    String currentUser = SecurityUtils.getUsername(auth);
    return isBlogContributor(auth) && blog.isUserInRole(Constants.BLOG_CONTRIBUTOR_ROLE, currentUser);
  }

  public static boolean isUserAuthorisedForBlogAsBlogReader(Authentication auth, Blog blog) {
    String currentUser = SecurityUtils.getUsername(auth);
    return blog.isUserInRole(Constants.BLOG_READER_ROLE, currentUser);
  }

  public static boolean isUserAuthorisedForBlog(Blog blog) {
    return isUserAuthorisedForBlogAsBlogOwner(blog) ||
        isUserAuthorisedForBlogAsBlogPublisher(blog) ||
        isUserAuthorisedForBlogAsBlogContributor(blog);
  }

  public static boolean isUserAuthorisedForBlog(Authentication auth, Blog blog) {
    return isUserAuthorisedForBlogAsBlogOwner(auth, blog) ||
        isUserAuthorisedForBlogAsBlogPublisher(auth, blog) ||
        isUserAuthorisedForBlogAsBlogContributor(auth, blog);
  }

  public static boolean isUserAuthenticated() {
    SecurityContext ctx = SecurityContextHolder.getContext();
    return ctx.getAuthentication() != null;
  }

  public static void main(String[] args) {
    if (args.length != 3) {
      System.out.println("Usage : [md5|sha|plaintext] username password");
    } else if (args[0].equals("md5")) {
      PasswordEncoder encoder = new Md5PasswordEncoder();
      System.out.println(encoder.encodePassword(args[2], args[1]));
    } else if (args[0].equals("sha")) {
      PasswordEncoder encoder = new ShaPasswordEncoder();
      System.out.println(encoder.encodePassword(args[2], args[1]));
    } else if (args[0].equals("plaintext")) {
      PasswordEncoder encoder = new PlaintextPasswordEncoder();
      System.out.println(encoder.encodePassword(args[2], args[1]));
    } else {
      System.out.println("Algorithm must be md5, sha or plaintext");
    }
  }

}