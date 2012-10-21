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
package net.sourceforge.pebble.domain;

import net.sourceforge.pebble.api.event.trackback.TrackBackEvent;

import java.util.Date;

/**
 * Represents a MovableType TrackBack - see
 * http://www.movabletype.org/docs/mttrackback.html for more information.
 *
 * @author    Simon Brown
 */
public class TrackBack extends Response {

  /** the excerpt */
  private String excerpt;

  /** the url */
  private String url;

  /** the blog name */
  private String blogName;

  /**
   * Creates a new TrackBack with the specified properties.
   *
   * @param title       the title of the entry
   * @param excerpt     the excerpt of the entry
   * @param url         the url (permalink) of the entry
   * @param blogName    the name of the blog
   * @param ipAddress   the IP address of the author
   * @param date        the date that this TrackBack was left
   * @param state       the stats of this TrackBack
   * @param blogEntry   the owning blog entry
   */
  TrackBack(String title, String excerpt, String url, String blogName, String ipAddress, Date date, State state, BlogEntry blogEntry) {
    super(title, ipAddress, date, state, blogEntry);

    setExcerpt(excerpt);
    setUrl(url);
    setBlogName(blogName);
  }

  /**
   * Gets the title of the blog entry for this trackback.
   *
   * @return  the title as a String
   */
  public String getTitle() {
    if (title != null && title.length() > 0) {
      return title;
    } else {
      return url;
    }
  }

  /**
   * Gets the excerpt of the blog entry for this trackback.
   *
   * @return  return the excerpt as a String
   */
  public String getExcerpt() {
    return excerpt;
  }

  /**
   * Sets the excerpt of the blog entry for this trackback.
   *
   * @param   excerpt   the excerpt as a String
   */
  public void setExcerpt(String excerpt) {
    if (excerpt != null) {
      this.excerpt = excerpt;
    } else {
      this.excerpt = "";
    }
  }

  /**
   * Gets the content of this response.
   *
   * @return a String
   */
  public String getContent() {
    return getExcerpt();
  }

  /**
   * Gets the url (permalink) of the blog entry for this trackback.
   *
   * @return  return the url as a String
   */
  public String getUrl() {
    return url;
  }

  /**
   * Gets the link to the source of this response.
   *
   * @return a String
   */
  public String getSourceLink() {
    return getUrl();
  }

  /**
   * Sets the url (permalink) of the blog entry for this trackback.
   *
   * @param url   the url as a String
   */
  public void setUrl(String url) {
    this.url = url;
  }

  /**
   * Gets the name of the blog for this trackback.
   *
   * @return  return the name as a String
   */
  public String getBlogName() {
    return blogName;
  }

  /**
   * Gets the name of the source of this response.
   *
   * @return a String
   */
  public String getSourceName() {
    return getBlogName();
  }

  /**
   * Sets the name of the blog for this trackback.
   *
   * @param   blogName    the name as a String
   */
  public void setBlogName(String blogName) {
    if (blogName != null) {
      this.blogName = blogName;
    } else {
      this.blogName = "";
    }
  }

  /**
   * Gets the permalink for this TrackBack.
   *
   * @return  a URL as a String
   */
  public String getPermalink() {
    if (blogEntry != null) {
      return blogEntry.getLocalPermalink() + "#trackback" + getId();
    } else {
      return "";
    }
  }

  /**
   * Creates and returns a copy of this object.
   *
   * @return a clone of this instance.
   * @see Cloneable
   */
  public Object clone() {
    TrackBack trackBack = new TrackBack(title, excerpt, url, blogName, ipAddress, date, getState(), blogEntry);
    return trackBack;
  }

  /**
   * Sets the state of this TrackBack.
   */
  void setState(State state) {
    State previousState = getState();
    super.setState(state);

    if (areEventsEnabled()) {
      if (isApproved() && previousState != State.APPROVED) {
        blogEntry.addEvent(new TrackBackEvent(this, TrackBackEvent.TRACKBACK_APPROVED));
      } else if (isRejected() && previousState != State.REJECTED) {
        blogEntry.addEvent(new TrackBackEvent(this, TrackBackEvent.TRACKBACK_REJECTED));
      }
    }
  }

  @Override
  public boolean isHasEmail() {
    return false;
  }
}
