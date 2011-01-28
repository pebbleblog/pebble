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
package net.sourceforge.pebble.plugins;

import java.util.*;

/**
 * @author James Roper
 */
public class AvailablePlugins {
  public static final String PERMALINK_PROVIDER = "permalink-provider";
  public static final String CONTENT_DECORATOR = "content-decorator";
  public static final String BLOG_LISTENER = "blog-listener";
  public static final String BLOG_ENTRY_LISTENER = "blog-entry-listener";
  public static final String COMMENT_LISTENER = "comment-listener";
  public static final String COMMENT_CONFIRMATION_STRATEGY = "comment-confirmation-strategy";
  public static final String TRACKBACK_LISTENER = "trackback-listener";
  public static final String TRACKBACK_CONFIRMATION_STRATEGY = "trackback-confirmation-strategy";
  public static final String LUCENCE_ANALYZER = "lucene-analyzer";
  public static final String LOGGER = "logger";
  public static final String PAGE_DECORATOR = "page-decorator";
  public static final String OPEN_ID_COMMENT_AUTHOR_PROVIDER = "open-id-comment-author-provider";
  public static final String FEED_DECORATOR = "feed-decorator";

  private final Map<String, List<Plugin>> plugins;

  public Map<String, List<Plugin>> copyMap()
  {
    Map<String, List<Plugin>> map = new HashMap<String, List<Plugin>>();
    for (Map.Entry<String, List<Plugin>> entry : plugins.entrySet())
    {
      map.put(entry.getKey(), new ArrayList<Plugin>(entry.getValue()));
    }
    return map;
  }

  public AvailablePlugins(Map<String, List<Plugin>> plugins) {
    this.plugins = plugins;
  }

  public Collection<Plugin> getPermalinkProviders() {
    return getEmptyIfNull(PERMALINK_PROVIDER);
  }

  public Collection<Plugin> getContentDecorators() {
    return getEmptyIfNull(CONTENT_DECORATOR);
  }

  public Collection<Plugin> getBlogListeners() {
    return getEmptyIfNull(BLOG_LISTENER);
  }

  public Collection<Plugin> getBlogEntryListeners() {
    return getEmptyIfNull(BLOG_ENTRY_LISTENER);
  }

  public Collection<Plugin> getCommentListeners() {
    return getEmptyIfNull(COMMENT_LISTENER);
  }

  public Collection<Plugin> getCommentConfirmationStrategies() {
    return getEmptyIfNull(COMMENT_CONFIRMATION_STRATEGY);
  }

  public Collection<Plugin> getTrackbackListeners() {
    return getEmptyIfNull(TRACKBACK_LISTENER);
  }

  public Collection<Plugin> getTrackbackConfirmationStrategies() {
    return getEmptyIfNull(TRACKBACK_CONFIRMATION_STRATEGY);
  }

  public Collection<Plugin> getLuceneAnalyzers() {
    return getEmptyIfNull(LUCENCE_ANALYZER);
  }

  public Collection<Plugin> getLoggers() {
    return getEmptyIfNull(LOGGER);
  }

  public Collection<Plugin> getPageDecorators() {
    return getEmptyIfNull(PAGE_DECORATOR);
  }
  
  public Collection<Plugin> getOpenIdCommentAuthorProviders() {
    return getEmptyIfNull(OPEN_ID_COMMENT_AUTHOR_PROVIDER);
  }

  public Collection<Plugin> getFeedDecorators() {
    return getEmptyIfNull(FEED_DECORATOR);
  }

  private Collection<Plugin> getEmptyIfNull(String key) {
    Collection<Plugin> list = plugins.get(key);
    if (list == null) {
      return Collections.emptyList();
    }
    else {
      return Collections.unmodifiableCollection(list);
    }
  }

}
