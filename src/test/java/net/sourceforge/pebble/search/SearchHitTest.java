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
package net.sourceforge.pebble.search;

import net.sourceforge.pebble.domain.SingleBlogTestCase;

import java.util.Date;

/**
 * Tests for the SearchHit class.
 *
 * @author    Simon Brown
 */
public class SearchHitTest extends SingleBlogTestCase {

  /**
   * Tests construction of SearchHit instances.
   */
  public void testConstruction() {
    Date date = new Date();

    SearchHit hit = new SearchHit(
        blog,
        "123456789",
        "http://www.simongbrown.com/blog/2003/05/01.html#a123456789",
        "A blog entry title",
        "A blog entry subtitle",
        "Some excerpt...",
        date,
        0.789f);

    assertEquals(blog, hit.getBlog());
    assertEquals("123456789", hit.getId());
    assertEquals("http://www.simongbrown.com/blog/2003/05/01.html#a123456789", hit.getPermalink());
    assertEquals("A blog entry title", hit.getTitle());
    assertEquals("Some excerpt...", hit.getExcerpt());
    assertEquals(date, hit.getDate());
    assertEquals(0.789f, hit.getScore(), 0);
    hit.setNumber(10);
    assertEquals(10, hit.getNumber());
  }

}
