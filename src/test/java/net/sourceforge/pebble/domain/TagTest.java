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
 * Tests for the Tag class.
 *
 * @author    Simon Brown
 */
public class TagTest extends SingleBlogTestCase {

  private Tag tag;

  protected void setUp() throws Exception {
    super.setUp();

    tag = new Tag("java", blog);
  }

  public void testConstruction() {
    assertEquals("java", tag.getName());

    assertEquals("http://www.yourdomain.com/blog/tags/java/", tag.getPermalink());
  }

  public void testNameWithSpaces() {
    tag.setName("automated+unit+testing");
    assertEquals("automated unit testing", tag.getName());

    assertEquals("http://www.yourdomain.com/blog/tags/automated unit testing/", tag.getPermalink());
  }

  public void testNameWithMixedCase() {
    tag.setName("automatedUnitTesting");
    assertEquals("automatedunittesting", tag.getName());

    assertEquals("http://www.yourdomain.com/blog/tags/automatedunittesting/", tag.getPermalink());
  }

  public void testParse() {
    assertTrue(Tag.parse(blog, null).isEmpty());

    List tags = Tag.parse(blog, "java junit automated+unit+testing java");
    assertEquals(3, tags.size());
    assertTrue(tags.contains(blog.getTag("java")));
    assertTrue(tags.contains(blog.getTag("junit")));
    assertTrue(tags.contains(blog.getTag("automated unit testing")));
  }

  public void testFormat() {
    assertEquals("", Tag.format(null));

    List tags = Tag.parse(blog, "java junit automatedunittesting java");
    assertEquals("java, junit, automatedunittesting", Tag.format(tags));
  }

  public void testEncode() {
    assertEquals("", Tag.encode(null));
    assertEquals("sometag", Tag.encode("sometag"));
    assertEquals("sometag", Tag.encode(" sometag "));
    assertEquals("someothertag", Tag.encode("SomeOtherTag"));
    assertEquals("some other tag", Tag.encode("Some+Other+Tag"));
  }

}