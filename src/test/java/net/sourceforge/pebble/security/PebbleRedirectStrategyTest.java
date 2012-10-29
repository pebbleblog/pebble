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

package net.sourceforge.pebble.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class PebbleRedirectStrategyTest {
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;

  private PebbleRedirectStrategy strategy = new PebbleRedirectStrategy();

  @Before
  public void setUp() {
    when(request.getContextPath()).thenReturn("/context");
    when(response.encodeRedirectURL(anyString())).thenAnswer(new Answer<Object>() {
      public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
        return invocationOnMock.getArguments()[0];
      }
    });
  }

  @Test
  public void absoluteUrlShouldBeMadeRelative() throws Exception {
    strategy.sendRedirect(request, response, "http://attacker.com/some/url");
    verify(response).sendRedirect("/some/url");
  }

  @Test
  public void absoluteUrlWithQueryStringShouldBeMadeRelative() throws Exception {
    strategy.sendRedirect(request, response, "http://attacker.com/some/url?foo=bar");
    verify(response).sendRedirect("/some/url?foo=bar");
  }

  @Test
  public void schemeRelativeUrlShouldBeMadeRelative() throws Exception {
    strategy.sendRedirect(request, response, "//attacker.com/some/url");
    verify(response).sendRedirect("/some/url");
  }

  @Test
  public void relativeUrlShouldHaveContextAdded() throws Exception {
    strategy.sendRedirect(request, response, "/some/url");
    verify(response).sendRedirect("/context/some/url");
  }

}
