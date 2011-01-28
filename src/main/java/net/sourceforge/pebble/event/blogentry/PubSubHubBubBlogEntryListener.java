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
package net.sourceforge.pebble.event.blogentry;

import net.sourceforge.pebble.api.event.blogentry.BlogEntryEvent;
import net.sourceforge.pebble.audit.AuditTrail;
import net.sourceforge.pebble.decorator.PubSubHubBubFeedDecorator;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Category;
import net.sourceforge.pebble.domain.Tag;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * Listener that posts to a PubSubHubBub Hub when a blog entry is added/published/changed.
 *
 * @author James Roper
 */
public class PubSubHubBubBlogEntryListener extends BlogEntryListenerSupport {
  private final static String HUB_MODE_PARAM = "hub.mode";
  private final static String HUB_URL_PARAM = "hub.url";
  private final static String HUB_MODE = "publish";
  private final static String ATOM_XML = "atom.xml";
  private static final Log log = LogFactory.getLog(PubSubHubBubBlogEntryListener.class);

  @Override
  public void blogEntryAdded(BlogEntryEvent event) {
    if (event.getBlogEntry().isPublished()) {
      postToHubs(event);
    }
  }

  @Override
  public void blogEntryRemoved(BlogEntryEvent event) {
    if (event.getBlogEntry().isPublished()) {
     postToHubs(event);
    }
  }

  @Override
  public void blogEntryChanged(BlogEntryEvent event) {
    if (event.getBlogEntry().isPublished()) {
      postToHubs(event);
    }
  }

  @Override
  public void blogEntryPublished(BlogEntryEvent event) {
    postToHubs(event);
  }

  @Override
  public void blogEntryUnpublished(BlogEntryEvent event) {
    postToHubs(event);
  }

  private void postToHubs(BlogEntryEvent event) {
    BlogEntry entry = event.getBlogEntry();
    Blog blog = entry.getBlog();
    String blogUrl = blog.getUrl();
    for (String hub : getHubs(blog)) {
      HttpClient httpClient = new HttpClient();
      PostMethod method = new PostMethod(hub);
      method.addParameter(HUB_MODE_PARAM, HUB_MODE);
      // Add every feed that might have this
      method.addParameter(HUB_URL_PARAM, blogUrl + ATOM_XML);
      for (Category category : entry.getCategories()) {
        method.addParameter(HUB_URL_PARAM, category.getPermalink() + ATOM_XML);
      }
      for (Tag tag : entry.getAllTags()) {
        method.addParameter(HUB_URL_PARAM, tag.getPermalink() + ATOM_XML);
      }
      method.addParameter(HUB_URL_PARAM, blogUrl + "authors/" + entry.getAuthor() + "/" + ATOM_XML);
      try {
        int rc = httpClient.executeMethod(method);
        if (rc != HttpServletResponse.SC_NO_CONTENT) {
          String errorMessage = "Unexpected response code received from hub: " + hub + " - " + rc + " " +
              method.getStatusText();
          log.warn(errorMessage);
          blog.warn(errorMessage);
        }
      } catch (IOException e) {
        log.error("Error posting to hub: " + hub, e);
        blog.warn("Error publishing to hub: " + hub + ". Message: " + e.getMessage());
      }
    }
  }

  private Collection<String> getHubs(Blog blog) {
    String hubs = blog.getPluginProperties().getProperty(PubSubHubBubFeedDecorator.HUBS_PROPERTY);
    if (hubs == null || hubs.length() == 0) {
      return Collections.emptyList();
    }
    return Arrays.asList(hubs.split(","));
  }
  
}
