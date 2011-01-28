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

import java.util.List;


/**
 * Tests for the Category class.
 *
 * @author    Simon Brown
 */
public class CategoryTest extends SingleBlogTestCase {

  private Category category;

  protected void setUp() throws Exception {
    super.setUp();

    category = new Category("id", "name");
  }

  /**
   * Tests that toString() works.
   */
  public void testToString() {
    assertEquals("name", category.toString());
  }

  /**
   * Tests for the hasParent() method.
   */
  public void testHasParent() {
    Category java = new Category("/java", "Java");
    Category junit = new Category("/java/junit", "JUnit");
    java.addSubCategory(junit);
    assertTrue(junit.hasParent(java));
  }

  /**
   * Tests for the getParent() method.
   */
  public void testGetParent() {
    Category java = new Category("/java", "Java");
    Category junit = new Category("/java/junit", "JUnit");
    java.addSubCategory(junit);
    assertEquals(java, junit.getParent());
  }

  /**
   * Tests for the addSubCategories() method.
   */
  public void testAddSubCategories() {
    Category java = new Category("/java", "Java");
    Category junit = new Category("/java/junit", "JUnit");
    java.addSubCategory(junit);
    assertEquals(1, java.getSubCategories().size());
    assertEquals(junit, java.getSubCategories().get(0));

    Category jdbc = new Category("/java/jdbc", "JDBC");
    java.addSubCategory(jdbc);
    assertEquals(2, java.getSubCategories().size());
    assertEquals(junit, java.getSubCategories().get(0));
    assertEquals(jdbc, java.getSubCategories().get(1));

    // can't add the same category twice
    java.addSubCategory(jdbc);
    assertEquals(2, java.getSubCategories().size());
    assertEquals(junit, java.getSubCategories().get(0));
    assertEquals(jdbc, java.getSubCategories().get(1));
  }

  /**
   * Tests for the removeSubCategories() method.
   */
  public void testRemoveSubCategories() {
    Category java = new Category("/java", "Java");
    Category junit = new Category("/java/junit", "JUnit");
    Category jdbc = new Category("/java/jdbc", "JDBC");
    java.addSubCategory(junit);
    java.addSubCategory(jdbc);
    assertEquals(2, java.getSubCategories().size());

    java.removeSubCategory(junit);
    assertEquals(1, java.getSubCategories().size());
    assertEquals(jdbc, java.getSubCategories().get(0));

    java.removeSubCategory(jdbc);
    assertEquals(0, java.getSubCategories().size());

    // can't remove a category that hasn't been added
    java.removeSubCategory(jdbc);
    assertEquals(0, java.getSubCategories().size());
  }

  /**
   * Tests the getPermalink() method.
   */
  public void testGetPermalink() {
    Category root = new Category("/", "All");
    root.setBlog(blog);
    Category java = new Category("/java", "Java");
    java.setBlog(blog);
    Category junit = new Category("/java/junit", "JUnit");
    junit.setBlog(blog);

    assertEquals("http://www.yourdomain.com/blog/categories/", root.getPermalink());
    assertEquals("http://www.yourdomain.com/blog/categories/java/", java.getPermalink());
    assertEquals("http://www.yourdomain.com/blog/categories/java/junit/", junit.getPermalink());
  }

  /**
   * Tests for the tags.
   */
  public void testTagsSeparatedByCommas() {
    Category root = new Category("/", "All");
    root.setBlog(blog);
    root.setTags("blog");
    Category java = new Category("/java", "Java");
    java.setBlog(blog);
    java.setTags("java");
    Category junit = new Category("/java/junit", "JUnit");
    junit.setBlog(blog);
    junit.setTags("junit, automatedunittesting");

    assertEquals("blog", root.getTags());
    assertEquals(1, root.getTagsAsList().size());
    assertEquals(blog.getTag("blog"), root.getTagsAsList().get(0));
    assertEquals("java", java.getTags());
    assertEquals(1, java.getTagsAsList().size());
    assertEquals(blog.getTag("java"), java.getTagsAsList().get(0));
    assertEquals("junit automatedunittesting", junit.getTags());
    assertEquals(2, junit.getTagsAsList().size());
    assertEquals(blog.getTag("junit"), junit.getTagsAsList().get(0));
    assertEquals(blog.getTag("automatedunittesting"), junit.getTagsAsList().get(1));

    List tags = root.getTagsAsList();
    assertEquals(tags, root.getAllTags());

    tags = java.getTagsAsList();
    tags.addAll(root.getTagsAsList());
    assertEquals(tags, java.getAllTags());

    tags = junit.getTagsAsList();
    tags.addAll(java.getTagsAsList());
    tags.addAll(root.getTagsAsList());
    assertEquals(tags, junit.getAllTags());
  }

  /**
   * Tests for the tags.
   */
  public void testTagsSeparatedByWhitespace() {
    Category root = new Category("/", "All");
    root.setBlog(blog);
    root.setTags("blog");
    Category java = new Category("/java", "Java");
    java.setBlog(blog);
    java.setTags("java");
    Category junit = new Category("/java/junit", "JUnit");
    junit.setBlog(blog);
    junit.setTags("junit automatedunittesting");

    assertEquals("blog", root.getTags());
    assertEquals(1, root.getTagsAsList().size());
    assertEquals(blog.getTag("blog"), root.getTagsAsList().get(0));
    assertEquals("java", java.getTags());
    assertEquals(1, java.getTagsAsList().size());
    assertEquals(blog.getTag("java"), java.getTagsAsList().get(0));
    assertEquals("junit automatedunittesting", junit.getTags());
    assertEquals(2, junit.getTagsAsList().size());
    assertEquals(blog.getTag("junit"), junit.getTagsAsList().get(0));
    assertEquals(blog.getTag("automatedunittesting"), junit.getTagsAsList().get(1));

    List tags = root.getTagsAsList();
    assertEquals(tags, root.getAllTags());

    tags = java.getTagsAsList();
    tags.addAll(root.getTagsAsList());
    assertEquals(tags, java.getAllTags());

    tags = junit.getTagsAsList();
    tags.addAll(java.getTagsAsList());
    tags.addAll(root.getTagsAsList());
    assertEquals(tags, junit.getAllTags());
  }

  /**
   * Tests for the isRootCategory() method.
   */
  public void testIsRootCategory() {
    Category root = new Category("/", "All");
    Category java = new Category("/java", "Java");

    assertTrue(root.isRootCategory());
    assertFalse(java.isRootCategory());
  }

  /**
   * Tests for the getNumberOfParents() method.
   */
  public void testGetNumberOfParents() {
    Category root = new Category("/", "All");
    Category java = new Category("/java", "Java");
    Category junit = new Category("/java/junit", "JUnit");
    root.addSubCategory(java);
    java.addSubCategory(junit);

    assertEquals(0, root.getNumberOfParents());
    assertEquals(1, java.getNumberOfParents());
    assertEquals(2, junit.getNumberOfParents());
  }

}