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
package net.sourceforge.pebble.api.decorator;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Response;

/**
 * A feed decorator plugin, used by plugins to modify feeds
 *
 * @author James Roper
 */
public interface FeedDecorator {

  /**
   * Decorate a synd feed element.  This will be called after decoration of all the synd entries.
   *
   * @param feed The feed to decorate
   * @param blog The blog the feed is for
   */
  void decorate(SyndFeed feed, Blog blog);

  /**
   * Decorate a synd feed element, for response feeds.  This will be called after decoration of all the synd entries.
   *
   * @param feed  The feed to decorate
   * @param blog  The blog the feed is for
   * @param blogEntry The blog entry the feed is for
   */
  void decorate(SyndFeed feed, Blog blog, BlogEntry blogEntry);

  /**
   * Decorate a synd entry element.
   *
   * @param entry  The entry to decorate
   * @param blog   The blog the feed is for
   * @param blogEntry The blog entry the entry is for
   */
  void decorate(SyndEntry entry, Blog blog, BlogEntry blogEntry);

  /**
   * Decorate a synd entry element, for response feeds.
   *
   * @param entry  The entry to decorate
   * @param blog   The blog the feed is for
   * @param response The response the entry is for
   */
  void decorate(SyndEntry entry, Blog blog, Response response);

}
