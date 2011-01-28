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

import java.util.Date;

/**
 * Tests for the LogEntry class.
 *
 * @author Simon Brown
 */
public class LogEntryTest extends TestCase {

  private LogEntry logEntry;

  protected void setUp() throws Exception {
    this.logEntry = new LogEntry();
  }

  /**
   * Tests the host property.
   */
  public void testHost() {
    logEntry.setHost("192.168.0.1");
    assertEquals("192.168.0.1", logEntry.getHost());
  }

  /**
   * Tests the date property.
   */
  public void testDate() {
    Date d = new Date();
    logEntry.setDate(d);
    assertEquals(d, logEntry.getDate());
  }

  /**
   * Tests the request property.
   */
  public void testRequest() {
    logEntry.setRequest("GET /blog/");
    assertEquals("GET /blog/", logEntry.getRequest());
    assertEquals("GET", logEntry.getRequestMethod());
    assertEquals("/blog/", logEntry.getRequestUri());
  }

  /**
   * Tests the status code property.
   */
  public void testStatusCode() {
    logEntry.setStatusCode(200);
    assertEquals(200, logEntry.getStatusCode());
  }

  /**
   * Tests the referer property.
   */
  public void testReferer() {
    logEntry.setReferer("http://www.simongbrown.com/blog/");
    assertEquals("http://www.simongbrown.com/blog/", logEntry.getReferer());
  }

  /**
   * Tests the agent property.
   */
  public void testAgent() {
    logEntry.setAgent("Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/125.4 (KHTML, like Gecko) Safari/125.9");
    assertEquals("Mozilla/5.0 (Macintosh; U; PPC Mac OS X; en) AppleWebKit/125.4 (KHTML, like Gecko) Safari/125.9", logEntry.getAgent());
  }

}
