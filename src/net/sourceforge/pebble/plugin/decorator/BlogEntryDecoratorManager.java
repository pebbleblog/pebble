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
package net.sourceforge.pebble.plugin.decorator;

import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages blog entry decorators for a specific blog.
 *
 * @author    Simon Brown
 */
public class BlogEntryDecoratorManager {

  /** the log used by this class */
  private Log log = LogFactory.getLog(BlogEntryDecoratorManager.class);

  /** the list of decorators */
  private List decorators = new ArrayList();

  /**
   * Gets the list of blog entry decorators.
   *
   * @return  a List of BlogEntryDecorator instances
   */
  public List getBlogEntryDecorators() {
    return this.decorators;
  }

  /**
   * Creates a new decorator chain from a list of class names.
   *
   * @param pluginList    a String containing decorator class names
   */
  public BlogEntryDecoratorManager(Blog blog, String pluginList) {
    if (pluginList != null && pluginList.length() > 0) {
      String classes[] = pluginList.split("\\s+");
      for (int i = 0; i < classes.length; i++) {
        if (!classes[i].startsWith("#")) {
          try {
            Class c = Class.forName(classes[i].trim());
            BlogEntryDecorator decorator = (BlogEntryDecorator)c.newInstance();
            decorator.setBlog(blog);
            decorators.add(decorator);
          } catch (Exception e) {
            e.printStackTrace();
            log.error(classes[i] + " could not be started", e);
          }
        }
      }
    }
  }

  /**
   * Executes the decorators associated with this chain for a list of
   * BlogEntry instances.
   *
   * @param blogEntries   a List of BlogEntry instances
   */
  public static List applyDecorators(List blogEntries, BlogEntryDecoratorContext context) {
    if (blogEntries == null || blogEntries.size() == 0) {
      return blogEntries;
    }

    List list = new ArrayList();
    Iterator it = blogEntries.iterator();
    while (it.hasNext()) {
      BlogEntry blogEntry = (BlogEntry)it.next();
      blogEntry = applyDecorators(blogEntry, context);
      if (blogEntry != null) {
        list.add(blogEntry);
      }
    }

    return list;
  }

  /**
   * Executes the decorators for a BlogEntry instance.
   *
   * @param blogEntry   the BlogEntry to run against
   */
  public static BlogEntry applyDecorators(BlogEntry blogEntry, BlogEntryDecoratorContext context) {
    context.setBlogEntry(blogEntry);
    return blogEntry.getBlog().getBlogEntryDecoratorManager().execute(blogEntry, context);
  }

  /**
   * Executes the decorators for a BlogEntry instance.
   *
   * @param blogEntry   the BlogEntry to run against
   */
  private BlogEntry execute(BlogEntry blogEntry, BlogEntryDecoratorContext context) {
    // clone so that we don't actually change the real entry
    BlogEntry clone = (BlogEntry)blogEntry.clone();

    BlogEntryDecoratorChain chain = new BlogEntryDecoratorChain(decorators);
    context.setBlogEntry(clone);
    try {
      chain.decorate(context);
    } catch (BlogEntryDecoratorException e) {
      log.error(e);
      e.printStackTrace();
    }

    return context.getBlogEntry();
  }

}
