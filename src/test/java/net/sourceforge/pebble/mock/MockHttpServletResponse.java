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
package net.sourceforge.pebble.mock;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.TimeZone;

/**
 * A mock HttpServletResponse implementation.
 *
 * @author    Simon Brown
 */
public class MockHttpServletResponse implements HttpServletResponse {

  private String contentType = "text/html";

  private int status = HttpServletResponse.SC_OK;

  private Properties headers = new Properties();

  private PrintWriter writer = new PrintWriter(new StringWriter());
  private String sendRedirect;

  public void addCookie(Cookie cookie) {
  }

  public boolean containsHeader(String s) {
    return false;
  }

  public String encodeURL(String s) {
    return null;
  }

  public String encodeRedirectURL(String s) {
    return null;
  }

  public String encodeUrl(String s) {
    return null;
  }

  public String encodeRedirectUrl(String s) {
    return null;
  }

  public void sendError(int i, String s) throws IOException {
    this.status = i;
  }

  public void sendError(int i) throws IOException {
    this.status = i;
  }

  public void sendRedirect(String s) throws IOException {
    this.sendRedirect = s;
  }

  public String getSendRedirect() {
    return this.sendRedirect;
  }

  public void setDateHeader(String name, long value) {
    SimpleDateFormat httpFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
    httpFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    setHeader(name, "" + httpFormat.format(new Date(value)));
  }

  public void addDateHeader(String s, long l) {
  }

  public void setHeader(String name, String value) {
    headers.put(name, value);
  }

  public void addHeader(String name, String value) {
    headers.put(name, value);
  }

  public String getHeader(String name) {
    return headers.getProperty(name);
  }

  public void setIntHeader(String s, int i) {
  }

  public void addIntHeader(String s, int i) {
  }

  public void setStatus(int i) {
    this.status = i;
  }

  public void setStatus(int i, String s) {
    this.status = i;
  }

  public int getStatus() {
    return this.status;
  }

  public String getCharacterEncoding() {
    return null;
  }

  public ServletOutputStream getOutputStream() throws IOException {
    return null;
  }

  public PrintWriter getWriter() throws IOException {
    return this.writer;
  }

  public void setWriter(PrintWriter writer)
  {
    this.writer = writer;
  }

  public void setContentLength(int i) {
  }

  public void setContentType(String s) {
    this.contentType = s;
  }

  public String getContentType() {
    return this.contentType;
  }

  public void setBufferSize(int i) {
  }

  public int getBufferSize() {
    return 0;
  }

  public void flushBuffer() throws IOException {
  }

  public void resetBuffer() {
  }

  public boolean isCommitted() {
    return false;
  }

  public void reset() {
  }

  public void setLocale(Locale locale) {
  }

  public Locale getLocale() {
    return null;
  }

  public void setCharacterEncoding(String s) {
    //To change body of implemented methods use File | Settings | File Templates.
  }

    @Override
    public Collection<String> getHeaders(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Collection<String> getHeaderNames() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
