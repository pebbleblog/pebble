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
package net.sourceforge.pebble.aggregator;

import net.sourceforge.pebble.util.StringUtils;

import java.util.Date;

/**
 * @author Simon Brown
 */
public class NewsFeedEntry {

  private String link;
  private String title;
  private String body;
  private String author;
  private Date date;
  private NewsFeed feed;

  public NewsFeedEntry(String link, String title, String body, String author, Date date) {
    this.link = link;
    this.title = title;
    this.body = body;
    this.author = author;
    this.date = date;
  }

  void setFeed(NewsFeed feed) {
    this.feed = feed;
  }

  public NewsFeed getFeed() {
    return feed;
  }

  public String getLink() {
    return link;
  }

  public String getTitle() {
    return title;
  }

  public String getBody() {
    return body;
  }

  public String getTruncatedBody() {
    return StringUtils.truncate(getBody());
  }

  public String getAuthor() {
    return author;
  }

  public Date getDate() {
    return this.date;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    NewsFeedEntry feedEntry = (NewsFeedEntry) o;

    if (!link.equals(feedEntry.link)) return false;

    return true;
  }

  public int hashCode() {
    return link.hashCode();
  }

  public String toString() {
    return "title=" + title + " | body=" + body + " | author=" + author + " | date=" + date + " | link=" + link;
  }

}
