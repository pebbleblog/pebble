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
package net.sourceforge.pebble.web.filter;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.SingleBlogTestCase;
import net.sourceforge.pebble.domain.BlogManager;
import net.sourceforge.pebble.mock.MockFilterChain;
import net.sourceforge.pebble.mock.MockFilterConfig;
import net.sourceforge.pebble.mock.MockHttpServletRequest;
import net.sourceforge.pebble.mock.MockHttpServletResponse;

/**
 * Tests for the TransformingFilter class.
 *
 * @author    Simon Brown
 */
public class SingleBlogTransformingFilterTest extends SingleBlogTestCase {

  private TransformingFilter filter;
  private MockFilterConfig config;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;

  protected void setUp() throws Exception {
    super.setUp();

    filter = new TransformingFilter();
    config = new MockFilterConfig();
    filter.init(config);
    request = new MockHttpServletRequest();
    request.setContextPath("/somecontext");
    request.setRequestUri("/somecontext/");
    request.setAttribute(Constants.BLOG_KEY, blog);
    response = new MockHttpServletResponse();
  }

  public void tearDown() throws Exception {
    super.tearDown();

    filter.destroy();
  }

  public void testUrlTransformed() throws Exception {
    request.setAttribute(Constants.EXTERNAL_URI, "/");
    filter.doFilter(request, response, new MockFilterChain());
    assertEquals("/viewHomePage.action", request.getAttribute(Constants.INTERNAL_URI));
  }

  /**
   * Tests that the important part of the URI is inserted into request scope.
   *
   * @throws Exception
   */
  public void testUriInsertedIntoRequestScope() throws Exception {
    request.setAttribute(Constants.BLOG_KEY, blog);
    request.setRequestUri("/somecontext");
    filter.doFilter(request, response, new MockFilterChain());
    assertEquals("/", request.getAttribute(Constants.EXTERNAL_URI));

    request.setRequestUri("/somecontext/rss.xml");
    filter.doFilter(request, response, new MockFilterChain());
    assertEquals("/rss.xml", request.getAttribute(Constants.EXTERNAL_URI));

    request.setRequestUri("/somecontext/images/myImage.jpg");
    filter.doFilter(request, response, new MockFilterChain());
    assertEquals("/images/myImage.jpg", request.getAttribute(Constants.EXTERNAL_URI));
  }

//  /**
//   * Tests that the important part of the URI is inserted into request scope.
//   *
//   * @throws Exception
//   */
//  public void testUriInsertedIntoRequestScope() throws Exception {
//    request.setAttribute(Constants.BLOG_KEY, BlogManager.getInstance().getMultiBlog());
//    request.setRequestUri("/somecontext");
//    filter.doFilter(request, response, new MockFilterChain());
//    assertEquals("/", request.getAttribute(Constants.EXTERNAL_URI));
//
//    request.setRequestUri("/somecontext/rss.xml");
//    filter.doFilter(request, response, new MockFilterChain());
//    assertEquals("/rss.xml", request.getAttribute(Constants.EXTERNAL_URI));
//
//    request.setRequestUri("/somecontext/blog1/rss.xml");
//    filter.doFilter(request, response, new MockFilterChain());
//    assertEquals("/rss.xml", request.getAttribute(Constants.EXTERNAL_URI));
//
//    request.setRequestUri("/somecontext/blog1/images/myImage.jpg");
//    filter.doFilter(request, response, new MockFilterChain());
//    assertEquals("/images/myImage.jpg", request.getAttribute(Constants.EXTERNAL_URI));
//  }

}
