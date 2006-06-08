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

import net.sourceforge.pebble.web.validation.ValidationContext;

/**
 * Represents a page.
 *
 * @author Simon Brown
 */
public class StaticPage extends PageBasedContent {

  /** the name of the page */
  private String name;

  /**
   * Creates a new blog entry.
   *
   * @param blog    the owning Blog
   */
  public StaticPage(Blog blog) {
    super(blog);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets a permalink for this blog entry that is local to the blog. In other
   * words, it doesn't take into account the original permalink for
   * aggregated content.
   *
   * @return an absolute URL as a String
   */
  public String getLocalPermalink() {
    return getBlog().getUrl() + "pages/" + name + ".html";
  }

  public void validate(ValidationContext context) {
    if (name == null || name.length() == 0) {
      context.addError("Name cannot be empty");
    } else if (!name.matches("[\\w_/-]+")) {
      context.addError("Name \"" + name + "\" must contain only A-Za-z0-9_-/");
    }

    String id = getBlog().getStaticPageIndex().getStaticPage(name);
    if (id != null && !id.equals(getId())) {
      context.addError("A page with the name \"" + name + "\" already exists");
    }
  }

  public String getGuid() {
    return "page/" + getBlog().getId() + "/" + getId();
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

    StaticPage staticPage = (StaticPage)o;
    return getGuid().equals(staticPage.getGuid());
  }

  /**
   * Creates and returns a copy of this object.
   *
   * @return a clone of this instance.
   * @see Cloneable
   */
  public Object clone() {
    StaticPage page = new StaticPage(getBlog());
    page.setEventsEnabled(false);
    page.setTitle(getTitle());
    page.setSubtitle(getSubtitle());
    page.setBody(getBody());
    page.setDate(getDate());
    page.setState(getState());
    page.setAuthor(getAuthor());
    page.setOriginalPermalink(getOriginalPermalink());
    page.setName(getName());

    return page;
  }

}