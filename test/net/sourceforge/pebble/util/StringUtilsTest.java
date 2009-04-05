/*
 * Copyright (c) 2003-2006, Simon Brown
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
package net.sourceforge.pebble.util;

import junit.framework.TestCase;

/**
 * Tests for the utilities in the StringUtils class.
 *
 * @author    Simon Brown
 */
public class StringUtilsTest extends TestCase {

  public void testTransformHTML() {
    assertNull(StringUtils.transformHTML(null));
    assertEquals("Here is some text", StringUtils.transformHTML("Here is some text"));
    assertEquals("Here is a &lt; symbol", StringUtils.transformHTML("Here is a < symbol"));
    assertEquals("Here is a &gt; symbol", StringUtils.transformHTML("Here is a > symbol"));
    assertEquals("Here is a &amp; symbol", StringUtils.transformHTML("Here is a & symbol"));
    assertEquals("&lt;a href=&quot;http://www.google.com&quot;&gt;Google&lt;/a&gt;", StringUtils.transformHTML("<a href=\"http://www.google.com\">Google</a>"));
  }

  public void testTransformToHTMLSubset() {
    assertNull(StringUtils.transformToHTMLSubset(null));
    assertEquals("Here is some text", StringUtils.transformToHTMLSubset("Here is some text"));
    assertEquals("Here is a &lt; symbol", StringUtils.transformToHTMLSubset("Here is a &amp;lt; symbol"));
    assertEquals("Here is a &lt; symbol", StringUtils.transformToHTMLSubset("Here is a &lt; symbol"));
    assertEquals("Here is a &gt; symbol", StringUtils.transformToHTMLSubset("Here is a &amp;gt; symbol"));
    assertEquals("Here is a &gt; symbol", StringUtils.transformToHTMLSubset("Here is a &gt; symbol"));
    assertEquals("Here is a <b> tag", StringUtils.transformToHTMLSubset("Here is a &lt;b&gt; tag"));
    assertEquals("Here is a <strong> tag", StringUtils.transformToHTMLSubset("Here is a &lt;strong&gt; tag"));
    assertEquals("Here is a <i> tag", StringUtils.transformToHTMLSubset("Here is a &lt;i&gt; tag"));
    assertEquals("Here is a <em> tag", StringUtils.transformToHTMLSubset("Here is a &lt;em&gt; tag"));
    assertEquals("Here is a <p> tag", StringUtils.transformToHTMLSubset("Here is a &lt;p&gt; tag"));
    assertEquals("Here is a </p> tag", StringUtils.transformToHTMLSubset("Here is a &lt;/p&gt; tag"));
    assertEquals("Here is a <br /> tag", StringUtils.transformToHTMLSubset("Here is a &lt;br&gt; tag"));
    assertEquals("Here is a <br /> tag", StringUtils.transformToHTMLSubset("Here is a &lt;br/&gt; tag"));
    assertEquals("Here is a <br /> tag", StringUtils.transformToHTMLSubset("Here is a &lt;br /&gt; tag"));
    assertEquals("Here is a <pre> tag", StringUtils.transformToHTMLSubset("Here is a &lt;pre&gt; tag"));
    assertEquals("Here is a </pre> tag", StringUtils.transformToHTMLSubset("Here is a &lt;/pre&gt; tag"));
    assertEquals("Here is a <a href=\"http://www.google.com\">link</a> to Google", StringUtils.transformToHTMLSubset("Here is a &lt;a href=\"http://www.google.com\"&gt;link&lt;/a&gt; to Google"));
    assertEquals("Here is a <a href=\"http://www.google.com\">link</a> to Google and another <a href=\"http://www.google.com\">link</a>", StringUtils.transformToHTMLSubset("Here is a &lt;a href=\"http://www.google.com\"&gt;link&lt;/a&gt; to Google and another &lt;a href=\"http://www.google.com\"&gt;link&lt;/a&gt;"));
    assertEquals("Here is a <a href='http://www.google.com'>link</a> to Google", StringUtils.transformToHTMLSubset("Here is a &lt;a href='http://www.google.com'&gt;link&lt;/a&gt; to Google"));
    assertEquals("Here is a <a href='http://www.google.com'>link</a> to Google and another <a href='http://www.google.com'>link</a>", StringUtils.transformToHTMLSubset("Here is a &lt;a href='http://www.google.com'&gt;link&lt;/a&gt; to Google and another &lt;a href='http://www.google.com'&gt;link&lt;/a&gt;"));
    assertEquals("Here is a <a href=\"http://www.google.com\">link</a> to Google", StringUtils.transformToHTMLSubset("Here is a &lt;a href=\"http://www.google.com\" target=\"_blank\"&gt;link&lt;/a&gt; to Google"));
    assertEquals("Here is a &lt;script&gt; tag", StringUtils.transformToHTMLSubset("Here is a &lt;script&gt; tag"));
    assertEquals("Here is a <blockquote> tag", StringUtils.transformToHTMLSubset("Here is a &lt;blockquote&gt; tag"));
    assertEquals("Here is a </blockquote> tag", StringUtils.transformToHTMLSubset("Here is a &lt;/blockquote&gt; tag"));
    assertEquals("Here is a <a href=\"mailto:somebody@somedomain.com\">mail link</a>", StringUtils.transformToHTMLSubset("Here is a &lt;a href=\"mailto:somebody@somedomain.com\"&gt;mail link&lt;/a&gt;"));
    assertEquals("Here is a &#8217; character", StringUtils.transformToHTMLSubset("Here is a &amp;#8217; character"));
    assertEquals("Here is a &quot; symbol", StringUtils.transformToHTMLSubset("Here is a &amp;quot; symbol"));
    assertEquals("&uacute;", StringUtils.transformToHTMLSubset("&amp;uacute;"));
    assertEquals("see contracts as &quot;fly-by-night&quot; sorts", StringUtils.transformToHTMLSubset("see contracts as &amp;quot;fly-by-night&amp;quot; sorts"));
    assertEquals("Here is a <sup> tag", StringUtils.transformToHTMLSubset("Here is a &lt;sup&gt; tag"));
    assertEquals("Here is a <sub> tag", StringUtils.transformToHTMLSubset("Here is a &lt;sub&gt; tag"));
  }

  public void testFilterNewLines() {
    assertNull(StringUtils.filterNewlines(null));
    assertEquals("Here is some text", StringUtils.filterNewlines("Here is some text"));
    assertEquals("Line 1\n", StringUtils.filterNewlines("Line 1\r\n"));
    assertEquals("Line 1\nLine2", StringUtils.filterNewlines("Line 1\r\nLine2"));
  }

  public void testFilterHTML() {
    assertEquals("Here is some text.", StringUtils.filterHTML("Here is <!-- <rdf>...</rdf> -->some text."));
    assertEquals("Here is some text.", StringUtils.filterHTML("Here is <!-- <rdf/> -->some text."));
    assertEquals("Here is some text.", StringUtils.filterHTML("<b>Here</b> is <i>some</i> text."));
    assertEquals("Here is a link.", StringUtils.filterHTML("Here is <a href=\"http://www.google.com\">a link</a>."));
    assertEquals("Here is a link.", StringUtils.filterHTML("Here is <a \nhref=\"http://www.google.com\">a link</a>."));
    assertEquals("Here is some text", StringUtils.filterHTML("Here is &lt;some&gt; text"));
  }

  public void testStripScriptTags() {
    assertEquals("some text", StringUtils.stripScriptTags("some <script>alert(1)</script>text"));
    assertEquals("some text", StringUtils.stripScriptTags("some <script >alert(1)</script>text"));
    assertEquals("some text", StringUtils.stripScriptTags("some <script >alert(1)</script >text"));
    assertEquals("some text", StringUtils.stripScriptTags("some <script language=\"JavaScript\">alert(1)</script >text"));
    assertEquals("some text", StringUtils.stripScriptTags("some <script src=\"something.js\"/>text"));
  }


   public void testUnescapeHTMLEntities() {
    assertEquals("\u00A0", StringUtils.unescapeHTMLEntities("&nbsp;"));
	assertEquals("\u00A0\u00A0nbsp\u00A0\u00A0", StringUtils.unescapeHTMLEntities("&nbsp;&nbsp;nbsp&nbsp;&nbsp;"));
	assertEquals("\u00E7", StringUtils.unescapeHTMLEntities("&ccedil;"));
	assertEquals("\u00C7", StringUtils.unescapeHTMLEntities("&Ccedil;"));
	assertEquals("\u03BC\u03BD\u039E", StringUtils.unescapeHTMLEntities("&mu;&nu;&Xi;"));
    assertEquals("Seven \u00D0 and eight \u00A3 \u00E7 5 \u2666", StringUtils.unescapeHTMLEntities("Seven &ETH; and eight &pound; &ccedil; 5 &diams;"));
  }

}
