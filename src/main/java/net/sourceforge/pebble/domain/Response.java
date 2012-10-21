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

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.pebble.util.StringUtils;

/**
 * Represents a response to a blog entry - either a comment or a TrackBack.
 *
 * @author    Simon Brown
 */
public abstract class Response extends Content {

  /** the title */
  protected String title;

  /** the ip address of the author */
  protected String ipAddress;

  /** the date that the trackback was received */
  protected Date date;

  /** the parent blog entry */
  protected BlogEntry blogEntry;

  /** a score used to help identify spam when repsonses are added */
  private int spamScore = 0;

  /**
   * Default, no args constructor.
   */
  public Response() {
  }

  /**
   * Creates a new instance with the specified properties.
   *
   * @param title       the title of the entry
   * @param ipAddress   the IP address of the author
   * @param date        the date that this comment was left
   * @param state       the state of the comment
   * @param blogEntry   the owning blog entry
   */
  Response(String title, String ipAddress, Date date, State state, BlogEntry blogEntry) {
    this.blogEntry = blogEntry;

    setTitle(title);
    setIpAddress(ipAddress);
    setDate(date);
    setState(state);
  }

  /**
   * Gets the id of this comment.
   *
   * @return    the id as a primitive long
   */
  public long getId() {
    return date.getTime();
  }

  /**
   * Gets the globally unique id of this response.
   *
   * @return  a String of the form type/blogEntryId/responseId
   */
  public String getGuid() {
    String s = "";
    if (this instanceof Comment) {
      s = "c/";
    } else if (this instanceof TrackBack) {
      s = "t/";
    }

    s+= getBlogEntry().getId() + "/" + getId();

    return s;
  }

  /**
   * Gets the title.
   *
   * @return  the title as a String
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Sets the title of the blog entry for this trackback.
   *
   * @param   title   the title as a String
   */
  public void setTitle(String title) {
    this.title = StringUtils.transformHTML(title);
  }

  /**
   * Gets the name of the source of this response.
   *
   * @return  a String
   */
  public abstract String getSourceName();

  /**
   * Gets the link to the source of this response.
   *
   * @return  a String
   */
  public abstract String getSourceLink();

  /**
   * Gets the IP address.
   *
   * @return  the IP address as a String
   */
  public String getIpAddress() {
    return ipAddress;
  }

  /**
   * Sets the IP address.
   *
   * @param ipAddress   the IP address of the responder
   */
  public void setIpAddress(String ipAddress) {
    if (ipAddress == null || ipAddress.length() == 0) {
      this.ipAddress = null;
    } else {
      this.ipAddress = ipAddress;
    }
  }

  /**
   * Gets the date that this response was received.
   *
   * @return    the date as a java.util.Date instance.
   */
  public Date getDate() {
    return date;
  }

  /**
   * Sets the date that this response was received.
   *
   * @param   date    the date as a java.util.Date instance.
   */
  public void setDate(Date date) {
    if (date == null) {
      date = new Date();
    }

    Calendar cal = blogEntry.getBlog().getCalendar();
    cal.setTime(date);
    this.date = cal.getTime();
  }

  /**
   * Gets the owning blog entry.
   *
   * @return    the owning BlogEntry instance
   */
  public BlogEntry getBlogEntry() {
    return blogEntry;
  }

  /**
   * Sets the owning blog entry.
   *
   * @param blogEntry   the owning BlogEntry instance
   */
  void setBlogEntry(BlogEntry blogEntry) {
    this.blogEntry = blogEntry;
  }

  /**
   * Gets the spam score.
   *
   * @return  an int
   */
  public int getSpamScore() {
    return this.spamScore;
  }

  /**
   * Increments the spam score by 1.
   */
  public void incrementSpamScore() {
    this.spamScore++;
  }

  /**
   * Sets the state of this response to rejected.
   */
  public void setRejected() {
    setState(State.REJECTED);
  }

  /**
   * Determines whether this response is rejected.
   *
   * @return  true if the state is rejected, false otherwise
   */
  public boolean isRejected() {
    return getState().equals(State.REJECTED);
  }

  /**
   * Sets the state of this response to approved.
   */
  public void setApproved() {
    setState(State.APPROVED);
  }

  /**
   * Determines whether this response is approved.
   *
   * @return  true if the state is approved, false otherwise
   */
  public boolean isApproved() {
    return getState().equals(State.APPROVED);
  }

  /**
   * Sets the state of this response to pending.
   */
  public void setPending() {
    setState(State.PENDING);
  }

  /**
   * Determines whether this response is pending.
   *
   * @return  true if the state is pending, false otherwise
   */
  public boolean isPending() {
    return getState().equals(State.PENDING);
  }

  /**
   * Whether this response has an email field
   */
  public abstract boolean isHasEmail();

}
