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

import net.sourceforge.pebble.comparator.SearchHitByDateComparator;
import net.sourceforge.pebble.comparator.SearchHitByScoreComparator;
import net.sourceforge.pebble.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A container for the results (hits) of a search.
 *
 * @author    Simon Brown
 */
public class SearchResults {

  /** the original query */
  private String query;

  /** an optional message */
  private String message;

  /** the collection of search results */
  private List hits = new ArrayList();

  /**
   * Gets the query that was used to generate these results.
   *
   * @return  the original query
   */
  public String getQuery() {
    return this.query;
  }

  /**
   * Sets the query that was used to generate these results.
   *
   * @param s   the original query
   */
  public void setQuery(String s) {
    this.query = StringUtils.transformHTML(s);
  }

  /**
   * Gets the message associated with these results. This may contain
   * information or an error message.
   *
   * @return  the message (can be null)
   */
  public String getMessage() {
    return this.message;
  }

  /**
   * Gets the message associated with these results. This may contain
   * information or an error message.
   *
   * @param s   the message (can be null)
   */
  public void setMessage(String s) {
    this.message = s;
  }

  /**
   * Adds a hit to the collection of hits.
   *
   * @param hit   a SearchHit instance
   */
  public void add(SearchHit hit) {
    hits.add(hit);
  }

  /**
   * Gets the number of hits that the query returned.
   *
   * @return  the number of hits as an int
   */
  public int getNumberOfHits() {
    return hits.size();
  }

  /**
   * Gets a collection containing all of the hits.
   *
   * @return  a Collection of SearchHit instances
   */
  public List getHits() {
    return this.hits;
  }

  /**
   * Sorts the search results by score, in reverse order.
   */
  public void sortByScoreDescending() {
    Collections.sort(hits, new SearchHitByScoreComparator());

    int number = 1;
    Iterator it = hits.iterator();
    while (it.hasNext()) {
      SearchHit hit = (SearchHit)it.next();
      hit.setNumber(number);
      number++;
    }
  }

  /**
   * Sorts the search results by date, in reverse order.
   */
  public void sortByDateDescending() {
    Collections.sort(hits, new SearchHitByDateComparator());

    int number = 1;
    Iterator it = hits.iterator();
    while (it.hasNext()) {
      SearchHit hit = (SearchHit)it.next();
      hit.setNumber(number);
      number++;
    }
  }

}
