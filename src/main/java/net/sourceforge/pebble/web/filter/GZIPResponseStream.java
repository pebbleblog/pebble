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
package net.sourceforge.pebble.web.filter;

import java.io.*;
import java.util.zip.GZIPOutputStream;
import javax.servlet.*;
import javax.servlet.http.*;

public class GZIPResponseStream extends ServletOutputStream {

  protected ByteArrayOutputStream baos = null;
  protected GZIPOutputStream gzipstream = null;
  protected boolean closed = false;
  protected HttpServletResponse response = null;
  protected ServletOutputStream output = null;

  public GZIPResponseStream(HttpServletResponse response) throws IOException {
    closed = false;
    this.response = response;
    this.output = response.getOutputStream();
    baos = new ByteArrayOutputStream();
    gzipstream = new GZIPOutputStream(baos);
  }

  public void close() throws IOException {
    if (closed) {
      throw new IOException("This output stream has already been closed");
    }
    gzipstream.finish();
    gzipstream.flush();
    gzipstream.close();

    byte[] bytes = baos.toByteArray();
    response.setContentLength(bytes.length);
    response.addHeader("Content-Encoding", "gzip");
    output.write(bytes);
    output.flush();
    output.close();
    closed = true;
  }

  public void flush() throws IOException {
    if (closed) {
      throw new IOException("Cannot flush a closed output stream");
    }
    gzipstream.flush();
  }

  public void write(int b) throws IOException {
    if (closed) {
      throw new IOException("Cannot write to a closed output stream");
    }
    gzipstream.write((byte) b);
  }

  public void write(byte b[]) throws IOException {
    write(b, 0, b.length);
  }

  public void write(byte b[], int off, int len) throws IOException {
    if (closed) {
      throw new IOException("Cannot write to a closed output stream");
    }
    gzipstream.write(b, off, len);
  }
}