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

/**
 * Tests for the CombinedFormatLogEntryFormat class.
 *
 * @author Simon Brown
 */
public class CombinedFormatLogEntryFormatTest extends SingleBlogTestCase {

  private CombinedFormatLogEntryFormat format;
  private LogEntry logEntry;
  private LogEntry parsedLogEntry;

  protected void setUp() throws Exception {
    super.setUp();

    this.format = new CombinedFormatLogEntryFormat(blog);
    this.logEntry = new LogEntry();
  }

  public void testFormatWhenHostNotSpecified() {
    logEntry.setHost(null);
    assertEquals("- - - " + format.dateFormatter.format(logEntry.getDate()) + " \"\" 200 - - -", format.format(logEntry));
  }

  public void testFormatWhenHostSpecified() {
    logEntry.setHost("127.0.0.1");
    assertEquals("127.0.0.1 - - " + format.dateFormatter.format(logEntry.getDate()) + " \"\" 200 - - -", format.format(logEntry));
  }

  public void testFormatWhenRequestSpecified() {
    logEntry.setRequest("GET /blog/index.jsp");
    assertEquals("- - - " + format.dateFormatter.format(logEntry.getDate()) + " \"GET /blog/index.jsp\" 200 - - -", format.format(logEntry));
  }

  public void testFormatWhenRefererNotSpecified() {
    logEntry.setReferer(null);
    assertEquals("- - - " + format.dateFormatter.format(logEntry.getDate()) + " \"\" 200 - - -", format.format(logEntry));
  }

  public void testFormatWhenRefererSpecified() {
    logEntry.setReferer("http://www.google.com");
    assertEquals("- - - " + format.dateFormatter.format(logEntry.getDate()) + " \"\" 200 - \"http://www.google.com\" -", format.format(logEntry));
  }

  public void testFormatWhenAgentNotSpecified() {
    logEntry.setAgent(null);
    assertEquals("- - - " + format.dateFormatter.format(logEntry.getDate()) + " \"\" 200 - - -", format.format(logEntry));
  }

  public void testFormatWhenAgentSpecified() {
    logEntry.setAgent("Some user agent");
    assertEquals("- - - " + format.dateFormatter.format(logEntry.getDate()) + " \"\" 200 - - \"Some user agent\"", format.format(logEntry));
  }

  public void testParseWhenHostNotSpecified() {
    logEntry.setHost(null);
    parsedLogEntry = format.parse(format.format(logEntry));
    assertNull(parsedLogEntry.getHost());
  }

  public void testParseWhenHostSpecified() {
    logEntry.setHost("127.0.0.1");
    parsedLogEntry = format.parse(format.format(logEntry));
    assertEquals("127.0.0.1", parsedLogEntry.getHost());
  }

  public void testDateIsCorrectlyParsed() {
    parsedLogEntry = format.parse(format.format(logEntry));
    assertTrue(logEntry.getDate().getTime() - parsedLogEntry.getDate().getTime() <= 1000);
  }

  public void testParseWhenRequestSpecified() {
    logEntry.setRequest("GET /blog/index.jsp");
    parsedLogEntry = format.parse(format.format(logEntry));
    assertEquals("GET /blog/index.jsp", parsedLogEntry.getRequest());
  }

  public void testParseWhenRefererNotSpecified() {
    logEntry.setReferer(null);
    parsedLogEntry = format.parse(format.format(logEntry));
    assertNull(parsedLogEntry.getReferer());
  }

  public void testParseWhenRefererSpecified() {
    logEntry.setReferer("http://www.google.com");
    parsedLogEntry = format.parse(format.format(logEntry));
    assertEquals("http://www.google.com", parsedLogEntry.getReferer());
  }

  public void testParseWhenAgentNotSpecified() {
    logEntry.setAgent(null);
    parsedLogEntry = format.parse(format.format(logEntry));
    assertNull(parsedLogEntry.getAgent());
  }

  public void testParseWhenAgentSpecified() {
    logEntry.setAgent("Some user agent");
    parsedLogEntry = format.parse(format.format(logEntry));
    assertEquals("Some user agent", parsedLogEntry.getAgent());
  }

}
