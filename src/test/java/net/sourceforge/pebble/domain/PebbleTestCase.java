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

import junit.framework.TestCase;
import net.sourceforge.pebble.Configuration;
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.util.FileUtils;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.StaticApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.StaticWebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.io.File;

/**
 * Superclass for all Pebble unit test cases.
 *
 * @author    Simon Brown
 */
public abstract class PebbleTestCase extends TestCase {

  protected static final File TEST_BLOG_LOCATION;
  protected static final File TEST_RESOURCE_LOCATION;

  static {
    TEST_BLOG_LOCATION = new File(System.getProperty("java.io.tmpdir"), "pebble");
    TEST_RESOURCE_LOCATION = new File("src/test/resources");
  }

  protected StaticApplicationContext testApplicationContext;
  protected ServletContext servletContext;

  protected void setUp() throws Exception {
    super.setUp();

    // Make sure we aren't logged in
    SecurityContextHolder.getContext().setAuthentication(null);

    TEST_BLOG_LOCATION.mkdir();
    new File(TEST_BLOG_LOCATION, "blogs").mkdir();

    testApplicationContext = new StaticWebApplicationContext();
    // Make sure it uses annotation injection
    AnnotationConfigUtils.registerAnnotationConfigProcessors(testApplicationContext);
    testApplicationContext.refresh();

    servletContext = new MockServletContext();
    servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, testApplicationContext);

    Configuration config = new Configuration();
    config.setUrl("http://www.yourdomain.com/blog/");
    config.setDataDirectory(TEST_BLOG_LOCATION.getAbsolutePath());
    PebbleContext.getInstance().setConfiguration(config);
    PebbleContext.getInstance().setApplicationContext(testApplicationContext);
  }

  protected void tearDown() throws Exception {
    FileUtils.deleteFile(TEST_BLOG_LOCATION);

    super.tearDown();
  }

  protected void addComponents(Object... components) {
    for (Object component : components) {
      testApplicationContext.getBeanFactory().registerSingleton(component.getClass().getSimpleName(), component);
    }
  }

  protected void addComponent(String name, Object component) {
      testApplicationContext.getBeanFactory().registerSingleton(name, component);
  }

  protected <T> T createBean(Class<T> clazz) {
    return (T) testApplicationContext.getAutowireCapableBeanFactory().createBean(clazz, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
  }

  protected <T> T autowire(Class<T> clazz) {
    return (T) testApplicationContext.getAutowireCapableBeanFactory().autowire(clazz, AutowireCapableBeanFactory.AUTOWIRE_NO, false);
  }

}