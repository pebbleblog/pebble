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

import net.sourceforge.pebble.comparator.FileMetaDataComparator;
import net.sourceforge.pebble.util.FileUtils;
import net.sourceforge.pebble.PebbleContext;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.util.*;

/**
 * Encapsulates methods for managing and manipulating files under the
 * following locations:
 * <ul>
 * <li>${blog.dir}/images</li>
 * <li>${blog.dir}/files</li>
 * <li>${editableTheme}</li>
 * </ul>
 *
 * @author Simon Brown
 */
public class FileManager {

  /** the type of files being managed */
  private String type;

  /** the root directory for the particular file type */
  private File root;

  /**
   * Creates a new instande for the specified blog and type.
   *
   * @param blog    the blog that this manager refers to
   * @param type    the type of files to manage
   */
  public FileManager(Blog blog, String type) {
    this.type = type;

    // which directory are we looking at?
    if (type.equals(FileMetaData.BLOG_IMAGE)) {
      this.root = new File(blog.getImagesDirectory());
    } else if (type.equals(FileMetaData.THEME_FILE)) {
      this.root = blog.getEditableTheme().getPathToLiveTheme();
    } else if (type.equals(FileMetaData.BLOG_DATA)) {
      this.root = new File(blog.getRoot());
    } else {
      this.root = new File(blog.getFilesDirectory());
    }
  }

  /**
   * Gets the root directory that this class is managing.
   *
   * @return  a File instance
   */
  public File getRootDirectory() {
    return this.root;
  }

  /**
   * Gets meta data about a specific file or directory.
   *
   * @param path    the path of the file/directory
   * @return        a FileMetaData object
   */
  public FileMetaData getFileMetaData(String path) {
    FileMetaData metaData = new FileMetaData(this, path);
    metaData.setType(type);

    // is it a directory?
    File file = getFile(metaData);
    if (file.exists()) {
      if (file.isDirectory()) {
        metaData.setDirectory(true);
        try {
          List files = getFiles(metaData, true);
          long size = 0;
          Iterator it = files.iterator();
          while (it.hasNext()) {
            size += ((FileMetaData)it.next()).getSize();
          }
          metaData.setSize(size);
        } catch (IllegalFileAccessException ifae) {
          // do nothing
        }
      } else {
        metaData.setSize(file.length());
      }

      metaData.setLastModified(new Date(file.lastModified()));
    }

    return metaData;
  }

  public FileMetaData getParent(FileMetaData file) {
    if (file.getAbsolutePath().equals("/")) {
      return null;
    } else {
      return getFileMetaData(file.getPath());
    }
  }

  /**
   * Gets meta data about a specific file or directory.
   *
   * @param path    the path of the file/directory
   * @param name    the name of the file/directory
   * @return        a FileMetaData object
   */
  public FileMetaData getFileMetaData(String path, String name) {
    if (path != null && path.endsWith("/")) {
      return getFileMetaData(path + name);
    } else {
      return getFileMetaData(path + "/" + name);
    }
  }

  /**
   * Gets a java.io.File reference to the specified path, regardless
   * of whether it represents a file or directory.
   *
   * @param path    an absolute path from the root
   * @return    a java.io.File instance
   */
  public File getFile(String path) {
    String relativePath;
    if (path != null && path.startsWith("/")) {
      relativePath = path.substring(1);
    } else {
      relativePath = path;
    }

    return new File(root, relativePath);
  }

  /**
   * Convenience method to get a java.io.File reference to the file represented
   * by the specified FileMetaData object.
   *
   * @param file    the FileMetaData object representing the path
   * @return    a java.io.File instance
   */
  private File getFile(FileMetaData file) {
    String relativePath = file.getAbsolutePath().substring(1);
    return new File(root, relativePath);
  }

  /**
   * Determines whether the specified file is underneath the root directory
   * for this file manager.
   *
   * @param file    the java.io.File to test
   * @return    true if the file is underneath the root, false otherwise
   */
  public boolean isUnderneathRootDirectory(File file) {
    return FileUtils.underneathRoot(root, file);
  }

  /**
   * Creates a new directory with the specified name underneath the given path.
   *
   * @param path    the path under which to create the directory
   * @param name    the name of the directory
   * @return    a java.io.File instance representing the new directory
   */
  public File createDirectory(String path, String name) throws IllegalFileAccessException {
    FileMetaData subDirectory = getFileMetaData(path);

    File newDirectory = new File(getFile(subDirectory), name);
    if (!isUnderneathRootDirectory(newDirectory)) {
      throw new IllegalFileAccessException();
    } else {
      newDirectory.mkdirs();
    }

    return newDirectory;
  }

  /**
   * Copies a file.
   *
   * @param path      the path under which the file exists
   * @param name      the name of the file
   * @param newName   the new name of the file
   * @return    a java.io.File instance representing the new file, or null
   *            if the file isn't copied because no new name was given, or the
   *            new name is the same as the old name
   */
  public File copyFile(String path, String name, String newName) throws IOException, IllegalFileAccessException {
    if (newName != null && newName.length() > 0 && !newName.equals(name)) {
      FileMetaData subDirectory = getFileMetaData(path);
      File originalFile = new File(getFile(subDirectory), name);
      File newFile = new File(getFile(subDirectory), newName);

      if (!isUnderneathRootDirectory(originalFile) || !isUnderneathRootDirectory(newFile)) {
        throw new IllegalFileAccessException();
      }

      FileUtils.copyFile(originalFile, newFile);

      return newFile;
    } else {
      return null;
    }
  }

  /**
   * Renames a file.
   *
   * @param path      the path under which the file exists
   * @param name      the name of the file
   * @param newName   the new name of the file
   * @return    a java.io.File instance representing the new file, or null
   *            if the file isn't copied because no new name was given, or the
   *            new name is the same as the old name
   */
  public File renameFile(String path, String name, String newName) throws IllegalFileAccessException {
    if (newName != null && newName.length() > 0 && !newName.equals(name)) {
      FileMetaData subDirectory = getFileMetaData(path);
      File originalFile = new File(getFile(subDirectory), name);
      File newFile = new File(getFile(subDirectory), newName);

      // check that newFile is underneath the root directory
      if (!isUnderneathRootDirectory(originalFile) || !isUnderneathRootDirectory(newFile)) {
        throw new IllegalFileAccessException();
      }

      originalFile.renameTo(newFile);
      return newFile;
    } else {
      return null;
    }
  }

  /**
   * Deletes a file.
   *
   * @param path      the path under which the file exists
   * @param name      the name of the file
   */
  public void deleteFile(String path, String name) throws IllegalFileAccessException {
    FileMetaData subDirectory = getFileMetaData(path);
    File fileToDelete = new File(getFile(subDirectory), name);

    if (!isUnderneathRootDirectory(fileToDelete)) {
      throw new IllegalFileAccessException();
    }

    FileUtils.deleteFile(fileToDelete);
  }

  /**
   * Loads a file into a String.
   *
   * @param path      the path under which the file exists
   * @param name      the name of the file
   * @return  a String containing the content of the specified file
   */
  public String loadFile(String path, String name) throws IllegalFileAccessException {
    StringBuffer content = new StringBuffer();

    FileMetaData subDirectory = getFileMetaData(path);
    File fileToLoad = new File(getFile(subDirectory), name);

    if (!isUnderneathRootDirectory(fileToLoad)) {
      throw new IllegalFileAccessException();
    }

    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(fileToLoad));
      String line = reader.readLine();
      while (line != null) {
        content.append(line);

        line = reader.readLine();
        if (line != null) {
          content.append(System.getProperty("line.separator"));
        }
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      IOUtils.closeQuietly(reader);
    }

    return content.toString();
  }

  /**
   * Saves a file with the given content.
   *
   * @param path      the path under which the file exists
   * @param name      the name of the file
   * @param content   the content as a String
   */
  public void saveFile(String path, String name, String content) throws IOException, IllegalFileAccessException {
    FileMetaData subDirectory = getFileMetaData(path);
    File fileToSave = new File(getFile(subDirectory), name);

    if (!isUnderneathRootDirectory(fileToSave)) {
      throw new IllegalFileAccessException();
    }

    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(new FileWriter(fileToSave));
      writer.write(content);
      writer.flush();
    } finally {
      IOUtils.closeQuietly(writer);
    }
  }

  /**
   * Saves a file with the given binary content.
   *
   * @param name      the name of the file
   * @param content   the binary content
   * @return  a FileMetaData instance representing the saved file
   */
  public FileMetaData saveFile(String name, byte[] content) throws IOException, IllegalFileAccessException {
    FileMetaData file = getFileMetaData(name);
    File fileToSave = getFile(name);

    if (!isUnderneathRootDirectory(fileToSave)) {
      throw new IllegalFileAccessException();
    }

    BufferedOutputStream out = null;
    try {
      out = new BufferedOutputStream(new FileOutputStream(fileToSave));
      out.write(content);
      out.flush();
    } finally {
      IOUtils.closeQuietly(out);
    }

    return file;
  }

  /**
   * Gets a list of files that reside under a given path.
   *
   * @param path      the path under which the file exists
   * @return  a List of FileMetaData instances
   * @throws IllegalFileAccessException   if trying to access a file outside the root
   */
  public List getFiles(String path) throws IllegalFileAccessException {
    return getFiles(path, false);
  }

  public List getFiles(String path, boolean includeChildren) throws IllegalFileAccessException {
    FileMetaData subDirectory = getFileMetaData(path);
    return getFiles(subDirectory, includeChildren);
  }

  private List getFiles(FileMetaData path, boolean includeChildren) throws IllegalFileAccessException {
    File directoryToView = getFile(path);

    if (!isUnderneathRootDirectory(directoryToView)) {
      throw new IllegalFileAccessException();
    }

    List directoriesAndFiles = new ArrayList();
    List files = new ArrayList();
    List directories = new ArrayList();
    File f[] = directoryToView.listFiles();
    if (f != null) {
      File file;
      for (int i = 0; i < f.length; i++) {
        file = f[i];
        if (file.isDirectory()) {
          FileMetaData metaData = getFileMetaData(path.getAbsolutePath(), file.getName());
          directories.add(metaData);

          if (includeChildren) {
            directories.addAll(getFiles(metaData.getAbsolutePath(), true));
          } else {
            Collections.sort(directories, new FileMetaDataComparator());
          }
        }
      }

      for (int i = 0; i < f.length; i++) {
        file = f[i];
        if (file.isFile()) {
          FileMetaData metaData = getFileMetaData(path.getAbsolutePath(), file.getName());
          files.add(metaData);
        }
      }

      Collections.sort(files, new FileMetaDataComparator());
    }

    directoriesAndFiles.addAll(directories);
    directoriesAndFiles.addAll(files);

    return directoriesAndFiles;
  }

  /**
   * Determines how much space is being used in files, images and theme.
   *
   * @param blog    the blog to check against
   * @return  the number of KB
   */
  public static double getCurrentUsage(Blog blog) {
    FileManager imagesFileManager = new FileManager(blog, FileMetaData.BLOG_IMAGE);
    FileManager filesFileManager = new FileManager(blog, FileMetaData.BLOG_FILE);
    FileManager themeFileManager = new FileManager(blog, FileMetaData.THEME_FILE);
    return  imagesFileManager.getFileMetaData("/").getSizeInKB() +
            filesFileManager.getFileMetaData("/").getSizeInKB() +
            themeFileManager.getFileMetaData("/").getSizeInKB();
  }


  /**
   * Determines whether there is enough space to store the given number of KB.
   *
   * @param blog    the blog to check against
   * @param itemSize  the size of the item to be written
   * @return  true if there is enough space or quotas aren't active
   */
  public static boolean hasEnoughSpace(Blog blog, double itemSize) {
    long quota = PebbleContext.getInstance().getConfiguration().getFileUploadQuota();

    return (quota == -1) || ((quota - getCurrentUsage(blog)) > itemSize);
  }

}
