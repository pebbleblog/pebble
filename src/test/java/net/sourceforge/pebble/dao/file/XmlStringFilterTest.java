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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlStringFilterTest {

  @Test
  public void writeSimpleString() throws Exception {
    assertEquals("blah", XmlStringFilter.filter("blah"));
  }

  @Test
  public void writeEmptyString() throws Exception {
    assertEquals("", XmlStringFilter.filter(""));
  }

  @Test
  public void writeOneControl() throws Exception {
    assertEquals("", XmlStringFilter.filter("\u0000"));
  }

  @Test
  public void writeManyControl() throws Exception {
    assertEquals("", XmlStringFilter.filter("\u0000\u0000\u0000"));
  }

  @Test
  public void writeStringWithControlCharacterAtStart() throws Exception {
    assertEquals("blah", XmlStringFilter.filter("\u0000blah"));
  }

  @Test
  public void writeStringWithControlCharacterInMiddle() throws Exception {
    assertEquals("blah", XmlStringFilter.filter("bl\u0000ah"));
  }

  @Test
  public void writeStringWithControlCharacterAtEnd() throws Exception {
    assertEquals("blah", XmlStringFilter.filter("blah\u0000"));
  }

  @Test
  public void writeStringWithLotsOfCharacters() throws Exception {
    assertEquals("The quick\nbrownfox \tjumps over\r the lazy dog.",
        XmlStringFilter.filter("\u0000The quick\nbrown\u0000\u0000\u0000fox \tjumps ov\u0000er\r the lazy dog.\u0000"));
  }

}
