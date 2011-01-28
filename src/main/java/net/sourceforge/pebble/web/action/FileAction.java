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
import net.sourceforge.pebble.domain.MultiBlog;
import net.sourceforge.pebble.domain.FileManager;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.service.LastModifiedService;
import net.sourceforge.pebble.util.FileUtils;
import net.sourceforge.pebble.web.view.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

/**
 * Gets a file/image from a blog.
 *
 * @author    Simon Brown
 */
public class FileAction extends Action {

  /** the log used by this class */
  private static final Log log = LogFactory.getLog(FileAction.class);

  @Inject
  private LastModifiedService lastModifiedService;

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Object o = request.getAttribute(Constants.BLOG_KEY);
    if (o instanceof MultiBlog) {
      return new NotFoundView();
    }

    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);

    String name = request.getParameter("name");
    String type = request.getParameter("type");
    if (name == null || name.length() == 0 || name.equals("/")) {
      // forward to secure file browser
      return new ForwardView("/viewFiles.secureaction?type=" + type);
    }

    FileManager fileManager = new FileManager(blog, type);
    File root = fileManager.getRootDirectory();
    File file = fileManager.getFile(name);

    if (!file.exists()) {
      // file doesn't exist so send back a 404
      return new NotFoundView();
    }

    if (!FileUtils.underneathRoot(root, file) || file.isDirectory()) {
      // forward to secure file browser
      return new ForwardView("/viewFiles.secureaction?type=" + type + "&path=" + name);
    }

    Date lastModified = new Date(file.lastModified());
    Calendar expires = blog.getCalendar();
    expires.add(Calendar.MONTH, 1);

    if (lastModifiedService.checkAndProcessLastModified(request, response, lastModified, expires.getTime())) {
      return new NotModifiedView();
    } else {
      return new FileView(file);
    }
  }

}