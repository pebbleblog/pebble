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
package net.sourceforge.pebble.comparator;

import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.SingleBlogTestCase;

/**
 * Tests for the BlogEntryByTitleComparator class.
 *
 * @author    Simon Brown
 */
public class BlogEntryByTitleComparatorTest extends SingleBlogTestCase {

  public void testCompare() {
    PageBasedContentByTitleComparator comp = new PageBasedContentByTitleComparator();

    BlogEntry b1 = new BlogEntry(blog);
    b1.setTitle("A title");
    BlogEntry b2 = new BlogEntry(blog);
    b2.setTitle("B title");

    assertTrue(comp.compare(b1, b1) == 0);
    assertTrue(comp.compare(b1, b2) != 0);
    assertTrue(comp.compare(b1, b2) < 0);
    assertTrue(comp.compare(b2, b1) > 0);
  }

  public void testCompareIgnoreCase() {
    PageBasedContentByTitleComparator comp = new PageBasedContentByTitleComparator();

    BlogEntry b1 = new BlogEntry(blog);
    b1.setTitle("a title");
    BlogEntry b2 = new BlogEntry(blog);
    b2.setTitle("B title");

    assertTrue(comp.compare(b1, b1) == 0);
    assertTrue(comp.compare(b1, b2) != 0);
    assertTrue(comp.compare(b1, b2) < 0);
    assertTrue(comp.compare(b2, b1) > 0);
  }

}
