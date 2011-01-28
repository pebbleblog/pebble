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

import net.sourceforge.pebble.domain.SingleBlogTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the Log class.
 *
 * @author Simon Brown
 */
public class LogTest extends SingleBlogTestCase {

  private Log log;

  protected void setUp() throws Exception {
    super.setUp();

    this.log = new Log(blog, null);
  }

  public void testEmptyLog() {
    assertTrue(log.getRequests().isEmpty());
    assertTrue(log.getReferers().isEmpty());
    assertEquals(0, log.getTotalLogEntries());
    assertTrue(log.getLogEntries().isEmpty());
  }

  public void testAddLogEntry() {
    LogEntry logEntry = new LogEntry();
    log.addLogEntry(logEntry);
    assertEquals(1, log.getTotalLogEntries());
    assertTrue(log.getLogEntries().contains(logEntry));
  }

  public void testAddLogEntries() {
    LogEntry logEntry1 = new LogEntry();
    LogEntry logEntry2 = new LogEntry();
    LogEntry logEntry3 = new LogEntry();
    List logEntries = new ArrayList();
    logEntries.add(logEntry1);
    logEntries.add(logEntry2);
    logEntries.add(logEntry3);
    log.addLogEntries(logEntries);
    assertEquals(3, log.getTotalLogEntries());
    assertTrue(log.getLogEntries().contains(logEntry1));
    assertTrue(log.getLogEntries().contains(logEntry2));
    assertTrue(log.getLogEntries().contains(logEntry3));
  }

}
