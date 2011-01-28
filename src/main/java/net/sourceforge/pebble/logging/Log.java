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

import net.sourceforge.pebble.domain.Blog;

import java.util.*;

/**
 * Represents a log, containing log entries.
 *
 * @author    Simon Brown
 */
public class Log {

  /** the blog that this instance is associated with */
  protected Blog blog;

  /** the collection of log entries */
  private Collection<LogEntry> logEntries;

  /**
   * Creates a new log associated with the given blog.
   *
   * @param blog          a Blog instance
   * @param entries       a Collection of LogEntry objects
   */
  Log(Blog blog, Collection<LogEntry> entries) {
    this.blog = blog;
    this.logEntries = entries;

    if (logEntries == null) {
      logEntries = new ArrayList<LogEntry>();
    }
  }

  /**
   * Gets all log entries..
   *
   * @return    a collection of LogEntry instances
   */
  public Collection<LogEntry> getLogEntries() {
    return Collections.unmodifiableCollection(logEntries);
  }

  /**
   * Gets a list of referers.
   *
   * @return    a Collection of Referer instances
   */
  public Collection<Referer> getReferers() {
    Map<String,Referer> refererMap = new HashMap<String,Referer>();
    for (LogEntry logEntry : logEntries) {
      Referer referer = refererMap.get(logEntry.getReferer());
      if (referer == null) {
        referer = new Referer(logEntry.getReferer());
        refererMap.put(referer.getName(), referer);
      }

      referer.addLogEntry(logEntry);
    }
    return refererMap.values();
  }

  /**
   * Gets a list of referers.
   *
   * @return    a Collection of Request instances
   */
  public Collection<Request> getRequests() {
    Map<String,Request> requestMap = new HashMap<String,Request>();
    for (LogEntry logEntry : logEntries) {
      Request request = requestMap.get(logEntry.getRequestUri());
      if (request == null) {
        request = new Request(logEntry.getRequestUri(), blog);
        requestMap.put(logEntry.getRequestUri(), request);
      }
      request.addLogEntry(logEntry);
    }
    return requestMap.values();
  }

  /**
   * Gets the total number of entries.
   *
   * @return  the total number as an int
   */
  public int getTotalLogEntries() {
    return logEntries.size();
  }

  /**
   * Adds an entry to this log.
   *
   * @param logEntry    a LogEntry instance
   */
  void addLogEntry(LogEntry logEntry) {
    logEntries.add(logEntry);
  }

  /**
   * Adds a collection of entries to this log.
   *
   * @param entries   a Collection of LogEntry instances
   */
  void addLogEntries(Collection<LogEntry> entries) {
    logEntries.addAll(entries);
  }

}