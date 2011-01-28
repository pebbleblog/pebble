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
package net.sourceforge.pebble.web.model;

import java.util.HashMap;
import java.util.Set;

/**
 * Represents the model in web MVC.
 *
 * @author    Simon Brown
 */
public class Model {

  private HashMap<String, Object> data = new HashMap<String, Object>();

  /**
   * Puts data into the model.
   *
   * @param name    the name of the data
   * @param value   the value
   */
  public void put(String name, Object value) {
    data.put(name, value);
  }

  /**
   * Gets data from the model.
   *
   * @param name    the name of the data
   * @return        the value
   */
  public Object get(String name) {
    return data.get(name);
  }

  /**
   * Gets the set of all keys.
   *
   * @return a Set of String instances
   */
  public Set keySet() {
    return data.keySet();
  }

  /**
   * Determines whether the model contains an element with the specified key.
   *
   * @param   key   a String
   * @return  true if an element with the key exists, false otherwise
   */
  public boolean contains(String key) {
    return data.containsKey(key);
  }

}
