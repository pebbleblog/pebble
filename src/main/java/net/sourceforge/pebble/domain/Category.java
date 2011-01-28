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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.*;

import net.sourceforge.pebble.comparator.ReverseBlogEntryIdComparator;

/**
 * Represents a blog category.
 *
 * @author    Simon Brown
 */
public class Category implements Permalinkable, Comparable, Serializable {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(Category.class);

  /** the root category identifier */
  private static final String ROOT_CATEGORY_IDENTIFIER = "/";

  /** the owning blog */
  private transient Blog blog;

  /** the id of the category */
  private String id = "";

  /** the name of the category */
  private String name = "";

  /** the parent category, if applicable */
  private Category parent = null;

  /** the set of tags for this category */
  private String tags = "";

  /** the list of tags for this category */
  private List tagsAsList = new ArrayList();

  /** the sub-categories */
  private List subCategories = new ArrayList();

  /** the blog entries associated with this category */
  private List<String> blogEntries = new ArrayList<String>();

  /**
   * Default, no args constructor.
   */
  public Category() {
  }

  /**
   * Creates a new category with the specified properties.
   *
   * @param id          the id
   * @param name          the name
   */
  public Category(String id, String name) {
    setId(id);

    this.name = name;
  }

  /**
   * Gets the id of this category.
   *
   * @return    the id as a String
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the id of this category.
   *
   * @param id    the id as a String
   */
  public void setId(String id) {
    this.id = id;
    if (this.id == null || !this.id.startsWith("/")) {
      this.id = "/" + this.id;
    }
  }

  /**
   * Gets the name of this category.
   *
   * @return    the name as a String
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of this category.
   *
   * @param name    the new category name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Determines whether this category is a root category.
   *
   * @return  true if the ID is "/", false otherwise
   */
  public boolean isRootCategory() {
    return id.equals(ROOT_CATEGORY_IDENTIFIER);
  }

  /**
   * Gets the parent of thie category.
   *
   * @return  a Category instance, or null if this category has no parent
   */
  public Category getParent() {
    return this.parent;
  }

  /**
   * Determines whether this category has the specified parent.
   *
   * @param category    a Category to test for
   * @return  true if this category has the specified category as one of its
   *          parents, false otherwise
   */
  public boolean hasParent(Category category) {
    Category parent = getParent();
    while (parent != null) {
      if (parent.equals(category)) {
        return true;
      } else {
        parent = parent.getParent();
      }
    }

    return false;
  }

  /**
   * Sets the parent of this category.
   *
   * @param parent  a Category instance
   */
  public void setParent(Category parent) {
    this.parent = parent;
  }

  /**
   * Gets the number of parents that this category has.
   *
   * @return  the number of parents this category has, or 0 if it is
   *          the root category
   */
  public int getNumberOfParents() {
    int parents = 0;
    Category parent = getParent();
    while (parent != null) {
      parents++;
      parent = parent.getParent();
    }

    return parents;
  }

  /**
   * Adds a sub-category.
   *
   * @param category    a Category instance
   */
  public synchronized void addSubCategory(Category category) {
    if (subCategories != null && !subCategories.contains(category)) {
      subCategories.add(category);
      category.setParent(this);
    }
  }

  /**
   * Removes a sub-category.
   *
   * @param category    a Category instance
   */
  public synchronized void removeSubCategory(Category category) {
    if (subCategories != null && subCategories.contains(category)) {
      subCategories.remove(category);
      category.setParent(null);
    }
  }

  /**
   * Gets the list of sub-categories.
   *
   * @return  a List of Category instances
   */
  public List getSubCategories() {
    return Collections.unmodifiableList(subCategories);
  }

  /**
   * Gets the tags associated with this category.
   *
   * @return  a list of tags
   */
  public String getTags() {
    return this.tags;
  }

  /**
   * Gets the tags associated with this category, as a List.
   *
   * @return  a List of tags
   */
  public List getTagsAsList() {
    return this.tagsAsList;
  }

  /**
   * Gets the tags associated with this category and its parents.
   *
   * @return  a list of tags
   */
  public List getAllTags() {
    List l = new ArrayList();

    l.addAll(getTagsAsList());
    Category parent = getParent();
    while (parent != null) {
      l.addAll(parent.getTagsAsList());
      parent = parent.getParent();
    }

    return l;
  }

  /**
   * Sets the set of tags associated with this category.
   *
   * @param newTags    a set of tags
   */
  public void setTags(String newTags) {
    if (newTags != null && newTags.indexOf(",") > -1) {
      // if the tags have been comma separated, convert them to
      // whitespace separated by
      // - remove whitespace
      // - convert commas to whitespace
      newTags = newTags.replaceAll(" ", "").replaceAll(",", " ");
    }
    this.tags = newTags;
    this.tagsAsList = Tag.parse(blog, tags);
  }

  /**
   * Sets the owning blog.
   *
   * @param blog    a Blog instance
   */
  public void setBlog(Blog blog) {
    this.blog = blog;
  }

  /**
   * Gets the permalink for this object.
   *
   * @return  a URL as a String
   */
  public String getPermalink() {
    if (isRootCategory()) {
      return blog.getUrl() + "categories/";
    } else {
      return blog.getUrl() + "categories" + id + "/";
    }
  }

  /**
   * Gets the hashcode of this object.
   *
   * @return  the hashcode as an int
   */
  public int hashCode() {
    return id.hashCode();
  }

  /**
   * Determines whether the specified object is equal to this one.
   *
   * @param o   the object to compare against
   * @return    true if Object o represents the same category, false otherwise
   */
  public boolean equals(Object o) {
    if (!(o instanceof Category)) {
      return false;
    }

    Category cat = (Category)o;
    return (cat.getId().equals(id));
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
    Category category = (Category)o;
    return getId().compareTo(category.getId());
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
   * Gets the blog entries associated with this category.
   *
   * @return  a Collection of BlogEntry instances
   */
  public List<String> getBlogEntries() {
    return new ArrayList<String>(blogEntries);
  }

  /**
   * Adds a blog entry to this category.
   *
   * @param blogEntry   a blog entry id
   */
  public synchronized void addBlogEntry(String blogEntry) {
    if (blogEntry != null && !blogEntries.contains(blogEntry)) {
      blogEntries.add(blogEntry);
      Collections.sort(blogEntries, new ReverseBlogEntryIdComparator());

      if (getParent() != null) {
        getParent().addBlogEntry(blogEntry);
      }
    }
  }

  /**
   * Removes a blog entry from this category.
   *
   * @param blogEntry   a blog entry id
   */
  public synchronized void removeBlogEntry(String blogEntry) {
    if (blogEntry != null) {
      blogEntries.remove(blogEntry);

      if (getParent() != null) {
        getParent().removeBlogEntry(blogEntry);
      }
    }
  }

  /**
   * Removes all blog entries from this category.
   */
  public synchronized void removeAllBlogEntries() {
    blogEntries = new ArrayList<String>();
  }

  /**
   * Gets the number of blog entries associated with this category.
   *
   * @return  an int
   */
  public int getNumberOfBlogEntries() {
    return this.blogEntries.size();
  }

}
