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

import junit.framework.TestCase;

/**
 * Tests for the Category class.
 *
 * @author    Simon Brown
 */
public class BlogCategoryTest extends TestCase {

  /**
   * Test that a Category instance can be created correctly.
   */
  public void testConstructionOfSimpleInstance() {
    Category blogCategory = new Category("/testCategory1", "Test Category 1");
    assertEquals("/testCategory1", blogCategory.getId());
    assertEquals("Test Category 1", blogCategory.getName());
  }

  /**
   * Tests that blog categories are comparable.
   */
  public void testCategoriesAreComparable() {
    Category blogCategory1 = new Category("/testCategory1", "Test Category 1");
    Category blogCategory2 = new Category("/testCategory2", "Test Category 2");
    assertEquals(-1, blogCategory1.compareTo(blogCategory2));
    assertEquals(0, blogCategory1.compareTo(blogCategory1));
    assertEquals(1, blogCategory2.compareTo(blogCategory1));
  }

  /**
   * Tests that categories can be tested for equality.
   */
  public void testCategoriesCanBeTestedForEquality() {
    Category blogCategory1 = new Category("/testCategory1", "Test Category 1");
    Category blogCategory2 = new Category("/testCategory1", "Test Category 1");

    assertTrue(blogCategory1.equals(blogCategory1));
    assertTrue(blogCategory1.equals(blogCategory2));
    assertTrue(blogCategory2.equals(blogCategory1));

    blogCategory2 = new Category("/testCategory2", "Test Category 2");
    assertFalse(blogCategory1.equals(blogCategory2));
    assertFalse(blogCategory2.equals(blogCategory1));

    assertFalse(blogCategory1.equals("A String Object"));
  }

}
