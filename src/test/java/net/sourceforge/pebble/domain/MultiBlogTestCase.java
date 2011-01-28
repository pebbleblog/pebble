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
package net.sourceforge.pebble.domain;

import net.sourceforge.pebble.dao.DAOFactory;
import net.sourceforge.pebble.dao.mock.MockDAOFactory;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.security.MockSecurityRealm;

import java.io.File;

/**
 * Superclass for test cases that make uses of multiple blogs.
 *
 * @author    Simon Brown
 */
public abstract class MultiBlogTestCase extends PebbleTestCase {

  protected Blog blog1, blog2;

  protected void setUp() throws Exception {
    super.setUp();

    DAOFactory.setConfiguredFactory(new MockDAOFactory());

    PebbleContext.getInstance().getConfiguration().setUrl("http://www.yourdomain.com/blog/");
    BlogManager.getInstance().setMultiBlog(true);

    PebbleContext.getInstance().getConfiguration().setSecurityRealm(new MockSecurityRealm());

    // and set up some blogs
    File blogDirectory1 = new File(TEST_BLOG_LOCATION, "blogs/blog1");
    blogDirectory1.mkdir();
    blog1 = new Blog(blogDirectory1.getAbsolutePath());
    blog1.setId("blog1");
    Theme theme = new Theme(blog1, "user-blog1", TEST_BLOG_LOCATION.getAbsolutePath());
    blog1.setEditableTheme(theme);
    BlogManager.getInstance().addBlog(blog1);
    blog1.start();

    File blogDirectory2 = new File(TEST_BLOG_LOCATION, "blogs/blog2");
    blogDirectory2.mkdir();
    blog2 = new Blog(blogDirectory2.getAbsolutePath());
    blog2.setId("blog2");
    theme = new Theme(blog2, "user-blog2", TEST_BLOG_LOCATION.getAbsolutePath());
    blog2.setEditableTheme(theme);
    BlogManager.getInstance().addBlog(blog2);
    blog2.start();
  }

  protected void tearDown() throws Exception {
    blog1.stop();
    blog2.stop();
    BlogManager.getInstance().removeAllBlogs();

    super.tearDown();
  }

}