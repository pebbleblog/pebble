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
package net.sourceforge.pebble.domain;

import java.io.File;
import java.util.Date;

/**
 * Represents the meta data associated with a file.
 *
 * @author    Simon Brown
 */
public class FileMetaData {

  public static final String BLOG_FILE = "blogFile";
  public static final String BLOG_IMAGE = "blogImage";
  public static final String THEME_FILE = "themeFile";
  public static final String BLOG_DATA = "blogData";

  private FileManager context;

  private String name;
  private String path;
  private Date lastModified;
  private long size;
  private boolean directory;
  private String type;

  FileMetaData(FileManager context, String absolutePath) {
    this.context = context;

    if (absolutePath == null || absolutePath.equals("") || absolutePath.equals("/")) {
      this.path="/";
      this.name = "";
    } else {
      if (absolutePath.endsWith("/")) {
        absolutePath = absolutePath.substring(0, absolutePath.length()-1);
      }

      if (absolutePath.indexOf("/") > -1) {
        this.path = absolutePath.substring(0, absolutePath.lastIndexOf("/"));
        if (this.path.length() == 0) {
          this.path = "/";
        }
        this.name = absolutePath.substring(absolutePath.lastIndexOf("/")+1, absolutePath.length());
      } else {
        this.path = absolutePath;
        this.name = "";
      }

      // finally, all paths must start with "/"
      if (!this.path.startsWith("/")) {
        this.path = "/" + this.path;
      }
    }
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getLastModified() {
    return lastModified;
  }

  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  public boolean isDirectory() {
    return directory;
  }

  public void setDirectory(boolean directory) {
    this.directory = directory;
  }

  public String getPath() {
    return path;
  }

  public String getAbsolutePath() {
    if (!path.endsWith("/")) {
      return path + "/" + name;
    } else {
      return path + name;
    }
  }

  public String getUrl() {
    String url = null;

    if (type != null && type.equals(FileMetaData.BLOG_IMAGE)) {
      url = "images" + getAbsolutePath();
    } else if (type != null && type.equals(FileMetaData.BLOG_FILE)) {
      url = "files" + getAbsolutePath();
    } else if (type != null && type.equals(FileMetaData.THEME_FILE)) {
      url = "theme" + getAbsolutePath();
    }

    if (url != null && isDirectory() && !url.endsWith("/")) {
      url += "/";
    }

    return url;
  }

  public boolean isEditable() {
    if (isDirectory()) {
      return false;
    } else {
      String filename = name.toLowerCase();
      return
          filename.endsWith(".txt") ||
          filename.endsWith(".properties") ||
          filename.endsWith(".jsp") ||
          filename.endsWith(".jspf") ||
          filename.endsWith(".html") ||
          filename.endsWith(".htm") ||
          filename.endsWith(".css") ||
          filename.endsWith(".xml");
    }
  }

  public long getSize() {
    return this.size;
  }

  public double getSizeInKB() {
    return (this.size / 1024.0);
  }

  public void setSize(long size) {
    this.size = size;
  }

  public void setType(String type) {
    this.type = type;
  }

  public File getFile() {
    return context.getFile(getAbsolutePath());
  }

}
