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
package net.sourceforge.pebble.security;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.domain.Blog;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.web.FilterInvocation;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Bespoke FilterInvocationDefinitionSource that holds a mapping between blog
 * IDs and the roles that can access them. This is used when blog owners mark
 * their blog as "private", which forces authentication before the content
 * can be accessed. This implementation allows mappings to be removed
 * and added at runtime, making it possible to make blogs private
 * without restarting the web/application server.
 *
 * @author Simon Brown
 */
public class PrivateBlogSecurityMetadataSource implements SecurityMetadataSource {

  private static final Log log = LogFactory.getLog(PrivateBlogSecurityMetadataSource.class);


  /**
   * Accesses the <code>ConfigAttributeDefinition</code> that applies to a given secure object.<P>Returns
   * <code>null</code> if no <code>ConfigAttribiteDefinition</code> applies.</p>
   *
   * @param object the object being secured
   * @return the <code>ConfigAttributeDefinition</code> that applies to the passed object
   * @throws IllegalArgumentException if the passed object is not of a type supported by the
   *                                  <code>ObjectDefinitionSource</code> implementation
   */
  public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
    if ((object == null) || !this.supports(object.getClass())) {
        throw new IllegalArgumentException("Object must be a FilterInvocation");
    }

    HttpServletRequest request = ((FilterInvocation)object).getHttpRequest();
    String uri = (String)request.getAttribute(Constants.INTERNAL_URI);
    if (
        uri.endsWith("loginPage.action") ||
        uri.endsWith(".secureaction") ||
        uri.startsWith("/themes/") ||
        uri.startsWith("/scripts/") ||
        uri.startsWith("/common/") ||
        uri.startsWith("/dwr/") ||
        uri.equals("/robots.txt") ||
        uri.equals("/pebble.css") ||
        uri.equals("/favicon.ico") ||
        uri.startsWith("/FCKeditor/")
        ) {
      return null;
    }
    
    AbstractBlog ab = (AbstractBlog)((FilterInvocation)object).getHttpRequest().getAttribute(Constants.BLOG_KEY);
    if (ab instanceof Blog) {
      Blog blog = (Blog)ab;
      List<String> blogReaders = blog.getBlogReaders();
      if (blogReaders != null && blogReaders.size() > 0) {
        return Arrays.<ConfigAttribute>asList(new PrivateBlogConfigAttributeDefinition(blog));
      }
    }

    return null;
  }

  /**
   * If available, all of the <code>ConfigAttributeDefinition</code>s defined by the implementing class.<P>This
   * is used by the {@link org.springframework.security.access.intercept.AbstractSecurityInterceptor} to perform startup time validation of each
   * <code>ConfigAttribute</code> configured against it.</p>
   *
   * @return an iterator over all the <code>ConfigAttributeDefinition</code>s or <code>null</code> if unsupported
   */
  public Collection<ConfigAttribute> getAllConfigAttributes() {
    return null;
  }

  /**
   * Indicates whether the <code>ObjectDefinitionSource</code> implementation is able to provide
   * <code>ConfigAttributeDefinition</code>s for the indicated secure object type.
   *
   * @param clazz the class that is being queried
   * @return true if the implementation can process the indicated class
   */
  public boolean supports(Class clazz) {
    return FilterInvocation.class.isAssignableFrom(clazz);
  }
}