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

import net.sourceforge.pebble.util.FileUtils;
import net.sourceforge.pebble.domain.AbstractBlog;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.FileMetaData;
import net.sourceforge.pebble.Constants;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

/**
 * Represents a binary view component and prepares the model for display.
 *
 * @author    Simon Brown
 */
public class FileView extends BinaryView {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(FileView.class);

  private File file;

  public FileView(File file) {
    this.file = file;
  }

  /**
   * Gets the title of this view.
   *
   * @return the title as a String
   */
  public String getContentType() {
    String mimeType = null;
    ServletContext servletContext = getServletContext();
    if (servletContext != null) {
      mimeType = servletContext.getMimeType(file.getName());
    }

    if (mimeType == null) {
      return FileUtils.getContentType(file.getName());
    }

    return mimeType;
  }

  public long getContentLength() {
    return file.length();
  }

  /**
   * Dispatches this view.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @param context
   */
  public void dispatch(HttpServletRequest request, HttpServletResponse response, ServletContext context) throws ServletException {
    response.setContentLength((int)getContentLength());
    try {
      BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
      BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());

      byte[] buf = new byte[1024];
      int len = in.read(buf);
      while (len > 0) {
          out.write(buf, 0, len);
          len = in.read(buf);
      }

      in.close();
      out.close();
    } catch (IOException ioe) {
      log.warn(ioe);
    } finally {
      AbstractBlog blog = (AbstractBlog)getModel().get(Constants.BLOG_KEY);
      blog.log(request, HttpServletResponse.SC_OK);
    }
  }

}
