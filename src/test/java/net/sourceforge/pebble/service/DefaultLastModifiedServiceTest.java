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
package net.sourceforge.pebble.service;

import net.sourceforge.pebble.mock.MockHttpServletRequest;
import net.sourceforge.pebble.mock.MockHttpServletResponse;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static org.junit.Assert.*;

/**
 * @author James Roper
 */
public class DefaultLastModifiedServiceTest {

  private DefaultLastModifiedService service;
  private MockHttpServletRequest request;
  private MockHttpServletResponse response;
  private SimpleDateFormat httpFormat;

  @Before
  public void setUp() {
    service = new DefaultLastModifiedService();
    request = new MockHttpServletRequest();
    response = new MockHttpServletResponse();
    httpFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
    httpFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
  }

  @Test
  public void testResponseHeaders() {
    assertFalse(service.checkAndProcessLastModified(request, response, new Date(10000000), null));
    assertEquals(httpFormat.format(new Date(10000000)), response.getHeader("Last-Modified"));
    assertEquals("\"" + httpFormat.format(new Date(10000000)) + "\"", response.getHeader("ETag"));
  }

  @Test
  public void testIfModifiedSinceMatch() {
    request.setHeader("If-Modified-Since", httpFormat.format(new Date(10000000)));
    assertTrue(service.checkAndProcessLastModified(request, response, new Date(10000000), null));
  }

  @Test
  public void testIfModifiedSinceNotMatch() {
    request.setHeader("If-Modified-Since", httpFormat.format(new Date(5000000)));
    assertFalse(service.checkAndProcessLastModified(request, response, new Date(10000000), null));
    assertEquals(httpFormat.format(new Date(10000000)), response.getHeader("Last-Modified"));
  }

  @Test
  public void testIfNoneMatchMatch() {
    request.setHeader("If-None-Match", "\"" + httpFormat.format(new Date(10000000)) + "\"");
    assertTrue(service.checkAndProcessLastModified(request, response, new Date(10000000), null));
  }

  @Test
  public void testIfNoneMatchNotMatch() {
    request.setHeader("If-None-Match", "\"" + httpFormat.format(new Date(5000000)) + "\"");
    assertFalse(service.checkAndProcessLastModified(request, response, new Date(10000000), null));
    assertEquals("\"" + httpFormat.format(new Date(10000000)) + "\"", response.getHeader("ETag"));
  }

  @Test
  public void testExpires() {
    assertFalse(service.checkAndProcessLastModified(request, response, new Date(10000000), new Date(999999999)));
    assertEquals(httpFormat.format(new Date(999999999)), response.getHeader("Expires"));
  }

}
