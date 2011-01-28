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
import net.sourceforge.pebble.Configuration;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.security.MockSecurityRealm;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.StaticApplicationContext;

import java.io.File;

/**
 * Superclass for test cases that make uses of simple blogs.
 *
 * @author    Simon Brown
 */
public abstract class SingleBlogTestCase extends PebbleTestCase {

  protected Blog blog;

  protected void setUp() throws Exception {
    super.setUp();

    DAOFactory.setConfiguredFactory(new MockDAOFactory());
    BlogManager.getInstance().setMultiBlog(false);

    File blogDirectory = new File(TEST_BLOG_LOCATION, "blogs/default");
    blogDirectory.mkdir();
    blog = new Blog(blogDirectory.getAbsolutePath());
    Theme theme = new Theme(blog, "user-default", TEST_BLOG_LOCATION.getAbsolutePath());
    blog.setEditableTheme(theme);
    // There is some code that does i18n and runs assertions assuming that the locale is English
    blog.setProperty(Blog.LANGUAGE_KEY, "en");

    Configuration config = new Configuration();
    config.setDataDirectory(TEST_BLOG_LOCATION.getAbsolutePath());
    config.setUrl("http://www.yourdomain.com/blog/");
    PebbleContext.getInstance().setConfiguration(config);

    PebbleContext.getInstance().getConfiguration().setSecurityRealm(new MockSecurityRealm());

    BlogManager.getInstance().addBlog(blog);
    blog.start();
  }

  protected void tearDown() throws Exception {
    blog.stop();
    BlogManager.getInstance().removeAllBlogs();

    super.tearDown();
  }

}