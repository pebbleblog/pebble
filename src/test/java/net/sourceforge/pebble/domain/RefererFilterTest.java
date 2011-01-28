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

import java.util.regex.Pattern;

/**
 * Tests for the RefererFilter class.
 *
 * @author    Simon Brown
 */
public class RefererFilterTest extends TestCase {

  private RefererFilter filter;

  protected void setUp() throws Exception {
    filter = new RefererFilter(".*localhost.*");
  }

  public void testConstruction() {
    assertEquals(".*localhost.*", filter.getExpression());

    Pattern pattern = Pattern.compile(".*localhost.*");
    assertEquals(pattern.pattern(), filter.getCompiledExpression().pattern());
  }

  public void testCreatingFilterWithNullExpression() {
    filter = new RefererFilter(null);
    assertEquals("", filter.getExpression());
  }

  public void testHashCode() {
    assertEquals(".*localhost.*".hashCode(), filter.hashCode());
  }

  public void testEquals() {
    RefererFilter filter1 = new RefererFilter(".*localhost.*");
    RefererFilter filter2 = new RefererFilter(".*localhost.*");

    assertTrue(filter1.equals(filter2));
    assertFalse(filter1.equals(null));
    assertFalse(filter1.equals("some other object"));
  }

  public void testComparisonOfFilters() {
    RefererFilter filter1 = new RefererFilter(".*localhost.*");
    RefererFilter filter2 = new RefererFilter(".*mocalhost.*");

    assertTrue(filter1.compareTo(filter1) == 0);
    assertTrue(filter1.compareTo(filter2) < 0);
    assertTrue(filter2.compareTo(filter1) > 0);
  }

  public void testId() {
    RefererFilter filter = new RefererFilter(".*localhost.*");
    filter.setId(123);
    assertEquals(123, filter.getId());
  }

}
