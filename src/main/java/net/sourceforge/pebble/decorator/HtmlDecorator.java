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

import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.Comment;
import net.sourceforge.pebble.domain.TrackBack;
import net.sourceforge.pebble.util.StringUtils;

/**
 * Decorates blog entries and comments by rendering them as HTML.
 * 
 * @author Simon Brown
 */
public class HtmlDecorator extends ContentDecoratorSupport {

  /**
   * Decorates the specified comment.
   *
   * @param context the context in which the decoration is running
   * @param comment the comment to be decorated
   */
  public void decorate(ContentDecoratorContext context, Comment comment) {
    comment.setAuthor(StringUtils.filterHTML(comment.getAuthor()));
    comment.setWebsite(StringUtils.filterHTML(comment.getWebsite()));
    comment.setEmail(StringUtils.filterHTML(comment.getEmail()));
    comment.setTitle(StringUtils.filterHTML(comment.getTitle()));
    comment.setBody(StringUtils.transformToHTMLSubset(StringUtils.transformHTML(comment.getBody())));
  }

  /**
   * Decorates the specified TrackBack.
   *
   * @param context   the context in which the decoration is running
   * @param trackBack the TrackBack to be decorated
   */
  public void decorate(ContentDecoratorContext context, TrackBack trackBack) {
    trackBack.setBlogName(StringUtils.filterHTML(trackBack.getBlogName()));
    trackBack.setUrl(StringUtils.filterHTML(trackBack.getUrl()));
    trackBack.setTitle(StringUtils.filterHTML(trackBack.getTitle()));
    trackBack.setExcerpt(StringUtils.transformToHTMLSubset(StringUtils.transformHTML(trackBack.getExcerpt())));
  }
}
