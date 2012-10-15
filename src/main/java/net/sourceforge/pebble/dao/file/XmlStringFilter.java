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

package net.sourceforge.pebble.dao.file;

import java.util.BitSet;

/**
 * Filters strings in XML so that they don't contain any illegal characters
 *
 * @author James Roper
 */
public class XmlStringFilter {

  private static final BitSet LEGAL_CONTROL_CHARACTERS;

  static {
    LEGAL_CONTROL_CHARACTERS = new BitSet(32);
    LEGAL_CONTROL_CHARACTERS.set('\t');
    LEGAL_CONTROL_CHARACTERS.set('\n');
    LEGAL_CONTROL_CHARACTERS.set('\r');
  }

  private static boolean isLegal(char c) {
    // A character is legal if it's a low or high surrogate, if it's not a
    // control character, or if it's a legal control character
    return Character.isLowSurrogate(c) || Character.isHighSurrogate(c) ||
        c >= 32 || c < 0 || LEGAL_CONTROL_CHARACTERS.get(c);
  }

  public static String filter(String s) {
    if (s.isEmpty()) {
      return s;
    }

    StringBuilder result = null;
    int start = 0;
    int pos;

    for (pos = 0; pos < s.length(); pos++) {
      if (!isLegal(s.charAt(pos))) {
        if (pos > start) {
          if (result == null) {
            result = new StringBuilder(s.substring(start, pos));
          } else {
            result.append(s.substring(start, pos));
          }
        }
        start = pos + 1;
      }
    }

    if (start == 0) {
      return s;
    } else if (pos > start) {
      if (result == null) {
        return s.substring(start, pos);
      } else {
        result.append(s.substring(start, pos));
      }
    }
    if (result != null) {
      return result.toString();
    } else {
      return "";
    }
  }
}
