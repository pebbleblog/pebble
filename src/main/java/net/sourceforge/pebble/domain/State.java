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
package net.sourceforge.pebble.domain;

import java.io.Serializable;

/**
 * Represents a state.
 *
 * @author    Simon Brown
 */
public class State implements Serializable {

  public static final State APPROVED = new State("approved");
  public static final State REJECTED = new State("rejected");
  public static final State PENDING = new State("pending");

  public static final State UNPUBLISHED = new State("unpublished");
  public static final State PUBLISHED = new State("published");

  public static State getState(String name) {
    if (name == null) {
      return null;
    } else if (name.equals("approved")) {
      return APPROVED;
    } else if (name.equals("rejected")) {
      return REJECTED;
    } else if (name.equals("pending")) {
      return PENDING;
    } else if (name.equals("unpublished")) {
      return UNPUBLISHED;
    } else if (name.equals("published")) {
      return PUBLISHED;
    } else {
      return null;
    }
  }

  /** the name of the state */
  private String name;

  /**
   * Creates a new instance.
   *
   * @param name    the name of the state
   */
  private State(String name) {
    this.name = name;
  }

  /**
   * Gets the name of this state.
   *
   * @return    the name as a String
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the hashcode of this object.
   *
   * @return  the hashcode as an int
   */
  public int hashCode() {
    return name.hashCode();
  }

  /**
   * Determines whether the specified object is equal to this one.
   *
   * @param o   the object to compare against
   * @return    true if Object o represents the same category, false otherwise
   */
  public boolean equals(Object o) {
    if (!(o instanceof State)) {
      return false;
    }

    State state = (State)o;
    return state.getName().equals(name);
  }

  /**
   * Returns a String representation of this object.
   *
   * @return  a String
   */
  public String toString() {
    return this.name;
  }

}
