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
package net.sourceforge.pebble.decorator;

import net.sourceforge.pebble.domain.*;
import net.sourceforge.pebble.api.decorator.ContentDecorator;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages a list of content decorators at runtime.
 *
 * @author    Simon Brown
 */
public class ContentDecoratorChain implements ContentDecorator {

  /** the blog associated with this chain */
  private Blog blog;

  /** the list of decorators */
  private List<ContentDecorator> decorators = new ArrayList<ContentDecorator>();

  /**
   * Creates a new chain.
   *
   * @param blog The blog to decorate for
   */
  public ContentDecoratorChain(Blog blog) {
    setBlog(blog);
  }

  /**
   * Adds a new decorator.
   *
   * @param decorator   a ContentDecorator instance
   */
  public void add(ContentDecorator decorator) {
    decorators.add(decorator);
  }

  /**
   * Gets the list of decorators in use.
   *
   * @return The content decorators
   */
  public List<ContentDecorator> getContentDecorators() {
    return new ArrayList<ContentDecorator>(decorators);
  }

  /**
   * Decorates the specified blog entry.
   *
   * @param context   the context in which the decoration is running
   * @param blogEntry the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {
    for (ContentDecorator decorator : decorators) {
      decorator.decorate(context, blogEntry);
    }

    // if the view is detail, decorate the comments and TrackBacks too
    if (context.getView() == ContentDecoratorContext.DETAIL_VIEW) {
      for (Comment comment : blogEntry.getComments()) {
        decorate(context, comment);
      }

      for (TrackBack trackBack : blogEntry.getTrackBacks()) {
        decorate(context, trackBack);
      }
    }
  }

  /**
   * Decorates the specified comment.
   *
   * @param context the context in which the decoration is running
   * @param comment the comment to be decorated
   */
  public void decorate(ContentDecoratorContext context, Comment comment) {
    for (ContentDecorator decorator : decorators) {
      decorator.decorate(context, comment);
    }
  }

  /**
   * Decorates the specified TrackBack.
   *
   * @param context   the context in which the decoration is running
   * @param trackBack the TrackBack to be decorated
   */
  public void decorate(ContentDecoratorContext context, TrackBack trackBack) {
    for (ContentDecorator decorator : decorators) {
      decorator.decorate(context, trackBack);
    }
  }

  /**
   * Decorates the specified static page.
   *
   * @param context    the context in which the decoration is running
   * @param staticPage the static page to be decorated
   */
  public void decorate(ContentDecoratorContext context, StaticPage staticPage) {
    for (ContentDecorator decorator : decorators) {
      decorator.decorate(context, staticPage);
    }
  }

  /**
   * Gets the blog to which this decorator is associated.
   *
   * @return a Blog instance
   */
  public Blog getBlog() {
    return this.blog;
  }

  /**
   * Sets the blog to which this decorator is associated.
   *
   * @param blog a Blog instance
   */
  public void setBlog(Blog blog) {
    this.blog = blog;
  }

  /**
   * Gets the list of content decorators.
   *
   * @return  a List of ContentDecorator instances
   */
  public List<ContentDecorator> getDecorators() {
    return new ArrayList<ContentDecorator>(decorators);
  }

  /**
   * Decorates the specified blog entries.
   *
   * @param context       the context
   * @param blogEntries   a List of BlogEntry instances
   */
  public static void decorate(ContentDecoratorContext context, List<BlogEntry> blogEntries) {
    if (blogEntries != null) {
      for (BlogEntry blogEntry : blogEntries) {
        blogEntry.getBlog().getContentDecoratorChain().decorate(context, blogEntry);
      }
    }
  }

}
