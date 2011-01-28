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
package net.sourceforge.pebble.util;

import junit.framework.TestCase;
import net.sourceforge.pebble.web.validation.ValidationContext;
import net.sourceforge.pebble.web.validation.ValidationContext;

/**
 * Tests for the utilities in the MailUtils class.
 *
 * @author    Simon Brown
 */
public class MailUtilsTest extends TestCase {

  public void testValidEmailAddresses() {
    ValidationContext context = new ValidationContext();

    MailUtils.validate("somebody@somedomain.com", context);
    assertFalse(context.hasErrors());

    MailUtils.validate("somebody@somedomain.co.uk", context);
    assertFalse(context.hasErrors());

    MailUtils.validate("somebody1234567890@somedomain.com", context);
    assertFalse(context.hasErrors());

    MailUtils.validate("firstname.lastname@somedomain.com", context);
    assertFalse(context.hasErrors());

    MailUtils.validate("firstname_lastname@somedomain.com", context);
    assertFalse(context.hasErrors());

    MailUtils.validate("firstname-lastname@somedomain.com", context);
    assertFalse(context.hasErrors());

    MailUtils.validate("firstname+lastname@somedomain.com", context);
    assertFalse(context.hasErrors());

    MailUtils.validate("firstname#lastname@somedomain.com", context);
    assertFalse(context.hasErrors());

    MailUtils.validate("me&you@somedomain.com", context);
    assertFalse(context.hasErrors());

    MailUtils.validate("me$you@somedomain.com", context);
    assertFalse(context.hasErrors());

    MailUtils.validate("somebody@some-domain.com", context);
    assertFalse(context.hasErrors());

    context = new ValidationContext();    
    MailUtils.validate("first,last@some-domain.com", context);
    assertTrue(context.hasErrors());

    context = new ValidationContext();    
    MailUtils.validate("first@last@some-domain.com", context);
    assertTrue(context.hasErrors());

    context = new ValidationContext();    
    MailUtils.validate("first last@some-domain.com", context);
    assertTrue(context.hasErrors());

  }

  public void testInvalidMailAddresses() {
	    ValidationContext context = new ValidationContext();

	    MailUtils.validate("somebody@somedomain@someotherdomain.com", context);
	    assertTrue(context.hasErrors());

	    context = new ValidationContext();

	    MailUtils.validate("somebody with spaces at somedomain@someotherdomain.com", context);
	    assertTrue(context.hasErrors());
  }
  
  
}
