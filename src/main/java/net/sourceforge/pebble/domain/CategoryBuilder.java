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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.pebble.util.I18n;

/**
 * A class to manage blog categories.
 *
 * @author    Simon Brown
 */
public class CategoryBuilder {

  /** the category separator */
  private static final String CATEGORY_SEPARATOR = "/";

  /** the owning blog */
  private Blog blog;

  /** the root category */
  private Category rootCategory;

  /**
   * Creates a new instance.
   */
  public CategoryBuilder(Blog blog) {
    this.blog = blog;
  }

  /**
   * Creates a new instance.
   */
  public CategoryBuilder(Blog blog, Category rootCategory) {
    this.blog = blog;
    this.rootCategory = rootCategory;
  }

  /**
   * Adds a category.
   *
   * @param category    a Category instance
   */
  public void addCategory(Category category) {
    category.setBlog(blog);

    if (category.isRootCategory()) {
      this.rootCategory = category;
    } else {
      Category parent = getParent(category, true);
      parent.addSubCategory(category);
    }
  }

  /**
   * Removes a category.
   *
   * @param category    a Category instance
   */
  public void removeCategory(Category category) {
    Category parent = getParent(category);
    parent.removeSubCategory(category);
  }

  /**
   * Gets (and creates if necessary), the parent of the specified category.
   *
   * @param category    the Category to find the parent of
   * @param create      true if the parent should be created if it doesn't
   *                    exist, false otherwise
   * @return  a Category instance
   *
   */
  private Category getParent(Category category, boolean create) {
    String id = category.getId();
    int index = id.lastIndexOf(CATEGORY_SEPARATOR);
    String parentId = id.substring(0, index);
    if (parentId.equals("")) {
      // the parent is the root category
      parentId = "/";
    }

    return getCategory(parentId, create);
  }

  /**
   * Gets the parent of the specified category.
   *
   * @return  a Category instance
   */
  private Category getParent(Category category) {
    return getParent(category, false);
  }

  /**
   * Gets (and creates if necessary), the specified category.
   *
   * @param id          the id of the category to find
   * @param create      true if the category should be created if it doesn't
   *                    exist, false otherwise
   * @return  a Category instance
   */
  private Category getCategory(String id, boolean create) {
    if (id == null || !id.startsWith("/")) {
      id = "/" + id;
    }

    if (id.equals("/")) {
      if (rootCategory == null) {
        Category category = new Category("/", I18n.getMessage(blog.getLocale(), "category.all"));
        addCategory(category);
      }

      return rootCategory;
    } else {
      Category parentCategory = getRootCategory();
      Category category = null;
      boolean found = false;
      int index = 0;
      do {
        index = id.indexOf("/", index+1);
        String categoryId;
        if (index == -1) {
          categoryId = id.substring(0, id.length());
        } else {
          categoryId = id.substring(0, index);
        }

        found = false;
        Iterator it = parentCategory.getSubCategories().iterator();
        while (it.hasNext()) {
          category = (Category)it.next();
          if (category.getId().equals(categoryId)) {
            found = true;
            break;
          }
        }

        if (!found) {
          if (create) {
            category = new Category(categoryId, categoryId);
            addCategory(category);
          } else {
            return null;
          }
        }

        parentCategory = category;
      } while (index != -1);

      return category;
    }
  }

  /**
   * Gets the specified category.
   *
   * @param id          the id of the category to find
   * @return  a Category instance, or null if not found
   */
  public Category getCategory(String id) {
    return getCategory(id, false);
  }

  /**
   * Gets the root category.
   *
   * @return  a Category instance
   */
  public Category getRootCategory() {
    return getCategory("/", true);
  }

  /**
   * Gets a collection containing all blog categories,
   * ordered by category name.
   *
   * @return  a sorted List of Category instances
   */
  public List<Category> getCategories() {
    List<Category> allCategories = new ArrayList<Category>();
    allCategories.addAll(getCategories(getRootCategory()));
    Collections.sort(allCategories);

    return allCategories;
  }

  public List<Category> getCategories(Category category) {
    List<Category> allCategories = new ArrayList<Category>();
    allCategories.add(category);
    Iterator it = category.getSubCategories().iterator();
    while (it.hasNext()) {
      allCategories.addAll(getCategories((Category)it.next()));
    }

    return allCategories;
  }

}