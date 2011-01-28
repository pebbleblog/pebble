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
package net.sourceforge.pebble.web.validation;

import junit.framework.TestCase;
import net.sourceforge.pebble.web.validation.ValidationContext;

/**
 * Tests for the ValidationContext class.
 *
 * @author    Simon Brown
 */
public class ValidationContextTest extends TestCase {

  private ValidationContext context;

  protected void setUp() throws Exception {
    this.context = new ValidationContext();
  }

  public void testConstruction() {
    assertFalse(context.hasErrors());
  }

  public void testContextWithNoErrors() {
    assertFalse(context.hasErrors());
    assertEquals(0, context.getNumberOfErrors());
    assertTrue(context.getErrors().isEmpty());
  }

  public void testContextWithOneError() {
    ValidationError error = new ValidationError("Some message");
    context.addError(error);
    assertTrue(context.hasErrors());
    assertEquals(1, context.getNumberOfErrors());
    assertFalse(context.getErrors().isEmpty());
    assertSame(error, context.getError(0));
  }

  public void testContextWithOneErrorMessage() {
    context.addError("Some error message");
    assertTrue(context.hasErrors());
    assertEquals(1, context.getNumberOfErrors());
    assertFalse(context.getErrors().isEmpty());
    assertEquals("Some error message", context.getError(0).getMessage());
  }

}
