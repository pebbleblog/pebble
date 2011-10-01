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

import net.sourceforge.pebble.util.StringUtils;

import java.util.*;

/**
 * Represents a tag.
 *
 * @author    Simon Brown
 */
public class Tag implements Permalinkable, Comparable {
  /** the owning blog */
  private final Blog blog;

  /** the name of the tag */
  private final String name;

  /** the rank for this tag */
  private final int rank;

  /** the number of blog entries for this tag */
  private final int numberOfBlogEntries;

  /**
   * Creates a new tag with the specified properties.
   *
   * @param name    the name
   * @param blog    a Blog instance
   * @param rank    the rank of the tag
   * @param numberOfBlogEntries the number of blog entries for the tag
   */
  public Tag(String name, Blog blog, int rank, int numberOfBlogEntries) {
    this.name = name;
    this.blog = blog;
    this.rank = rank;
    this.numberOfBlogEntries = numberOfBlogEntries;
  }

  /**
   * Creates a new tag with the specified properties.
   *
   * @param name    the name
   * @param blog    a Blog instance
   */
  public Tag(String name, Blog blog) {
    this(name, blog, 0, 0);
  }

  /**
   * Gets the name of this tag.
   *
   * @return    the name as a String
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the permalink for this object.
   *
   * @return  a URL as a String
   */
  public String getPermalink() {
    return blog.getUrl() + "tags/" + name + "/";
  }

  /**
   * Gets the hashcode of this object.
   *
   * @return  the hashcode as an int
   */
  public int hashCode() {
    return name.hashCode();
  }

  /**
   * Determines whether the specified object is equal to this one.
   *
   * @param o   the object to compare against
   * @return    true if Object o represents the same tag, false otherwise
   */
  public boolean equals(Object o) {
    if (!(o instanceof Tag)) {
      return false;
    }

    Tag tag = (Tag)o;
    return tag.getName().equals(name);
  }

  /**
   * Compares this object with the specified object for order.  Returns a
   * negative integer, zero, or a positive integer as this object is less
   * than, equal to, or greater than the specified object.<p>
   *
   * @param   o the Object to be compared.
   * @return  a negative integer, zero, or a positive integer as this object
   *		is less than, equal to, or greater than the specified object.
   *
   * @throws ClassCastException if the specified object's type prevents it
   *         from being compared to this Object.
   */
  public int compareTo(Object o) {
    Tag tag = (Tag)o;
    return getName().compareTo(tag.getName());
  }

  /**
   * Returns a String representation of this object.
   *
   * @return  a String
   */
  public String toString() {
    return this.name;
  }

  /**
   * Gets the rank for this tag.
   *
   * @return  an int between 1 and 10;
   */
  public int getRank() {
    return this.rank;
  }

  public int getNumberOfBlogEntries() {
    return numberOfBlogEntries;
  }

  public Blog getBlog() {
    return blog;
  }

  /**
   * Given a string containing whitespace separated tags, this method returns a
   * List containing the tags.
   *
   * @param tags    a whitespace separated list of tags
   * @return        a List of Tag instances
   */
  public static List<Tag> parse(Blog blog, String tags) {
    List<Tag> list = new ArrayList<Tag>();

    if (tags != null && tags.trim().length() > 0) {
      String s[] = tags.trim().split(" ");
      for (int i = 0; i < s.length; i++) {
        Tag tag = new Tag(Tag.encode(StringUtils.transformHTML(s[i].trim())), blog);
        if (!list.contains(tag)) {
          list.add(tag);
        }
      }
    }

    return list;
  }

  /**
   * Given a list of tags, this method formats them into a comma separated list.
   *
   * @param tags    a List of tags
   * @return  a comma separated String
   */
  public static String format(List<Tag> tags) {
    StringBuilder builder = new StringBuilder();

    if (tags != null) {
      Iterator it = tags.iterator();
      while (it.hasNext()) {
        Tag tag = (Tag)it.next();
        builder.append(tag.getName());
        if (it.hasNext()) {
          builder.append(", ");
        }
      }
    }

    return builder.toString();
  }

  /**
   * Encodes a tag.
   *
   * @param tag   a String
   */
  public static String encode(String tag) {
    if (tag == null) {
      return "";
    } else {
      String encodedTag = tag.trim().toLowerCase();
      encodedTag = encodedTag.replaceAll("\\+", " ");
      return encodedTag;
    }
  }

}
