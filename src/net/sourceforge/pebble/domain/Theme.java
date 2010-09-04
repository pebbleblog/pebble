/*
 * Copyright (c) 2003-2006, Simon Brown
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

import net.sourceforge.pebble.util.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * Represents the user's editable theme.
 *
 * @author    Simon Brown
 */
public class Theme {

  /** the log used by this class */
  private static Log log = LogFactory.getLog(Theme.class);

  /** the name of the theme that should be used as a default */
  public static final String DEFAULT_THEME_NAME = "default";

  /** the blog to which this theme belongs */
  private Blog blog;

  /** the name of the theme */
  private String name;

  /** the path of the live theme (under the webapp root) */
  private String pathToLiveThemes;

  /**
   * Creates a new Theme instance with the specified details.
   *
   * @param blog                the owning Blog instance
   * @param name                the name of the theme
   * @param pathToLiveThemes    the path to the live themes
   */
  public Theme(Blog blog, String name, String pathToLiveThemes) {
    this.blog = blog;
    this.name = name;
    this.pathToLiveThemes = pathToLiveThemes;
  }

  /**
   * Gets the location where the backup version of the blog theme is stored -
   * under the blog.dir directory, in a sub-directory called "theme".
   *
   * @return    an absolute, local path on the filing system
   */
  String getBackupThemeDirectory() {
    return blog.getRoot() + File.separator + "theme";
  }

  public File getPathToLiveTheme() {
    return new File(pathToLiveThemes, name);
  }

  /**
   * Restores the theme from the blog.dir to the webapp.
   */
  public void restore() {
    restore(DEFAULT_THEME_NAME);
  }

  /**
   * Restores the theme from the blog.dir to the webapp.
   */
  public void restore(String themeName) {
    File blogTheme = new File(getBackupThemeDirectory());
    if (!blogTheme.exists() || blogTheme.listFiles().length == 0) {
      copy(themeName);
    }

    log.debug("Restoring " + name + " theme from " + getBackupThemeDirectory());
    copy(blogTheme, getPathToLiveTheme());
  }

  /**
   * Restores the theme from the blog.dir to the webapp.
   */
  public void restoreToSpecifiedTheme(String themeName) {
    File blogTheme = new File(getBackupThemeDirectory());
    FileUtils.deleteFile(blogTheme);
    FileUtils.deleteFile(getPathToLiveTheme());
    restore(themeName);
  }

  /**
   * Backs up the theme from the webapp to the blog.dir.
   */
  public void backup() {
    backup(name);
  }

  /**
   * Backs up the named theme from the webapp to the blog.dir.
   *
   * @param themeName   the name of the theme to backup
   */
  private void backup(String themeName) {
    log.debug("Backing up " + themeName + " theme to " + getBackupThemeDirectory());
    File liveTheme = new File(pathToLiveThemes, themeName);
    File blogTheme = new File(getBackupThemeDirectory());
    File blogThemeBackup = new File(getBackupThemeDirectory() + ".bak");

    if (blogTheme.exists()) {
      blogTheme.renameTo(blogThemeBackup);
    }
    copy(liveTheme, blogTheme);
    FileUtils.deleteFile(blogThemeBackup);
  }

  /**
   * Copies the named theme from the webapp to blog.dir/theme.
   *
   * @param themeName   the name of the theme to backup
   */
  private void copy(String themeName) {
    log.info("Copying " + themeName + " theme to " + getBackupThemeDirectory());
    File liveTheme = new File(pathToLiveThemes, themeName);
    File blogTheme = new File(getBackupThemeDirectory());
    File blogThemeBackup = new File(getBackupThemeDirectory() + ".bak");

    if (blogTheme.exists()) {
      blogTheme.renameTo(blogThemeBackup);
    }
    copy(liveTheme, blogTheme);
    FileUtils.deleteFile(blogThemeBackup);
  }

  /**
   * Copies one file to another.
   *
   * @param source        the source
   * @param destination   the destination
   */
  private void copy(File source, File destination) {
    if (!destination.exists()) {
      destination.mkdir();
    }

    File files[] = source.listFiles();
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        if (files[i].isDirectory()) {
          copy(files[i], new File(destination, files[i].getName()));
        } else {
          try {
              FileChannel srcChannel = new FileInputStream(files[i]).getChannel();
              FileChannel dstChannel = new FileOutputStream(new File(destination, files[i].getName())).getChannel();
              dstChannel.transferFrom(srcChannel, 0, srcChannel.size());
              srcChannel.close();
              dstChannel.close();
          } catch (IOException ioe) {
            log.error("Could not write to " + destination.getAbsolutePath(), ioe);
          }
        }
      }
    }
  }

  /**
   * Gets the name of this theme.
   *
   * @return    the name
   */
  public String getName() {
    return name;
  }

}