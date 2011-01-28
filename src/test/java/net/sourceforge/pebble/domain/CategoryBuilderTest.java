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




/**
 * Tests for the CategoryBuilder class.
 *
 * @author    Simon Brown
 */
public class CategoryBuilderTest extends SingleBlogTestCase {

  private CategoryBuilder builder;

  protected void setUp() throws Exception {
    super.setUp();
    builder = new CategoryBuilder(blog);
  }

  /**
   * Tests that a root category is created by default.
   */
  public void testRootCategoryCreatedByDefault() {
    Category category = builder.getRootCategory();
    assertNotNull(category);
    assertTrue(category.isRootCategory());
  }

  /**
   * Tests that a root category can be added.
   */
  public void testRootCategoryCanBeAdded() {
    Category root = new Category("/", "/");
    builder.addCategory(root);
    root.setTags("blog");
    Category category = builder.getRootCategory();
    assertNotNull(category);
    assertTrue(category.isRootCategory());
    assertNull(category.getParent());
    assertEquals("blog", category.getTags());
  }

  /**
   * Tests that a level 2 category can be added with parent.
   */
  public void testLevel2CategoryCanBeAddedWithParent() {
    Category root = new Category("/", "/");
    Category java = new Category("/java", "Java");
    builder.addCategory(root);
    builder.addCategory(java);
    root.setTags("blog");
    java.setTags("java");

    Category category = (Category)builder.getRootCategory().getSubCategories().get(0);
    assertNotNull(category);
    assertEquals("/java", category.getId());
    assertEquals("Java", category.getName());
    assertEquals("java", category.getTags());
    assertEquals(builder.getRootCategory(), category.getParent());
  }

  /**
   * Tests that a level 2 category can be added without parent.
   */
  public void testLevel2CategoryCanBeAddedWithoutParent() {
    Category java = new Category("/java", "Java");
    builder.addCategory(java);
    java.setTags("java");

    Category category = (Category)builder.getRootCategory().getSubCategories().get(0);
    assertNotNull(category);
    assertEquals("/java", category.getId());
    assertEquals("Java", category.getName());
    assertEquals("java", category.getTags());
    assertEquals(builder.getRootCategory(), category.getParent());
  }

  /**
   * Tests that multiple level 2 categories can be added with parent.
   */
  public void testLevel2CategoriesCanBeAddedWithParent() {
    Category root = new Category("/", "/");
    Category java = new Category("/java", "Java");
    Category apple = new Category("/apple", "Apple");
    builder.addCategory(root);
    builder.addCategory(java);
    builder.addCategory(apple);
    root.setTags("blog");
    java.setTags("java");
    apple.setTags("apple");

    assertEquals(2, builder.getRootCategory().getSubCategories().size());
    Category category = (Category)builder.getRootCategory().getSubCategories().get(0);
    assertNotNull(category);
    assertEquals("/java", category.getId());
    assertEquals("Java", category.getName());
    assertEquals("java", category.getTags());
    category = (Category)builder.getRootCategory().getSubCategories().get(1);
    assertNotNull(category);
    assertEquals("/apple", category.getId());
    assertEquals("Apple", category.getName());
    assertEquals("apple", category.getTags());
  }

  /**
   * Tests that a multiple level 2 categories can be added without parent.
   */
  public void testLevel2CategoriesCanBeAddedWithoutParent() {
    Category java = new Category("/java", "Java");
    Category apple = new Category("/apple", "Apple");
    builder.addCategory(java);
    builder.addCategory(apple);
    java.setTags("java");
    apple.setTags("apple");

    assertEquals(2, builder.getRootCategory().getSubCategories().size());
    Category category = (Category)builder.getRootCategory().getSubCategories().get(0);
    assertNotNull(category);
    assertEquals("/java", category.getId());
    assertEquals("Java", category.getName());
    assertEquals("java", category.getTags());
    category = (Category)builder.getRootCategory().getSubCategories().get(1);
    assertNotNull(category);
    assertEquals("/apple", category.getId());
    assertEquals("Apple", category.getName());
    assertEquals("apple", category.getTags());
  }

  /**
   * Tests that a level 3 category can be added with parent.
   */
  public void testLevel3CategoryCanBeAddedWithParent() {
    Category root = new Category("/", "/");
    Category java = new Category("/java", "Java");
    Category junit = new Category("/java/junit", "JUnit");
    builder.addCategory(root);
    builder.addCategory(java);
    builder.addCategory(junit);
    root.setTags("blog");
    java.setTags("java");
    junit.setTags("junit");

    Category category = (Category)builder.getRootCategory().getSubCategories().get(0);
    assertNotNull(category);
    assertEquals("/java", category.getId());
    assertEquals("Java", category.getName());
    assertEquals("java", category.getTags());
    category = (Category)category.getSubCategories().get(0);
    assertNotNull(category);
    assertEquals("/java/junit", category.getId());
    assertEquals("JUnit", category.getName());
    assertEquals("junit", category.getTags());

    assertEquals(builder.getRootCategory(), java.getParent());
    assertEquals(java, junit.getParent());
  }

  /**
   * Tests that a level 3 category can be added without parent.
   */
  public void testLevel3CategoryCanBeAddedWithoutParent() {
    Category junit = new Category("/java/junit", "JUnit");
    builder.addCategory(junit);
    junit.setTags("junit");

    Category java = (Category)builder.getRootCategory().getSubCategories().get(0);
    assertNotNull(java);
    assertEquals("/java", java.getId());
    assertEquals("/java", java.getName());
    assertEquals("", java.getTags());
    junit = (Category)java.getSubCategories().get(0);
    assertNotNull(junit);
    assertEquals("/java/junit", junit.getId());
    assertEquals("JUnit", junit.getName());
    assertEquals("junit", junit.getTags());

    assertEquals(builder.getRootCategory(), java.getParent());
    assertEquals(java, junit.getParent());
  }

  /**
   * Tests that a category can be added and retrieved.
   */
  public void testCategoryCanBeAddedAndRetrieved() {
    Category apple = new Category("/apple", "Apple");
    builder.addCategory(apple);
    apple.setTags("apple");
    Category junit = new Category("/java/junit", "JUnit");
    builder.addCategory(junit);
    junit.setTags("junit");

    Category category = builder.getCategory("/apple");
    assertEquals(apple, category);
    category = builder.getCategory("/java/junit");
    assertEquals(junit, category);
  }

}