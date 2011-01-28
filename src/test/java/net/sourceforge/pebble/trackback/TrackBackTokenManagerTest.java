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
package net.sourceforge.pebble.trackback;

import junit.framework.TestCase;

/**
 * Tests for the TrackBackTokenManager class.
 *
 * @author    Simon Brown
 */
public class TrackBackTokenManagerTest extends TestCase {

  private TrackBackTokenManager manager = TrackBackTokenManager.getInstance();

  public void testGenerateToken() {
    String token1 = manager.generateToken();
    String token2 = manager.generateToken();
    String token3 = manager.generateToken();

    assertFalse(token1.equals(token2));
    assertFalse(token1.equals(token3));
    assertFalse(token2.equals(token3));
  }

  public void testIsValid() {
    assertFalse(manager.isValid(null));
    assertFalse(manager.isValid(""));

    // tokens are numeric, so this token won't be valid
    assertFalse(manager.isValid("atoken"));

    assertFalse(manager.isValid("123456789"));

    String token = manager.generateToken();
    assertTrue(manager.isValid(token));
  }

  public void testExpire() {
    String token = manager.generateToken();
    manager.expire(token);
    assertFalse(manager.isValid(token));
  }

}
