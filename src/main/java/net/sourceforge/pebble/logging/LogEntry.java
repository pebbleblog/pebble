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
package net.sourceforge.pebble.logging;

import java.util.Date;

/**
 * Represents an entry (line) in a log file.
 *
 * @author Simon Brown
 */
public class LogEntry {

  /** the host that made the request */
  private String host;

  /** the date the request was made */
  private Date date = new Date();

  /** the HTTP request */
  private String request = "";

  /** the HTTP status code returned */
  private int statusCode = 200;

  /** the number of bytes returned */
  private long bytes = -1;

  /** the referer (if applicable) */
  private String referer;

  /** the user-agent (if applicable) */
  private String agent;

  /**
   * Gets the host (an IP address or DNS name).
   *
   * @return  the host as a String
   */
  public String getHost() {
    return host;
  }

  /**
   * Sets the host (an IP address or DNS name).
   *
   * @param host    the host as a String
   */
  public void setHost(String host) {
    this.host = host;
  }

  /**
   * Gets the date.
   *
   * @return  a Date
   */
  public Date getDate() {
    return date;
  }

  /**
   * Sets the date.
   *
   * @param date    a Date instance
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * Gets the request.
   *
   * @return  the request as a String
   */
  public String getRequest() {
    return request;
  }

  /**
   * Gets just the method portion of the request.
   *
   * @return  the request method as a String
   */
  public String getRequestMethod() {
    return request.substring(0, request.indexOf("/")-1);
  }

  /**
   * Gets just the URI portion of the request.
   *
   * @return  the request URI as a String
   */
  public String getRequestUri() {
    return request.substring(request.indexOf("/"));
  }

  /**
   * Sets the request.
   *
   * @param request   the HTTP request as a String
   */
  public void setRequest(String request) {
    this.request = request;
  }

  /**
   * Gets the HTTP status code.
   *
   * @return    the status code as an int (-1 if not set)
   */
  public int getStatusCode() {
    return statusCode;
  }

  /**
   * Sets the HTTP status code.
   *
   * @param statusCode    the status code
   */
  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  /**
   * Gets the number of bytes sent.
   *
   * @return    the number of bytes as a long (-1 if not set)
   */
  public long getBytes() {
    return bytes;
  }

  /**
   * Sets the number of bytes sent.
   *
   * @param bytes   the number of bytes sent
   */
  public void setBytes(long bytes) {
    this.bytes = bytes;
  }

  /**
   * Gets the referer.
   *
   * @return  the refering URL as a String
   */
  public String getReferer() {
    return referer;
  }

  /**
   * Sets the referer.
   *
   * @param referer   the refering URL as a String
   */
  public void setReferer(String referer) {
    this.referer = referer;
  }

  /**
   * Gets the user agent (e.g. Mozilla, Internet Explorer, Safari, etc).
   *
   * @return  the user agent as a String
   */
  public String getAgent() {
    return agent;
  }

  /**
   * Sets the user agent.
   *
   * @param agent   the user agent as a String
   */
  public void setAgent(String agent) {
    this.agent = agent;
  }

}
