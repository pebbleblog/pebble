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
package net.sourceforge.pebble.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Default implementation of the last modified service
 *
 * @author James Roper
 */
public class DefaultLastModifiedService implements LastModifiedService {

  public boolean checkAndProcessLastModified(HttpServletRequest request, HttpServletResponse response,
                                             Date lastModified, Date expires) {

    SimpleDateFormat httpFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
    httpFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

    // Set the headers
    response.setDateHeader("Last-Modified", lastModified.getTime());
    response.setHeader("ETag", "\"" + httpFormat.format(lastModified) + "\"");
    if (expires != null) {
      response.setHeader("Expires", httpFormat.format(expires));
    }

    // Get the headers to check
    String ifModifiedSince = request.getHeader("If-Modified-Since");
    String ifNoneMatch = request.getHeader("If-None-Match");

    if (ifModifiedSince != null && ifModifiedSince.equals(httpFormat.format(lastModified))) {
      return true;
    } else if (ifNoneMatch != null && ifNoneMatch.equals("\"" + httpFormat.format(lastModified) + "\"")) {
      return true;
    } else {
      return false;
    }
  }
}
