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
package net.sourceforge.pebble.web.view;

import net.sourceforge.pebble.domain.FileMetaData;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Represents a binary view component and prepares the model for display.
 *
 * @author    Simon Brown
 */
public class ZipView extends BinaryView {

  private List files;
  private String filename;

  public ZipView(List files, String filename) {
    this.files = files;
    this.filename = filename;
  }

  /**
   * Gets the title of this view.
   *
   * @return the title as a String
   */
  public String getContentType() {
    return "application/zip";
  }

  public long getContentLength() {
    return 0;
  }

  /**
   * Dispatches this view.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @param context
   */
  public void dispatch(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws ServletException {
    try {
      response.setHeader("Content-Disposition", "filename=" + filename);
      byte[] buf = new byte[1024];
      ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
      Iterator it = files.iterator();
      while (it.hasNext()) {
        FileMetaData file = (FileMetaData)it.next();
        if (file.isDirectory()) {
          continue;
        }

        FileInputStream in = new FileInputStream(file.getFile());
        out.putNextEntry(new ZipEntry(file.getAbsolutePath().substring(1)));
        int len;
        while ((len = in.read(buf)) > 0) {
          out.write(buf, 0, len);
        }
        out.closeEntry();
        in.close();
      }

      out.close();
    } catch (IOException ioe) {
      throw new ServletException(ioe);
    }
  }

}
