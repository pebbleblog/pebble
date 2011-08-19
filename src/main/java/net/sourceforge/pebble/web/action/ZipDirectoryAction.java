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
package net.sourceforge.pebble.web.action;

import net.sourceforge.pebble.Constants;
import net.sourceforge.pebble.domain.FileManager;
import net.sourceforge.pebble.domain.FileMetaData;
import net.sourceforge.pebble.domain.IllegalFileAccessException;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.web.view.ForbiddenView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.ZipView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Allows the user to export a directory as a ZIP file.
 *
 * @author Simon Brown
 */
public class ZipDirectoryAction extends AbstractFileAction {

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    String type = request.getParameter("type");
    String path = request.getParameter("path");

    // if there is no path, we're looking at the root
    if (path == null || path.length() == 0) {
      path = "/";
    }

    try {
      String filename;
      if (type.equals(FileMetaData.BLOG_DATA)) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        if (path.equals("/logs")) {
          filename = blog.getId() + "-logs-" + sdf.format(blog.getCalendar().getTime()) + ".zip";
        } else {
          filename = blog.getId() + "-" + sdf.format(blog.getCalendar().getTime()) + ".zip";
        }
      } else {
        filename = "export.zip";
      }

      FileManager fileManager = new FileManager(blog, type);
      List files = fileManager.getFiles(path, true);

      return new ZipView(files, filename);
    } catch (IllegalFileAccessException e) {
      return new ForbiddenView();
    }
  }

}
