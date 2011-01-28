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
import net.sourceforge.pebble.util.StringUtils;
import net.sourceforge.pebble.web.security.RequireSecurityToken;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.ForbiddenView;
import net.sourceforge.pebble.web.view.impl.NotEnoughSpaceView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Allows the user to copy (or rename/move) a file.
 *
 * @author Simon Brown
 */
@RequireSecurityToken
public class CopyFileAction extends AbstractFileAction {

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);
    String name = request.getParameter("name");
    String oldName = StringUtils.filterHTML(name);
    String type = request.getParameter("type");
    String path = request.getParameter("path");
    String newName = StringUtils.filterHTML(request.getParameter("newName"));
    String submit = request.getParameter("submit");

    FileManager fileManager = new FileManager(blog, type);
    FileMetaData directory = fileManager.getFileMetaData(path);
    try {
      if (submit.equalsIgnoreCase("rename")) {
        fileManager.renameFile(path, name, newName);

        // if it's a theme file, also rename the copy in blog.dir/theme
        if (type.equals(FileMetaData.THEME_FILE)) {
          fileManager = new FileManager(blog, FileMetaData.BLOG_DATA);
          fileManager.renameFile("/theme" + path, name, newName);
        }

        blog.info("File \"" + oldName + "\" renamed to \"" + newName + "\".");
      } else {
        if (FileManager.hasEnoughSpace(blog, fileManager.getFileMetaData(path, name).getSizeInKB())) {
          fileManager.copyFile(path, name, newName);

          // if it's a theme file, also create a copy in blog.dir/theme
          if (type.equals(FileMetaData.THEME_FILE)) {
            fileManager = new FileManager(blog, FileMetaData.BLOG_DATA);
            fileManager.copyFile("/theme" + path, name, newName);
          }

          blog.info("File \"" + oldName + "\" copied to \"" + newName + "\".");
        } else {
          return new NotEnoughSpaceView();
        }
      }
    } catch (IllegalFileAccessException e) {
      return new ForbiddenView();
    } catch (IOException ioe) {
      throw new ServletException(ioe);
    }

    return new RedirectView(blog.getUrl() + directory.getUrl());
  }

}