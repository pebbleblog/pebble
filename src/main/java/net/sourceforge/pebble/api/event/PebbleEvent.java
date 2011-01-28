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
package net.sourceforge.pebble.api.event;

import java.util.EventObject;

/**
 * Superclass for Pebble events.
 *
 * @author Simon Brown
 */
public abstract class PebbleEvent extends EventObject {

  /** the type of the event */
  private int type;

  /** flag to indicate whether this event has been vetoed */
  private boolean vetoed = false;

  /**
   * Creates a new instance with the specified source and type.
   *
   * @param source    the Object that created this event
   * @param type    the type of this event
   */
  public PebbleEvent(Object source, int type) {
    super(source);

    this.type = type;
  }

  /**
   * Gets the type of this event.
   *
   * @return  the type as an int
   */
  public int getType() {
    return this.type;
  }

  /**
   * Vetos this event so that other listeners will not reveive it.
   */
  public void veto() {
    this.vetoed = true;
  }

  /**
   * Determines whether this event has been vetoed.
   *
   * @return  true if this event has been vetoed, false otherwise
   */
  public boolean isVetoed() {
    return this.vetoed;
  }

  public String toString() {
    return getClass().getName() + "/" + source + "/" + type;
  }

}
