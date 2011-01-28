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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class GZIPResponseWrapper extends HttpServletResponseWrapper {

  private final HttpServletResponse wrappedResponse;
  private final String encoding;

  private ServletOutputStream stream;
  private PrintWriter writer;
  private int status;
  private Integer contentLength;

  public GZIPResponseWrapper(HttpServletResponse wrappedResponse, String encoding) {
    super(wrappedResponse);
    this.wrappedResponse = wrappedResponse;
    this.encoding = encoding;
  }

  public ServletOutputStream createOutputStream() throws IOException {
    if (shouldGzipResponse()) {
      return (new GZIPResponseStream(wrappedResponse));
    } else {
      // If we aren't zipping the response, then we need to pass on the content length
      if (contentLength != null) {
        wrappedResponse.setContentLength(contentLength);
      }
      return wrappedResponse.getOutputStream();
    }
  }

  private boolean shouldGzipResponse() {
    // PEBBLE-43 We shouldn't zip responses that are not allowed to have any content, because a zipped
    // empty response is actually 20 bytes long
    return status != HttpServletResponse.SC_NOT_MODIFIED && status != HttpServletResponse.SC_NO_CONTENT;
  }

  public void finishResponse() {
    try {
      if (writer != null) {
        writer.close();
      } else {
        if (stream != null) {
          stream.close();
        }
      }
    } catch (IOException e) {
      // Ignore
    }
  }

  public void flushBuffer() throws IOException {
	if (stream != null) {
	  stream.flush();
	}
  }

  public ServletOutputStream getOutputStream() throws IOException {
    if (writer != null) {
      throw new IllegalStateException("getWriter() has already been called!");
    }

    if (stream == null)
      stream = createOutputStream();
    return (stream);
  }

  public PrintWriter getWriter() throws IOException {
    if (writer != null) {
      return (writer);
    }

    if (stream != null) {
      throw new IllegalStateException("getOutputStream() has already been called!");
    }

    stream = createOutputStream();
    writer = new PrintWriter(new OutputStreamWriter(stream, encoding));
    return (writer);
  }

  public void setContentLength(int length) {
    contentLength = length;
  }

  @Override
  public void setStatus(int sc) {
    this.status = sc;
    super.setStatus(sc);
  }

  @Override
  public void setStatus(int sc, String sm) {
    this.status = sc;
    super.setStatus(sc, sm);
  }

  @Override
  public void sendError(int sc, String msg) throws IOException {
    this.status = sc;
    super.sendError(sc, msg);
  }

  @Override
  public void sendError(int sc) throws IOException {
    this.status = sc;
    super.sendError(sc);
  }
}
