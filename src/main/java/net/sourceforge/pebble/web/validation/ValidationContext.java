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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A context within which to perform validation and raise errors against.
 *
 * @author    Simon Brown
 */
public class ValidationContext {

  /** the list of errors */
  private List errors = new ArrayList();

  /**
   * Adds a new error to this context.
   *
   * @param message   the error message as a String
   */
  public void addError(String message) {
    addError(new ValidationError(message));
  }

  /**
   * Adds a new error to this context.
   *
   * @param error   a ValidationError instance
   */
  public void addError(ValidationError error) {
    errors.add(error);
  }

  /**
   * Determines whether this context has errors rasied against it.
   *
   * @return  true if this context has errors, false otherwise
   */
  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  /**
   * Gets a list of all errors raised against this context.
   *
   * @return  a List of ValidationError instances
   */
  public List getErrors() {
    return Collections.unmodifiableList(errors);
  }

  /**
   * Gets the number of errors.
   *
   * @return  the number of errors as in int
   */
  public int getNumberOfErrors() {
    return errors.size();
  }

  /**
   * Gets the specified ValidationError.
   *
   * @param index   the index of the error to get
   * @return  the ValidationError at the specified index
   */
  public ValidationError getError(int index) {
    return (ValidationError)errors.get(index);
  }

}