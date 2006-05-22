/*
 * Copyright (c) 2003-2006, Simon Brown
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

import net.sourceforge.pebble.security.PebbleUserDetails;
import net.sourceforge.pebble.security.PebbleUserDetailsService;
import net.sourceforge.pebble.web.validation.ValidationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;

/**
 * Represents a static page.
 *
 * @author Simon Brown
 */
public class StaticPage extends Content {

  /**
   * the log used by this class
   */
  private static Log log = LogFactory.getLog(StaticPage.class);

  /**
   * the id of the blog entry
   */
  private String id;

  /** the permalink */
  private String permalink;

  /**
   * the title of the blog entry
   */
  private String title = "";

  /**
   * the subtitle of the blog entry
   */
  private String subtitle = "";

  /**
   * the body/content of the blog entry
   */
  private String body = "";

  /**
   * the date that the entry was created
   */
  private Date date;

  /**
   * the author of the blog entry
   */
  private String author = "";

  /** the enriched user details */
  private PebbleUserDetails user;

  /**
   * the alternative permalink for this blog entry
   */
  private String originalPermalink;

  /** the owning blog */
  private Blog blog;

  private boolean persistent = false;

  /**
   * Creates a new blog entry.
   *
   * @param blog    the owning Blog
   */
  public StaticPage(Blog blog) {
    this.blog = blog;
    setDate(new Date());
  }

  /**
   * Gets the unique id of this blog entry.
   *
   * @return the id as a String
   */
  public String getId() {
    return id;
  }

  /**
   * Gets the title of this blog entry.
   *
   * @return the title as a String
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of this blog entry.
   *
   * @param newTitle  the title as a String
   */
  public void setTitle(String newTitle) {
    this.title = newTitle;

    // and cause the permalink to be re-generated
    this.permalink = null;
  }

  /**
   * Gets the subtitle of this blog entry.
   *
   * @return the subtitle as a String
   */
  public String getSubtitle() {
    return subtitle;
  }

  /**
   * Sets the subtitle of this blog entry.
   *
   * @param newSubtitle  the subtitle as a String
   */
  public void setSubtitle(String newSubtitle) {
    this.subtitle = newSubtitle;
  }

  /**
   * Gets the body of this blog entry.
   *
   * @return the body as a String
   */
  public String getBody() {
    return body;
  }

  /**
   * Gets the content of this response.
   *
   * @return a String
   */
  public String getContent() {
    return body;
  }

  /**
   * Sets the body of this blog entry.
   *
   * @param newBody the body as a String
   */
  public void setBody(String newBody) {
    this.body = newBody;
  }

  /**
   * Gets the date that this blog entry was created.
   *
   * @return a java.util.Date instance
   */
  public Date getDate() {
    return date;
  }

  /**
   * Gets the date that this blog entry was last updated.
   *
   * @return  a Date instance representing the time of the last comment/TrackBack
   */
  public Date getLastModified() {
    return date;
  }

  /**
   * Sets the date that this blog entry was created.
   *
   * @param newDate a java.util.Date instance
   */
  public void setDate(Date newDate) {
    this.date = newDate;
    this.id = "" + this.date.getTime();

    // and cause the permalink to be re-generated
    this.permalink = null;
  }

  /**
   * Gets the author of this blog entry.
   *
   * @return the author as a String
   */
  public String getAuthor() {
    return author;
  }

  /**
   * Gets full user details about the author including name, email-address, etc.
   *
   * @return  a PebbleUserDetails instance
   */
  public PebbleUserDetails getUser() {
    if (this.user == null) {
      PebbleUserDetailsService puds = new PebbleUserDetailsService();
      puds.setPebbleContext(BlogManager.getInstance().getPebbleContext());
      try {
        this.user = (PebbleUserDetails)puds.loadUserByUsername(getAuthor());
      } catch (Exception e) {
        // do nothing
      }
    }

    return this.user;
  }

  /**
   * Sets the author of this blog entry.
   *
   * @param newAuthor the author as a String
   */
  public void setAuthor(String newAuthor) {
    this.author = newAuthor;
  }

  /**
   * Determines whether this blog entry has been aggregated from another
   * source. An aggregated blog entry will have a specified permalink.
   *
   * @return true if this blog entry has been aggegrated, false otherwise
   */
  public boolean isAggregated() {
    return (originalPermalink != null);
  }

  /**
   * Gets the alternative permalink for this blog entry.
   *
   * @return an absolute URL as a String
   */
  public String getOriginalPermalink() {
    return this.originalPermalink;
  }

  /**
   * Sets the alternative permalink for this blog entry.
   *
   * @param newPermalink an absolute URL as a String
   */
  public void setOriginalPermalink(String newPermalink) {
    if (newPermalink == null || newPermalink.length() == 0) {
      this.originalPermalink = null;
    } else {
      this.originalPermalink = newPermalink;
    }
  }

  /**
   * Gets a permalink for this blog entry.
   *
   * @return an absolute URL as a String
   */
  public String getPermalink() {
    if (isAggregated()) {
      return getOriginalPermalink();
    } else {
      return getLocalPermalink();
    }
  }

  /**
   * Gets a permalink for this blog entry that is local to the blog. In other
   * words, it doesn't take into account the original permalink for
   * aggregated content.
   *
   * @return an absolute URL as a String
   */
  public String getLocalPermalink() {
    return getBlog().getUrl() + "pages/" + staticName + ".html";
  }

  private String staticName;

  public void setStaticName(String staticName) {
    this.staticName = staticName;
  }

  public String getStaticName() {
    return this.staticName;
  }

  /**
   * Helper method to get the owning Blog instance.
   *
   * @return the overall owning Blog instance
   */
  public Blog getBlog() {
    return this.blog;
  }

  public void validate(ValidationContext context) {
    if (staticName == null || staticName.length() == 0) {
      context.addError("Name cannot be empty");
    } else if (!staticName.matches("[\\w_/-]+")) {
      context.addError("Name \"" + staticName + "\" must contain only A-Za-z0-9_-/");
    }

    String id = getBlog().getStaticPageIndex().getStaticPage(staticName);
    if (id != null && !id.equals(getId())) {
      context.addError("A page with the name \"" + staticName + "\" already exists");
    }
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param o   the reference object with which to compare.
   * @return <code>true</code> if this object is the same as the obj
   *         argument; <code>false</code> otherwise.
   * @see #hashCode()
   * @see java.util.Hashtable
   */
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof StaticPage)) {
      return false;
    }

    StaticPage blogEntry = (StaticPage)o;
    return
        id.equals(blogEntry.getId()) &&
        getBlog().equals(blogEntry.getBlog());
  }

  public String getGuid() {
    return getBlog().getId() + "/" + getId();
  }

  public int hashCode() {
    return getGuid().hashCode();
  }

  /**
   * Gets a string representation of this object.
   *
   * @return  a String
   */
  public String toString() {
    return getBlog().getId() + "/" + getTitle();
  }

  /**
   * Creates and returns a copy of this object.
   *
   * @return a clone of this instance.
   * @see Cloneable
   */
  public Object clone() {
    StaticPage entry = new StaticPage(blog);
    entry.setEventsEnabled(false);
    entry.setTitle(title);
    entry.setSubtitle(subtitle);
    entry.setBody(body);
    entry.setDate(date);
    entry.setState(getState());
    entry.setAuthor(author);
    entry.setOriginalPermalink(originalPermalink);
    entry.setStaticName(staticName);

    return entry;
  }

  public boolean isPersistent() {
    return persistent;
  }

  public void setPersistent(boolean persistent) {
    this.persistent = persistent;
  }

}
