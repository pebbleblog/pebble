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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.pebble.PluginProperties;
import net.sourceforge.pebble.api.decorator.ContentDecoratorContext;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Tag;
import net.sourceforge.pebble.util.I18n;
import net.sourceforge.pebble.util.StringUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Adds related posts to the current post. The posts are selected by matching
 * tags of the current post to the tags of other posts in the blog. One related
 * post per tag.
 * 
 * Each blog entry can have up to six related posts or none.
 * 
 * @author Alexander Zagniotov
 */
public class RelatedPostsDecorator extends ContentDecoratorSupport {

  private static final Log log = LogFactory.getLog(RelatedPostsDecorator.class);

  /** the name of the max number of posts property */
  public static final String MAX_POSTS = "RelatedPostsDecorator.maxPosts";

  /**
   * Decorates the specified blog entry.
   * 
   * @param context
   *          the context in which the decoration is running
   * @param blogEntry
   *          the blog entry to be decorated
   */
  public void decorate(ContentDecoratorContext context, BlogEntry blogEntry) {

    PluginProperties props = blogEntry.getBlog().getPluginProperties();
    int maxPosts = StringUtils.MAX_NUM_OF_POSTS;

    if (props.hasProperty(RelatedPostsDecorator.MAX_POSTS)) {
      try {
        maxPosts = Integer.parseInt(props.getProperty(MAX_POSTS));
      }
      catch (NumberFormatException nfe) {
        log.error(nfe.getMessage());
        // do nothing, the value has already been defaulted
      }
    }

    Blog blog = blogEntry.getBlog();
    String body = blogEntry.getBody();

    if (body != null && body.trim().length() > 0) {

      StringBuffer buf = new StringBuffer();
      buf.append(body);
      buf.append("<p><b>" + I18n.getMessage(blog, "common.relatedPosts") + "</b><br />");

      // tags of the current entry
      List<Tag> currentEntryTags = blogEntry.getAllTags();

      // all blog entries of the current blog
      List<BlogEntry> allBlogEntries = (List<BlogEntry>) blog.getBlogEntries();

      // temporary holder for accumulated unique related posts.
      // using hash set assures that we wont have same related post twice for
      // different tags.
      Set<BlogEntry> relatedEntries = new HashSet<BlogEntry>();

      for (BlogEntry entry : allBlogEntries) {

        // don't add current entry as a related post of it self, skip it
        if (entry.getTitle().equals(blogEntry.getTitle()))
          continue;

        // loop through each of the current entry tags, and try to find related
        // post by matching current tag to the posts tags
        for (Tag currentTag : currentEntryTags) {
          if (entry.hasTag(currentTag.getName())) {
            // if we successfully selected related post - create hyperlink for
            // it
            // TODO: Missing escaping -- XSS vulnerabilities here :(
            if (relatedEntries.add(entry))
              buf.append("<a href=\"" + entry.getPermalink() + "\" rel=\"bookmark\" title=\"" + entry.getTitle()
                  + "\">" + entry.getTitle() + "</a><br />");
          }
        }

        // do not allow more than default amount of posts or
        // amount set through the RelatedPostsDecorator.maxPosts property
        if (relatedEntries.size() == maxPosts) {
          break;
        }
      }

      if (relatedEntries.size() == 0)
        buf.append("<i>" + I18n.getMessage(blog, "common.noRelatedPosts") + "</i>");

      buf.append("</p><br />");
      blogEntry.setBody(buf.toString());
    }
  }
}
