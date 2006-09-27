/*
 * Copyright (c) 2003-2006, Simon Brown
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

import net.sourceforge.pebble.domain.Blog;
import org.acegisecurity.ConfigAttributeDefinition;
import org.acegisecurity.intercept.web.AbstractFilterInvocationDefinitionSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
public class PrivateBlogFilterInvocationDefinitionSource extends AbstractFilterInvocationDefinitionSource {

  private static final Log log = LogFactory.getLog(PrivateBlogFilterInvocationDefinitionSource.class);
  private Map<String,ConfigAttributeDefinition> mappings = new HashMap<String,ConfigAttributeDefinition>();

  public void addBlog(Blog blog) {
    ConfigAttributeDefinition cad = new ConfigAttributeDefinition();

    // TODO : associate the following role mappings with the blog to be secured
    //  - ROLE_BLOG_ADMIN
    //  - ROLE_BLOG_READER_<blog id>
    //cad.addConfigAttribute(new SecurityConfig(Constants.BLOG_OWNER_ROLE));
    mappings.put(blog.getId(), cad);
  }

  public void removeBlog(Blog blog) {
    mappings.remove(blog.getId());
  }

  public ConfigAttributeDefinition lookupAttributes(String url) {
    if (url.matches("/.*/.*")) {
      String blogId = url.substring(1, url.indexOf("/", 1));
      return mappings.get(blogId);
    } else {
      return null;
    }
  }

  public Iterator getConfigAttributeDefinitions() {
    return mappings.values().iterator();
  }

}