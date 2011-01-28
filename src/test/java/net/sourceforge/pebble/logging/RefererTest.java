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
package net.sourceforge.pebble.logging;

import junit.framework.TestCase;

/**
 * Tests for the Referer class.
 *
 * @author    Simon Brown
 */
public class RefererTest extends TestCase {

  private Referer url;

  protected void setUp() throws Exception {
    url = new Referer("http://www.somedomain.com");
    url.addLogEntry(new LogEntry());
  }

  public void testConstruction() {
    assertEquals("http://www.somedomain.com", url.getName());
    assertEquals("http://www.somedomain.com", url.getUrl());
    assertEquals(1, url.getCount());
  }

  public void testIncrementingCount() {
    assertEquals(1, url.getCount());
    url.addLogEntry(new LogEntry());
    assertEquals(2, url.getCount());
  }

  public void testShortUrlIsNotTruncated() {
    String s = "http://www.somedomain.com";
    url = new Referer(s);
    assertEquals("http://www.somedomain.com", url.getTruncatedName());
  }

  public void testLongUrlIsTruncated() {
    String s = "http://www.somedomain.com/here/is/a/long/url/abcdefghijklmnopqrstuvwxyz012345678012345678901234567890123456789";
    url = new Referer(s);
    assertEquals(s.substring(0, Referer.NAME_LENGTH_LIMIT - 3) + "...", url.getTruncatedName());
  }

  public void testEmptyUrl() {
    url = new Referer("");
    assertEquals("", url.getUrl());
    assertEquals("None", url.getName());
  }

  public void testNullUrl() {
    url = new Referer(null);
    assertEquals(null, url.getUrl());
    assertEquals("None", url.getName());
  }

  public void testHashCode() {
    url = new Referer(null);
    assertEquals(0, url.hashCode());
    url = new Referer("http://www.somedomain.com");
    assertEquals("http://www.somedomain.com".hashCode(), url.hashCode());
  }

  public void testEquals() {
    Referer url1 = new Referer("http://www.somedomain.com");
    Referer url2 = new Referer("http://www.yahoo.com");
    Referer url3 = new Referer(null);
    Referer url4 = new Referer(null);

    assertFalse(url1.equals(null));
    assertFalse(url1.equals("http://www.somedomain.com"));
    assertTrue(url1.equals(url1));
    assertFalse(url1.equals(url2));
    assertFalse(url2.equals(url1));
    assertFalse(url1.equals(url3));
    assertFalse(url3.equals(url1));
    assertTrue(url3.equals(url4));
  }

  public void testFriendlyNamesForGoogleSearchUrls() {
    url = new Referer("http://www.google.com");
    assertEquals("Google : ", url.getName());
    url = new Referer("http://www.GOOGLE.com");
    assertEquals("Google : ", url.getName());
    url = new Referer("http://www.google.com.au");
    assertEquals("Google : ", url.getName());
    url = new Referer("http://www.google.com?q=some+search+term");
    assertEquals("Google : some search term", url.getName());
    url = new Referer("http://www.google.com?q=some+search+term&abc=123");
    assertEquals("Google : some search term", url.getName());
    url = new Referer("http://www.google.com?abc=123&q=some+search+term&xyz=456");
    assertEquals("Google : some search term", url.getName());
    url = new Referer("http://www.google.com?q=%22some+search+term%22");
    assertEquals("Google : \"some search term\"", url.getName());
  }

  public void testFriendlyNamesForGoogleImageSearchUrls() {
    url = new Referer("http://images.google.com/imgres?imgurl=http://www.wingedsheep.com/images/climbing/birdsboro/20060402_JessFav2VV3W2238.jpg&imgrefurl=http://www.wingedsheep.com/tags/outdoor&h=450&w=300&sz=34&hl=en&start=1&tbnid=3YhO1f-n_UmSsM:&tbnh=127&tbnw=85&prev=/images%3Fq%3Dclimbing%2Bbirdsboro%26svnum%3D10%26hl%3Den");
    assertEquals("Google Images : climbing birdsboro", url.getName());
    url = new Referer("http://images.google.com/imgres?imgurl=http://www.wingedsheep.com/images/knitting/20060403_ragRugCatScratch.jpg&imgrefurl=http://www.wingedsheep.com/2006/04.html&h=300&w=300&sz=52&hl=en&start=57&tbnid=--uD5ySwWKcHGM:&tbnh=116&tbnw=116&prev=/images%3Fq%3Drag%2Brug%26start%3D40%26ndsp%3D20%26svnum%3D10%26hl%3Den%26sa%3DN");
    assertEquals("Google Images : rag rug", url.getName());
  }

  public void testFriendlyNamesForYahooSearchUrls() {
    url = new Referer("http://search.yahoo.com");
    assertEquals("Yahoo! : ", url.getName());
    url = new Referer("http://search.YAHOO.com");
    assertEquals("Yahoo! : ", url.getName());
    url = new Referer("http://search.yahoo.co.uk");
    assertEquals("Yahoo! : ", url.getName());
    url = new Referer("http://search.yahoo.com?p=some+search+term");
    assertEquals("Yahoo! : some search term", url.getName());
    url = new Referer("http://search.yahoo.com?p=some+search+term&abc=123");
    assertEquals("Yahoo! : some search term", url.getName());
    url = new Referer("http://search.yahoo.com?abc=123&p=some+search+term&xyz=456");
    assertEquals("Yahoo! : some search term", url.getName());
    url = new Referer("http://search.yahoo.com?p=%22some+search+term%22");
    assertEquals("Yahoo! : \"some search term\"", url.getName());
  }

  public void testFriendlyNamesForBingSearchUrls() {
    url = new Referer("http://www.bing.com");
    assertEquals("Bing : ", url.getName());
    url = new Referer("http://www.BING.com");
    assertEquals("Bing : ", url.getName());
    url = new Referer("http://www.bing.com?q=some+search+term");
    assertEquals("Bing : some search term", url.getName());
    url = new Referer("http://www.bing.com?q=some+search+term&abc=123");
    assertEquals("Bing : some search term", url.getName());
    url = new Referer("http://www.bing.com?abc=123&q=some+search+term&xyz=456");
    assertEquals("Bing : some search term", url.getName());
    url = new Referer("http://www.bing.com?q=%22some+search+term%22");
    assertEquals("Bing : \"some search term\"", url.getName());
  }
  public void testFriendlyNamesForJavaBlogsUrls() {
    url = new Referer("http://www.javablogs.com/Welcome.action");
    assertEquals("java.blogs : Welcome", url.getName());
    url = new Referer("http://javablogs.com/Welcome.action");
    assertEquals("java.blogs : Welcome", url.getName());
    url = new Referer("http://www.javablogs.com/Welcome.jspa");
    assertEquals("java.blogs : Welcome", url.getName());
    url = new Referer("http://javablogs.com/Welcome.jspa");
    assertEquals("java.blogs : Welcome", url.getName());
    url = new Referer("http://www.javablogs.com/ViewHotBlogEntries.action;jsessionid=MHDGMNPJGIHD");
    assertEquals("java.blogs : Hot Entries", url.getName());
  }

  public void testDomainFilter() {
    assertEquals(".*www.somedomain.com.*", url.getDomainFilter());
  }

  public void testRefererNotInUTF8() {
    url = new Referer("http://www.google.com/search?num=50&lr=lang_ja&q=%u30EF%u30FC%u30EB%u30C9%u30E1%u30A4%u30C8%u554F%u984C&ie=Shift_JIS&oe=Shift_JIS");
    assertEquals("Google : %u30EF%u30FC%u30EB%u30C9%u30E1%u30A4%u30C8%u554F%u984C", url.getName());
  }

  public void testFakeRefererDoesntBreak() {
    url = new Referer("0");
    assertEquals("0", url.getName());
    assertEquals("0", url.getDomainFilter());
  }

}
