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

package net.sourceforge.pebble.index;

import net.sourceforge.pebble.comparator.ReverseResponseIdComparator;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogEntry;
import net.sourceforge.pebble.domain.Response;
import net.sourceforge.pebble.domain.State;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Collection;

/**
 * Keeps an index of all responses.
 *
 * @author    Simon Brown
 */
public class ResponseIndex {

  private static final Log log = LogFactory.getLog(ResponseIndex.class);

  private Blog blog;

  private List<String> approvedResponses = new ArrayList<String>();
  private List<String> pendingResponses = new ArrayList<String>();
  private List<String> rejectedResponses = new ArrayList<String>();

  public ResponseIndex(Blog blog) {
    this.blog = blog;

    approvedResponses = readIndex(State.APPROVED);
    pendingResponses = readIndex(State.PENDING);
    rejectedResponses = readIndex(State.REJECTED);
  }

  /**
   * Clears the index.
   */
  public void clear() {
    approvedResponses = new ArrayList<String>();
    writeIndex(State.APPROVED);

    pendingResponses = new ArrayList<String>();
    writeIndex(State.PENDING);

    rejectedResponses = new ArrayList<String>();
    writeIndex(State.REJECTED);
  }

  /**
   * Indexes one or more blog entries.
   *
   * @param blogEntries   a List of BlogEntry instances
   */
  public synchronized void index(Collection<BlogEntry> blogEntries) {
    for (BlogEntry blogEntry : blogEntries) {
      for (Response response : blogEntry.getResponses()) {
        if (response.isApproved()) {
          approvedResponses.add(response.getGuid());
        } else if (response.isPending()) {
          pendingResponses.add(response.getGuid());
        } else if (response.isRejected()) {
          rejectedResponses.add(response.getGuid());
        }
      }
    }

    Collections.sort(approvedResponses, new ReverseResponseIdComparator());
    Collections.sort(pendingResponses, new ReverseResponseIdComparator());
    Collections.sort(rejectedResponses, new ReverseResponseIdComparator());
    writeIndex(State.APPROVED);
    writeIndex(State.PENDING);
    writeIndex(State.REJECTED);
  }

  /**
   * Indexes a single response.
   *
   * @param response    a Response instance
   */
  public synchronized void index(Response response) {
    if (response.isApproved()) {
      approvedResponses.add(response.getGuid());
      Collections.sort(approvedResponses, new ReverseResponseIdComparator());
      writeIndex(State.APPROVED);
    } else if (response.isPending()) {
      pendingResponses.add(response.getGuid());
      Collections.sort(pendingResponses, new ReverseResponseIdComparator());
      writeIndex(State.PENDING);
    } else if (response.isRejected()) {
      rejectedResponses.add(response.getGuid());
      Collections.sort(rejectedResponses, new ReverseResponseIdComparator());
      writeIndex(State.REJECTED);
    }
  }

  /**
   * Unindexes a single response.
   *
   * @param response    a Response instance
   */
  public synchronized void unindex(Response response) {
    if (approvedResponses.contains(response.getGuid())) {
      approvedResponses.remove(response.getGuid());
      writeIndex(State.APPROVED);
    } else if (pendingResponses.contains(response.getGuid())) {
      pendingResponses.remove(response.getGuid());
      writeIndex(State.PENDING);
    } else if (rejectedResponses.contains(response.getGuid())) {
      rejectedResponses.remove(response.getGuid());
      writeIndex(State.REJECTED);
    }
  }

  /**
   * Helper method to load the index.
   */
  private List<String> readIndex(State state) {
    String filename = null;
    List<String> responses = new ArrayList<String>();
    if (state == State.APPROVED) {
        filename = "responses-approved.index";
    } else if (state == State.PENDING) {
      filename = "responses-pending.index";
    } else if (state == State.REJECTED) {
      filename = "responses-rejected.index";
    }

    File indexFile = new File(blog.getIndexesDirectory(), filename);
    if (indexFile.exists()) {
      try {
        BufferedReader reader = new BufferedReader(new FileReader(indexFile));
        String response = reader.readLine();
        while (response != null) {
          responses.add(response);
          response = reader.readLine();
        }

        reader.close();
      } catch (Exception e) {
        log.error("Error while reading index", e);
      }
    }

    Collections.sort(responses, new ReverseResponseIdComparator());

    return responses;
  }

  /**
   * Helper method to write out the index to disk.
   */
  private void writeIndex(State state) {
    String filename = null;
    List<String> responses = null;
    if (state == State.APPROVED) {
        filename = "responses-approved.index";
        responses = approvedResponses;
    } else if (state == State.PENDING) {
      filename = "responses-pending.index";
      responses = pendingResponses;
    } else if (state == State.REJECTED) {
      filename = "responses-rejected.index";
      responses = rejectedResponses;
    }

    try {
      File indexFile = new File(blog.getIndexesDirectory(), filename);
      BufferedWriter writer = new BufferedWriter(new FileWriter(indexFile));

      for (String response : responses) {
        writer.write(response);
        writer.newLine();
      }

      writer.flush();
      writer.close();
    } catch (Exception e) {
      log.error("Error while writing index", e);
    }
  }

  /**
   * Gets the number of approved responses for this blog.
   *
   * @return  an int
   */
  public int getNumberOfApprovedResponses() {
    return approvedResponses.size();
  }

  /**
   * Gets the number of pending responses for this blog.
   *
   * @return  an int
   */
  public int getNumberOfPendingResponses() {
    return pendingResponses.size();
  }

  /**
   * Gets the number of rejected responses for this blog.
   *
   * @return  an int
   */
  public int getNumberOfRejectedResponses() {
    return rejectedResponses.size();
  }

  /**
   * Gets the number of responses for this blog.
   *
   * @return  an int
   */
  public int getNumberOfResponses() {
    return getNumberOfApprovedResponses() + getNumberOfPendingResponses() + getNumberOfRejectedResponses();
  }

  /**
   * Gets the most recent N approved responses.
   *
   * @return  a List of response IDs
   */
  public List<String> getRecentResponses(int number) {
    return getRecentApprovedResponses(number);
  }

  /**
   * Gets the most recent N approved responses.
   *
   * @return  a List of response IDs
   */
  public List<String> getRecentApprovedResponses(int number) {
    if (approvedResponses.size() >= number) {
      return approvedResponses.subList(0, number);
    } else {
      return approvedResponses;
    }
  }

  /**
   * Gets the list of approved responses.
   *
   * @return  a List of response IDs
   */
  public List<String> getApprovedResponses() {
    return new ArrayList<String>(approvedResponses);
  }

  /**
   * Gets the list of pending responses.
   *
   * @return  a List of response IDs
   */
  public List<String> getPendingResponses() {
    return new ArrayList<String>(pendingResponses);
  }

  /**
   * Gets the list of rejected responses.
   *
   * @return  a List of response IDs
   */
  public List<String> getRejectedResponses() {
    return new ArrayList<String>(rejectedResponses);
  }

}