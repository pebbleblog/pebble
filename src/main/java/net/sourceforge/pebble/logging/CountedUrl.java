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
package net.sourceforge.pebble.logging;

import net.sourceforge.pebble.domain.Blog;

import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

/**
 * Represents a visited or referer URL along with a count of how many times
 * that URL has been accessed/referred from.
 *
 * @author    Simon Brown
 */
public abstract class CountedUrl {

  /** the maximum length of the name */
  public static final int NAME_LENGTH_LIMIT = 60;

  /** the URL as a String */
  private String url;

  /** the displayable name for the URL */
  private String name;

  /** the collection of log entries that relate to this url */
  private List<LogEntry> logEntries = new LinkedList<LogEntry>();

  private boolean newsFeed = false;
  private boolean pageView = false;
  private boolean fileDownload = false;

  protected Blog blog;

  /**
   * Creates a new CountedUrl representing the specified url.
   *
   * @param url   the url as a String
   */
  public CountedUrl(String url) {
    setUrl(url);
  }

  /**
   * Creates a new CountedUrl representing the specified url.
   *
   * @param url   the url as a String
   */
  public CountedUrl(String url, Blog blog) {
    this.blog = blog;
    setUrl(url);
  }

  /**
   * Gets the underlying url.
   *
   * @return    the url as a String
   */
  public String getUrl() {
    return url;
  }

  /**
   * Sets the underlying url.
   *
   * @param url   the url as a String
   */
  protected void setUrl(String url) {
    this.url = url;
  }

  /**
   * Gets a name representation of the url. This is just the url, but truncated
   * to a maximum number of characters.
   *
   * @return    a String
   */
  public String getName() {
    return this.name;
  }

  /**
   * Sets the name.
   *
   * @param name    the name as a String
   */
  protected void setName(String name) {
    this.name = name;
  }

  /**
   * Gets a name representation of the url. This is just the url, but truncated
   * to a maximum number of characters.
   *
   * @return    a String
   */
  public String getTruncatedName() {
    String s = getName();
    if (s.length() <= NAME_LENGTH_LIMIT) {
      return s;
    } else {
      return s.substring(0, NAME_LENGTH_LIMIT - 3) + "...";
    }
  }

  /**
   * Adds a LogEntry.
   *
   * @param logEntry    a LogEntry instance
   */
  public void addLogEntry(LogEntry logEntry) {
    logEntries.add(logEntry);
  }

  /**
   * Gets the list of log entries associated with this URL
   *
   * @return  a List of LogEntry instances
   */
  public List<LogEntry> getLogEntries() {
    return new LinkedList<LogEntry>(logEntries);
  }

  /**
   * Gets the count associated with this url.
   *
   * @return    the count as an int
   */
  public int getCount() {
    return logEntries.size();
  }

  /**
   * Implementation of the hashCode() method.
   *
   * @return  the hashcode of the underlying url
   */
  public int hashCode() {
    return url == null ? 0 : url.hashCode();
  }

  /**
   * Determines whether this object is equal to another.
   *
   * @param o   the object to test against
   * @return  true if the specified object is the same as this one (i.e. the
   *          underlying urls match, false otherwise
   */
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof CountedUrl)) return false;

    CountedUrl cUrl = (CountedUrl)o;

    if (url == null && cUrl.getUrl() == null) {
      return true;
    } else {
      return (url != null && url.equals(cUrl.getUrl()));
    }
  }

  public boolean isNewsFeed() {
    return newsFeed;
  }

  public void setNewsFeed(boolean newsFeed) {
    this.newsFeed = newsFeed;
  }

  public boolean isPageView() {
    return pageView;
  }

  public void setPageView(boolean pageView) {
    this.pageView = pageView;
  }

  public boolean isFileDownload() {
    return fileDownload;
  }

  public void setFileDownload(boolean fileDownload) {
    this.fileDownload = fileDownload;
  }
  
}