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
package net.sourceforge.pebble;

/**
 * Contains constants for use in the presentation tier.
 *
 * @author    Simon Brown
 */
public class Constants {

  public static final String BLOG_KEY = "blog";
  public static final String MULTI_BLOG_KEY = "multiBlog";
  public static final String BLOG_URL = "blogUrl";
  public static final String MULTI_BLOG_URL = "multiBlogUrl";
  
  public static final String BLOG_TYPE = "blogType";
  public static final String BLOGS = "blogs";
  public static final String MONTHLY_BLOG = "monthlyBlog";
  public static final String DAILY_BLOG = "dailyBlog";
  public static final String BLOG_ENTRIES = "blogEntries";
  public static final String RESPONSES = "responses";

  public static final String RECENT_BLOG_ENTRIES = "recentBlogEntries";
  public static final String RECENT_RESPONSES = "recentResponses";
  public static final String CATEGORIES = "categories";
  public static final String TAGS = "tags";
  public static final String PLUGIN_PROPERTIES = "pluginProperties";
  public static final String ARCHIVES = "archives";

  public static final String BLOG_ENTRY_KEY = "blogEntry";
  public static final String STATIC_PAGE_KEY = "staticPage";
  public static final String COMMENT_KEY = "comment";
  public static final String BLOG_MANAGER = "blogManager";
  public static final String PEBBLE_CONTEXT = "pebbleContext";
  public static final String PEBBLE_PROPERTIES = "pebbleProperties";
  public static final String TITLE_KEY = "title";
  public static final String CATEGORY_KEY = "category";
  public static final String USER_KEY = "user";

  public static final String EXTERNAL_URI = "externalUri";
  public static final String INTERNAL_URI = "internalUri";
  public static final String ORIGINAL_URI = "originalUri";
  public static String FILTERS_APPLIED = "filtersApplied";
  public static String AUTHENTICATED_USER = "authenticatedUser";

  public static String THEME = "theme";

  public static String BLOG_ADMIN_ROLE = "ROLE_BLOG_ADMIN";
  public static String BLOG_OWNER_ROLE = "ROLE_BLOG_OWNER";
  public static String BLOG_PUBLISHER_ROLE = "ROLE_BLOG_PUBLISHER";
  public static String BLOG_CONTRIBUTOR_ROLE = "ROLE_BLOG_CONTRIBUTOR";
  public static String BLOG_READER_ROLE = "ROLE_BLOG_READER";
  public static String ANY_ROLE = "*";

}
