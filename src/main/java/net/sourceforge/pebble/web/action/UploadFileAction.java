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
import net.sourceforge.pebble.PebbleContext;
import net.sourceforge.pebble.domain.FileManager;
import net.sourceforge.pebble.domain.FileMetaData;
import net.sourceforge.pebble.domain.Blog;
import net.sourceforge.pebble.domain.BlogManager;
import net.sourceforge.pebble.web.view.RedirectView;
import net.sourceforge.pebble.web.view.View;
import net.sourceforge.pebble.web.view.impl.FileTooLargeView;
import net.sourceforge.pebble.web.view.impl.NotEnoughSpaceView;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 * Superclass for actions that allow the user to upload a file.
 *
 * @author    Simon Brown
 */
public abstract class UploadFileAction extends AbstractFileAction {

  private static final Log log = LogFactory.getLog(UploadFileAction.class);

  /**
   * Peforms the processing associated with this action.
   *
   * @param request  the HttpServletRequest instance
   * @param response the HttpServletResponse instance
   * @return the name of the next view
   */
  public View process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
    Blog blog = (Blog)getModel().get(Constants.BLOG_KEY);

    String type = getType();
    String path = "";
    String[] filenames = new String[10];

    FileManager fileManager = new FileManager(blog, type);

    try {
      boolean isMultipart = FileUpload.isMultipartContent(request);

      if (isMultipart) {
        DiskFileUpload upload = new DiskFileUpload();
        long sizeInBytes = PebbleContext.getInstance().getConfiguration().getFileUploadSize() * 1024; // convert to bytes
        upload.setSizeMax(sizeInBytes);
        upload.setSizeThreshold((int)sizeInBytes/4);
        upload.setRepositoryPath(System.getProperty("java.io.tmpdir"));

        List items;
        try {
          items = upload.parseRequest(request);
        } catch (FileUploadBase.SizeLimitExceededException e) {
          return new FileTooLargeView();
        }

        // find the form fields first
        Iterator it = items.iterator();
        while (it.hasNext()) {
          FileItem item = (FileItem)it.next();
          if (item.isFormField() && item.getFieldName().startsWith("filename")) {
            int index = Integer.parseInt(item.getFieldName().substring(item.getFieldName().length()-1));
            filenames[index] = item.getString();
            log.debug("index is " + index + ", filename is " + filenames[index]);
          } else if (item.isFormField() && item.getFieldName().equals("path")) {
            path = item.getString();
          }
        }

        // now the actual files
        it = items.iterator();
        while (it.hasNext()) {
          FileItem item = (FileItem)it.next();

          if (!item.isFormField() && item.getSize() > 0 && item.getFieldName().startsWith("file")) {
            int index = Integer.parseInt(item.getFieldName().substring(item.getFieldName().length()-1));

            // if the filename hasn't been specified, use that from the file
            // being uploaded
            if (filenames[index] == null || filenames[index].length() == 0) {
              filenames[index] = item.getName();
            }

            File destinationDirectory = fileManager.getFile(path);
            File file = new File(destinationDirectory, filenames[index]);
            if (!fileManager.isUnderneathRootDirectory(file)) {
              response.setStatus(HttpServletResponse.SC_FORBIDDEN);
              return null;
            }

            long itemSize = item.getSize()/1024;
            if (FileManager.hasEnoughSpace(blog, itemSize)) {
              log.debug("Writing file " + filenames[index] + ", size is " + item.getSize());
              writeFile(fileManager, path, filenames[index], item);

              // if it's a theme file, also create a copy in blog.dir/theme
              if (type.equals(FileMetaData.THEME_FILE)) {
                writeFile(new FileManager(blog, FileMetaData.BLOG_DATA), "/theme" + path, filenames[index], item);
              }
            } else {
              return new NotEnoughSpaceView();
            }
          }
        }
      }

      blog.info("Files uploaded.");
    } catch (Exception e) {
      throw new ServletException(e);
    }

    FileMetaData directory = fileManager.getFileMetaData(path);

    return new RedirectView(blog.getUrl() + directory.getUrl());
  }

  /**
   * Helper method to write a file.
   *
   * @param fileManager   a FileManager instance
   * @param path          the path where to save the file
   * @param filename      the filename
   * @param item          the uploaded item
   * @throws Exception    if something goes wrong writing the file
   */
  private void writeFile(FileManager fileManager, String path, String filename, FileItem item) throws Exception {
    File destinationDirectory = fileManager.getFile(path);
    destinationDirectory.mkdirs();

    File file = new File(destinationDirectory, filename);
    item.write(file);
  }

  /**
   * Gets the type of this upload (blog image, blog file or theme file).
   *
   * @return    a String representing the type
   * @see       net.sourceforge.pebble.domain.FileMetaData
   */
  protected abstract String getType();

  /**
   * Gets a list of all roles that are allowed to access this action.
   *
   * @return  an array of Strings representing role names
   */
  public abstract String[] getRoles(HttpServletRequest request);

}