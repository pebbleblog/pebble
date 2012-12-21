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

import junit.framework.TestCase;

import java.util.Date;

/**
 * Tests for the SearchResults class.
 *
 * @author    Simon Brown
 */
public class SearchResultsTest extends TestCase {

  private SearchResults results;

  protected void setUp() {
    results = new SearchResults();
  }

  /**
   * Tests the query property.
   */
  public void testQuery() {
    results.setQuery("some query");
    assertEquals("some query", results.getQuery());
  }

  /**
   * Tests the message property.
   */
  public void testMessage() {
    results.setMessage("A message");
    assertEquals("A message", results.getMessage());
  }

  /**
   * Tests the hits.
   */
  public void testHits() {
    assertEquals(0, results.getNumberOfHits());
    assertTrue(results.getHits().isEmpty());

    // and add a hit
    SearchHit hit1 = new SearchHit(null, "id", "alink", "A Title", "A Subtitle", "An excerpt", new Date(), 0.123f);
    results.add(hit1);
    assertEquals(1, results.getNumberOfHits());
    assertFalse(results.getHits().isEmpty());
    assertEquals(hit1, results.getHits().get(0));

    // and add another hit
    SearchHit hit2 = new SearchHit(null, "id2", "alink2", "A Title2", "A Subitle2", "An excerpt2", new Date(), 0.456f);
    results.add(hit2);
    assertEquals(2, results.getNumberOfHits());
    assertFalse(results.getHits().isEmpty());
    assertEquals(hit1, results.getHits().get(0));
    assertEquals(hit2, results.getHits().get(1));
  }

  public void testSortByScore() {
    SearchHit hit1 = new SearchHit(null, "id1", "alink1", "A Title1", "A Title1", "An excerpt1", new Date(), 0.123f);
    results.add(hit1);
    SearchHit hit2 = new SearchHit(null, "id2", "alink2", "A Title2", "A Subtitle2", "An excerpt2", new Date(), 0.456f);
    results.add(hit2);
    SearchHit hit3 = new SearchHit(null, "id3", "alink3", "A Title2", "A Subtitle2", "An excerpt2", new Date(), 0.789f);
    results.add(hit3);

    results.sortByScoreDescending();
    assertEquals(hit3, results.getHits().get(0));
    assertEquals(hit2, results.getHits().get(1));
    assertEquals(hit1, results.getHits().get(2));
  }

  public void testSortByDate() {
    SearchHit hit1 = new SearchHit(null, "id1", "alink1", "A Title1", "A Subtitle1", "An excerpt1", new Date(123), 0.123f);
    results.add(hit1);
    SearchHit hit2 = new SearchHit(null, "id2", "alink2", "A Title2", "A Subtitle2", "An excerpt2", new Date(456), 0.456f);
    results.add(hit2);
    SearchHit hit3 = new SearchHit(null, "id3", "alink3", "A Title2", "A Subtitle2", "An excerpt2", new Date(789), 0.789f);
    results.add(hit3);

    results.sortByDateDescending();
    assertEquals(hit3, results.getHits().get(0));
    assertEquals(hit2, results.getHits().get(1));
    assertEquals(hit1, results.getHits().get(2));
  }

}
